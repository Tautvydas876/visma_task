package com.example.Visma_Internship_Task.controller;

import com.example.Visma_Internship_Task.entity.Data;
import com.example.Visma_Internship_Task.entity.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Controller
public class MeetingController {

    public static List<Data> dataList = new ArrayList<>();
    public static User user = new User();
    public static List<JSONObject> person = new ArrayList<>();

    public static List<String> personList = new ArrayList<>();

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public MeetingController(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("username") String userName, @ModelAttribute("password") String password, Model model) {
        JSONParser jsonParser = new JSONParser();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
            FileReader fileReader = new FileReader("src/main/resources/Users/" + userName + ".json");

            Object obj = jsonParser.parse(fileReader);
            JSONObject jObj = (JSONObject) obj;


            user.setUsername((String) jObj.get("username"));
            user.setPassword((String) jObj.get("password"));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            model.addAttribute("error", "User Name or Password incorrect!");
            return "login";
        }

        if (user.getUsername().equals(userName) && encoder.matches(password, user.getPassword())) {
            return "home-Page";
        } else {
            model.addAttribute("error", "User Name or Password incorrect!");
            return "login";
        }

    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("username") String userName, @ModelAttribute("password") String password) {
        JSONObject jsonObject = new JSONObject();
        String pass = bCryptPasswordEncoder.encode(password);
        jsonObject.put("username", userName);
        jsonObject.put("password", pass);


        try {
            FileWriter file = new FileWriter("src/main/resources/Users/" + userName + ".json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "home-Page";
    }

    @PostMapping("/createMeeting")
    public String creatNewMeeting(@ModelAttribute("name") String name, @ModelAttribute("ResponsiblePerson") String responsiblePerson,
                                  @ModelAttribute("description") String description, @ModelAttribute("category") String category,
                                  @ModelAttribute("type") String type, @ModelAttribute("start") Date startDate,
                                  @ModelAttribute("end") Date endDate, Model model) {

        String startDateFormatted = new SimpleDateFormat("yyyy-MM-dd").format(startDate);
        String endDateFormatted = new SimpleDateFormat("yyyy-MM-dd").format(endDate);

        Data data = new Data(name, responsiblePerson, description, category, type, startDateFormatted, endDateFormatted);

        dataList.add(data);


        return "home-Page";
    }

    @PostMapping("/delete-meeting")
    public String deleteMeeting(@RequestParam("data_id") int id, Model model) {
        List<Data> datalist = dataList;
        Data data = dataList.get(id);
        System.out.println(id + " | " + data.getResponsiblePerson() + " | " + user.getUsername());
        if (data.getResponsiblePerson().equals(user.getUsername())) {
            dataList.remove(id);
            model.addAttribute("dataList", datalist);
        } else {
            model.addAttribute("dataList", datalist);
            model.addAttribute("error", "You are not responsible Person to delete this meeting!");
        }
        return "delete-meeting";
    }

    @PostMapping("/add-person")
    public String addPersonToTheMeeting(@RequestParam("name_surname") String nameSurname, @RequestParam("meeting_name") String meetingName,
                                        Model model) {

        List<Data> list = dataList;
        model.addAttribute("listData", list);


        for (String s : personList) {
            if (s.equals(nameSurname)) {
                model.addAttribute("error", "Person already added to the meeting! ");
                return "add-person";
            }
        }

        personList.add(nameSurname);

        for (Data data : list) {
            if (data.getName().equals(meetingName)) {
                for (String s : data.getPerson()) {
                    if (s.equals(nameSurname)) {
                        model.addAttribute("error", "Person was already added to the meeting!");
                        return "add-person";
                    }
                }
                data.setPerson(personList);
            }
        }

        model.addAttribute("success", "Person added!");
        return "add-person";
    }

    @PostMapping("/delete-person")
    public String deletePersonFormMeeting(@RequestParam("meeting") String meetingName, @RequestParam("person") String nameSurname, Model model) {

        List<Data> list = dataList;


        for (Data data : dataList) {
            if (data.getResponsiblePerson().equals(nameSurname)) {
                model.addAttribute("error", "Responsible person can't be removed from the meeting!");
                model.addAttribute("listData", list);
                return "delete-person";
            }

            if (data.getName().equals(meetingName)) {
                for (String person : data.getPerson())
                    if (person.equals(nameSurname)) {
                        data.getPerson().remove(nameSurname);
                        model.addAttribute("success", "Person " + nameSurname + " was removed from meeting!");
                        model.addAttribute("listData", list);
                        return "delete-person";
                    } else {
                        model.addAttribute("error", "Person " + nameSurname + " does not exist in the meeting!");
                        model.addAttribute("listData", list);
                        return "delete-person";
                    }
            }

        }
        model.addAttribute("listData", list);
        return "delete-person";
    }


//    ======== Forms ============

    @GetMapping("/")
    public String showLoginForm(Model model) {
        return "login";
    }

    @GetMapping("/home-Page")
    public String showHomePageForm(Model model) {
        return "home-Page";
    }

    @GetMapping("registration")
    public String showRegisterForm() {
        return "registration";
    }

    @GetMapping("/creat-meeting")
    public String showCreateMeetingForm() {
        return "creat-meeting";
    }

    @GetMapping("/delete-meeting")
    public String showDeleteMeetingForm(Model model) {

        List<Data> data = dataList;
        model.addAttribute("dataList", data);

        return "delete-meeting";
    }

    @GetMapping("/add-person")
    public String showAddPersonForm(Model model) {
        List<Data> list = dataList;
        model.addAttribute("listData", list);
        return "add-person";
    }

    @GetMapping("/delete-person")
    public String showDeletePersonForm(Model model) {
        List<Data> list = dataList;

        model.addAttribute("listData", list);
        return "delete-person";
    }

    @GetMapping("/meeting-list")
    public String showMeetingListForm(Model model) {
        List<Data> list = dataList;

        model.addAttribute("dataList", list);

        return "meeting-list";
    }
}
