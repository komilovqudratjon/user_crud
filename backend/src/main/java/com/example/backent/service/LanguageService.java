package com.example.backent.service;

import com.example.backent.entity.Attachment;
import com.example.backent.entity.ProgramingLanguage;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqLanguage;
import com.example.backent.payload.ResLanguage;
import com.example.backent.repository.AttachmentRepository;
import com.example.backent.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LanguageService {

    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    AttachmentRepository attachmentRepository;

    public ApiResponseModel addOrEditLanguage(ReqLanguage reqLanguage){
        ApiResponseModel response = new ApiResponseModel();
        ProgramingLanguage language = new ProgramingLanguage();
        try{
            if(reqLanguage.getId()!=null){
                Optional<ProgramingLanguage> optionalLanguage = languageRepository.findById(reqLanguage.getId());
                if(optionalLanguage.isPresent()){
                    language = optionalLanguage.get();
                }
            }
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(reqLanguage.getLogo());
            if(optionalAttachment.isPresent()){
                language.setLogo(optionalAttachment.get());
            }
            language.setName(reqLanguage.getName());
            languageRepository.save(language);
            response.setCode(200);
            response.setMessage("saved");
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error!");
        }
        return response;
    }

    public ApiResponseModel deleteLanguage(Long id) {
        ApiResponseModel response = new ApiResponseModel();
        try{
            Optional<ProgramingLanguage> optionalLanguage = languageRepository.findById(id);
            if(optionalLanguage.isPresent()){
                optionalLanguage.get().setDeleted(false);
                languageRepository.save(optionalLanguage.get());
            }else{
                response.setMessage("saved");
                response.setCode(200);
            }
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }

    public ApiResponseModel getAllLanguages(){
        ApiResponseModel response = new ApiResponseModel();
        try{
            List<ResLanguage> resLanguageList = languageRepository.findAllByDeleted(false).stream().map(this::getLanguage).collect(Collectors.toList());
            response.setCode(200);
            response.setMessage("success");
            response.setData(resLanguageList);
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }

    public ApiResponseModel getOneLanguage(Long id){
        ApiResponseModel response = new ApiResponseModel();
        try{
            Optional<ProgramingLanguage> language = languageRepository.findById(id);
            if (language.isPresent()){
                ResLanguage resLanguage = getLanguage(language.get());
                response.setMessage("success !");
                response.setCode(200);
                response.setData(resLanguage);
            }else {
                response.setMessage("error !");
                response.setCode(500);
            }
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }

    public ResLanguage getLanguage(ProgramingLanguage language){
        return new ResLanguage(
                language.getId(),
                ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(language.getLogo().getId().toString()).toUriString(),
                language.getName()
        );
    }
}