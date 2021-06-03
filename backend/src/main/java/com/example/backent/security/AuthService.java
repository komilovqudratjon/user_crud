package com.example.backent.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.ssd.testapp.entity.Role;
import uz.ssd.testapp.entity.User;
import uz.ssd.testapp.entity.enums.RoleName;
import uz.ssd.testapp.exception.ResourceNotFoundException;
import uz.ssd.testapp.model.MailRequest;
import uz.ssd.testapp.model.MailResponse;
import uz.ssd.testapp.payload.ApiResponseModel;
import uz.ssd.testapp.payload.JwtResponse;
import uz.ssd.testapp.payload.ReqSignUp;
import uz.ssd.testapp.repository.RoleRepository;
import uz.ssd.testapp.repository.TakenTestRepository;
import uz.ssd.testapp.repository.UserRepository;
import uz.ssd.testapp.repository.VariableRepository;
import uz.ssd.testapp.service.MailService;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthenticationManager authenticate;

    @Autowired
    MailService mailService;

    @Autowired
    VariableRepository variableRepository;

    @Autowired
    TakenTestRepository takenTestRepository;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.username}")
    private String mailUsername;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return user.isDeleted() ? null : user;
    }

    public UserDetails loadUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User id not found: " + userId));
        return user.isDeleted() ? null : user;
    }

    public ApiResponseModel register(ReqSignUp reqSignUp, String lang) throws Exception {
        ApiResponseModel response = new ApiResponseModel();
        User user;
        if (reqSignUp.getId() != null){
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("User edited");
            user = userRepository.findById(reqSignUp.getId()).orElseThrow(() -> new ResourceNotFoundException("user","id", reqSignUp.getId()));
        }else {
            Optional<User> optionalUser = userRepository.getByPhoneNumber(reqSignUp.getPhoneNumber());
            Optional<User> optionalUser1 = userRepository.getByEmail(reqSignUp.getEmail());
            Optional<User> optionalUser2 = userRepository.getByPassportNumber(reqSignUp.getPassportNumber());
            if (optionalUser.isPresent()) {
                return new ApiResponseModel(HttpStatus.CONFLICT.value(), lang.equals("uz") ? "Bunday telefon nomerli foydalanuvchi tizimda mavjud" : "номер существует", null);
            } else if (optionalUser1.isPresent()) {
                return new ApiResponseModel(HttpStatus.CONFLICT.value(), lang.equals("uz") ? "Bunday email egasi tizimda mavjud" : "Пользователь с этим адресом электронной почты существует в системе", null);
            } else if (optionalUser2.isPresent()) {
                return new ApiResponseModel(HttpStatus.CONFLICT.value(), lang.equals("uz") ? "Bunday passport nomerli foydalanuvchi tizimda mavjud" : "Пользователь с таким номером паспорта существует в системе", null);
            } else {
                user = new User();
                response.setStatusCode(HttpStatus.CREATED.value());
                if (reqSignUp.getId() != null) {
                    user = userRepository.findById(reqSignUp.getId()).orElseThrow(() -> new ResourceNotFoundException("user", "id", reqSignUp.getId()));
                }
                if (reqSignUp.getRole().equals(RoleName.ROLE_USER.name().toLowerCase())) {
                    user.setRoles(roleRepository.findAllByNameIn(
                            Arrays.asList(RoleName.ROLE_USER)
                    ));
                    response.setMessage(lang.equals("uz") ? "Foydalanuvchi ro'yxatdan muvofaqqiyatli o'tkazildi" : "Пользователь успешно зарегистрирован");
                } else if (reqSignUp.getRole().equals(RoleName.ROLE_ADMIN.name().toLowerCase())) {
                    user.setRoles(roleRepository.findAllByNameIn(
                            Arrays.asList(RoleName.ROLE_ADMIN)
                    ));
                    response.setMessage(lang.equals("uz") ? "Admin ro'yxatdan muvofaqqiyatli o'tkazildi": "Админ успешно зарегистрирован");
                } else if (reqSignUp.getRole().equals(RoleName.ROLE_SUPER_ADMIN.name().toLowerCase())) {
                    if (userRepository.countAllByRoles(roleRepository.findByName(RoleName.ROLE_SUPER_ADMIN).get().getId()) < 2){
                        user.setRoles(roleRepository.findAllByNameIn(
                                Arrays.asList(RoleName.ROLE_SUPER_ADMIN)
                        ));
                        response.setMessage(lang.equals("uz") ? "Superadmin ro'yxatdan muvofaqqiyatli o'tkazildi": "Суперадмин успешно зарегистрирован");
                    }else {
                        return new ApiResponseModel(HttpStatus.CONFLICT.value(), lang.equals("uz")? "Tizimda allaqachon 2 ta superadmin mavjud. Siz boshqa superadmin qo'sha olmaysiz!" : "В системе уже 2 суперадмина. Вы не можете добавить больше!", null);
                    }
                }
                String password = generatePassword();
                MailResponse mailResponse = new MailResponse();
                mailResponse.setName(reqSignUp.getFirstname() + " " + reqSignUp.getLastname());
                mailResponse.setIp("localhost:8080/api/ru/auth/register");
                mailResponse.setUser(reqSignUp.getEmail());
                mailResponse.setPass(password);
                boolean send = mailService.sendEmail(new MailRequest(reqSignUp.getFirstname() + " " + reqSignUp.getLastname(), reqSignUp.getEmail(), mailUsername, "Логин "), mailResponse);
                if (!send){
                    return new ApiResponseModel(HttpStatus.CONFLICT.value(), lang.equals("uz")? "emailga xabar yuborilmadi" : "Электронное письмо не было отправлено", null);
                }
                user.setPassword(passwordEncoder.encode(password));
            }
        }
        user.setEmail(reqSignUp.getEmail());
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(reqSignUp.getDateOfBirth());
        user.setDateOfBirth(date);
        user.setPhoneNumber(reqSignUp.getPhoneNumber());
        user.setPassportNumber(reqSignUp.getPassportNumber());
        user.setLastname(reqSignUp.getFirstname());
        user.setFirstname(reqSignUp.getLastname());
        user.setMiddlename(reqSignUp.getMiddleName());
        user.setCategory(reqSignUp.getCategory());
        userRepository.save(user);
        response.setData(user);
        return response;
    }

    public String generatePassword() {
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public HttpEntity<?> getApiToken(String email, String password, String lang) {
        Authentication authentication = authenticate.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.getByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user", "email", email));
        if (!user.isEnabled()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseModel(HttpStatus.CONFLICT.value(), lang.equals("uz")? "Siz bloklangansiz" : "Вы заблокированы"));
        }
        if (user.getRoles().contains(roleRepository.findByName(RoleName.ROLE_USER).get())) {
//            List<TakenTest> takenTestList = takenTestRepository.findAllByUserAndFinished(user, false);
//            if (takenTestList.isEmpty()){
                int limit = variableRepository.getOne(1L).getMaxLoginAttempts();
                if (user.getLoginAttempts() == (limit-1)) {
                    Role roleBlocked = roleRepository.findByName(RoleName.ROLE_BLOCKED).orElseThrow(() -> new ResourceNotFoundException("role", "rolename", RoleName.ROLE_BLOCKED));
                    List<Role> roles = new ArrayList<>(user.getRoles());
                    roles.add(roleBlocked);
                    user.setRoles(roles);
                    user.setLoginAttempts((user.getLoginAttempts() + 1));
                    userRepository.save(user);
                }else if (user.getLoginAttempts() > (limit-1)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponseModel(HttpStatus.CONFLICT.value(), lang.equals("uz") ? "Login qilishlar soni  " + limit + " dan oshdi" : "Количество попыток входа в систему превысило ограничение в " + limit));
                } else {
                    user.setLoginAttempts((user.getLoginAttempts() + 1));
                    userRepository.save(user);
                }
            }
//        }

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
