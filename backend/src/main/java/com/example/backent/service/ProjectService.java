package com.example.backent.service;

import com.example.backent.entity.*;
import com.example.backent.payload.*;
import com.example.backent.repository.*;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

  public HttpEntity<?> addOrEditProject(ReqProject reqProject) throws ParseException {

    String info = "save";

    User pm = null;
    Company company = null;
    Project project = new Project();

    if (reqProject.getId() != null) {
      Optional<Project> projectOptional = projectRepository.findById(reqProject.getId());
      if (projectOptional.isPresent()) {
        project = projectOptional.get();
        info = "edit";
      }
    }

    if (reqProject.getResponsible() != null) {
      Optional<User> pmO = userRepository.findById(reqProject.getResponsible());
      if (pmO.isPresent()) {
        pm = pmO.get();
      } else {
        return ResponseEntity.badRequest()
            .body(
                new ApiResponseModel(
                    HttpStatus.CONFLICT.value(),
                    "field",
                    List.of(new ErrorsField("responsible", "not found responsible"))));
      }
    }
    if (reqProject.getCompanyId() != null) {
      Optional<Company> company1 = companyRepository.findById(reqProject.getCompanyId());
      if (company1.isPresent()) {
        company = company1.get();
      } else {
        return ResponseEntity.badRequest()
            .body(
                new ApiResponseModel(
                    HttpStatus.CONFLICT.value(),
                    "field",
                    List.of(new ErrorsField("worker", "not found worker"))));
      }
    }

    if (reqProject.getUsers() != null && !reqProject.getUsers().isEmpty()) {
      project.setUsers(userRepository.findAllByIdIn(reqProject.getUsers()));
    }

    if (reqProject.getAgreements() != null && !reqProject.getAgreements().isEmpty()) {
      project.setAgreementList(agreementRepository.findAllByIdIn(reqProject.getAgreements()));
    }

    project.setCompany(company);
    project.setName(reqProject.getName());
    project.setProjectType(reqProject.getProjectType());
    project.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(reqProject.getStartDate()));
    project.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(reqProject.getEndDate()));
    project.setResponsible(pm);

    projectRepository.save(project);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponseModel(HttpStatus.OK.value(), info, project));
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
        //        res.setStartDate(project.get().getStartDate());
        //        res.setEndDate(project.get().getEndDate());
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
