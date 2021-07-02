package com.example.backent.controller;

import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqProject;
import com.example.backent.payload.ReqTicket;
import com.example.backent.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

  @Autowired TicketService ticketService;

  // **************** TICKET CREATE OR EDIT  ****************//
  @PostMapping
  public ApiResponseModel addTicketOrEdit(@RequestBody ReqTicket reqTicket) {
    return ticketService.addOrEditTicket(reqTicket);
  }

  // **************** DELETE TICKET ****************//
  @DeleteMapping("/{id}")
  public ApiResponseModel deleteTicket(@PathVariable Long id) {
    return ticketService.deleteTicket(id);
  }

  @GetMapping("/backlog")
  public HttpEntity<?> backlog(@RequestParam Long projectId,@RequestParam String condition){
    ApiResponseModel response = ticketService.backLock(projectId,condition);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public HttpEntity<?> editTicket(@PathVariable Long id){
    ApiResponseModel response = ticketService.getTicket(id);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/filter")
  public HttpEntity<?> filter(@RequestParam Long projectId , @RequestParam String type, @RequestParam Long tagId){
    ApiResponseModel response = ticketService.filterTicket(projectId, type, tagId);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/edit")
  public HttpEntity<?> editTicket(@RequestBody ReqTicket reqTicket){
    ApiResponseModel response = ticketService.editTicket(reqTicket);
    return ResponseEntity.ok(response);
  }

}
