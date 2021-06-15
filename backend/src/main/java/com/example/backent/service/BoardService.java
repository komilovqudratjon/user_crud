package com.example.backent.service;

import com.example.backent.entity.Project;
import com.example.backent.entity.template.AbsEntity;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqBoard;
import com.example.backent.repository.BoardRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;



}
