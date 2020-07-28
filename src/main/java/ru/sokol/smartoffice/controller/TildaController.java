package ru.sokol.smartoffice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class TildaController {
    @GetMapping("/")
    public String getIndexPage(){
        return "forward:/page12537394.html";
    }
    @GetMapping("/portfolio")
    public String getPortfolioPage(){
        return "forward:/page12562019.html";
    }
    @GetMapping("/pricelist")
    public String getPriceListPage(){
        return "forward:/page12562230.html";
    }
    @GetMapping("/office-interactive")
    public String getOfficeInteractivePage(){
        return "forward:/page12562361.html";
    }
    @GetMapping("/contacts")
    public String getContactsPage(){
        return "forward:/page12562407.html";
    }
    @GetMapping("/otzivy")
    public String getReviewsPage(){
        return "forward:/page12562466.html";
    }
    @GetMapping("/uslugi")
    public String getUslugiPage(){
        return "forward:/page12562587.html";
    }


    @PostMapping("/procces")
    @ResponseStatus(value = HttpStatus.OK)
    void processForm(){
    }
}
