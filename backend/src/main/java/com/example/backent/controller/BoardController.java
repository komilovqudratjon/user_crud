package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqBoard;
import com.example.backent.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @PostMapping
    public HttpEntity<?> addBoard(@RequestBody ReqBoard reqBoard){
        ApiResponseModel response = boardService.addBoard(reqBoard);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public HttpEntity<?> akcem(@RequestBody ReqBoard reqBoard){
        ApiResponseModel response = boardService.editBoard(reqBoard);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteBoard(@PathVariable Long id){
        ApiResponseModel response = boardService.deleteBoard(id);
        return ResponseEntity.ok(response);
    }

}
