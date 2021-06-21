package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqLanguage;
import com.example.backent.repository.LanguageRepository;
import com.example.backent.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/language")
public class LanguageController {

    @Autowired
    LanguageService languageService;

    @PostMapping
    public HttpEntity<?> addLanguage(@RequestBody ReqLanguage reqLanguage){
        ApiResponseModel response = languageService.addOrEditLanguage(reqLanguage);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteLanguage(@PathVariable Long id){
        ApiResponseModel response = languageService.deleteLanguage(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll(){
        ApiResponseModel response = languageService.getAllLanguages();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/one/{id}")
    public HttpEntity<?> getOne(@PathVariable Long id){
        ApiResponseModel response = languageService.getOneLanguage(id);
        return ResponseEntity.ok(response);
    }
}
