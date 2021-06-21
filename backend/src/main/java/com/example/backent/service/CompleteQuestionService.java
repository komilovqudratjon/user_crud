package com.example.backent.service;

import com.example.backent.entity.Attachment;
import com.example.backent.entity.CompleteQuestion;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqFullQuestion;
import com.example.backent.repository.AttachmentRepository;
import com.example.backent.repository.CompleteQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompleteQuestionService {

    @Autowired
    CompleteQuestionRepository full;
    @Autowired
    AttachmentRepository attachmentRepository;

    public ApiResponseModel addOrEditFullQuestion(ReqFullQuestion reqFullQuestion){
        ApiResponseModel response = new ApiResponseModel();
        CompleteQuestion fullQuestion = new CompleteQuestion();
        try{
            if(reqFullQuestion.getId()!=null){
                Optional<CompleteQuestion> optionalCompleteQuestion = full.findById(reqFullQuestion.getId());
                if(optionalCompleteQuestion.isPresent()){
                    fullQuestion = optionalCompleteQuestion.get();
                }
            }
            if(reqFullQuestion.getPhoto()!=null){
                Optional<Attachment> optionalAttachment = attachmentRepository.findById(reqFullQuestion.getPhoto());
                if(optionalAttachment.isPresent()){
                    fullQuestion.setQuestionPhoto(optionalAttachment.get());
                }else {
                    response.setMessage("bunaqa idlik rasm yuq");
                    response.setCode(207);
                }
            }
            fullQuestion.setText(reqFullQuestion.getText());
            fullQuestion.setLink(reqFullQuestion.getLink());
            full.save(fullQuestion);
            response.setCode(200);
            response.setMessage("success !");
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }

    public ApiResponseModel deleteComplateQuestion(Long id){
        ApiResponseModel response = new ApiResponseModel();
        try{
            Optional<CompleteQuestion> optionalCompleteQuestion = full.findById(id);
            if(optionalCompleteQuestion.isPresent()){
                optionalCompleteQuestion.get().setDeleted(true);
                full.save(optionalCompleteQuestion.get());
            }else{
                response.setMessage("bunaqa idlik savol mavjud emas");
                response.setCode(207);
            }
            response.setCode(200);
            response.setMessage("success !");
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }

}
