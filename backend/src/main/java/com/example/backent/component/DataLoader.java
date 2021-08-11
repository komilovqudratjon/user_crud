package com.example.backent.component;

import com.example.backent.entity.*;
import com.example.backent.entity.enums.*;
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

  @Autowired ProjectRepository projectRepository;

  @Autowired BoardRepository boardRepository;

  @Autowired TicketRepository ticketRepository;

  @Autowired CompanyRepository companyRepository;

  @Autowired TagRepository tagRepository;

  @Override
  public void run(String... args) {

    //    testRepository.save( new TestArrayJson( "koinot" ,new ErrorsField[]{new ErrorsField(
    // "ghjrtfgh","hrthrth" )}));
    //    testRepository.save( new TestArrayJson( "koinot1" ,new ErrorsField[]{new ErrorsField(
    // "dfhd","ghdfhgdf" )}));
    //    testRepository.save( new TestArrayJson( "koinot2" ,new ErrorsField[]{new ErrorsField(
    // "dfhdfh","dfghdfgh" )}));
    //    testRepository.save( new TestArrayJson( "koinot3" ,new ErrorsField[]{new ErrorsField(
    // "trurtujrt","dghdrfg" )}));

    if (roleRepository.count() == 0) {
      int i = 1;
      for (RoleName value : RoleName.values()) {
        roleRepository.save(new Role(i, value));
        i++;
      }
    }

    if (initialMode) {
      List<FieldsForUsers> save =
          List.of(
              userFieldsRepository.save(new FieldsForUsers("backend")),
              userFieldsRepository.save(new FieldsForUsers("frontend")),
              userFieldsRepository.save(new FieldsForUsers("tester")),
              userFieldsRepository.save(new FieldsForUsers("design")));
      List<UsersLanguage> save1 =
          List.of(
              userLanguageRepository.save(new UsersLanguage("uz")),
              userLanguageRepository.save(new UsersLanguage("en")),
              userLanguageRepository.save(new UsersLanguage("ru")));

      userRepository.save(
          new User(
              "Qudratjon",
              "Komilov",
              "Qobil o'g'li",
              "Ko'kcha masjit",
              WorkTimeType.FULL_TIME,
              Family.NOT_MARRED,
              "AB53939666",
              new Date(1999 - 1900, Calendar.MARCH, 3),
              new Date(2020 - 1900, Calendar.NOVEMBER, 20),
              "+99891779778",
              "koinot@gmail.com",
              save,
              List.of(
                  userExperiencesRepository.save(
                      new UserExperience("postgres", 6L, TimeType.MONTH)),
                  userExperiencesRepository.save(new UserExperience("java", 6L, TimeType.YEAR))),
              save1,
              null,
              passwordEncoder.encode("koinot"),
              true,
              roleRepository.findAllByNameIn(Collections.singletonList(RoleName.SUPER_ADMIN)),
              null));
      for (int i = 0; i < 10; i++) {
        User user =
            userRepository.save(
                new User(
                    "Qudratjon" + 1,
                    "Komilov" + 1,
                    "Qobil o'g'li",
                    "Ko'kcha masjit",
                    WorkTimeType.FULL_TIME,
                    Family.NOT_MARRED,
                    "AB539177" + i,
                    new Date(1999 - 1900, Calendar.MARCH, 3),
                    new Date(2020 - 1900, Calendar.NOVEMBER, 20),
                    "+99891794" + i,
                    "koinot" + i + "@gmail.com",
                    save,
                    List.of(
                        userExperiencesRepository.save(
                            new UserExperience("postgres", 6L, TimeType.MONTH)),
                        userExperiencesRepository.save(
                            new UserExperience("java", 6L, TimeType.YEAR))),
                    save1,
                    null,
                    passwordEncoder.encode("koinot"),
                    true,
                    roleRepository.findAllByNameIn(Collections.singletonList(RoleName.DEV)),
                    null));
        Project project =
            projectRepository.save(
                new Project(
                    "BRBT",
                    companyRepository.save(
                        new Company(
                            "koinot",
                            "asdgdf",
                            123L,
                            456L,
                            789L,
                            789L,
                            "tyjy,",
                            "tyjtyrtujry",
                            "sdfghj",
                            null,
                            false)),
                    null,
                    List.of(user),
                    "fgnhjty",
                    "rtgbht",
                    false));

        Board board =
            boardRepository.save(
                new Board("name" + i, project, false, 321L, BoardCondition.CREATED));

        for (int j = 0; j < 10; j++) {
          ticketRepository.save(
              new Ticket(
                  WorkType.BACKEND,
                  "login qilish userni ems verification bilan birga..",
                  user,
                  user,
                  user,
                  123L,
                  456L,
                  board,
                  null,
                  null,
                  false,
                  null,
                  TicketCondition.ATTACHED,
                  tagRepository.save(new Tag("tag"))));
        }
      }
    }
  }
}
