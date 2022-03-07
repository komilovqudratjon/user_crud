package com.example.backend.service;

import com.example.backend.entity.Attachment;
import com.example.backend.entity.User;
import com.example.backend.exception.ResourceException;
import com.example.backend.payload.*;
import com.example.backend.repository.AttachmentRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Komilov Qudrtajon
 * @link Telegram Link https://t.me/qudratjon03031999
 * @since 31/01/22
 */
@Service
public class AuthService {

    private final UserRepository userRepository;

    @Value("${upload.folder}")
    private String path;

    private final AttachmentRepository attachmentRepository;
    private final UserService userService;


    @Autowired
    public AuthService(
            UserRepository userRepository,
            AttachmentRepository attachmentRepository, UserService userService) {
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.userService = userService;
    }

    public HttpEntity<?> register(ReqSignUp reqSignUp) throws ParseException {

        User user = new User();

        String info = "";

        if (reqSignUp.getId() == null) {
            if (userRepository.existsByPhoneNumber(reqSignUp.getPhoneNumber())) {
                return ResponseEntity.badRequest()
                        .body(
                                new ApiResponseModel(
                                        HttpStatus.CONFLICT.value(),
                                        "field",
                                        List.of(new ErrorsField("phoneNumber", "this phoneNumber is busy"))));
            }
            if (userRepository.existsByPinfl(reqSignUp.getPinfl())) {
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
                            new SimpleDateFormat("yyyy-MM-dd").parse(reqSignUp.getDateOfBirth()),
                            reqSignUp.getGender(),
                            reqSignUp.getNation(),
                            reqSignUp.getAddressOfBirth(),
                            reqSignUp.getCitizenship(),
                            reqSignUp.getPassportGivenTime() == null ? null : new SimpleDateFormat("yyyy-MM-dd").parse(reqSignUp.getPassportGivenTime()),
                            reqSignUp.getPassportWhoGave(),
                            reqSignUp.getPinfl(),
                            reqSignUp.getPhoneNumber(),
                            reqSignUp.getPhotoId() == null
                                    ? null
                                    : attachmentRepository.findById(reqSignUp.getPhotoId()).orElse(null)
                    );
            info = "create";
        } else {
            Optional<User> userOptional = userRepository.findById(reqSignUp.getId());
            if (userOptional.isPresent()) {
                if (userRepository.existsByPhoneNumberAndIdNot(
                        reqSignUp.getPhoneNumber(), reqSignUp.getId())) {
                    return ResponseEntity.badRequest()
                            .body(
                                    new ApiResponseModel(
                                            HttpStatus.CONFLICT.value(),
                                            "field",
                                            List.of(new ErrorsField("phoneNumber", "this phoneNumber is busy"))));
                }
                if (userRepository.existsByPinflAndIdNot(
                        reqSignUp.getPinfl(), reqSignUp.getId())) {
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
                user.setMiddleName(reqSignUp.getMiddleName());
                user.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(reqSignUp.getDateOfBirth()));
                user.setGender(reqSignUp.getGender());
                user.setNation(reqSignUp.getNation());
                user.setAddressOfBirth(reqSignUp.getAddressOfBirth());
                user.setCitizenship(reqSignUp.getCitizenship());
                user.setPassportGivenTime(new SimpleDateFormat("yyyy-MM-dd").parse(reqSignUp.getPassportGivenTime()));
                user.setPassportWhoGave(reqSignUp.getPassportWhoGave());
                user.setPinfl(reqSignUp.getPinfl());
                user.setPhoneNumber(reqSignUp.getPhoneNumber());
                userRepository.save(user);
                user.setPhoto(
                        reqSignUp.getPhotoId() == null
                                ? null
                                : attachmentRepository.findById(reqSignUp.getPhotoId()).orElse(null));
            } else {
                return ResponseEntity.badRequest()
                        .body(new ResourceException(HttpStatus.CONFLICT.value(), "not found this id", null));
            }
            info = "edit";
        }

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponseModel(
                                HttpStatus.OK.value(),
                                info,
                                userService.makeReqUser(user)
                        )
                );
    }

    public ApiResponseModel uploadPhotoFileList(MultipartHttpServletRequest request) {
        try {

            Iterator<String> iterator = request.getFileNames();
            MultipartFile multipartFile;
            List<ResUploadFile> resUploadFiles = new ArrayList<>();
            while (iterator.hasNext()) {
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
                                save1.getSize()));
            }
            return new ApiResponseModel(HttpStatus.OK.value(), "saved", resUploadFiles);

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

    public HttpEntity<?> deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponseModel(HttpStatus.OK.value(), "delete user", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(
                            new ApiResponseModel(
                                    HttpStatus.CONFLICT.value(),
                                    "not delete user because this user working a lot of project",
                                    null));
        }
    }

    public HttpEntity<?> registerForm(ReqSignUpForm reqSignUp) {
        User user = new User();
        Optional<User> userOptional = userRepository.findById(reqSignUp.getId());
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setCurrentStatus(reqSignUp.getCurrentStatus());
            user.setSusceptibilityToDisease(reqSignUp.getSusceptibilityToDisease());
            user.setPropensityToAssassinate(reqSignUp.getPropensityToAssassinate());
            user.setWeaknessesAndStrengths(reqSignUp.getWeaknessesAndStrengths());
            user.setSocialResponsibility(reqSignUp.getSocialResponsibility());
            user.setPositionToConform(reqSignUp.getPositionToConform());
            if(reqSignUp.getAnotherPhotos()!=null) {
                List<Attachment> attachments = new ArrayList<>();
                for (Long anotherPhoto : reqSignUp.getAnotherPhotos()) {
                    attachments.add(attachmentRepository.findById(anotherPhoto).orElse(null));
                }
                user.setAnotherPhotos(attachments);
            }
            userRepository.save(user);


            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseModel(
                                    HttpStatus.OK.value(),
                                    "success",
                                    userService.makeReqUser(user)
                            )
                    );

        } else {
            return ResponseEntity.badRequest()
                    .body(new ResourceException(HttpStatus.CONFLICT.value(), "not found this id", null));
        }
    }
}
