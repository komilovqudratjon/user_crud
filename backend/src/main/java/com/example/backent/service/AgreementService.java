package com.example.backent.service;

import com.example.backent.entity.Agreement;
import com.example.backent.entity.Attachment;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqAgreement;
import com.example.backent.repository.AgreementRepository;
import com.example.backent.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            apiResponseModel.setCode(200);
            apiResponseModel.setMessage("success");
        }catch(Exception e){
            apiResponseModel.setCode(500);
            apiResponseModel.setMessage("success");
        }
        return apiResponseModel;
    }

}
