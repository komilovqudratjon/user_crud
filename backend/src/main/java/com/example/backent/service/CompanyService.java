package com.example.backent.service;

import com.example.backent.entity.Agreement;
import com.example.backent.entity.Company;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqCompany;
import com.example.backent.repository.AgreementRepository;
import com.example.backent.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

  @Autowired CompanyRepository companyRepository;
  @Autowired AgreementRepository agreementRepository;

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
      } else {
        apiResponseModel.setCode(207);
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
        response.setCode(207);
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
      response.setMessage("error");
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
        response.setCode(207);
        response.setMessage("bunaqa idlik campaniya mavjud emas");
      }
    } catch (Exception e) {
      response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
      response.setMessage("error");
    }
    return response;
  }

  public ReqCompany getOneCompany(Company company) {
    return new ReqCompany(company.getId(), company.getName());
  }
}
