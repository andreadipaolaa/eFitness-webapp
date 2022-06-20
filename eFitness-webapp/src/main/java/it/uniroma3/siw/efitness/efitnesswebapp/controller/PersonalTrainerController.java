
package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.service.PersonalTrainerService;
import it.uniroma3.siw.efitness.efitnesswebapp.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("admin/trainer/")
public class PersonalTrainerController {

    @Autowired
    private PersonalTrainerService personalTrainerService;

    public static String DIR = System.getProperty("user.dir")+"/src/main/resources/static/images/personalTrainer/";

    @RequestMapping(value = "list", method = RequestMethod.GET)
    public String getTrainers(Model model) {
        List<PersonalTrainer> trainers = this.personalTrainerService.getAll();
        model.addAttribute("trainers", trainers);
        return "admin/trainer-list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addTrainer (Model model) {
        model.addAttribute("trainer", new PersonalTrainer());
        return "admin/trainer-form";
    }

    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addTrainer(@ModelAttribute("trainer") PersonalTrainer trainer,
                             @RequestParam("image") MultipartFile multipartFile, Model model) {
        trainer.setPhoto(savePhoto(multipartFile, trainer));
        this.personalTrainerService.save(trainer);
        return getTrainers(model);
    }

    @RequestMapping(value ={"modify/{id}"}, method = RequestMethod.GET)
    public String modifyTrainer( @PathVariable("id") Long idTrainer, Model model ){
        model.addAttribute("trainer", this.personalTrainerService.getPersonalTrainerById(idTrainer));
        return "admin/trainer-modify-form";
    }

    @RequestMapping(value ={"modify/{id}"}, method = RequestMethod.POST)
    public String modifyTrainer(@Valid @ModelAttribute("trainer") PersonalTrainer trainer, @RequestParam("image")MultipartFile multipartFile,
                                @PathVariable("id") Long idTrainer, Model model ){
        trainer.setPhoto(modifyPhoto(multipartFile, idTrainer, trainer));
        this.personalTrainerService.modifyById(idTrainer, trainer);
        return "admin/trainer-list";
    }

    public String savePhoto(MultipartFile multipartFile, PersonalTrainer trainer){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = getUploadDir(trainer);
        FileManager.store(multipartFile, uploadDir);
        return fileName;
    }

    public String modifyPhoto(MultipartFile multipartFile, Long id, PersonalTrainer newTrainer){
        PersonalTrainer oldTrainer = this.personalTrainerService.getPersonalTrainerById(id);
        if(! multipartFile.isEmpty()){
            FileManager.removeImgAndDir(getUploadDir(oldTrainer), oldTrainer.getPhoto());
            return savePhoto(multipartFile, newTrainer);
        }
        else{
            FileManager.dirChangeName(getUploadDir(oldTrainer), getUploadDir(newTrainer));
            return oldTrainer.getPhoto();
        }
    }

    public String getUploadDir(PersonalTrainer trainer){
        return DIR + trainer.getName().trim() + trainer.getSurname().trim();
    }
}
