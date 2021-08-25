package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ErrorsField;
import com.example.backent.payload.ReqProject;
import com.example.backent.service.ProjectService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

  @Autowired ProjectService projectService;

  @PostMapping
  public HttpEntity<?> addProject(@RequestBody ReqProject reqProject, BindingResult error)
      throws ParseException {
    if (error.hasErrors()) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "field",
                  error.getFieldErrors().stream()
                      .map(
                          fieldError ->
                              new ErrorsField(
                                  fieldError.getField(), fieldError.getDefaultMessage()))));
    }
    return projectService.addOrEditProject(reqProject);
  }

  @PostMapping("/add/user")
  public HttpEntity<?> addUser(@RequestParam Long userId, @RequestParam Long projectId) {
    ApiResponseModel response = projectService.addUserToProject(userId, projectId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/all")
  public HttpEntity<?> getAll() {
    ApiResponseModel response = projectService.getAllProjects();
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public HttpEntity<?> deleteAll(@PathVariable Long id) {
    ApiResponseModel response = projectService.delete(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/process/{id}")
  public HttpEntity<?> getProcess(@PathVariable Long id) {
    ApiResponseModel response = projectService.oneProjectsStatus(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/all/status")
  public HttpEntity<?> allProjectsStatus() {
    ApiResponseModel response = projectService.allProjectStatus();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/get/one/{id}")
  public HttpEntity<?> getOneProject(@PathVariable Long id) {
    ApiResponseModel response = projectService.getOneProject(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/getTickets/{id}")
  public HttpEntity<?> getTickets(@PathVariable Long id) {
    return projectService.getTickets(id);
  }

  @GetMapping("/generateTz/{id}")
  public HttpEntity<?> generateTz(@PathVariable Long id) throws IOException {
    return projectService.generateTz(id);
  }
}
