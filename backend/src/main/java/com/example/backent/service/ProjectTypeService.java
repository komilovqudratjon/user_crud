package com.example.backent.service;

import com.example.backent.entity.ProjectType;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.repository.ProjectTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectTypeService {

  @Autowired ProjectTypeRepository projectTypeRepository;

  public ApiResponseModel addType(String type) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      if (!projectTypeRepository.existsByNameAndDeleted(type, false)) {
        ProjectType projectType = new ProjectType();
        projectType.setName(type);
        projectTypeRepository.save(projectType);
        response.setCode(200);
        response.setMessage("success");
      } else {
        response.setCode(207);
        response.setMessage("bunaqa nomli type mavjud");
      }
    } catch (Exception e) {
      response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel deleteType(Long id) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<ProjectType> type = projectTypeRepository.findByIdAndDeleted(id, false);
      if (type.isPresent()) {
        type.get().setDeleted(true);
        projectTypeRepository.save(type.get());
        response.setCode(200);
        response.setMessage("success !");
      } else {
        response.setCode(207);
        response.setMessage("bunaqa idlik type mavjud emas");
      }
    } catch (Exception e) {
      response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel getAll() {
    ApiResponseModel response = new ApiResponseModel();
    try {
      response.setCode(200);
      response.setMessage("success");
      response.setData(projectTypeRepository.findAllByDeleted(false));
    } catch (Exception e) {
      response.setCode(200);
      response.setMessage("error");
    }
    return response;
  }
}
