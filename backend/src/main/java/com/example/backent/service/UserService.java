package com.example.backent.service;

import com.example.backent.entity.FieldsForUsers;
import com.example.backent.entity.UsersLanguage;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqIdAndName;
import com.example.backent.repository.*;
import com.example.backent.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

  public Object getAllUsers(Integer page, Integer size, String search) {
    try {
      return userRepository
          .findAllByLastnameContainingOrMiddlenameContainingOrFirstnameContainingOrPhoneNumberContainingOrPassportNumberContainingOrEmailContainingOrAddressContaining(
              CommonUtils.getPageable(page, size),
              search,
              search,
              search,
              search,
              search,
              search,
              search);
    } catch (Exception e) {
      return null;
    }
  }
}
