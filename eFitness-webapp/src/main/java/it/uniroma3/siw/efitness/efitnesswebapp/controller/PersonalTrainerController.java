package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("admin/trainer/")
public class PersonalTrainerController {

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String getTrainers(Model model) {
        //TODO
        return "admin/trainer-list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addTrainer (Model model) {
        //TODO
        return "admin/trainer-form";
    }

}
