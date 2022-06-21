package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.service.PersonalTrainerService;
import it.uniroma3.siw.efitness.efitnesswebapp.util.FileManager;
import it.uniroma3.siw.efitness.efitnesswebapp.validator.PersonalTrainerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller @RequestMapping("admin/trainer/")
public class PersonalTrainerController {

    @Autowired
    private PersonalTrainerService personalTrainerService;

    @Autowired
    private PersonalTrainerValidator personalTrainerValidator;

    public static String DIR = System.getProperty("user.dir")+"/eFitness-webapp/src/main/resources/static/images/personalTrainer/";

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String getTrainers(Model model) {
        List<PersonalTrainer> trainers = this.personalTrainerService.getAll();
        model.addAttribute("trainers", trainers);
        return "admin/trainer/list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addTrainer (Model model) {
        model.addAttribute("trainer", new PersonalTrainer());
        return "admin/trainer/form";
    }

    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addTrainer(@ModelAttribute("trainer") PersonalTrainer trainer,
                             @RequestParam("image") MultipartFile multipartFile, Model model,
                             BindingResult bindingResult) {
        this.personalTrainerValidator.validate(trainer, bindingResult);
        if(!bindingResult.hasErrors()) {
            trainer.setPhoto(savePhoto(multipartFile, trainer));
            this.personalTrainerService.save(trainer);
            return getTrainers(model);
        }
        return "admin/trainer/form";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.GET)
    public String deleteTrainer(@PathVariable("id")Long id, Model model){
        model.addAttribute("trainer", this.personalTrainerService.getPersonalTrainerById(id));
        return "admin/trainer/confirm-delete";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.POST)
    public String deleteTrainerConfirmed(@PathVariable("id")Long id, Model model){
        FileManager.dirEmptyEndDelete(getUploadDir(this.personalTrainerService.getPersonalTrainerById(id)));
        this.personalTrainerService.deleteById(id);
        return getTrainers(model);
    }

    @RequestMapping(value ={"modify/{id}"}, method = RequestMethod.GET)
    public String modifyTrainer( @PathVariable("id") Long idTrainer, Model model ){
        model.addAttribute("trainer", this.personalTrainerService.getPersonalTrainerById(idTrainer));
        return "admin/trainer/modify";
    }

    @RequestMapping(value ={"modify/{id}"}, method = RequestMethod.POST)
    public String modifyTrainer(@Valid @ModelAttribute("trainer") PersonalTrainer trainer, @RequestParam("image")MultipartFile multipartFile,
                                @PathVariable("id") Long idTrainer, BindingResult bindingResult ){
        this.personalTrainerValidator.validate(trainer,bindingResult);
        if(!bindingResult.hasErrors()) {
            trainer.setPhoto(modifyPhoto(multipartFile, idTrainer, trainer));
            this.personalTrainerService.modifyById(idTrainer, trainer);
            return "admin/trainer/list";
        }
        return "admin/trainer-modify-form";
    }

    public String savePhoto(MultipartFile multipartFile, PersonalTrainer trainer){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = getUploadDir(trainer);
        FileManager.store(multipartFile, uploadDir);
        return getDirectoryName(trainer) + "/" + fileName;
    }

    public String modifyPhoto(MultipartFile multipartFile, Long id, PersonalTrainer newTrainer){
        PersonalTrainer oldTrainer = this.personalTrainerService.getPersonalTrainerById(id);
        if(! multipartFile.isEmpty()){
            FileManager.dirEmptyEndDelete(getUploadDir(oldTrainer));
            return savePhoto(multipartFile, newTrainer);
        }
        FileManager.dirChangeName(getUploadDir(oldTrainer), getUploadDir(newTrainer));
        return getDirectoryName(newTrainer) + "/" + oldTrainer.getPhoto();
    }

    public String getUploadDir(PersonalTrainer trainer){
        return DIR + getDirectoryName(trainer);
    }

    public String getDirectoryName(PersonalTrainer personalTrainer){
        return personalTrainer.getName().replaceAll("\\s", "") + personalTrainer.getSurname().replaceAll("\\s", "");
    }
}
