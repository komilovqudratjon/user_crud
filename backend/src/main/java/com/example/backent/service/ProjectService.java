package com.example.backent.service;

import com.example.backent.entity.Agreement;
import com.example.backent.entity.Company;
import com.example.backent.entity.Project;
import com.example.backent.entity.ProjectType;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqProject;
import com.example.backent.payload.ResProject;
import com.example.backent.repository.AgreementRepository;
import com.example.backent.repository.CompanyRepository;
import com.example.backent.repository.ProjectRepository;
import com.example.backent.repository.ProjectTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    AgreementRepository agreementRepository;
    @Autowired
    ProjectTypeRepository typeRepository;

    public ApiResponseModel addOrEditProject(ReqProject reqProject) {
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        Company company = null;
        Project project = new Project();
        try {
            if (reqProject.getId() != null) {
                Optional<Project> optionalProject = projectRepository.findById(reqProject.getId());
                if (optionalProject.isPresent()) {
                    project = optionalProject.get();
                } else {
                    apiResponseModel.setCode(205);
                    apiResponseModel.setMessage("bunaqa id lik project mavjud emas");
                    return apiResponseModel;
                }
            }
            Optional<ProjectType> type = typeRepository.findById(reqProject.getType());
            if (type.isPresent()){
                project.setType(type.get());
            }else {
                apiResponseModel.setCode(207);
                apiResponseModel.setMessage("bunaqa id lik type mavjud emas");
                return apiResponseModel;
            }
            Optional<Company> optionalCompany = companyRepository.findById(reqProject.getCompanyId());
            if (optionalCompany.isPresent()) {
                project.setCompany(optionalCompany.get());
                if (reqProject.getAgreements() != null) {
                    List<Agreement> allById = agreementRepository.findAllById(reqProject.getAgreements());
                    project.setAgreementList(allById);
                }
                project.setName(reqProject.getName());
            } else {
                apiResponseModel.setCode(205);
                apiResponseModel.setMessage("bunaqa idlik companiya mavjud emas");
            }
        } catch (Exception e) {
            apiResponseModel.setCode(500);
            apiResponseModel.setMessage("error");
        }
        return apiResponseModel;
    }

    public ApiResponseModel editAgreementAndCompany(ReqProject reqProject) {
        ApiResponseModel apiResponseModel = new ApiResponseModel();
        List<Agreement> allById = null;
        try {
            Optional<Project> optionalProject = projectRepository.findById(reqProject.getId());
            if (optionalProject.isPresent()) {
                if (reqProject.getAgreements() != null) {
                    try {
                        allById = agreementRepository.findAllById(reqProject.getAgreements());
                        optionalProject.get().setAgreementList(allById);
                    } catch (Exception e) {
                        apiResponseModel.setCode(500);
                        apiResponseModel.setMessage("error");
                    }
                }
                if (reqProject.getCompanyId() != null) {
                    Optional<Company> optionalCompany = companyRepository.findById(reqProject.getCompanyId());
                    optionalProject.get().setCompany(optionalCompany.get());
                }
            } else {
                apiResponseModel.setCode(207);
                apiResponseModel.setMessage("project id did not found");
            }
            apiResponseModel.setCode(200);
            apiResponseModel.setMessage("success");
        } catch (Exception e) {
            apiResponseModel.setCode(500);
            apiResponseModel.setMessage("error");
        }
        return apiResponseModel;
    }

    public ApiResponseModel getAllProjects(){
        ApiResponseModel response = new ApiResponseModel();
        try{
            List<ResProject> projectList = projectRepository.findAll().stream().map(this::getProject).collect(Collectors.toList());
            response.setCode(200);
            response.setMessage("success !");
            response.setData(projectList);
        }catch(Exception e){
            response.setCode(200);
            response.setMessage("error");
        }
        return response;
    }

    public ResProject getProject(Project project){
        return new ResProject(
                project.getId(),
                project.getName(),
                project.getType(),
                project.getCompany(),
                getListLink(project.getAgreementList())
        );
    }

    public List<String> getListLink(List<Agreement> list){
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            list1.add(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(list.get(i).getAFile().getId().toString()).toUriString());
        }
        return list1;
    }
}
