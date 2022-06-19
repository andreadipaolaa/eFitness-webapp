package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("admin/course/")
public class CourseController {

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String addCourse (Model model) {
        model.addAttribute("course", new Course());
        return "admin/course-form";
    }

    /*@RequestMapping(value = { "add" }, method = RequestMethod.POST)
    public String addCourse(@ModelAttribute("course") Course course, Model model) {
        courseService.saveCourse(course);
        return getCourses(model);
    }*/
}
