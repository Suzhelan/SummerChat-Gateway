package top.ocsc.summerchat.gateway.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 动态路由操作类
 */
@Component
public class DynamicRouteManager implements ApplicationEventPublisherAware {

    private static final Logger logger = LogManager.getLogger(DynamicRouteManager.class);

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;



    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * 增加路由
     */
    public void add(RouteDefinition definition) {
        logger.info("添加路由:{}", definition.toString());
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 更新路由
     */
    public void update(RouteDefinition definition) {
        logger.info("更新路由:{}", definition.toString());
        routeDefinitionWriter.delete(Mono.just(definition.getId()));
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        publisher.publishEvent(new RefreshRoutesEvent(this));
    }

    /**
     * 批量更新路由
     */
    public void updateList(List<RouteDefinition> routeDefinitions) {
        routeDefinitions.forEach(this::update);
    }

    /**
     * 删除路由
     */
    public void delete(String id) {
        logger.info("delete route:{}", id);
        routeDefinitionWriter.delete(Mono.just(id));
    }

}
