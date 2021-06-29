package com.example.backent.service;

import com.example.backent.entity.SubTask;
import com.example.backent.entity.Task;
import com.example.backent.payload.ApiResponseModel;
import com.example.backent.repository.SubTaskRepository;
import com.example.backent.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    SubTaskRepository subTaskRepository;

    public ApiResponseModel addTask(String text, List<String> subTasks){
        ApiResponseModel response = new ApiResponseModel();
        try{
            Task task = new Task();
            task.setText(text);
            Task task1 = taskRepository.save(task);
            for (int i = 0; i < subTasks.size(); i++) {
                subTaskRepository.save(new SubTask(task1,subTasks.get(i)));
            }
            response.setCode(200);
            response.setMessage("success");
        }catch(Exception e){
            response.setCode(500);
            response.setMessage("error");
        }
        return response;
    }
}
