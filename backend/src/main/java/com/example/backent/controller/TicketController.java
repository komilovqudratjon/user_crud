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

  @Autowired TicketService projectService;

  @PostMapping
  public ApiResponseModel addTicket(@RequestBody ReqTicket reqTicket) {
    return projectService.addOrEditTicket(reqTicket);
  }
}
