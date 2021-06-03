package com.example.backent.component;


import com.example.backent.entity.User;
import com.example.backent.entity.enums.RoleName;
import com.example.backent.repository.RoleRepository;
import com.example.backent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.sql.init.enabled}")
    private boolean initialMode;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args)  {


        if (initialMode) {

            userRepository.save(new User(
                    "koinot",
                    passwordEncoder.encode("koinot"),
                    "Qudratjon",
                    "Komilov",
                    roleRepository.findAllByNameIn(
                            Collections.singletonList(RoleName.ROLE_SUPER_ADMIN)
                    ))
            );


        }


    }

}
