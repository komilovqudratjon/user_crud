package com.example.backent.component;

import com.example.backent.entity.*;
import com.example.backent.entity.enums.Family;
import com.example.backent.entity.enums.RoleName;
import com.example.backent.entity.enums.TimeType;
import com.example.backent.entity.enums.WorkTimeType;
import com.example.backent.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

  @Value("${spring.sql.init.enabled}")
  private boolean initialMode;

  @Autowired UserRepository userRepository;

  @Autowired RoleRepository roleRepository;

  @Autowired PasswordEncoder passwordEncoder;

  @Autowired UserLanguageRepository userLanguageRepository;

  @Autowired UserFieldsRepository userFieldsRepository;

  @Autowired UserExperiencesRepository userExperiencesRepository;

  @Override
  public void run(String... args) {

    if (roleRepository.count() == 0) {
      int i = 1;
      for (RoleName value : RoleName.values()) {
        roleRepository.save(new Role(i, value));
        i++;
      }
    }

    if (initialMode) {

      userRepository.save(
          new User(
              "Qudratjon",
              "Komilov",
              "Qobil o'g'li",
              "Ko'kcha masjit",
              WorkTimeType.FULL_TIME,
              Family.NOT_MARRED,
              "AB53939666",
              new Date(1999, Calendar.MARCH, 3),
              new Date(2020, Calendar.NOVEMBER, 20),
              "+99891779778",
              "koinot@koinot.com",
              List.of(
                  userFieldsRepository.save(new FieldsForUsers("backend")),
                  userFieldsRepository.save(new FieldsForUsers("frontend")),
                  userFieldsRepository.save(new FieldsForUsers("tester")),
                  userFieldsRepository.save(new FieldsForUsers("design"))),
              List.of(
                  userExperiencesRepository.save(
                      new UserExperience("postgres", 6L, TimeType.MONTH)),
                  userExperiencesRepository.save(
                      new UserExperience("postgres", 6L, TimeType.MONTH))),
              List.of(
                  userLanguageRepository.save(new UsersLanguage("uz")),
                  userLanguageRepository.save(new UsersLanguage("en")),
                  userLanguageRepository.save(new UsersLanguage("ru"))),
              null,
              passwordEncoder.encode("koinot"),
              true,
              roleRepository.findAllByNameIn(Collections.singletonList(RoleName.SUPER_ADMIN)),
              null));
    }
  }
}
