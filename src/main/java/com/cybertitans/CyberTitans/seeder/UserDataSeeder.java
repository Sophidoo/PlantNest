package com.cybertitans.CyberTitans.seeder;

import com.cybertitans.CyberTitans.enums.Roles;
import com.cybertitans.CyberTitans.model.User;
import com.cybertitans.CyberTitans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataSeeder implements CommandLineRunner{

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) {
    seedUserData();
    }

    private void seedUserData() {
        if(userRepository.count()==0){
            User defaultAdmin = new User();
            defaultAdmin.setEmail("aptech@gmail.com");
            defaultAdmin.setPassword(passwordEncoder().encode( "rootpassword"));
            defaultAdmin.setUsername("admin");
            defaultAdmin.setFirstname("john");
            defaultAdmin.setLastname("doe");
            defaultAdmin.setVerified(true);
            defaultAdmin.setRole(Roles.SUPER_ADMIN);
            userRepository.save(defaultAdmin);
        }
    }

    private static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
