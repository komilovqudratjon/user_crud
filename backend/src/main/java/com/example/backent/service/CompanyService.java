package com.example.backent.service;

import com.example.backent.entity.Agreement;
import com.example.backent.entity.Company;
import com.example.backent.payload.*;
import com.example.backent.repository.AgreementRepository;
import com.example.backent.repository.AttachmentRepository;
import com.example.backent.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {

  @Autowired CompanyRepository companyRepository;

  @Autowired AgreementRepository agreementRepository;

  @Autowired AttachmentRepository attachmentRepository;

  public HttpEntity<?> addOrEditCompany(ReqCompany reqCompany) {
    if (reqCompany.getId() == null) {
      if (companyRepository.existsByName(reqCompany.getName())) {
        return ResponseEntity.badRequest()
            .body(
                new ApiResponseModel(
                    HttpStatus.CONFLICT.value(),
                    "field",
                    List.of(new ErrorsField("name", "this name is busy"))));
      }

      Company company = new Company();
      company.setName(reqCompany.getName());
      company.setResponsiblePerson(reqCompany.getResponsiblePerson());
      company.setBalance(reqCompany.getBalance());
      company.setOked(reqCompany.getOked());
      company.setMfo(reqCompany.getMfo());
      company.setStir(reqCompany.getStir());
      company.setPhoneNumber(reqCompany.getPhoneNumber());
      company.setEmail(reqCompany.getEmail());
      company.setAddress(reqCompany.getAddress());
      Company save = companyRepository.save(company);
      return ResponseEntity.status(HttpStatus.ACCEPTED)
          .body(new ApiResponseModel(HttpStatus.OK.value(), "save", save));
    } else {
      Optional<Company> optionalCompany = companyRepository.findById(reqCompany.getId());
      if (optionalCompany.isPresent()) {
        if (companyRepository.existsByNameAndIdNot(reqCompany.getName(), reqCompany.getId())) {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(new ErrorsField("name", "this name is busy"))));
        }
        Company company = optionalCompany.get();
        company.setName(reqCompany.getName());
        company.setResponsiblePerson(reqCompany.getResponsiblePerson());
        company.setBalance(reqCompany.getBalance());
        company.setOked(reqCompany.getOked());
        company.setMfo(reqCompany.getMfo());
        company.setStir(reqCompany.getStir());
        company.setPhoneNumber(reqCompany.getPhoneNumber());
        company.setEmail(reqCompany.getEmail());
        company.setAddress(reqCompany.getAddress());
        Company save = companyRepository.save(company);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(new ApiResponseModel(HttpStatus.OK.value(), "edit", save));
      }
      return ResponseEntity.badRequest()
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "field",
                  List.of(new ErrorsField("id", "id not found"))));
    }
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
      List<ResCompany> companyList =
          companyRepository.findAllByDeleted(false).stream()
              .map(this::getCompany)
              .collect(Collectors.toList());
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
        response.setData(getCompany(optionalCompany.get()));
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

  public ResCompany getCompany(Company company) {
    return new ResCompany(
        company.getId(),
        company.getName(),
        company.getResponsiblePerson(),
        company.getBalance(),
        company.getOked(),
        company.getMfo(),
        company.getStir(),
        company.getPhoneNumber(),
        company.getEmail(),
        company.getAddress(),
        company.getAgreement() != null
            ? company.getAgreement().stream().map(this::getAgreement).collect(Collectors.toList())
            : null);
  }

  public ResAgreement getAgreement(Agreement agreement) {
    return new ResAgreement(
        agreement.getId(),
        agreement.getWhy(),
        agreement.getAFile() != null
            ? ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/attach/")
                .path(agreement.getAFile().getId().toString())
                .toUriString()
            : null);
  }
}
