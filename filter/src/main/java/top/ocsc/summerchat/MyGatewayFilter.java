package top.ocsc.summerchat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class MyGatewayFilter implements GlobalFilter, Ordered {
    private static final Logger log = LogManager.getLogger(MyGatewayFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestUrl = request.getPath().value();
        log.info("request url: {}", requestUrl);
        System.out.println(requestUrl);
        /*String userType = request.getHeaders().getFirst(UserConstant.USER_TYPE);
        AntPathMatcher pathMatcher = new AntPathMatcher();
        if (!pathMatcher.match(LOGIN_PATH, requestUrl) && "admin".equalsIgnoreCase(userType)) {
            String token = request.getHeaders().getFirst(UserConstant.TOKEN);
            Claims claim = TokenUtils.getClaim(token);
            if (StringUtils.isBlank(token) || claim == null) {
                return FilterUtils.invalidToken(exchange);
            }
        }*/


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
