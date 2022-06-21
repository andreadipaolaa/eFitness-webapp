package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.model.TrainingType;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CourseService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.PersonalTrainerService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.TrainingTypeService;
import it.uniroma3.siw.efitness.efitnesswebapp.util.FileManager;
import it.uniroma3.siw.efitness.efitnesswebapp.validator.CourseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller @RequestMapping("admin/course/")
public class CourseController {

    @Autowired private CourseService courseService;

    @Autowired private PersonalTrainerService personalTrainerService;

    @Autowired private TrainingTypeService trainingTypeService;

    @Autowired private CourseValidator courseValidator;

    public static String DIR = System.getProperty("user.dir")+"/eFitness-webapp/src/main/resources/static/images/course/";

    @RequestMapping(value={"list"}, method = RequestMethod.GET)
    public String getCourses(Model model){
        model.addAttribute("courses", this.courseService.getAll());
        return "admin/course/list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addCourse (Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("trainers", this.personalTrainerService.getAll());
        model.addAttribute("trainings", this.trainingTypeService.getAll());
        return "admin/course/form";
    }

    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addCourse(@ModelAttribute("course") Course course, @RequestParam("image") MultipartFile multipartFile,
                            @RequestParam("trainer") Long idTrainer, @RequestParam("training") Long idTraining,
                            Model model, BindingResult bindingResult) {
        PersonalTrainer trainer = personalTrainerService.getPersonalTrainerById(idTrainer);
        TrainingType training = trainingTypeService.getTrainingTypeById(idTraining);
        course.setPersonalTrainer(trainer);
        course.setTrainingType(training);
        this.courseValidator.validate(course, bindingResult);
        if(!bindingResult.hasErrors()) {
            if(!multipartFile.isEmpty())
                course.setPhoto(savePhoto(multipartFile, course));
            courseService.save(course);
            return getCourses(model);
        }
        return "admin/course/form";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.GET)
    public String deleteCourse(@PathVariable("id")Long id, Model model){
        model.addAttribute("course", this.courseService.getCourseById(id));
        return "admin/course/confirm-delete";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.POST)
    public String deleteCourseConfirmed(@PathVariable("id")Long id, Model model){
        this.courseService.deleteById(id);
        return getCourses(model);
    }

    @RequestMapping(value={"modify/{id}"}, method = RequestMethod.GET)
    public String modifyCourse(@PathVariable("id") Long idCourse, Model model){
        model.addAttribute("course", this.courseService.getCourseById(idCourse));
        model.addAttribute("trainers", this.personalTrainerService.getAll());
        model.addAttribute("trainings", this.trainingTypeService.getAll());
        return "admin/course/modify";
    }

    @RequestMapping(value = {"modify/{id}"}, method = RequestMethod.POST)
    public String modifyCourse(@PathVariable("id") Long idCourse, @ModelAttribute("course") Course course, Model model,
                               @RequestParam("trainer") Long idTrainer, @RequestParam("training") Long idTraining,
                               @RequestParam("image")MultipartFile multipartFile, BindingResult bindingResult){
        Course oldCourse = this.courseService.getCourseById(idCourse);
        if(!oldCourse.getName().equals(course.getName()))
            this.courseValidator.validate(course,bindingResult);
        if(!bindingResult.hasErrors()) {
            PersonalTrainer trainer = personalTrainerService.getPersonalTrainerById(idTrainer);
            TrainingType training = trainingTypeService.getTrainingTypeById(idTraining);
            course.setPersonalTrainer(trainer);
            course.setTrainingType(training);
            course.setPhoto(modifyPhoto(multipartFile, idCourse, course));
            this.courseService.modifyById(idCourse, course);
            return getCourses(model);
        }
        model.addAttribute("trainers", this.personalTrainerService.getAll());
        model.addAttribute("trainings", this.trainingTypeService.getAll());
        return "admin/course/modify";
    }

    public String savePhoto(MultipartFile multipartFile, Course course){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = getUploadDir(course);
        FileManager.store(multipartFile, uploadDir);
        return fileName;
    }

    public String modifyPhoto(MultipartFile multipartFile, Long id, Course newCourse){
        Course oldCourse = this.courseService.getCourseById(id);
        if(! multipartFile.isEmpty()){
            FileManager.removeImgAndDir(getUploadDir(oldCourse), oldCourse.getPhoto());
            return savePhoto(multipartFile, newCourse);
        }
        FileManager.dirChangeName(getUploadDir(oldCourse), getUploadDir(newCourse));
        return oldCourse.getPhoto();
    }

    public static String getUploadDir(Course course){
        return DIR + course.getName().replaceAll("\\s", "");
    }
}
