package com.example.lmsapplication.service;

import com.example.lmsapplication.dto.EmployeeRepo;
import com.example.lmsapplication.dto.SessionRepo;
import com.example.lmsapplication.requisites.LoginBody;
import com.example.lmsapplication.requisites.PasswordHasher;
import com.example.lmsapplication.tables.Session;
import org.springframework.stereotype.Service;

import java.util.random.RandomGenerator;

@Service
public class LoginService {

    private final EmployeeRepo employeeRepo;
    private final SessionRepo sessionRepo;
    private final PasswordHasher passwordHasher = new PasswordHasher();

    public LoginService(EmployeeRepo employeeRepo, SessionRepo sessionRepo) {
        this.employeeRepo = employeeRepo;
        this.sessionRepo = sessionRepo;
    }

    public record AuthResult(boolean success, String message, String token) {}

    public AuthResult authorize(LoginBody loginBody) {
        return employeeRepo.findByEmail(loginBody.getEmail())
                .map(emp -> {
                    if (!passwordHasher.matches(loginBody.getPassword(), emp.getPassword())) {
                        return new AuthResult(false, "Invalid EmailId or Password", null);
                    }

                    sessionRepo.deleteByEmployeeId((emp.getEmployeeId()));

                    String rawToken = loginBody.getEmail()
                            + System.currentTimeMillis()
                            + RandomGenerator.getDefault().nextInt();

                    String token = passwordHasher.hash(rawToken);

                    Session session = Session.builder()
                            .employeeId(emp.getEmployeeId())
                            .sessionToken(token)
                            .build();

                    sessionRepo.save(session);

                    return new AuthResult(true, "Login Successful", token);
                })
                .orElse(new AuthResult(false, "Invalid EmailId or Password", null));
    }
}
