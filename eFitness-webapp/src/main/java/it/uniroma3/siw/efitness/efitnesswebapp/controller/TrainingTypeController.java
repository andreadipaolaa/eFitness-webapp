package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.Room;
import it.uniroma3.siw.efitness.efitnesswebapp.model.TrainingType;
import it.uniroma3.siw.efitness.efitnesswebapp.service.TrainingTypeService;
import it.uniroma3.siw.efitness.efitnesswebapp.validator.TrainingTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("admin/trainingType/")
public class TrainingTypeController {
    @Autowired
    private TrainingTypeService trainingTypeService;

    @Autowired
    private TrainingTypeValidator trainingTypeValidator;

    @RequestMapping(value={"list"}, method = RequestMethod.GET)
    public String getTrainingTypes(Model model){
        model.addAttribute("trainingTypes", this.trainingTypeService.getAll());
        return "admin/trainingType-list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addTrainingType (Model model) {
        model.addAttribute("trainingType", new TrainingType());
        return "admin/trainingType-form";
    }

    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addTrainingType(@ModelAttribute("trainingType") TrainingType trainingType, BindingResult bindingResult, Model model) {
        this.trainingTypeValidator.validate(trainingType, bindingResult);
        if(!bindingResult.hasErrors()) {
            this.trainingTypeService.save(trainingType);
            return getTrainingTypes(model);
        }
        else
            return "admin/trainingType-form";
    }

    @RequestMapping(value={"modify/{id}"}, method = RequestMethod.GET)
    public String modifyTrainingType(@PathVariable("id") Long id, Model model){
        model.addAttribute("trainingType", this.trainingTypeService.getTrainingTypeById(id));
        return "admin/trainingType-modify-form";
    }

    @RequestMapping(value = {"modify/{id}"}, method = RequestMethod.POST)
    public String modifyTrainingType(@PathVariable("id") Long id, @ModelAttribute("trainingType") TrainingType trainingType,
                                    BindingResult bindingResult, Model model){
        this.trainingTypeValidator.validate(trainingType, bindingResult);
        if(!bindingResult.hasErrors()) {
            this.trainingTypeService.modifyById(id, trainingType);
            return "admin/course-list";
        }
        else
            return "admin/trainingType-modify-form";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.GET)
    public String deleteTrainingType(@PathVariable("id")Long id, Model model){
        model.addAttribute("trainingType", this.trainingTypeService.getTrainingTypeById(id));
        return "confirmDeleteTrainingType";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.POST)
    public String deleteTrainingTypeConfirmed(@PathVariable("id")Long id, Model model){
        this.trainingTypeService.deleteById(id);
        return getTrainingTypes(model);
    }
}
