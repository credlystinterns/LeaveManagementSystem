package com.example.lmsapplication.controller;
import com.example.lmsapplication.requisites.LoginBody;
import com.example.lmsapplication.requisites.Requests;
import com.example.lmsapplication.requisites.SignUpBody;
import com.example.lmsapplication.response.HomeResponse;
import com.example.lmsapplication.response.LoginResponse;
import com.example.lmsapplication.service.LoginService;
import com.example.lmsapplication.requisites.PasswordHasher;
import com.example.lmsapplication.service.LogoutService;
import com.example.lmsapplication.service.SignUpService;
import com.example.lmsapplication.tables.Session;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController {
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final SignUpService signUpService;

    PasswordHasher ps = new PasswordHasher();

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


        LoginResponse.AvailableAction a1 = new LoginResponse.AvailableAction("main" ,"/leave_requests","GET");
        List<LoginResponse.AvailableAction> li = new ArrayList<>();
        li.add(a1);


        return authResult.success()
                ? new LoginResponse("Successful","/leave_requests",authResult.token(),li)
                : new LoginResponse(authResult.message(), "/","",new ArrayList<LoginResponse.AvailableAction>());
    }

    @DeleteMapping("/logout")
    public SignUpService.Obj logOut(@RequestHeader("Authorization") String token){
        return logoutService.logout(token);
    }

    @PostMapping("/signup")
    public SignUpService.Obj Signup(@RequestBody SignUpBody signUpBody){
        return signUpService.createEmployee(signUpBody);
    }


}
