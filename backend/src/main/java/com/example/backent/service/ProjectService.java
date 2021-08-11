package com.example.backent.service;

import com.example.backent.entity.*;
import com.example.backent.entity.enums.BoardCondition;
import com.example.backent.payload.*;
import com.example.backent.repository.*;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

  @Autowired ProjectRepository projectRepository;

  @Autowired CompanyRepository companyRepository;

  @Autowired AgreementRepository agreementRepository;

  @Autowired TicketRepository ticketRepository;

  @Autowired UserRepository userRepository;

  @Autowired BoardRepository boardRepository;

  public ApiResponseModel addOrEditProject(ReqProject reqProject) {
    ApiResponseModel apiResponseModel = new ApiResponseModel();
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
      Optional<Company> optionalCompany = companyRepository.findById(reqProject.getCompanyId());
      if (optionalCompany.isPresent()) {
        project.setCompany(optionalCompany.get());
        if (reqProject.getAgreements() != null) {
          List<Agreement> allById = agreementRepository.findAllById(reqProject.getAgreements());
          project.setAgreementList(allById);
        }
        if (reqProject.getUsers() != null) {
          List<User> list = new ArrayList<>();
          for (int i = 0; i < reqProject.getUsers().size(); i++) {
            Optional<User> user = userRepository.findById(reqProject.getId());
            if (user.isPresent()) {
              list.add(user.get());
            } else {
              apiResponseModel.setCode(207);
              apiResponseModel.setMessage(
                  "user id:" + reqProject.getUsers().get(i) + " did not found ");
              return apiResponseModel;
            }
          }
          project.setUsers(list);
        }
        project.setStartDate(reqProject.getStartDate());
        project.setEndDate(reqProject.getEndDate());
        project.setName(reqProject.getName());
        Project project1 = projectRepository.save(project);
        boardRepository.save(new Board("name1", project1, 1L, BoardCondition.CREATED));
        boardRepository.save(new Board("name2", project1, 2L, BoardCondition.ATTACHED));
        boardRepository.save(new Board("name3", project1, 3L, BoardCondition.PROCESS));
        boardRepository.save(new Board("name4", project1, 4L, BoardCondition.TEST));
        boardRepository.save(new Board("name5", project1, 5L, BoardCondition.DONE));
      } else {
        apiResponseModel.setCode(205);
        apiResponseModel.setMessage("bunaqa idlik companiya mavjud emas");
        return apiResponseModel;
      }
      apiResponseModel.setCode(200);
      apiResponseModel.setMessage("success !");
    } catch (Exception e) {
      apiResponseModel.setCode(500);
      apiResponseModel.setMessage("error");
    }
    return apiResponseModel;
  }

  public ApiResponseModel editAgreementAndCompany(ReqProject reqProject) {
    ApiResponseModel apiResponseModel = new ApiResponseModel();
    //        List<Agreement> allById = null;
    try {
      if (reqProject.getCompanyId() == null) {
        apiResponseModel.setCode(207);
        apiResponseModel.setMessage("enter company id for edit");
        return apiResponseModel;
      }
      Optional<Project> optionalProject = projectRepository.findById(reqProject.getCompanyId());
      if (optionalProject.isPresent()) {
        if (reqProject.getAgreements() != null) {
          try {
            for (int i = 0; i < reqProject.getAgreements().size(); i++) {
              Optional<Agreement> agreement =
                  agreementRepository.findById(reqProject.getAgreements().get(i));
              if (agreement.isPresent()) {
                optionalProject.get().getAgreementList().add(agreement.get());
              } else {
                apiResponseModel.setCode(207);
                apiResponseModel.setMessage(
                    "AGREEMENT ID : " + reqProject.getAgreements().get(i) + " not found");
                return apiResponseModel;
              }
            }
            //                        optionalProject.get().getAgreementList().addAll(allById);
          } catch (Exception e) {
            apiResponseModel.setCode(500);
            apiResponseModel.setMessage("error");
            return apiResponseModel;
          }
        }
        if (reqProject.getCompanyId() != null) {
          Optional<Company> optionalCompany = companyRepository.findById(reqProject.getCompanyId());
          optionalProject.get().setCompany(optionalCompany.get());
        }
        projectRepository.save(optionalProject.get());
      } else {
        apiResponseModel.setCode(207);
        apiResponseModel.setMessage("project id did not found");
        return apiResponseModel;
      }
      apiResponseModel.setCode(200);
      apiResponseModel.setMessage("success");
    } catch (Exception e) {
      apiResponseModel.setCode(500);
      apiResponseModel.setMessage("error");
    }
    return apiResponseModel;
  }

  public ApiResponseModel getAllProjects() {
    ApiResponseModel response = new ApiResponseModel();
    try {
      List<ResProject> projectList =
          projectRepository.findAllByDeleted(false).stream()
              .map(this::getProject)
              .collect(Collectors.toList());
      response.setCode(200);
      response.setMessage("success !");
      response.setData(projectList);
    } catch (Exception e) {
      response.setCode(200);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel delete(Long id) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<Project> project = projectRepository.findById(id);
      if (project.isPresent()) {
        project.get().setDeleted(true);
        projectRepository.save(project.get());
      } else {
        response.setCode(207);
        response.setMessage("bunaqa idlik project mavjud emas !");
      }
      response.setMessage("success !");
      response.setCode(200);
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error !");
    }
    return response;
  }

  public ResProject getProject(Project project) {
    return new ResProject(
        project.getId(),
        project.getName(),
        project.getCompany(),
        getListLink(project.getAgreementList()),
        project.getUsers() != null ? getUsers(project.getUsers()) : null);
  }

  public List<ResUser> getUsers(List<User> list) {
    List<ResUser> res = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      res.add(getUser(list.get(i)));
    }
    return res;
  }

  public ApiResponseModel getOneProject(Long id) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<Project> project = projectRepository.findById(id);
      if (project.isPresent()) {
        response.setCode(200);
        response.setMessage("success");
        response.setData(getProject(project.get()));
      } else {
        response.setCode(207);
        response.setMessage("PROEJCT ID DID NOT FOUND");
      }
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public List<String> getListLink(List<Agreement> list) {
    List<String> list1 = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      list1.add(
          list.get(i).getAFile() != null
              ? ServletUriComponentsBuilder.fromCurrentContextPath()
                  .path("/api/attach/")
                  .path(list.get(i).getAFile().getId().toString())
                  .toUriString()
              : "null");
    }
    return list1;
  }

  public ApiResponseModel oneProjectsStatus(Long projectId) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<Project> project = projectRepository.findById(projectId);
      if (project.isPresent()) {
        Double backHours = ticketRepository.countAllTicketByWorkType("BACKEND", projectId);
        Double backHoursDone = ticketRepository.countDoneTicketByWorkType("BACKEND", projectId);
        Double frontendHours = ticketRepository.countAllTicketByWorkType("FRONTEND", projectId);
        Double frontendHoursDone =
            ticketRepository.countDoneTicketByWorkType("FRONTEND", projectId);
        Double designHours = ticketRepository.countAllTicketByWorkType("DESIGN", projectId);
        Double designHoursDone = ticketRepository.countDoneTicketByWorkType("DESIGN", projectId);
        ResProjectCondition res = new ResProjectCondition();
        res.setId(project.get().getId());
        res.setStartDate(project.get().getStartDate());
        res.setEndDate(project.get().getEndDate());
        if (backHours != null) {
          if (backHoursDone != null) {
            res.setBackend((backHoursDone / backHours) * 100);
          } else {
            res.setBackend(null);
          }
        }
        if (frontendHours != null) {
          if (frontendHoursDone != null) {
            res.setFrontend((frontendHoursDone / frontendHours) * 100);
          } else {
            res.setFrontend(null);
          }
        }
        if (designHours != null) {
          if (designHoursDone != null) {
            res.setDesign((designHoursDone / designHours) * 100);
          } else {
            res.setDesign(null);
          }
        }
        //                res.setAllPercentage((backHoursDone + frontendHoursDone +
        // designHoursDone)/(backHours + frontendHours + designHours));
        res.setBackends(setUsers(project.get().getUsers(), "backend"));
        res.setFrontends(setUsers(project.get().getUsers(), "frontend"));
        res.setDesigns(setUsers(project.get().getUsers(), "design"));
        res.setTesters(setUsers(project.get().getUsers(), "tester"));
        res.setQas(setUsers(project.get().getUsers(), "qu"));
        response.setData(res);
      } else {
        response.setCode(207);
        response.setMessage("PROJECT ID : " + projectId + " DID NOT DOUND");
        return response;
      }
      response.setCode(200);
      response.setMessage("success");
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel allProjectStatus() {
    ApiResponseModel response = new ApiResponseModel();
    try {
      List<ResProjectCondition> list = new ArrayList<>();
      List<Project> projects = projectRepository.findAll();
      for (int i = 0; i < projects.size(); i++) {
        ResProjectCondition oneStatus =
            (ResProjectCondition) oneProjectsStatus(projects.get(i).getId()).getData();
        list.add(oneStatus);
      }
      response.setCode(200);
      response.setMessage("success");
      response.setData(list);
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel addUserToProject(Long userId, Long projectId) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<Project> project = projectRepository.findById(projectId);
      Optional<User> user = userRepository.findById(userId);
      if (project.isPresent()) {
        if (user.isPresent()) {
          project.get().getUsers().add(user.get());
          projectRepository.save(project.get());
        } else {
          response.setCode(207);
          response.setMessage("USER IS DID NOT FOUND ");
          return response;
        }
      } else {
        response.setCode(207);
        response.setMessage("PROJECT IS DID NOT FOUND ");
        return response;
      }
      response.setCode(200);
      response.setMessage("success");
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public List<ResUser> setUsers(List<User> list, String workType) {
    List<ResUser> all = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      for (int j = 0; j < list.get(i).getFields().size(); j++) {
        if (list.get(i).getFields().get(j).getName().equals(workType)) {
          all.add(getUser(list.get(i)));
        }
      }
    }
    return all;
  }

  public ResUser getUser(User user) {
    return new ResUser(
        user.getId(),
        user.getFirstname(),
        user.getEmail(),
        user.getAvatar() != null
            ? ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/attach/")
                .path(user.getAvatar().getId().toString())
                .toUriString()
            : null);
  }

  public HttpEntity<?> getTickets(Long id) {
    try {
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new ApiResponseModel(
                  HttpStatus.OK.value(), "here it is", ticketRepository.findAllByProject_id11(id)));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "field",
                  List.of(new ErrorsField("error", "undefined mistake"))));
    }
  }

  public HttpEntity<?> generateTz(Long id) throws IOException {
    Project project = projectRepository.getById(id);
    List<Ticket> ticketList = ticketRepository.findAllByProject_id11(id);
    File document = GenerationTzPDF.generationPDF(project, ticketList);
    return ResponseEntity.ok()
        .contentType(
            MediaType.parseMediaType(Files.probeContentType(Path.of(document.getAbsolutePath()))))
        .body(Files.readAllBytes(Path.of(document.getAbsolutePath())));
  }
}
