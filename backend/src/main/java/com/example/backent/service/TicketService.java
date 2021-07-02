package com.example.backent.service;

import com.example.backent.entity.*;
import com.example.backent.entity.enums.TicketCondition;
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
    Ticket ticket = new Ticket();
    ticket.setWorkType(reqTicket.getWorkType());
    ticket.setText(reqTicket.getText());
    ticket.setTicketCondition(TicketCondition.CREATED);
    ticket.setWorker(getUser(reqTicket.getWorkerId()));
    ticket.setPm(getUser(reqTicket.getPmId()));
    ticket.setTester(getUser(reqTicket.getTesterId()));
    ticket.setHoursWorker(reqTicket.getHoursWorker());
    ticket.setHoursTester(reqTicket.getHoursTester());
    ticket.setBoard(getBoard(reqTicket.getBoard()));
    ticket.setProgramingLanguage(getProgramingLanguage(reqTicket.getProgramingLanguage()));
    ticket.setCompleteQuestion(getCompleteQuestion(ticket.getId()));
    ticketRepository.save(ticket);
    return new ApiResponseModel(200,"success");
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

  public ProgramingLanguage getProgramingLanguage(Long id) {
    try {
      if (id != null) {
        return programmingLanguageRepository.getById(id);
      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public CompleteQuestion getCompleteQuestion(Long id) {
    try {
      if (id != null) {
        return completeQuestionRepository.getById(id);
      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public User getUser(Long id) {
    try {
      if (id != null) {
        return userRepository.getById(id);
      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public Board getBoard(Long id) {
    try {
      if (id != null) {
        return boardRepository.getById(id);
      }
      return null;
    } catch (Exception e) {
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

  public ApiResponseModel backLock(Long projectId , String condition){
    ApiResponseModel response= new ApiResponseModel();
    try{
      List<ResTicket> backlog = ticketRepository.selectByConditionAndProjectId(projectId,condition).stream().map(this::getTicket).collect(Collectors.toList());
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
//            if(board.get().getCondition().name()=="DONE"){
//              ticket.get().setTicketCondition(TicketCondition.valueOf("DONE"));
//            }
//            if(board.get().getCondition().name()=="PROCESS"){
//              ticket.get().setTicketCondition(TicketCondition.valueOf("DONE"));
//            }
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
            ticket.get().setTicketCondition(TicketCondition.ATTACHED);
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
          ticket.get().setWorkType(reqTicket.getWorkType());
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
