package com.example.backent.security;

import com.example.backent.entity.User;
import com.example.backent.entity.enums.RoleName;
import com.example.backent.payload.*;
import com.example.backent.repository.RoleRepository;
import com.example.backent.repository.UserRepository;
import com.example.backent.service.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthenticationManager authenticate;

    @Autowired
    MailService mailService;

    private final JavaMailSender sender;

    @Autowired
    private Configuration config;



    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JavaMailSender sender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.sender = sender;
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

    public ApiResponseModel register(ReqSignUp reqSignUp)  {
        if (userRepository.existsByEmail(reqSignUp.getEmail())){
            return new ApiResponseModel(HttpStatus.CONFLICT.value(), "this email is busy");
        }
        User user = userRepository.save(new
                        User(
                        reqSignUp.getFirstname(),
                        reqSignUp.getLastname(),
                        reqSignUp.getMiddleName(),
                        reqSignUp.getPassportNumber(),
                        reqSignUp.getDateOfBirth(),
                        reqSignUp.getPhoneNumber(),
                        reqSignUp.getEmail(),
                        passwordEncoder.encode(reqSignUp.getPassword()),
                        roleRepository.findAllByNameIn(List.of(RoleName.USER))
                )
        );

        System.out.println(reqSignUp.getFirstname() + " " + reqSignUp.getLastname());
        mailService.sendEmail(
                reqSignUp.getFirstname()+" "+reqSignUp.getLastname(),
                reqSignUp.getEmail(),
                reqSignUp.getEmail(),
                "Логин ",
                reqSignUp.getFirstname() + " " + reqSignUp.getLastname(),
                "localhost:8080/api/ru/auth/register",
                reqSignUp.getPassword()
                );

        return new ApiResponseModel(HttpStatus.OK.value(), "saved",user.getEmail());
    }


//    public void sendEmail(String name, String to, String from, String subject, String user, String ip, String pass) {
//        MimeMessage message = sender.createMimeMessage();
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
//            Template t = config.getTemplate("email.ftl");
//
//            Map<String, String> mail = new HashMap<>();
//            mail.put("image", "<img src=\"https://www.cmda.gov.uz/images/logo_ru.png\">");
//            mail.put("name", name);
//            mail.put("user", user);
//            mail.put("ip", ip);
//            mail.put("pass", pass);
//
//            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail);
//            helper.setTo(to);
//            helper.setText(html, true);
//            helper.setSubject(subject);
//            helper.setFrom(from);
//            sender.send(message);
//            System.out.println("send message to "+ to);
//        } catch (MessagingException | IOException | TemplateException e) {
//            System.err.println("sending error "+ to + "   " +e);
//        }
//    }

    public HttpEntity<?> getApiToken(String phoneNumber, String password) {
        Authentication authentication = authenticate.authenticate(
                new UsernamePasswordAuthenticationToken(phoneNumber, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }


}
