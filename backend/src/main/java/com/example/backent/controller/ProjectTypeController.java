package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.service.ProjectTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project/type")
public class ProjectTypeController {

    @Autowired
    ProjectTypeService projectTypeService;

    @PostMapping("/add")
    public HttpEntity<?> addType(@RequestParam String type){
        ApiResponseModel response = projectTypeService.addType(type);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteType(@PathVariable Long id){
        ApiResponseModel response = projectTypeService.deleteType(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll(){
        ApiResponseModel response = projectTypeService.getAll();
        return ResponseEntity.ok(response);
    }

}
