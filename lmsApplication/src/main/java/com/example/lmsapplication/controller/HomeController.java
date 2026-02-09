package com.example.lmsapplication.controller;
import com.example.lmsapplication.requisites.AvailableAction;
import com.example.lmsapplication.requisites.LoginBody;

import com.example.lmsapplication.requisites.SignUpBody;
import com.example.lmsapplication.response.HomeResponse;
import com.example.lmsapplication.response.LoginResponse;
import com.example.lmsapplication.service.LoginService;

import com.example.lmsapplication.service.LogoutService;
import com.example.lmsapplication.service.SignUpService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController {
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final SignUpService signUpService;



    public HomeController(LoginService loginService,LogoutService logoutService,SignUpService signUpService){
        this.loginService = loginService;
        this.logoutService = logoutService;
        this.signUpService = signUpService;
    }
    @GetMapping("/home")
    public HomeResponse homePage(){
        return new HomeResponse(List.of(HomeResponse.Abc.values()));
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginBody loginBody){

        LoginService.AuthResult authResult  = loginService.authorize(loginBody);


        AvailableAction a1 = new AvailableAction("main" ,"/leave_requests","GET");
        List<AvailableAction> li = new ArrayList<>();
        li.add(a1);


        return authResult.success()
                ? new LoginResponse("Successful","/leave_requests",authResult.token(),li)
                : new LoginResponse(authResult.message(), "/","",new ArrayList<>());
    }

    @DeleteMapping("/logout")
    public SignUpService.Obj logOut(@RequestHeader("Authorization") String token){
        return logoutService.logout(token);
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpService.Obj>signUp(@RequestBody SignUpBody signUpBody){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(signUpService.createEmployee(signUpBody));
    }


}
