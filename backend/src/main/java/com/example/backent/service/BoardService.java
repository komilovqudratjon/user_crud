package com.example.backent.service;

import com.example.backent.entity.Board;
import com.example.backent.entity.Project;
import com.example.backent.entity.enums.BoardCondition;
import com.example.backent.entity.template.AbsEntity;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqBoard;
import com.example.backent.payload.ResBoard;
import com.example.backent.repository.BoardRepository;
import com.example.backent.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    ProjectRepository projectRepository;

    public ApiResponseModel addBoard(ReqBoard reqBoard){
        ApiResponseModel response = new ApiResponseModel();
        try{
            Board board = new Board();
            board.setName(reqBoard.getName());
            board.setIndex(reqBoard.getIndex());
            board.setCondition(BoardCondition.valueOf(reqBoard.getCondition()));
            Optional<Project> project = projectRepository.findById(reqBoard.getProject());
            if(project.isPresent()){
                board.setProject(project.get());
            }else{
                response.setMessage("bunaqa idlik project mavjud emas !");
                response.setCode(207);
                return response;
            }
            boardRepository.save(board);
            response.setMessage("success !");
            response.setCode(200);
        }catch(Exception e){
            response.setMessage("error !");
            response.setCode(500);
        }
        return response;
    }

    public ApiResponseModel editBoard(ReqBoard reqBoard){
        ApiResponseModel response = new ApiResponseModel();
        try{
            Optional<Board> board = boardRepository.findById(reqBoard.getId());
            if(board.isPresent()){
                board.get().setName(reqBoard.getName()!=null?reqBoard.getName():board.get().getName());
                Optional<Project> optionalProject = projectRepository.findById(reqBoard.getProject());
                if(optionalProject.isPresent()){
                    board.get().setProject(optionalProject.get());
                    board.get().setIndex(reqBoard.getIndex()!=null?reqBoard.getIndex():board.get().getIndex());
                    boardRepository.save(board.get());
                    response.setMessage("success !");
                    response.setCode(200);
                }else{
                    response.setMessage("PROJECT ID : "+reqBoard.getProject()+" did not found");
                    response.setCode(207);
                    return response;
                }
            }else{
                response.setMessage("BOARD ID : "+reqBoard.getId()+" did not found");
                response.setCode(207);
                return response;
            }
        }catch(Exception e){
            response.setMessage("error !");
            response.setCode(500);
        }
        return response;
    }

    public ApiResponseModel getByProject(Long projectId){
        ApiResponseModel response = new ApiResponseModel();
        try{
            List<ResBoard> list = boardRepository.findByProjectId(projectId).stream().map(this::getBoard).collect(Collectors.toList());
            response.setCode(200);
            response.setMessage("success");
            response.setData(list);
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }

    public ApiResponseModel deleteBoard(Long id){
        ApiResponseModel response = new ApiResponseModel();
        try{
            Optional<Board> board = boardRepository.findById(id);
            if(board.isPresent()){
                board.get().setDeleted(true);
                boardRepository.save(board.get());
                response.setMessage("success !");
                response.setCode(200);
            }else{
                response.setMessage("bunaqa idlik board mavjud emas !");
                response.setCode(207);
            }
        }catch(Exception e){
            response.setMessage("boardni uchirib bulmaydi !");
            response.setCode(500);
        }
        return response;
    }

    public ResBoard getBoard(Board board){
        return new ResBoard(
                board.getId(),
                board.getName(),
                board.getProject().getId()
        );
    }


}
