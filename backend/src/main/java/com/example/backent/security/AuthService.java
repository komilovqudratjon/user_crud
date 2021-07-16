package com.example.backent.security;

import com.example.backent.entity.Attachment;
import com.example.backent.entity.User;
import com.example.backent.entity.UserExperience;
import com.example.backent.entity.enums.AttachmentType;
import com.example.backent.exception.ResourceException;
import com.example.backent.payload.*;
import com.example.backent.repository.*;
import com.example.backent.service.MailService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final RoleRepository roleRepository;

  @Value("${upload.folder}")
  private String path;

  @Value("${spring.mail.password}")
  private String mailPassword;

  @Value("${spring.mail.username}")
  private String mailUsername;

  private final JwtTokenProvider jwtTokenProvider;

  private final AuthenticationManager authenticate;

  private final UserFieldsRepository userFieldsRepository;

  private final UserExperiencesRepository userExperiencesRepository;

  private final UserLanguageRepository usersLanguageRepository;

  private final ProgrammingLanguageRepository programmingLanguageRepository;

  private final AttachmentRepository attachmentRepository;

  private final MailService mailService;

  @Autowired
  public AuthService(
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      RoleRepository roleRepository,
      JwtTokenProvider jwtTokenProvider,
      AuthenticationManager authenticate,
      UserLanguageRepository usersLanguageRepository,
      AttachmentRepository attachmentRepository,
      MailService mailService,
      UserFieldsRepository userFieldsRepository,
      UserExperiencesRepository userExperiencesRepository,
      ProgrammingLanguageRepository programmingLanguageRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
    this.jwtTokenProvider = jwtTokenProvider;
    this.authenticate = authenticate;
    this.usersLanguageRepository = usersLanguageRepository;
    this.attachmentRepository = attachmentRepository;
    this.mailService = mailService;
    this.userFieldsRepository = userFieldsRepository;
    this.userExperiencesRepository = userExperiencesRepository;
    this.programmingLanguageRepository = programmingLanguageRepository;
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

  public HttpEntity<?> register(ReqSignUp reqSignUp) throws ParseException {

    User user = new User();

    String info = "";

    if (reqSignUp.getId() == null) {
      if (userRepository.existsByEmail(reqSignUp.getEmail())) {
        return ResponseEntity.badRequest()
            .body(
                new ApiResponseModel(
                    HttpStatus.CONFLICT.value(),
                    "field",
                    List.of(new ErrorsField("email", "this email is busy"))));
      }
      if (userRepository.existsByPhoneNumber(reqSignUp.getPhoneNumber())) {
        return ResponseEntity.badRequest()
            .body(
                new ApiResponseModel(
                    HttpStatus.CONFLICT.value(),
                    "field",
                    List.of(new ErrorsField("phoneNumber", "this phoneNumber is busy"))));
      }
      if (userRepository.existsByPassportNumber(reqSignUp.getPassportNumber())) {
        return ResponseEntity.badRequest()
            .body(
                new ApiResponseModel(
                    HttpStatus.CONFLICT.value(),
                    "field",
                    List.of(new ErrorsField("passportNumber", "this passportNumber is busy"))));
      }
      user =
          new User(
              reqSignUp.getFirstname().substring(0, 1).toUpperCase()
                  + reqSignUp.getFirstname().substring(1),
              reqSignUp.getLastname().substring(0, 1).toUpperCase()
                  + reqSignUp.getLastname().substring(1),
              reqSignUp.getMiddleName(),
              reqSignUp.getAddress(),
              reqSignUp.getWorkTimeType(),
              reqSignUp.getFamily(),
              reqSignUp.getPassportNumber(),
              new SimpleDateFormat("yyyy-MM-dd").parse(reqSignUp.getDateOfBirth()),
              new SimpleDateFormat("yyyy-MM-dd").parse(reqSignUp.getStartWorkingTime()),
              reqSignUp.getPhoneNumber(),
              reqSignUp.getEmail(),
              userFieldsRepository.findAllByIdIn(reqSignUp.getFields()),
              userExperiencesRepository.saveAll(reqSignUp.getExperiences()),
              usersLanguageRepository.findAllByIdIn(reqSignUp.getLanguages()),
              reqSignUp.getProgramingLanguages() == null
                  ? null
                  : programmingLanguageRepository.findAllByIdIn(reqSignUp.getProgramingLanguages()),
              passwordEncoder.encode(reqSignUp.getPassword()),
              reqSignUp.isActive(),
              roleRepository.findAllByNameIn(reqSignUp.getRoles()),
              reqSignUp.getPhotoId() == null
                  ? null
                  : attachmentRepository.findById(reqSignUp.getPhotoId()).orElse(null));
      info = "create";
    } else {
      Optional<User> userOptional = userRepository.findById(reqSignUp.getId());
      if (userOptional.isPresent()) {
        if (userRepository.existsByEmailAndIdNot(reqSignUp.getEmail(), reqSignUp.getId())) {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(new ErrorsField("email", "this email is busy"))));
        }
        if (userRepository.existsByPhoneNumberAndIdNot(
            reqSignUp.getPhoneNumber(), reqSignUp.getId())) {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(new ErrorsField("phoneNumber", "this phoneNumber is busy"))));
        }
        if (userRepository.existsByPassportNumberAndIdNot(
            reqSignUp.getPassportNumber(), reqSignUp.getId())) {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(new ErrorsField("passportNumber", "this passportNumber is busy"))));
        }
        user = userOptional.get();
        user.setFirstname(
            reqSignUp.getFirstname().substring(0, 1).toUpperCase()
                + reqSignUp.getFirstname().substring(1));
        user.setLastname(
            reqSignUp.getLastname().substring(0, 1).toUpperCase()
                + reqSignUp.getLastname().substring(1));
        user.setMiddlename(reqSignUp.getMiddleName());
        user.setAddress(reqSignUp.getAddress());
        user.setWorkTimeType(reqSignUp.getWorkTimeType());
        user.setFamily(reqSignUp.getFamily());
        user.setPassportNumber(reqSignUp.getPassportNumber());
        user.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(reqSignUp.getDateOfBirth()));
        user.setStartWorkingTime(
            new SimpleDateFormat("yyyy-MM-dd").parse(reqSignUp.getStartWorkingTime()));
        user.setPhoneNumber(reqSignUp.getPhoneNumber());
        user.setEmail(reqSignUp.getEmail());
        //        List<UserExperience> experiences = user.getExperiences( );
        user.setFields(null);
        user.setExperiences(null);
        user.setLanguages(null);
        user.setProgramingLanguages(null);
        userRepository.save(user);
        //        userExperiencesRepository.deleteAll( experiences );
        user.setFields(userFieldsRepository.findAllByIdIn(reqSignUp.getFields()));
        user.setExperiences(userExperiencesRepository.saveAll(reqSignUp.getExperiences()));
        user.setLanguages(usersLanguageRepository.findAllByIdIn(reqSignUp.getLanguages()));
        user.setProgramingLanguages(
            reqSignUp.getProgramingLanguages() == null
                ? null
                : programmingLanguageRepository.findAllByIdIn(reqSignUp.getProgramingLanguages()));
        if (reqSignUp.getPassword() != null && reqSignUp.getPassword().length() > 3) {
          user.setPassword(passwordEncoder.encode(reqSignUp.getPassword()));
        }
        user.setRoles(roleRepository.findAllByNameIn(reqSignUp.getRoles()));
        user.setActive(reqSignUp.isActive());
        user.setAvatar(
            reqSignUp.getPhotoId() == null
                ? null
                : attachmentRepository.findById(reqSignUp.getPhotoId()).orElse(null));
      } else {
        return ResponseEntity.badRequest()
            .body(new ResourceException(HttpStatus.CONFLICT.value(), "not found this id", null));
      }
      info = "edit";
    }

    mailService.sendEmail(
        reqSignUp.getFirstname() + " " + reqSignUp.getLastname(),
        reqSignUp.getEmail(),
        mailUsername,
        "Логин",
        reqSignUp.getFirstname() + " " + reqSignUp.getLastname(),
        "this is link put",
        reqSignUp.getPassword());

    userRepository.save(user);
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(
            new ApiResponseModel(
                HttpStatus.OK.value(),
                info,
                new ReqUser(
                    user.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    user.getMiddlename(),
                    user.getAddress(),
                    user.getWorkTimeType(),
                    user.getFamily(),
                    user.getPassportNumber(),
                    new SimpleDateFormat("yyyy-MM-dd").format(user.getDateOfBirth()),
                    new SimpleDateFormat("yyyy-MM-dd").format(user.getStartWorkingTime()),
                    user.getPhoneNumber(),
                    user.getEmail(),
                    user.getFields().stream()
                        .map(
                            fieldsForUsers ->
                                new ReqIdAndName(fieldsForUsers.getId(), fieldsForUsers.getName()))
                        .collect(Collectors.toList()),
                    user.getExperiences(),
                    user.getLanguages().stream()
                        .map(
                            usersLanguage ->
                                new ReqIdAndName(usersLanguage.getId(), usersLanguage.getName()))
                        .collect(Collectors.toList()),
                    user.getProgramingLanguages(),
                    user.getAvatar() == null
                        ? null
                        : ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/api/attach/")
                            .path(user.getAvatar().getId().toString())
                            .toUriString(),
                    user.getRoles(),
                    user.isActive())));
  }

  public HttpEntity<?> getApiToken(String phoneNumber, String password) {
    Authentication authentication =
        authenticate.authenticate(new UsernamePasswordAuthenticationToken(phoneNumber, password));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtTokenProvider.generateToken(authentication);
    return ResponseEntity.ok(new JwtResponse(jwt));
  }

  public ApiResponseModel uploadPhotoFileList(MultipartHttpServletRequest request) {
    try {

      Iterator<String> iterator = request.getFileNames();
      MultipartFile multipartFile;
      List<ResUploadFile> resUploadFiles = new ArrayList<>();
      if (iterator.hasNext()) {
        multipartFile = request.getFile(iterator.next());
        Attachment attachment = new Attachment();
        assert multipartFile != null;
        if (getExt(multipartFile.getOriginalFilename()) == null
            || multipartFile.getSize() == 0
            || !Objects.requireNonNull(multipartFile.getContentType()).startsWith("image")) {
          return new ApiResponseModel(
              HttpStatus.CONFLICT.value(), "file not confirmed ... send picture");
        }
        attachment.setContentType(multipartFile.getContentType());
        attachment.setSize(multipartFile.getSize());
        attachment.setName(multipartFile.getOriginalFilename());
        attachment.setAttachmentType(AttachmentType.PROFILE);
        attachment.setExtension(getExt(multipartFile.getOriginalFilename()));
        Attachment save = attachmentRepository.save(attachment);
        Calendar calendar = new GregorianCalendar();
        File uploadFolder =
            new File(
                path
                    + "/"
                    + calendar.get(Calendar.YEAR)
                    + "/"
                    + calendar.get(Calendar.MONTH)
                    + "/"
                    + calendar.get(Calendar.DAY_OF_MONTH));

        if (uploadFolder.mkdirs() && uploadFolder.exists()) {
          System.out.println("create folder for user profile " + uploadFolder.getAbsolutePath());
        }

        uploadFolder = uploadFolder.getAbsoluteFile();
        File file =
            new File(
                uploadFolder
                    + "/"
                    + save.getId()
                    + "_"
                    + attachment.getName()
                    + save.getExtension());
        save.setPath(file.getAbsolutePath());
        multipartFile.transferTo(file);

        Attachment save1 = attachmentRepository.save(save);
        resUploadFiles.add(
            new ResUploadFile(
                save1.getId(),
                save1.getName(),
                ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("api/attach/")
                    .path(attachment.getId().toString())
                    .toUriString(),
                save1.getAttachmentType(),
                save1.getSize()));
        return new ApiResponseModel(HttpStatus.OK.value(), "saved", resUploadFiles);
      }
      return new ApiResponseModel(HttpStatus.OK.value(), "not saved", null);

    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), e.getMessage(), null);
    }
  }

  public String getExt(String fileName) {
    String ext = null;
    if (fileName != null && !fileName.isEmpty()) {
      int dot = fileName.lastIndexOf(".");
      if (dot > 0 && dot <= fileName.length() - 2) {
        ext = fileName.substring(dot);
      }
    }
    return ext;
  }

  public HttpEntity<?> deleteUser(Long id, User user) {
    try {
      if (user.getId().equals(id)) {
        return ResponseEntity.badRequest()
            .body(
                new ApiResponseModel(
                    HttpStatus.CONFLICT.value(), "not delete because it is you ", null));
      }
      userRepository.deleteById(id);
      return ResponseEntity.status(HttpStatus.ACCEPTED)
          .body(new ApiResponseModel(HttpStatus.ACCEPTED.value(), "delete user", null));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "not delete user because this user working a lot of project",
                  null));
    }
  }
}
