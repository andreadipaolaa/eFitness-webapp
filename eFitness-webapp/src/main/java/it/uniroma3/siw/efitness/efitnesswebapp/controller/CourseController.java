package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CourseService;
import it.uniroma3.siw.efitness.efitnesswebapp.util.FileManager;
import it.uniroma3.siw.efitness.efitnesswebapp.validator.CourseValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("admin/course/")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @Autowired
    private CourseValidator courseValidator;
   

    public static String DIR = System.getProperty("user.dir")+"/eFitness-webapp/src/main/resources/static/images/course/";


    @RequestMapping(value={"list"}, method = RequestMethod.GET)
    public String getCourses(Model model){
        model.addAttribute("courses", this.courseService.getAll());
        return "admin/course-list";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addCourse (Model model) {
        model.addAttribute("course", new Course());
        return "admin/course-form";
    }


    @RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addCourse(@ModelAttribute("course") Course course, @RequestParam("image") MultipartFile multipartFile,
                            Model model, BindingResult bindingResult) {
        this.courseValidator.validate(course, bindingResult);
        if(!bindingResult.hasErrors()) {
            course.setPhoto(savePhoto(multipartFile, course));
            courseService.save(course);
            return getCourses(model);
        }
        else
            return "admin/course-form";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.GET)
    public String deleteCourse(@PathVariable("id")Long id, Model model){
        model.addAttribute("course", this.courseService.getCourseById(id));
        return "confirmDeleteCourse";
    }

    @RequestMapping(value = {"delete/{id}"}, method = RequestMethod.POST)
    public String deleteCourseConfirmed(@PathVariable("id")Long id, Model model){
        this.courseService.deleteById(id);
        return getCourses(model);
    }


    @RequestMapping(value={"modify/{id}"}, method = RequestMethod.GET)
    public String modifyCourse(@PathVariable("id") Long idCourse, Model model){
        model.addAttribute("course", this.courseService.getCourseById(idCourse));
        return "admin/course-modify-form";
    }

    @RequestMapping(value = {"modify/{id}"}, method = RequestMethod.POST)
    public String modifyCourse(@PathVariable("id") Long idCourse, @ModelAttribute("course") Course course,
                               @RequestParam("image")MultipartFile multipartFile, Model model, BindingResult bindingResult){
        this.courseValidator.validate(course,bindingResult);
        if(!bindingResult.hasErrors()) {
            course.setPhoto(modifyPhoto(multipartFile, idCourse, course));
            this.courseService.modifyById(idCourse, course);
            return "admin/course-list";
        }
        else
            return "admin/course-modify-form";
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
        else{
            FileManager.dirChangeName(getUploadDir(oldCourse), getUploadDir(newCourse));
            return oldCourse.getPhoto();
        }
    }

    public String getUploadDir(Course course){
        return DIR + course.getName().replaceAll("\\s", "");
    }
}
