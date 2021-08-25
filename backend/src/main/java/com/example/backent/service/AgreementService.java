package com.example.backent.service;

import com.example.backent.entity.Agreement;
import com.example.backent.entity.Attachment;
import com.example.backent.entity.enums.AttachmentType;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ResAgreement;
import com.example.backent.payload.ResUploadFile;
import com.example.backent.repository.AgreementRepository;
import com.example.backent.repository.AttachmentRepository;
import com.example.backent.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

@Service
public class AgreementService {

  @Autowired AgreementRepository agreementRepository;

  @Autowired AttachmentRepository attachmentRepository;

  @Autowired ProjectRepository projectRepository;

  @Value("${upload.folder}")
  private String path;

  //
  //    public ApiResponseModel addAgreement(ReqAgreement reqAgreement){
  //        ApiResponseModel apiResponseModel = new ApiResponseModel();
  //        try{
  //            Agreement agreement = new Agreement();
  //            agreement.setWhy(reqAgreement.getWhy());
  //            Optional<Attachment> optionalAttachment =
  // attachmentRepository.findById(reqAgreement.getFileId());
  //            if(optionalAttachment.isPresent()){
  //                agreement.setAFile(optionalAttachment.get());
  //                agreementRepository.save(agreement);
  //            }else{
  //                apiResponseModel.setCode(207);
  //                apiResponseModel.setMessage("FILE ID DID NOT FOUND");
  //                return apiResponseModel;
  //            }
  //            apiResponseModel.setCode(200);
  //            apiResponseModel.setMessage("success");
  //        }catch(Exception e){
  //            apiResponseModel.setCode(500);
  //            apiResponseModel.setMessage("success");
  //        }
  //        return apiResponseModel;
  //    }

  //    public ApiResponseModel getAllAgreement(){
  //        ApiResponseModel response = new ApiResponseModel();
  //        try{
  //            List<ResAgreement> all =
  // agreementRepository.findAll().stream().map(this::getAgreement).collect(Collectors.toList());
  //            response.setCode(200);
  //            response.setMessage("success");
  //            response.setData(all);
  //        }catch(Exception e){
  //            response.setCode(500);
  //            response.setMessage("error");
  //        }
  //        return response;
  //    }

  //  public ResAgreement getAgreement(Agreement agreement) {
  //    return new ResAgreement(
  //        agreement.getId(),
  //        agreement.getWhy(),
  //        agreement.getAFile() != null
  //            ? ServletUriComponentsBuilder.fromCurrentContextPath()
  //                .path("/api/attach/")
  //                .path(agreement.getAFile().getId().toString())
  //                .toUriString()
  //            : null);
  //  }

  public ApiResponseModel agreement(String why, MultipartHttpServletRequest request) {
    try {

      Iterator<String> iterator = request.getFileNames();
      MultipartFile multipartFile;
      if (iterator.hasNext()) {
        multipartFile = request.getFile(iterator.next());
        Attachment attachment = new Attachment();
        assert multipartFile != null;
        if (getExt(multipartFile.getOriginalFilename()) == null || multipartFile.getSize() == 0) {
          return new ApiResponseModel(HttpStatus.CONFLICT.value(), "file is invalid");
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

        Agreement agreement = new Agreement();
        agreement.setWhy(why);
        agreement.setAFile(save1);
        Agreement save2 = agreementRepository.save(agreement);

        HashMap<String, Object> map = new HashMap<>();
        map.put(
            "photo",
            new ResUploadFile(
                save1.getId(),
                save1.getName(),
                ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("api/attach/")
                    .path(attachment.getId().toString())
                    .toUriString(),
                save1.getAttachmentType(),
                save1.getSize()));
        map.put("AgreementId", save2.getId());
        map.put("Agreement", save2.getWhy());
        return new ApiResponseModel(HttpStatus.OK.value(), "saved", map);
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

  public ApiResponseModel getByProject(Long id) {
    try {
      return new ApiResponseModel(
          HttpStatus.OK.value(),
          "saved",
          projectRepository.getById(id).getAgreementList().stream()
              .map(
                  agreement ->
                      new ResAgreement(
                          agreement.getId(),
                          agreement.getWhy(),
                          agreement.getAFile() != null
                              ? ServletUriComponentsBuilder.fromCurrentContextPath()
                                  .path("/api/attach/")
                                  .path(agreement.getAFile().getId().toString())
                                  .toUriString()
                              : null))
              .collect(Collectors.toList()));
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONFLICT.value(), e.getMessage(), null);
    }
  }
}
