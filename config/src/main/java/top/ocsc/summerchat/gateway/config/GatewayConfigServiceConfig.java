package top.ocsc.summerchat.gateway.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 将configService交由spring管理
 *
 */
@Configuration
public class GatewayConfigServiceConfig {

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    @Bean
    public ConfigService configService() throws NacosException {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosConfigProperties.getServerAddr());
        properties.setProperty(PropertyKeyConst.NAMESPACE, nacosConfigProperties.getNamespace());
        properties.setProperty(PropertyKeyConst.USERNAME, nacosConfigProperties.getUsername());
        properties.setProperty(PropertyKeyConst.PASSWORD, nacosConfigProperties.getPassword());
        properties.setProperty(PropertyKeyConst.CLUSTER_NAME, nacosConfigProperties.getClusterName());
        return NacosFactory.createConfigService(properties);
    }

}
