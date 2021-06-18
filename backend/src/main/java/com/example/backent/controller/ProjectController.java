package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqProject;
import com.example.backent.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @PostMapping
    public HttpEntity<?> addProject(@RequestBody ReqProject reqProject){
        ApiResponseModel response = projectService.addOrEditProject(reqProject);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public HttpEntity<?> editProject(@RequestBody ReqProject reqProject){
        ApiResponseModel response = projectService.editAgreementAndCompany(reqProject);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll(){
        ApiResponseModel response = projectService.getAllProjects();
        return ResponseEntity.ok(response);
    }


}
