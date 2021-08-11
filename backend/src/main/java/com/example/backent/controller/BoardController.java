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

  @Autowired BoardService boardService;

  @PostMapping
  public HttpEntity<?> addBoard(@RequestBody ReqBoard reqBoard) {
    return boardService.addBoard(reqBoard);
  }

  @DeleteMapping("/{id}")
  public HttpEntity<?> deleteBoard(@PathVariable Long id) {
    return boardService.deleteBoard(id);
  }

  @GetMapping("/all/{projectId}")
  public HttpEntity<?> getBoards(@PathVariable Long projectId) {
    ApiResponseModel response = boardService.getByProject(projectId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/getTickets/{boardId}")
  public HttpEntity<?> getTickets(@PathVariable Long boardId) {
    return boardService.getTickets(boardId);
  }
}
