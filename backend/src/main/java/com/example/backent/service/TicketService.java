package com.example.backent.service;

import com.example.backent.entity.*;
import com.example.backent.entity.enums.BoardCondition;
import com.example.backent.entity.enums.WorkType;
import com.example.backent.payload.*;
import com.example.backent.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

  @Autowired TicketRepository ticketRepository;

  @Autowired UserRepository userRepository;

  @Autowired BoardRepository boardRepository;

  @Autowired ProgrammingLanguageRepository programmingLanguageRepository;

  @Autowired CompleteQuestionRepository completeQuestionRepository;

  @Autowired ProjectRepository projectRepository;

  @Autowired ProjectTypeRepository projectTypeRepository;

  public ApiResponseModel addOrEditTicket(ReqTicket reqTicket) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Ticket ticket = new Ticket();
      if (reqTicket.getWorkType() != null) {
        ticket.setWorkType(reqTicket.getWorkType());
      } else {
        response.setCode(207);
        response.setMessage("ENTER WORK TYPE");
        return response;
      }
      ticket.setText(reqTicket.getText());
      if (reqTicket.getWorkerId() != null) {
        Optional<User> worker = userRepository.findById(reqTicket.getWorkerId());
        if (worker.isPresent()) {
          ticket.setWorker(worker.get());
          //          ticket.setBoard(getOneBoard(reqTicket.getProject(), BoardCondition.ATTACHED));
        } else {
          response.setCode(207);
          response.setMessage("USER ID DID NOT FOUND");
          return response;
        }
      } else {
        //        ticket.setBoard(getOneBoard(reqTicket.getProject(), BoardCondition.CREATED));
      }
      if (reqTicket.getPmId() != null) {
        Optional<User> user = userRepository.findById(reqTicket.getPmId());
        if (user.isPresent()) {
          ticket.setPm(user.get());
        } else {
          response.setCode(207);
          response.setMessage("PM ID DID NOT FOUND ");
          return response;
        }
      }
      if (reqTicket.getTesterId() != null) {
        Optional<User> user = userRepository.findById(reqTicket.getTesterId());
        if (user.isPresent()) {
          ticket.setTester(user.get());
        } else {
          response.setCode(207);
          response.setMessage("TESTER ID DID NOT FOUND ");
          return response;
        }
      }
      ticket.setHoursWorker(reqTicket.getHoursWorker());
      ticket.setHoursTester(reqTicket.getHoursTester());
      if (reqTicket.getProgramingLanguage() != null) {
        Optional<ProgramingLanguage> language =
            programmingLanguageRepository.findById(reqTicket.getProgramingLanguage());
        if (language.isPresent()) {
          ticket.setProgramingLanguage(language.get());
        } else {
          response.setCode(207);
          response.setMessage("LANGUAGE ID DID NOT FOUND");
          return response;
        }
      }
      if (reqTicket.getCompleteQuestion() != null) {
        Optional<CompleteQuestion> question =
            completeQuestionRepository.findById(reqTicket.getCompleteQuestion());
        if (question.isPresent()) {
          ticket.setCompleteQuestion(question.get());
        } else {
          response.setCode(207);
          response.setMessage("QUESTION ID DID NOT FOUND");
          return response;
        }
      }
      ticket.setBoard(boardRepository.getById(reqTicket.getBoard()));
      response.setData(ticketRepository.save(ticket));
      response.setCode(200);
      response.setMessage("success");
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public HttpEntity<?> saveTicket(ReqTicket reqTicket) {

    try {
      String info = "save";
      Ticket ticket = new Ticket();
      User pm = null;
      User worker = null;
      User tester = null;
      Board board = null;
      ProgramingLanguage programingLanguage = null;
      CompleteQuestion completeQuestion = null;
      ProjectType projectType = null;
      if (reqTicket.getPmId() != null) {
        Optional<User> pmO = userRepository.findById(reqTicket.getPmId());
        if (pmO.isPresent()) {
          pm = pmO.get();
        }
      }
      if (reqTicket.getPmId() != null) {
        Optional<User> workerO = userRepository.findById(reqTicket.getWorkerId());
        if (workerO.isPresent()) {
          worker = workerO.get();
        } else {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(new ErrorsField("worker", "not found worker"))));
        }
      }
      if (reqTicket.getPmId() != null) {
        Optional<User> testerO = userRepository.findById(reqTicket.getTesterId());
        if (testerO.isPresent()) {
          tester = testerO.get();
        } else {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(new ErrorsField("tester", "not found tester"))));
        }
      }
      if (reqTicket.getProgramingLanguage() != null) {
        Optional<ProgramingLanguage> optionalProgramingLanguage =
            programmingLanguageRepository.findById(reqTicket.getProgramingLanguage());
        if (optionalProgramingLanguage.isPresent()) {
          programingLanguage = optionalProgramingLanguage.get();
        } else {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(
                          new ErrorsField("programingLanguage", "not found programingLanguage"))));
        }
      }
      if (reqTicket.getCompleteQuestion() != null) {
        Optional<CompleteQuestion> optionalCompleteQuestion =
            completeQuestionRepository.findById(reqTicket.getCompleteQuestion());
        if (optionalCompleteQuestion.isPresent()) {
          completeQuestion = optionalCompleteQuestion.get();
        } else {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(new ErrorsField("completeQuestion", "not found completeQuestion"))));
        }
      }
      if (reqTicket.getType() != null) {
        Optional<ProjectType> optionalProjectType =
            projectTypeRepository.findById(reqTicket.getType());
        if (optionalProjectType.isPresent()) {
          projectType = optionalProjectType.get();
        } else {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(new ErrorsField("projectType", "not found projectType"))));
        }
      }
      if (reqTicket.getBoard() != null) {
        Optional<Board> optionalBoard = boardRepository.findById(reqTicket.getBoard());
        if (optionalBoard.isPresent()) {
          board = optionalBoard.get();
        } else {
          return ResponseEntity.badRequest()
              .body(
                  new ApiResponseModel(
                      HttpStatus.CONFLICT.value(),
                      "field",
                      List.of(new ErrorsField("board", "not found board"))));
        }
      }

      if (reqTicket.getId() != null) {
        Optional<Ticket> ticketOptional = ticketRepository.findById(reqTicket.getId());
        if (ticketOptional.isPresent()) {
          ticket = ticketOptional.get();
          info = "edit";
        }
      }

      ticket.setPm(pm);
      ticket.setTester(tester);
      ticket.setWorker(worker);
      ticket.setText(reqTicket.getText());
      ticket.setWorkType(reqTicket.getWorkType());
      ticket.setHoursWorker(reqTicket.getHoursWorker());
      ticket.setHoursTester(reqTicket.getHoursTester());
      ticket.setProgramingLanguage(programingLanguage);
      ticket.setProjectType(projectType);
      ticket.setTicketCondition(reqTicket.getTicketCondition());
      ticket.setBoard(board);
      ticket.setCompleteQuestion(completeQuestion);
      ticketRepository.save(ticket);

      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponseModel(HttpStatus.OK.value(), info, ticket));

    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "field",
                  List.of(new ErrorsField("error", "sorry unspecified error"))));
    }
  }

  public ApiResponseModel getTicket(Long id) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<Ticket> ticket = ticketRepository.findById(id);
      if (ticket.isPresent()) {
        response.setData(getTicket(ticket.get()));
        response.setCode(200);
        response.setMessage("success");
      } else {
        response.setCode(207);
        response.setMessage("id did not found");
      }
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public Board getOneBoard(Long projectId, BoardCondition condition) {
    Optional<Board> board = boardRepository.findByProjectIdAndCondition(projectId, condition);
    if (board.isPresent()) {
      return board.get();
    } else {
      return null;
    }
  }

  public ApiResponseModel deleteTicket(Long id) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<Ticket> ticket = ticketRepository.findById(id);
      if (ticket.isPresent()) {
        ticket.get().setDeleted(true);
        ticketRepository.save(ticket.get());
        response.setCode(200);
        response.setMessage("success");
      } else {
        response.setCode(207);
        response.setMessage("id did not found");
      }
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel getTicketByBoardCondition(Long projectId, String condition) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      List<ResTicket> backlog =
          ticketRepository.selectByConditionAndProjectId(projectId, condition).stream()
              .map(this::getTicket)
              .collect(Collectors.toList());
      response.setData(backlog);
      response.setCode(200);
      response.setMessage("success");
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel getBecklog(Long projectId) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      List<ResTicket> backlog =
          ticketRepository.getBacklog(projectId).stream()
              .map(this::getTicket)
              .collect(Collectors.toList());
      response.setData(backlog);
      response.setCode(200);
      response.setMessage("success");
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel filterTicket(Long projectTypeId, String type, Long tagId) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      List<ResTicket> all =
          ticketRepository.findAllByTypeAndWorkType(projectTypeId, type, tagId).stream()
              .map(this::getTicket)
              .collect(Collectors.toList());
      response.setCode(200);
      response.setMessage("success!");
      response.setData(all);
    } catch (Exception e) {
      response.setCode(200);
      response.setMessage("success!");
    }
    return response;
  }

  public ResTicket getTicket(Ticket ticket) {
    return new ResTicket(
        ticket.getId(),
        ticket.getWorkType(),
        ticket.getText(),
        ticket.getWorker() != null
            ? new ResUser(
                ticket.getWorker().getId(),
                ticket.getWorker().getFirstname(),
                ticket.getWorker().getEmail(),
                ticket.getWorker() != null
                    ? ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/attach/")
                        .path(ticket.getWorker().getId().toString())
                        .toUriString()
                    : null)
            : null,
        ticket.getWorker() != null
            ? new ResUser(
                ticket.getWorker().getId(),
                ticket.getWorker().getFirstname(),
                ticket.getWorker().getEmail(),
                ticket.getWorker() != null
                    ? ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/attach/")
                        .path(ticket.getWorker().getId().toString())
                        .toUriString()
                    : null)
            : null,
        ticket.getWorker() != null
            ? new ResUser(
                ticket.getWorker().getId(),
                ticket.getWorker().getFirstname(),
                ticket.getWorker().getEmail(),
                ticket.getWorker() != null
                    ? ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/attach/")
                        .path(ticket.getWorker().getId().toString())
                        .toUriString()
                    : null)
            : null,
        ticket.getHoursWorker(),
        ticket.getHoursTester(),
        ticket.getProgramingLanguage() != null
            ? new ResLanguage(
                ticket.getProgramingLanguage().getId(),
                ticket.getProgramingLanguage().getLogo() != null
                    ? ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/attach/")
                        .path(ticket.getProgramingLanguage().getLogo().getId().toString())
                        .toUriString()
                    : null,
                ticket.getProgramingLanguage().getName())
            : null,
        ticket.getCompleteQuestion() != null
            ? new ResCompleteQuestion(
                ticket.getCompleteQuestion().getId(),
                ticket.getCompleteQuestion().getQuestionPhoto() != null
                    ? ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/attach/")
                        .path(ticket.getCompleteQuestion().getQuestionPhoto().getId().toString())
                        .toUriString()
                    : null,
                ticket.getCompleteQuestion().getText(),
                ticket.getCompleteQuestion().getLink())
            : null,
        ticket.getTag());
  }
}
