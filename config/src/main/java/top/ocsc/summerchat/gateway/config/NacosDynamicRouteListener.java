package top.ocsc.summerchat.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;


/**
 * 动态路由监听器
 *
 * @author Chill
 */
@Order
@RefreshScope
@Component
public class NacosDynamicRouteListener implements InitializingBean {

    private static final Logger log = LogManager.getLogger(NacosDynamicRouteListener.class);
    private final DynamicRouteManager dynamicRouteManager;
    private final NacosConfigProperties nacosConfigProperties;

    @Autowired
    private ConfigService configService;

    @Value("${spring.application.name}")
    private String dataId;

    public NacosDynamicRouteListener(DynamicRouteManager dynamicRouteManager,
                                     NacosConfigProperties nacosConfigProperties) {
        this.dynamicRouteManager = dynamicRouteManager;
        this.nacosConfigProperties = nacosConfigProperties;
    }

    /**
     * 监听Nacos的动态路由配置
     */
    private void dynamicRouteListener() {
        try {
            String dataId = this.dataId;
            String group = nacosConfigProperties.getGroup();

            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    log.info("---监听到路由信息刷新---\n{}\n---------------------------------------", configInfo);
                    //解析配置文件的json
                    JSONObject configJson = JSON.parseObject(configInfo);
                    JSONArray configList = configJson.getJSONArray("data");
                    //fastjson反序列化成成RouteDefinition对象
                    List<RouteDefinition> routeDefinitions = configList.toJavaList(RouteDefinition.class);
                    //推送
                    dynamicRouteManager.updateList(routeDefinitions);
                }

                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
            String configInfo = configService.getConfig(dataId, group, 1000 * 10);
            if (configInfo != null) {
                JSONObject configJson = JSON.parseObject(configInfo);
                JSONArray configList = configJson.getJSONArray("data");
                List<RouteDefinition> routeDefinitions = configList.toJavaList(RouteDefinition.class);
                dynamicRouteManager.updateList(routeDefinitions);
            }
        } catch (NacosException e) {
            log.error("动态路由监听异常", e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        dynamicRouteListener();
    }
}
