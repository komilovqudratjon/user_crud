package com.example.backent.service;

import com.example.backent.entity.Attachment;
import com.example.backent.entity.ProgramingLanguage;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqLanguage;
import com.example.backent.payload.ResLanguage;
import com.example.backent.repository.AttachmentRepository;
import com.example.backent.repository.ProgramingLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgrammingLanguageService {

  @Autowired ProgramingLanguageRepository programingLanguageRepository;
  @Autowired AttachmentRepository attachmentRepository;

  public ApiResponseModel addOrEditLanguage(ReqLanguage reqLanguage) {
    ApiResponseModel response = new ApiResponseModel();
    ProgramingLanguage language = new ProgramingLanguage();
    try {
      if (reqLanguage.getId() != null) {
        Optional<ProgramingLanguage> optionalLanguage = programingLanguageRepository.findByIdAndDeleted(reqLanguage.getId(),false);
        if (optionalLanguage.isPresent()) {
          language = optionalLanguage.get();
        }else{
          response.setCode(207);
          response.setMessage("LANGUAGE ID : "+reqLanguage.getId()+" did not found");
          return response;
        }
      }
      if(reqLanguage.getLogo()!=null){
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(reqLanguage.getLogo());
        if (optionalAttachment.isPresent()) {
          language.setLogo(optionalAttachment.get());
        }else{
          response.setCode(207);
          response.setMessage("LANGUAGE PHOTO ID : "+reqLanguage.getLogo()+" did not found");
          return response;
        }
      }
      language.setName(reqLanguage.getName());
      programingLanguageRepository.save(language);
      response.setCode(200);
      response.setMessage("saved");
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error!");
    }
    return response;
  }

  public ApiResponseModel deleteLanguage(Long id) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<ProgramingLanguage> optionalLanguage = programingLanguageRepository.findByIdAndDeleted(id,false);
      if (optionalLanguage.isPresent()) {
        optionalLanguage.get().setDeleted(true);
        programingLanguageRepository.save(optionalLanguage.get());
      } else {
        response.setMessage("LANGUAGE ID : "+id+" DID NOT FOUND ");
        response.setCode(207);
        return response;
      }
      response.setMessage("saved");
      response.setCode(200);
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel getAllLanguages() {
    ApiResponseModel response = new ApiResponseModel();
    try {
      List<ResLanguage> resLanguageList =
          programingLanguageRepository.findAllByDeleted(false).stream()
              .map(this::getLanguage)
              .collect(Collectors.toList());
      response.setCode(200);
      response.setMessage("success");
      response.setData(resLanguageList);
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel getOneLanguage(Long id) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<ProgramingLanguage> language = programingLanguageRepository.findByIdAndDeleted(id,false);
      if (language.isPresent()) {
        ResLanguage resLanguage = getLanguage(language.get());
        response.setMessage("success !");
        response.setCode(200);
        response.setData(resLanguage);
      } else {
        response.setMessage("LANGUAGE ID : "+id+" DID NOT FOUND");
        response.setCode(207);
      }
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ResLanguage getLanguage(ProgramingLanguage language) {
    return new ResLanguage(
        language.getId(),
        language.getLogo()!=null?ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(language.getLogo().getId().toString()).toUriString():null,
        language.getName());
  }
}
