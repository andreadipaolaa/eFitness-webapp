package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("admin/trainer/")
public class PersonalTrainerController {

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String getTrainers(Model model) {
        //List<PersonalTrainer> trainers = personalTrainerService.findAll();
        //model.addAttribute("trainers", trainers);
        return "admin/trainer-list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addTrainer (Model model) {
        model.addAttribute("trainer", new PersonalTrainer());
        return "admin/trainer-form";
    }

    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addTrainer(@ModelAttribute("trainer") PersonalTrainer trainer, Model model) {
        //personalTrainerService.saveTrainer(trainer);
        return getTrainers(model);
    }
}
