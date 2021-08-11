package com.example.backent.service;

import com.example.backent.entity.Board;
import com.example.backent.entity.Project;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ErrorsField;
import com.example.backent.payload.ReqBoard;
import com.example.backent.payload.ResBoard;
import com.example.backent.repository.BoardRepository;
import com.example.backent.repository.ProjectRepository;
import com.example.backent.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

  @Autowired BoardRepository boardRepository;

  @Autowired ProjectRepository projectRepository;

  @Autowired TicketRepository ticketRepository;

  public HttpEntity<?> addBoard(ReqBoard reqBoard) {
    Project project = null;
    if (reqBoard.getProject() != null) {
      Optional<Project> projectOptional = projectRepository.findById(reqBoard.getProject());
      if (projectOptional.isPresent()) {
        project = projectOptional.get();
      }
    }

    String info = "save";

    Board board = new Board();

    if (reqBoard.getId() != null) {
      Optional<Board> boardOptional = boardRepository.findById(reqBoard.getId());
      if (boardOptional.isPresent()) {
        board = boardOptional.get();
        info = "edit";
      }
    }

    board.setName(reqBoard.getName());
    board.setIndex(reqBoard.getIndex());
    board.setProject(project);
    board.setCondition(reqBoard.getCondition());

    boardRepository.save(board);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ApiResponseModel(HttpStatus.OK.value(), info, board));
  }

  public ApiResponseModel getByProject(Long projectId) {
    ApiResponseModel response = new ApiResponseModel();
    try {
      List<ResBoard> list =
          boardRepository.findByProjectId(projectId).stream()
              .map(this::getBoard)
              .collect(Collectors.toList());
      response.setCode(200);
      response.setMessage("success");
      response.setData(list);
    } catch (Exception e) {
      response.setCode(500);
      response.setMessage("error");
    }
    return response;
  }

  public HttpEntity<?> deleteBoard(Long id) {
    try {

      boardRepository.deleteById(id);
      return ResponseEntity.status(HttpStatus.OK)
          .body(new ApiResponseModel(HttpStatus.OK.value(), "delete"));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "field",
                  List.of(new ErrorsField("error", "sorry unspecified error"))));
    }
  }

  public ResBoard getBoard(Board board) {
    return new ResBoard(
        board.getId(),
        board.getName(),
        board.getProject().getId(),
        board.getIndex(),
        board.getCondition());
  }

  public HttpEntity<?> getTickets(Long boardId) {
    try {
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new ApiResponseModel(
                  HttpStatus.OK.value(), "here it is", ticketRepository.findAllByBoardId(boardId)));
    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(
              new ApiResponseModel(
                  HttpStatus.CONFLICT.value(),
                  "field",
                  List.of(new ErrorsField("error", "undefined mistake"))));
    }
  }
}
