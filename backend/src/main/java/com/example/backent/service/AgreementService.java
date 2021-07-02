package com.example.backent.service;

import com.example.backent.entity.Agreement;
import com.example.backent.entity.Attachment;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqAgreement;
import com.example.backent.payload.ResAgreement;
import com.example.backent.repository.AgreementRepository;
import com.example.backent.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgreementService {

    @Autowired
    AgreementRepository agreementRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

    public ApiResponseModel addAgreement(ReqAgreement reqAgreement){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try{
            Agreement agreement = new Agreement();
            agreement.setWhy(reqAgreement.getWhy());
            Optional<Attachment> optionalAttachment = attachmentRepository.findById(reqAgreement.getFileId());
            agreement.setAFile(optionalAttachment.get());
            agreementRepository.save(agreement);
            apiResponseModel.setCode(200);
            apiResponseModel.setMessage("success");
        }catch(Exception e){
            apiResponseModel.setCode(500);
            apiResponseModel.setMessage("success");
        }
        return apiResponseModel;
    }

    public ApiResponseModel getAllAgreement(){
        ApiResponseModel response = new ApiResponseModel();
        try{
            List<ResAgreement> all = agreementRepository.findAll().stream().map(this::getAgreement).collect(Collectors.toList());
            response.setCode(200);
            response.setMessage("success");
            response.setData(all);
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }

    public ResAgreement getAgreement(Agreement agreement){
        return new ResAgreement(
                agreement.getId(),
                agreement.getWhy(),
                agreement.getAFile()!=null?ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(agreement.getAFile().getId().toString()).toUriString():null
        );
    }



}
