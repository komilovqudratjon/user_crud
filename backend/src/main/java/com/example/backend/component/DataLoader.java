package com.example.backend.component;

import com.example.backend.entity.User;
import com.example.backend.entity.enums.Gender;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Value("${spring.sql.init.enabled}")
    private boolean initialMode;

    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) {


        if (initialMode) {

            userRepository.save(
                    new User(
                            "Qudratjon",
                            "Komilov",
                            "Qobil o'g'li",
                            new Date(1999 - 1900, Calendar.MARCH, 3),
                            Gender.MALE,
                            "UZBEK",
                            "Toshkent",
                            "UZBEKISTAN",
                            new Date(1999 - 1900, Calendar.MARCH, 3),
                            "Eshmatov Toshmat",
                            "AB53939666",
                            "+998917797278",
                            null));
            for (int i = 0; i < 10; i++) {
                User user =
                        userRepository.save(
                                new User(
                                        "Qudratjon" + i,
                                        "Komilov" + i,
                                        "Qobil o'g'li" + i,
                                        new Date(1999 - 1900, Calendar.MARCH, 4 + i),
                                        Gender.MALE,
                                        "UZBEK" + i,
                                        "Toshkent" + i,
                                        "UZBEKISTAN" + i,
                                        new Date(1999 - 1900, Calendar.MARCH, 3),
                                        "Eshmatov Toshmat" + i,
                                        "AB53939666" + i,
                                        "+998917797278" + i,
                                        null));


            }
        }
    }
}
