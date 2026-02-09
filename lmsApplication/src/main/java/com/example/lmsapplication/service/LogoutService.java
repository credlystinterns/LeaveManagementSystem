package com.example.lmsapplication.service;


import com.example.lmsapplication.dto.SessionRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {

    SessionRepo sessionRepo;

    public LogoutService(SessionRepo sessionRepo){
        this.sessionRepo = sessionRepo;
    }
    public SignUpService.Obj logout(String token){

        return sessionRepo.deleteSessionBySessionToken(token) >0
                ? new SignUpService.Obj("success","Logout Successful")
                :new SignUpService.Obj("failure" , "Session token doesn't exist");
    }
}
