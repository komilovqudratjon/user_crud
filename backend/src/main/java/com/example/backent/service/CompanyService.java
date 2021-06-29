package com.example.backent.service;

import com.example.backent.entity.Company;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqCompany;
import com.example.backent.repository.AgreementRepository;
import com.example.backent.repository.AttachmentRepository;
import com.example.backent.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CompanyService {

  @Autowired CompanyRepository companyRepository;
  @Autowired AgreementRepository agreementRepository;
  @Autowired AttachmentRepository attachmentRepository;

  public ApiResponseModel addOrEditCompany(ReqCompany reqCompany) {
    ApiResponseModel apiResponseModel = new ApiResponseModel();
    Company company = new Company();
    try {
      if (reqCompany.getId() != null) {
        Optional<Company> optionalCompany = companyRepository.findById(reqCompany.getId());
        if (optionalCompany.isPresent()) {
          company = optionalCompany.get();
        }
      }

      if (!companyRepository.existsByName(reqCompany.getName())) {
        company.setName(reqCompany.getName());
        company.setResponsiblePerson(reqCompany.getResponsiblePerson());
        company.setBalance(reqCompany.getBalance());
        company.setOked(reqCompany.getOked());
        company.setMfo(reqCompany.getMfo());
        company.setStir(reqCompany.getStir());
        company.setPhoneNumber(reqCompany.getPhoneNumber());
        company.setEmail(reqCompany.getEmail());
        company.setAddress(reqCompany.getAddress());
        company.setAgreement(
            Stream.concat(
                    company.getAgreement().stream(),
                    agreementRepository.findAllByIdIn(reqCompany.getDeleteFile()).stream())
                .collect(Collectors.toList()));
      } else {
        apiResponseModel.setCode(HttpStatus.MULTI_STATUS.value());
        apiResponseModel.setMessage("bunaqa nomli comoany mavjud !");
        return apiResponseModel;
      }
      companyRepository.save(company);
      apiResponseModel.setCode(HttpStatus.OK.value());
      apiResponseModel.setMessage("success");
    } catch (Exception e) {
      apiResponseModel.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      apiResponseModel.setMessage("saqlashda xatolik");
    }
    return apiResponseModel;
  }

  public ApiResponseModel deleteCompany(Long companyId) {
    ApiResponseModel response = new ApiResponseModel();
    Company company = null;
    try {
      Optional<Company> optionalCompany =
          Optional.ofNullable(companyRepository.findByIdAndDeleted(companyId, false));
      if (optionalCompany.isPresent()) {
        company = optionalCompany.get();
        company.setDeleted(true);
        companyRepository.save(company);
      } else {
        response.setCode(HttpStatus.MULTI_STATUS.value());
        response.setMessage("company did not found");
      }
      response.setMessage("deleted");
      response.setCode(HttpStatus.OK.value());
    } catch (Exception e) {
      response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setMessage("siz companiyani uchira olmaysiz");
    }
    return response;
  }

  public ApiResponseModel getAllCompany() {
    ApiResponseModel response = new ApiResponseModel();
    try {
      List<Company> companyList = companyRepository.findAllByDeleted(false);
      response.setCode(HttpStatus.OK.value());
      response.setMessage("success");
      response.setData(companyList);
    } catch (Exception e) {
      response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    }
    return response;
  }

  public ApiResponseModel getOneCompany(Long companyId) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<Company> optionalCompany =
          Optional.ofNullable(companyRepository.findByIdAndDeleted(companyId, false));
      if (optionalCompany.isPresent()) {
        response.setData(optionalCompany);
        response.setCode(HttpStatus.OK.value());
        response.setMessage("success");
      } else {
        response.setCode(HttpStatus.MULTI_STATUS.value());
        response.setMessage("bunaqa idlik campaniya mavjud emas");
      }
    } catch (Exception e) {
      response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setMessage("error");
    }
    return response;
  }
}
