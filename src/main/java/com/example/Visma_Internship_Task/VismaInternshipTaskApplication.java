package com.example.Visma_Internship_Task;


import com.example.Visma_Internship_Task.entity.Data;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.*;

import java.util.ArrayList;
import java.util.List;


import static com.example.Visma_Internship_Task.controller.MeetingController.dataList;
import static com.example.Visma_Internship_Task.controller.MeetingController.person;

@SpringBootApplication
public class VismaInternshipTaskApplication {




    public static void main(String[] args) {
        SpringApplication.run(VismaInternshipTaskApplication.class, args);

        try (FileReader reader  = new FileReader("src/main/resources/meetings/meetings.json")) {
            Object obj = new JSONParser().parse(reader);
            JSONArray jsonArray = (JSONArray) obj;
            jsonArray.forEach(data -> parseListToObj((JSONObject) data));

        } catch (ParseException | FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray jsonArray = new JSONArray();


                for (Data data : dataList) {
                    JSONObject jsonObject = new JSONObject();

                    jsonObject.put("Name", data.getName());
                    jsonObject.put("ResponsiblePerson", data.getResponsiblePerson());
                    jsonObject.put("Description", data.getDescription());
                    jsonObject.put("Category", data.getCategory());
                    jsonObject.put("Type", data.getType());
                    jsonObject.put("StartDate", data.getStartDate());
                    jsonObject.put("EndDate", data.getEndDate());
                    jsonObject.put("Person",data.getPerson());
                    jsonArray.add(jsonObject);
//                    System.out.println(jsonObject.toJSONString());
                }



                try {
                    FileWriter file = new FileWriter("src/main/resources/meetings/meetings.json");
                    file.write(jsonArray.toJSONString());
                    file.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }));



    }

    private static void parseListToObj(JSONObject obj){
        Data data = new Data();


        data.setName((String) obj.get("Name"));
        data.setResponsiblePerson((String) obj.get("ResponsiblePerson"));
        data.setDescription((String) obj.get("Description"));
        data.setCategory((String) obj.get("Category"));
        data.setType((String) obj.get("Type"));
        data.setStartDate((String) obj.get("StartDate"));
        data.setEndDate((String) obj.get("EndDate"));
        data.setPerson((List<String>) obj.get("Person"));
        dataList.add(data);
    }

}
