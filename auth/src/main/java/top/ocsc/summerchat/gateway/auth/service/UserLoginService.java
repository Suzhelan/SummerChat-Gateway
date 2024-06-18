package top.ocsc.summerchat.gateway.auth.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient("SummerChat-Login")
public interface UserLoginService {
    @GetMapping("doLogin")
    public Map<String, Object> doLogin();
}
