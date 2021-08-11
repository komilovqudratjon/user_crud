package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ErrorsField;
import com.example.backent.payload.ReqProject;
import com.example.backent.payload.ReqTicket;
import com.example.backent.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

  @Autowired TicketService ticketService;

  // **************** TICKET CREATE OR EDIT  ****************//
  //  @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
  @PostMapping
  public HttpEntity<?> addTicketOrEdit(
      @Valid @RequestBody ReqTicket reqTicket, BindingResult error) {
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
    return ticketService.saveTicket(reqTicket);
  }

  // **************** DELETE TICKET ****************//
  @DeleteMapping("/{id}")
  public ApiResponseModel deleteTicket(@PathVariable Long id) {
    return ticketService.deleteTicket(id);
  }

  @GetMapping("/ticket/by/board")
  public HttpEntity<?> tickets(@RequestParam Long projectId, @RequestParam String condition) {
    ApiResponseModel response = ticketService.getTicketByBoardCondition(projectId, condition);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/backlog")
  public HttpEntity<?> backlog(@RequestParam Long projectId) {
    ApiResponseModel response = ticketService.getBecklog(projectId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public HttpEntity<?> editTicket(@PathVariable Long id) {
    ApiResponseModel response = ticketService.getTicket(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/filter")
  public HttpEntity<?> filter(
      @RequestParam Long projectId, @RequestParam String type, @RequestParam Long tagId) {
    ApiResponseModel response = ticketService.filterTicket(projectId, type, tagId);
    return ResponseEntity.ok(response);
  }
}
