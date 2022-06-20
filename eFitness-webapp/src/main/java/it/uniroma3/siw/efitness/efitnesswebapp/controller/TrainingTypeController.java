package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.TrainingType;
import it.uniroma3.siw.efitness.efitnesswebapp.service.TrainingTypeService;
import it.uniroma3.siw.efitness.efitnesswebapp.validator.TrainingTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller @RequestMapping("admin/training/")
public class TrainingTypeController {
    @Autowired
    private TrainingTypeService trainingTypeService;

    @Autowired
    private TrainingTypeValidator trainingTypeValidator;

    @RequestMapping(value={"list"}, method = RequestMethod.GET)
    public String getTrainingTypes(Model model){
        model.addAttribute("trainings", this.trainingTypeService.getAll());
        return "admin/training/list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addTrainingType (Model model) {
        model.addAttribute("training", new TrainingType());
        return "admin/training/form";
    }

    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addTrainingType(@ModelAttribute("training") TrainingType trainingType, BindingResult bindingResult, Model model) {
        this.trainingTypeValidator.validate(trainingType, bindingResult);
        if(!bindingResult.hasErrors()) {
            this.trainingTypeService.save(trainingType);
            model.addAttribute("trainings", this.trainingTypeService.getAll());
            return "admin/training/list";
        }
        return "admin/training/form";
    }

    @RequestMapping(value={"modify/{id}"}, method = RequestMethod.GET)
    public String modifyTrainingType(@PathVariable("id") Long id, Model model){
        model.addAttribute("training", this.trainingTypeService.getTrainingTypeById(id));
        return "admin/training/modify";
    }

    @RequestMapping(value = {"modify/{id}"}, method = RequestMethod.POST)
    public String modifyTrainingType(@PathVariable("id") Long id, @ModelAttribute("training") TrainingType trainingType,
                                     BindingResult bindingResult, Model model){
        this.trainingTypeValidator.validate(trainingType, bindingResult);
        if(!bindingResult.hasErrors()) {
            this.trainingTypeService.modifyById(id, trainingType);
            return "admin/course/list";
        }
        else
            return "admin/training/modify";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.GET)
    public String deleteTrainingType(@PathVariable("id")Long id, Model model){
        model.addAttribute("training", this.trainingTypeService.getTrainingTypeById(id));
        return "admin/training/confirmDelete";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.POST)
    public String deleteTrainingTypeConfirmed(@PathVariable("id")Long id, Model model){
        this.trainingTypeService.deleteById(id);
        return getTrainingTypes(model);
    }
}
