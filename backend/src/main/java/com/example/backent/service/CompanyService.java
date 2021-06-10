package com.example.backent.service;

import com.example.backent.entity.Agreement;
import com.example.backent.entity.Company;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqCompany;
import com.example.backent.repository.AgreementRepository;
import com.example.backent.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    AgreementRepository agreementRepository;

    public ApiResponseModel addCompany(ReqCompany reqCompany){
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        try {
            Company company = new Company();
            company.setName(reqCompany.getName());
            company.setId(reqCompany.getId());
            apiResponseModel.setCode(200);
            apiResponseModel.setMessage("success");
        }catch(Exception e){
            apiResponseModel.setCode(500);
            apiResponseModel.setMessage("saqlashda xatolik");
        }
        return apiResponseModel;
    }

//    public ApiResponseModel addAgreement(ReqCompany reqCompany){
//        ApiResponseModel apiResponseModel = new ApiResponseModel();
//        Company company = null;
//        List<Agreement> list = null;
//        try{
//            Optional<Company> optionalCompany = companyRepository.findById(reqCompany.getId());
//            if (optionalCompany.isPresent()){
//                company = optionalCompany.get();
//                try{
//                    list = agreementRepository.findAllById(reqCompany.getAgreement());
//                    company.setAgreement(list);
//                }catch(Exception e){
//                    apiResponseModel.setCode(207);
//                    apiResponseModel.setMessage("agreement id did not found");
//                }
//            }else {
//                apiResponseModel.setCode(206);
//                apiResponseModel.setMessage("company did not found");
//            }
//        }catch(Exception e){
//            apiResponseModel.setCode(500);
//            apiResponseModel.setMessage("error");
//        }
//        return apiResponseModel;
//    }

}
