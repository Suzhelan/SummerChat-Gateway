package top.ocsc.summerchat.gateway.auth.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ocsc.summerchat.gateway.auth.service.UserLoginService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
public class UserLoginController {

    private static final Logger log = LogManager.getLogger(UserLoginController.class);
    @Autowired
    public  UserLoginService userLoginService;

    @RequestMapping("doLogin")
    public Object doLogin() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<Object> completableFuture = CompletableFuture.supplyAsync(userLoginService::doLogin);
        return completableFuture.get(5, TimeUnit.SECONDS);

    }
}
