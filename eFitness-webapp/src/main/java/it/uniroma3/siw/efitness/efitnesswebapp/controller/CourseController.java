package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.model.Room;
import it.uniroma3.siw.efitness.efitnesswebapp.model.TrainingType;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CourseService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.PersonalTrainerService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.RoomService;
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

    @Autowired private RoomService roomService;

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
        model.addAttribute("rooms", this.roomService.getAll());
        return "admin/course/form";
    }

    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addCourse(@ModelAttribute("course") Course course, @RequestParam("image") MultipartFile multipartFile,
                            @RequestParam("trainer") Long idTrainer, @RequestParam("training") Long idTraining,
                            @RequestParam("room") Long idRoom, Model model, BindingResult bindingResult) {
        PersonalTrainer trainer = personalTrainerService.getPersonalTrainerById(idTrainer);
        TrainingType training = trainingTypeService.getTrainingTypeById(idTraining);
        Room room = roomService.getRoomById(idRoom);

        course.setPersonalTrainer(trainer);
        course.setTrainingType(training);
        course.setRoom(room);
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
        if(!(this.courseService.getCourseById(id).getPhoto()==null))
            FileManager.dirEmptyEndDelete(getUploadDir(this.courseService.getCourseById(id)));
        this.courseService.deleteById(id);
        return getCourses(model);
    }

    @RequestMapping(value={"modify/{id}"}, method = RequestMethod.GET)
    public String modifyCourse(@PathVariable("id") Long idCourse, Model model){
        model.addAttribute("course", this.courseService.getCourseById(idCourse));
        model.addAttribute("trainers", this.personalTrainerService.getAll());
        model.addAttribute("trainings", this.trainingTypeService.getAll());
        model.addAttribute("rooms", this.roomService.getAll());
        return "admin/course/modify";
    }

    @RequestMapping(value = {"modify/{id}"}, method = RequestMethod.POST)
    public String modifyCourse(@PathVariable("id") Long idCourse, @ModelAttribute("course") Course course, Model model,
                               @RequestParam("trainer") Long idTrainer, @RequestParam("training") Long idTraining,
                               @RequestParam("image")MultipartFile multipartFile, BindingResult bindingResult,
                               @RequestParam("room") Long idRoom){
        Course oldCourse = this.courseService.getCourseById(idCourse);
        if(!oldCourse.getName().equals(course.getName()))
            this.courseValidator.validate(course,bindingResult);
        if(!bindingResult.hasErrors()) {
            PersonalTrainer trainer = personalTrainerService.getPersonalTrainerById(idTrainer);
            TrainingType training = trainingTypeService.getTrainingTypeById(idTraining);
            Room room = roomService.getRoomById(idRoom);

            course.setPersonalTrainer(trainer);
            course.setTrainingType(training);
            course.setRoom(room);
            if(!multipartFile.isEmpty())
                course.setPhoto(modifyPhoto(multipartFile, idCourse, course));
            this.courseService.modifyById(idCourse, course);
            return getCourses(model);
        }
        model.addAttribute("trainers", this.personalTrainerService.getAll());
        model.addAttribute("trainings", this.trainingTypeService.getAll());
        model.addAttribute("rooms", this.roomService.getAll());
        return "admin/course/modify";
    }

    public String savePhoto(MultipartFile multipartFile, Course course){
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uploadDir = getUploadDir(course);
        FileManager.store(multipartFile, uploadDir);
        return getDirectoryName(course) + "/" + fileName;
    }

    public String modifyPhoto(MultipartFile multipartFile, Long id, Course newCourse){
        Course oldCourse = this.courseService.getCourseById(id);
        if(! multipartFile.isEmpty()){
            FileManager.dirEmptyEndDelete(getUploadDir(oldCourse));
            return savePhoto(multipartFile, newCourse);
        }
        FileManager.dirChangeName(getUploadDir(oldCourse), getUploadDir(newCourse));
        return  getDirectoryName(newCourse) + "/" + oldCourse.getPhoto();
    }

    public String getUploadDir(Course course){
        return DIR + getDirectoryName(course);
    }

    public String getDirectoryName(Course course){return course.getName().replaceAll("\\s", ""); }
}
