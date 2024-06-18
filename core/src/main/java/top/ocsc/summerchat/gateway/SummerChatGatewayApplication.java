package top.ocsc.summerchat.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableFeignClients(basePackages = "top.ocsc.summerchat.gateway.auth.service")
@EnableDiscoveryClient
public class SummerChatGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SummerChatGatewayApplication.class, args);
        System.out.println("        ヾ(◍°∇°◍)ﾉﾞ    Application启动成功      ヾ(◍°∇°◍)ﾉﾞ\n");
    }

    @RequestMapping("/hello")
    public String get() {
        return "你好";
    }


}
