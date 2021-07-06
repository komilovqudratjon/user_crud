package com.example.backent.service;

import com.example.backent.entity.FieldsForUsers;
import com.example.backent.entity.User;
import com.example.backent.entity.UsersLanguage;
import com.example.backent.payload.*;
import com.example.backent.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final UserFieldsRepository userFieldsRepository;

  private final UserExperiencesRepository userExperiencesRepository;

  private final UserLanguageRepository usersLanguageRepository;

  private final ProgrammingLanguageRepository programmingLanguageRepository;

  @Autowired
  public UserService(
      UserRepository userRepository,
      RoleRepository roleRepository,
      UserFieldsRepository userFieldsRepository,
      UserExperiencesRepository userExperiencesRepository,
      UserLanguageRepository usersLanguageRepository,
      ProgrammingLanguageRepository programmingLanguageRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.userFieldsRepository = userFieldsRepository;
    this.userExperiencesRepository = userExperiencesRepository;
    this.usersLanguageRepository = usersLanguageRepository;
    this.programmingLanguageRepository = programmingLanguageRepository;
  }

  public ApiResponseModel saveLanguage(String language) {
    try {
      usersLanguageRepository.save(new UsersLanguage(language));
      return new ApiResponseModel(HttpStatus.OK.value(), "saved");
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), e.getMessage());
    }
  }

  public ApiResponseModel getLanguage() {
    try {
      return new ApiResponseModel(
          HttpStatus.OK.value(),
          "here it is",
          usersLanguageRepository.findAll().stream()
              .map(
                  usersLanguage ->
                      new ReqIdAndName(usersLanguage.getId(), usersLanguage.getName())));
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), e.getMessage());
    }
  }

  public ApiResponseModel deleteLanguage(Long id) {
    try {
      usersLanguageRepository.deleteById(id);
      return new ApiResponseModel(HttpStatus.OK.value(), "delete");
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), e.getMessage());
    }
  }

  public ApiResponseModel saveFields(String language) {
    try {
      userFieldsRepository.save(new FieldsForUsers(language));
      return new ApiResponseModel(HttpStatus.OK.value(), "saved");
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), e.getMessage());
    }
  }

  public ApiResponseModel getFields() {
    try {
      return new ApiResponseModel(
          HttpStatus.OK.value(),
          "here it is",
          userFieldsRepository.findAll().stream()
              .map(
                  fieldsForUsers ->
                      new ReqIdAndName(fieldsForUsers.getId(), fieldsForUsers.getName())));
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), e.getMessage());
    }
  }

  public ApiResponseModel deleteDelete(Long id) {
    try {
      userFieldsRepository.deleteById(id);
      return new ApiResponseModel(HttpStatus.OK.value(), "delete");
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), e.getMessage());
    }
  }

  public HttpEntity<?> getPageable(
      Optional<Integer> page, Optional<Integer> size, Optional<String> sortBy) {
    try {
      UserPageable userPageable = new UserPageable();
      Page<User> id =
          userRepository.findAllByDeleted(
              false,
              PageRequest.of(
                  page.orElse(0), size.orElse(5), Sort.Direction.ASC, sortBy.orElse("id")));
      userPageable.setPageable(id.getPageable());
      userPageable.setEmpty(id.isEmpty());
      userPageable.setSort(id.getSort());
      userPageable.setFirst(id.isFirst());
      userPageable.setLast(id.isLast());
      userPageable.setNumber(id.getNumber());
      userPageable.setSize(id.getSize());
      userPageable.setTotalPages(id.getTotalPages());
      userPageable.setNumberOfElements(id.getNumberOfElements());
      userPageable.setContent(
          id.stream()
              .map(
                  user ->
                      new ReqUser(
                          user.getId(),
                          user.getFirstname(),
                          user.getLastname(),
                          user.getMiddlename(),
                          user.getAddress(),
                          user.getWorkTimeType(),
                          user.getFamily(),
                          user.getPassportNumber(),
                          user.getDateOfBirth(),
                          user.getStartWorkingTime(),
                          user.getPhoneNumber(),
                          user.getEmail(),
                          user.getFields().stream()
                              .map(
                                  fieldsForUsers ->
                                      new ReqIdAndName(
                                          fieldsForUsers.getId(), fieldsForUsers.getName()))
                              .collect(Collectors.toList()),
                          user.getExperiences(),
                          user.getLanguages().stream()
                              .map(
                                  usersLanguage ->
                                      new ReqIdAndName(
                                          usersLanguage.getId(), usersLanguage.getName()))
                              .collect(Collectors.toList()),
                          user.getProgramingLanguages(),
                          user.getAvatar() == null
                              ? null
                              : ServletUriComponentsBuilder.fromCurrentContextPath()
                                  .path("/api/attach/")
                                  .path(user.getAvatar().getId().toString())
                                  .toUriString(),
                          user.getRoles()))
              .collect(Collectors.toList()));
      return ResponseEntity.status(HttpStatus.ACCEPTED)
          .body(new ApiResponseModel(HttpStatus.OK.value(), "users", userPageable));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "The request is incorrect",
                  List.of(new ErrorsField("error", e.getMessage()))));
    }
  }
}
