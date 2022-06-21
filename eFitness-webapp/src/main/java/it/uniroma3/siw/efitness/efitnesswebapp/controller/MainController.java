package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CourseService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.PersonalTrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user/")
public class MainController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private PersonalTrainerService personalTrainerService;

    @RequestMapping(value = {"home"}, method = RequestMethod.GET)
    public String getHome(Model model){
        return "index.html";
    }

    @RequestMapping(value = {"courses"}, method = RequestMethod.GET)
    public String getCourses(Model model){
        model.addAttribute("courses", this.courseService.getAll());
        return "user/course-list.html";
    }

    @RequestMapping(value = {"trainers"}, method = RequestMethod.GET)
    public String getTrainers(Model model){
        model.addAttribute("trainers", this.personalTrainerService.getAll());
        return "user/trainer-list.html";
    }

    @RequestMapping(value={"trainer/{id}"}, method = RequestMethod.GET)
    public String getTrainer(@PathVariable("id") Long idTrainer, Model model){
        PersonalTrainer trainer = this.personalTrainerService.getPersonalTrainerById(idTrainer);
        model.addAttribute("trainer", trainer);
        model.addAttribute("directory", PersonalTrainerController.getUploadDir(trainer));
        return "user/trainer/detailTrainer";
    }

}
