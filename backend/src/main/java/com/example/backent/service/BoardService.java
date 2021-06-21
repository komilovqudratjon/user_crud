package com.example.backent.service;

import com.example.backent.entity.Board;
import com.example.backent.entity.Project;
import com.example.backent.entity.template.AbsEntity;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqBoard;
import com.example.backent.repository.BoardRepository;
import com.example.backent.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Optional;

@Service
public class BoardService {

  @Autowired BoardRepository boardRepository;
  @Autowired ProjectRepository projectRepository;

  public ApiResponseModel addBoard(ReqBoard reqBoard) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Board board = new Board();
      board.setName(reqBoard.getName());
      response.setMessage("success !");
      response.setCode(HttpStatus.OK.value());
    } catch (Exception e) {
      response.setMessage("error !");
      response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    return response;
  }

  public ApiResponseModel editBoard(ReqBoard reqBoard) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      Optional<Board> board = boardRepository.findById(reqBoard.getId());
      if (board.isPresent()) {
        board.get().setName(reqBoard.getName());
        Optional<Project> optionalProject = projectRepository.findById(reqBoard.getProject());
        if (optionalProject.isPresent()) {
          board.get().setProject(optionalProject.get());
        }
        response.setMessage("success !");
        response.setCode(HttpStatus.OK.value());
      } else {
        response.setMessage("success !");
        response.setCode(HttpStatus.OK.value());
      }
    } catch (Exception e) {
      response.setMessage("error !");
      response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    return response;
  }

  public ApiResponseModel deleteBoard(Long id) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      boardRepository.deleteById(id);
      response.setMessage("success !");
      response.setCode(HttpStatus.OK.value());
    } catch (Exception e) {
      response.setMessage("boardni uchirib bulmaydi !");
      response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
    return response;
  }
}
