package com.example.backent.security;

import com.example.backent.entity.User;
import com.example.backent.entity.enums.RoleName;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.JwtResponse;
import com.example.backent.payload.ReqSignUp;
import com.example.backent.repository.RoleRepository;
import com.example.backent.repository.UserRepository;
import com.example.backent.service.MailService;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService implements UserDetailsService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final RoleRepository roleRepository;

  @Value("${spring.mail.password}")
  private String mailPassword;

  @Value("${spring.mail.username}")
  private String mailUsername;

  @Autowired JwtTokenProvider jwtTokenProvider;

  @Autowired AuthenticationManager authenticate;

  @Autowired MailService mailService;

  private final JavaMailSender sender;

  @Autowired private Configuration config;

  @Autowired
  public AuthService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      RoleRepository roleRepository,
      JavaMailSender sender) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
    this.sender = sender;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository.getByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    return user.isDeleted() ? null : user;
  }

  public UserDetails loadUserById(Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User id not found: " + userId));
    return user.isDeleted() ? null : user;
  }

  public ApiResponseModel register(ReqSignUp reqSignUp) {
    if (userRepository.existsByEmail(reqSignUp.getEmail())) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), "this email is busy");
    }

    System.out.println(reqSignUp.getFirstname() + " " + reqSignUp.getLastname());
    mailService.sendEmail(
        reqSignUp.getFirstname() + " " + reqSignUp.getLastname(),
        reqSignUp.getEmail(),
        mailUsername,
        "Логин",
        reqSignUp.getFirstname() + " " + reqSignUp.getLastname(),
        "this is link put",
        reqSignUp.getPassword());
    User user =
        userRepository.save(
            new User(
                reqSignUp.getFirstname(),
                reqSignUp.getLastname(),
                reqSignUp.getMiddleName(),
                reqSignUp.getPassportNumber(),
                reqSignUp.getDateOfBirth(),
                reqSignUp.getPhoneNumber(),
                reqSignUp.getEmail(),
                passwordEncoder.encode(reqSignUp.getPassword()),
                roleRepository.findAllByNameIn(List.of(RoleName.USER))));

    return new ApiResponseModel(HttpStatus.OK.value(), "saved", user.getEmail());
  }

  public HttpEntity<?> getApiToken(String phoneNumber, String password) {
    Authentication authentication =
        authenticate.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtTokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtResponse(jwt));
  }
}
