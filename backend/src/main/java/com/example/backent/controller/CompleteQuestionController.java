package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqFullQuestion;
import com.example.backent.service.CompleteQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/full/question")
public class CompleteQuestionController {

    @Autowired
    CompleteQuestionService service;

    @PostMapping
    public HttpEntity<?> addFullQuestion(@RequestBody ReqFullQuestion reqFullQuestion){
        ApiResponseModel response = service.addOrEditFullQuestion(reqFullQuestion);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteFullQuestion(@PathVariable Long id){
        ApiResponseModel response = service.deleteComplateQuestion(id);
        return ResponseEntity.ok(response);
    }

}
