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

    @Value("${spring.datasource.initialization-mode}")
    private String initialMode;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args)  {


        if (initialMode.equals("always")) {

            userRepository.save(new User(
                    "test@gmail.com",
                    passwordEncoder.encode("Akhmedov"),
                    "Akhmedov",
                    "Akhmedov",
                    roleRepository.findAllByNameIn(
                            Collections.singletonList(RoleName.ROLE_SUPER_ADMIN)
                    ))
            );


        }


    }

}
