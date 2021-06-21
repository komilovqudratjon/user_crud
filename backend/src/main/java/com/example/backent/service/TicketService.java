package com.example.backent.service;

import com.example.backent.entity.Board;
import com.example.backent.entity.Ticket;
import com.example.backent.entity.User;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqTicket;
import com.example.backent.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

  @Autowired TicketRepository ticketRepository;

  @Autowired UserRepository userRepository;

  @Autowired BoardRepository boardRepository;

  @Autowired ProgrammingLanguageRepository programmingLanguageRepository;

  @Autowired CompleteQuestionRepository completeQuestionRepository;

  public ApiResponseModel addOrEditTicket(ReqTicket reqTicket) {
    Ticket ticket = getTicket(reqTicket.getId());
    ticket.setWorkType(reqTicket.getWorkType());
    ticket.setText(reqTicket.getText());
    ticket.setWorker(getUser(reqTicket.getWorkerId()));
    ticket.setPm(getUser(reqTicket.getPmId()));
    ticket.setTester(getUser(reqTicket.getTesterId()));
    ticket.setHoursWorker(reqTicket.getHoursWorker());
    ticket.setHoursTester(reqTicket.getHoursTester());
    ticket.setBoard(getBoard(reqTicket.getBoard()));
    ticket.setProgramingLanguage(
        programmingLanguageRepository.getById(reqTicket.getProgramingLanguage()));
    ticket.setCompleteQuestion(completeQuestionRepository.getById(ticket.getId()));
    ticketRepository.save(ticket);
    return new ApiResponseModel();
  }

  public Ticket getTicket(Long id) {
    try {
      if (id != null) {
        return ticketRepository.getById(id);
      }
      return new Ticket();
    } catch (Exception e) {
      return new Ticket();
    }
  }

  public User getUser(Long id) {
    try {
      if (id != null) {
        return userRepository.getById(id);
      }
      return new User();
    } catch (Exception e) {
      return new User();
    }
  }

  public Board getBoard(Long id) {
    try {
      if (id != null) {
        return boardRepository.getById(id);
      }
      return new Board();
    } catch (Exception e) {
      return new Board();
    }
  }
}
