package com.example.lmsapplication.requisites;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    public String hash(String rawPass){
        return BCrypt.hashpw(rawPass,BCrypt.gensalt(2));
    }
    public boolean matches(String checkPass,String hashPass){
        return BCrypt.checkpw(checkPass,hashPass);
    }
}
