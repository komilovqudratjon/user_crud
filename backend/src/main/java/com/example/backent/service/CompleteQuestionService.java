package com.example.backent.service;

import com.example.backent.entity.Attachment;
import com.example.backent.entity.CompleteQuestion;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.payload.ReqFullQuestion;
import com.example.backent.payload.ResCompleteQuestion;
import com.example.backent.repository.AttachmentRepository;
import com.example.backent.repository.CompleteQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompleteQuestionService {

  @Autowired CompleteQuestionRepository full;
  @Autowired AttachmentRepository attachmentRepository;

    public ApiResponseModel addOrEditFullQuestion(ReqFullQuestion reqFullQuestion){
        ApiResponseModel response = new ApiResponseModel();
        CompleteQuestion fullQuestion = new CompleteQuestion();
        try{
            if(reqFullQuestion.getId()!=null){
                Optional<CompleteQuestion> optionalCompleteQuestion = full.findByIdAndDeleted(reqFullQuestion.getId(),false);
                if(optionalCompleteQuestion.isPresent()){
                    fullQuestion = optionalCompleteQuestion.get();
                }else{
                    response.setMessage("QUESTION ID : "+reqFullQuestion.getId()+" DID NOT FOUND ");
                    response.setCode(207);
                    return response;
                }
            }
            if(reqFullQuestion.getPhoto()!=null){
                Optional<Attachment> optionalAttachment = attachmentRepository.findById(reqFullQuestion.getPhoto());
                if(optionalAttachment.isPresent()){
                    fullQuestion.setQuestionPhoto(optionalAttachment.get());
                }else {
                    response.setMessage("PHOTO ID : "+reqFullQuestion.getPhoto()+" DID NOT FOUND ");
                    response.setCode(207);
                    return response;
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
                return response;
            }
            response.setCode(200);
            response.setMessage("success !");
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }

    public ApiResponseModel getOne(Long id){
        ApiResponseModel response = new ApiResponseModel();
        try{
            Optional<CompleteQuestion> question = full.findByIdAndDeleted(id, false);
            if(question.isPresent()){
                ResCompleteQuestion question1 = getCompleteQuestion(question.get());
                response.setCode(200);
                response.setMessage("success");
                response.setData(question1);
            }else{
                response.setCode(207);
                response.setMessage("QUESTION ID : "+id+" DID NOT FOUND ");
                return response;
            }
        }catch(Exception e){
            response.setCode(200);
            response.setMessage("error");
        }
        return response;
    }

    public ApiResponseModel getAllQuestions(){
        ApiResponseModel response = new ApiResponseModel();
        try{
            List<ResCompleteQuestion> list = full.findAllByDeleted(false).stream().map(this::getCompleteQuestion).collect(Collectors.toList());
            response.setCode(200);
            response.setMessage("success");
            response.setData(list);
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }

    public ResCompleteQuestion getCompleteQuestion(CompleteQuestion question){
        return new ResCompleteQuestion(
                question.getId(),
                question.getQuestionPhoto()!=null? ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/attach/").path(question.getQuestionPhoto().getId().toString()).toUriString():null,
                question.getText(),
                question.getLink()
        );
    }

}
