package com.example.backent.service;

import com.example.backent.entity.*;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqTicket;
import com.example.backent.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    ticket.setProgramingLanguage(getProgramingLanguage(reqTicket.getProgramingLanguage()));
    ticket.setCompleteQuestion(getCompleteQuestion(ticket.getId()));
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

  public ProgramingLanguage getProgramingLanguage(Long id) {
    try {
      if (id != null) {
        return programmingLanguageRepository.getById(id);
      }
      return new ProgramingLanguage();
    } catch (Exception e) {
      return new ProgramingLanguage();
    }
  }

  public CompleteQuestion getCompleteQuestion(Long id) {
    try {
      if (id != null) {
        return completeQuestionRepository.getById(id);
      }
      return new CompleteQuestion();
    } catch (Exception e) {
      return new CompleteQuestion();
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

  public ApiResponseModel deleteTicket(Long id) {
    try {
      ticketRepository.deleteById(id);
      return new ApiResponseModel(HttpStatus.OK.value(), "delete");
    } catch (Exception e) {
      return new ApiResponseModel(HttpStatus.CONTINUE.value(), "not delete");
    }
  }
}
