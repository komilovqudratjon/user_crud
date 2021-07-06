package com.example.backent.service;

import com.example.backent.entity.*;
import com.example.backent.entity.enums.BoardCondition;
import com.example.backent.entity.enums.TicketCondition;
import com.example.backent.entity.enums.WorkType;
import com.example.backent.payload.*;
import com.example.backent.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    try{
      Ticket ticket = new Ticket();
      if(reqTicket.getWorkType()!=null){
        ticket.setWorkType(WorkType.valueOf(reqTicket.getWorkType()));
      }else{
        response.setCode(207);
        response.setMessage("ENTER WORK TYPE");
        return response;
      }
      ticket.setText(reqTicket.getText());
      if(reqTicket.getWorkerId()!=null){
        Optional<User> worker = userRepository.findById(reqTicket.getWorkerId());
        if(worker.isPresent()){
          ticket.setWorker(worker.get());
          ticket.setBoard(getOneBoard(reqTicket.getProject(),BoardCondition.ATTACHED));
        }else{
          response.setCode(207);
          response.setMessage("USER ID DID NOT FOUND");
          return response;
        }
      }else{
        ticket.setBoard(getOneBoard(reqTicket.getProject(),BoardCondition.CREATED));
      }
      if(reqTicket.getPmId()!=null){
        Optional<User> user = userRepository.findById(reqTicket.getPmId());
        if(user.isPresent()){
          ticket.setPm(user.get());
        }else{
          response.setCode(207);
          response.setMessage("PM ID DID NOT FOUND ");
          return response;
        }
      }
      if(reqTicket.getTesterId()!=null){
        Optional<User> user = userRepository.findById(reqTicket.getTesterId());
        if(user.isPresent()){
          ticket.setTester(user.get());
        }else{
          response.setCode(207);
          response.setMessage("TESTER ID DID NOT FOUND ");
          return response;
        }
      }
      ticket.setHoursWorker(reqTicket.getHoursWorker());
      ticket.setHoursTester(reqTicket.getHoursTester());
      if(reqTicket.getProgramingLanguage()!=null){
        Optional<ProgramingLanguage> language = programmingLanguageRepository.findById(reqTicket.getProgramingLanguage());
        if(language.isPresent()){
          ticket.setProgramingLanguage(language.get());
        }else{
          response.setCode(207);
          response.setMessage("LANGUAGE ID DID NOT FOUND");
          return response;
        }
      }
      if(reqTicket.getCompleteQuestion()!=null){
        Optional<CompleteQuestion> question = completeQuestionRepository.findById(reqTicket.getCompleteQuestion());
        if(question.isPresent()){
          ticket.setCompleteQuestion(question.get());
        }else{
          response.setCode(207);
          response.setMessage("QUESTION ID DID NOT FOUND");
          return response;
        }
      }
      ticketRepository.save(ticket);
      response.setCode(200);
      response.setMessage("success");
    }catch(Exception e){
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
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

  public Board getOneBoard(Long projectId,BoardCondition condition){
    Optional<Board> board = boardRepository.findByProjectIdAndCondition(projectId, condition);
    if(board.isPresent()){
      return board.get();
    }else{
      return null;
    }
  }

  public ApiResponseModel deleteTicket(Long id) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<Ticket> ticket = ticketRepository.findById(id);
      if(ticket.isPresent()){
        ticket.get().setDeleted(true);
        ticketRepository.save(ticket.get());
        response.setCode(200);
        response.setMessage("success");
      }else{
        response.setCode(207);
        response.setMessage("id did not found");
      }
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel getTicketByBoardCondition(Long projectId , String condition){
    ApiResponseModel response= new ApiResponseModel();
    try{
      List<ResTicket> backlog = ticketRepository.selectByConditionAndProjectId(projectId,condition)
              .stream()
              .map(this::getTicket)
              .collect(Collectors.toList());
      response.setData(backlog);
      response.setCode(200);
      response.setMessage("success");
    }catch(Exception e){
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel getBecklog(Long projectId){
    ApiResponseModel response= new ApiResponseModel();
    try{
      List<ResTicket> backlog = ticketRepository.getBacklog(projectId)
              .stream()
              .map(this::getTicket)
              .collect(Collectors.toList());
      response.setData(backlog);
      response.setCode(200);
      response.setMessage("success");
    }catch(Exception e){
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public ApiResponseModel filterTicket(Long projectTypeId , String type , Long tagId){
    ApiResponseModel response = new ApiResponseModel();
    try{
      List<ResTicket> all = ticketRepository.findAllByTypeAndWorkType(projectTypeId, type, tagId).stream().map(this::getTicket).collect(Collectors.toList());
      response.setCode(200);
      response.setMessage("success!");
      response.setData(all);
    }catch(Exception e){
      response.setCode(200);
      response.setMessage("success!");
    }
    return response;
  }

  public ResTicket getTicket(Ticket ticket){
    return new ResTicket(
            ticket.getId(),
            ticket.getWorkType(),
            ticket.getText(),
            ticket.getWorker()!=null ? new ResUser(
                    ticket.getWorker().getId(),
                    ticket.getWorker().getFirstname(),
                    ticket.getWorker().getEmail(),
                    ticket.getWorker()!=null?
                            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(ticket.getWorker().getId().toString()).toUriString():null
                    ) : null,
            ticket.getWorker()!=null ? new ResUser(
                    ticket.getWorker().getId(),
                    ticket.getWorker().getFirstname(),
                    ticket.getWorker().getEmail(),
                    ticket.getWorker()!=null?
                            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(ticket.getWorker().getId().toString()).toUriString():null
            ) : null,
            ticket.getWorker()!=null ? new ResUser(
                    ticket.getWorker().getId(),
                    ticket.getWorker().getFirstname(),
                    ticket.getWorker().getEmail(),
                    ticket.getWorker()!=null?
                            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(ticket.getWorker().getId().toString()).toUriString():null
            ) : null,
            ticket.getHoursWorker(),
            ticket.getHoursTester(),
            ticket.getProgramingLanguage()!=null ? new ResLanguage(
                    ticket.getProgramingLanguage().getId(),
                    ticket.getProgramingLanguage().getLogo()!=null?
                            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(ticket.getProgramingLanguage().getLogo().getId().toString()).toUriString():null,
                    ticket.getProgramingLanguage().getName()
            ):null,
            ticket.getCompleteQuestion()!=null?new ResCompleteQuestion(
                    ticket.getCompleteQuestion().getId(),
                    ticket.getCompleteQuestion().getQuestionPhoto()!=null?
                            ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path( ticket.getCompleteQuestion().getQuestionPhoto().getId().toString()).toUriString():null,
                    ticket.getCompleteQuestion().getText(),
                    ticket.getCompleteQuestion().getLink()
            ):null,
            ticket.getTag()
    );
  }

  public ApiResponseModel editTicket(ReqTicket reqTicket){
    ApiResponseModel response = new ApiResponseModel();
    try{
      if(reqTicket.getId()==null){
        response.setCode(207);
        response.setMessage("ENTER ID OF TICKET");
        return response;
      }
      Optional<Ticket> ticket = ticketRepository.findById(reqTicket.getId());
      if(ticket.isPresent()){
        if(reqTicket.getBoard()!=null){
          Optional<Board> board = boardRepository.findById(reqTicket.getBoard());
          if(board.isPresent()){
            ticket.get().setBoard(board.get());
          }else{
            response.setCode(207);
            response.setMessage("bunaqa idlik board mavjud emas!");
            return response;
          }
        }
        if(reqTicket.getCompleteQuestion()!=null){
          Optional<CompleteQuestion> question = completeQuestionRepository.findById(reqTicket.getCompleteQuestion());
          if(question.isPresent()){
            System.out.println(question.get());
            ticket.get().setCompleteQuestion(question.get());
          }else{
            response.setCode(207);
            response.setMessage("bunaqa idlik complate question mavjud emas!");
            return response;
          }
        }
        if(reqTicket.getHoursWorker()!=null){
          ticket.get().setHoursWorker(reqTicket.getHoursWorker());
        }
        if(reqTicket.getWorkerId()!=null){
          Optional<User> worker = userRepository.findById(reqTicket.getWorkerId());
          if(worker.isPresent()){
            ticket.get().setWorker(worker.get());
//            ticket.get().setTicketCondition(TicketCondition.ATTACHED);
          }else{
            response.setCode(207);
            response.setMessage("bunaqa idlik worker mavjud emas");
            return response;
          }
        }
        if(reqTicket.getPmId()!=null){
          Optional<User> pm = userRepository.findById(reqTicket.getPmId());
          if(pm.isPresent()){
            ticket.get().setPm(pm.get());
          }else{
            response.setCode(207);
            response.setMessage("bunaqa idlik user mavjud emas");
            return response;
          }
        }
        if(reqTicket.getTesterId()!=null){
          Optional<User> tester = userRepository.findById(reqTicket.getTesterId());
          if(tester.isPresent()){
            ticket.get().setTester(tester.get());
          }else{
            response.setCode(207);
            response.setMessage("bunaqa idlik ue mavjud emas");
            return response;
          }
        }
        if(reqTicket.getHoursTester()!=null){
          ticket.get().setHoursTester(reqTicket.getHoursTester());
        }
        if(reqTicket.getText()!=null){
          ticket.get().setText(reqTicket.getText());
        }
        if(reqTicket.getWorkType()!=null){
          ticket.get().setWorkType(WorkType.valueOf(reqTicket.getWorkType()));
        }
        if(reqTicket.getType()!=null){
          Optional<ProjectType> type = projectTypeRepository.findById(reqTicket.getType());
          if(type.isPresent()){
            ticket.get().setProjectType(type.get());
          }else{
            response.setCode(207);
            response.setMessage("bunaqa idlik type mavjud emas");
            return response;
          }
        }
        ticketRepository.save(ticket.get());
        response.setCode(200);
        response.setMessage("success");
      }else{
        response.setCode(207);
        response.setMessage("bunaqa idlik ticket mavjud emas");
      }
    }catch(Exception e){
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }
}
