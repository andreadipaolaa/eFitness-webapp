package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.Credentials;
import it.uniroma3.siw.efitness.efitnesswebapp.model.User;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CourseService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CredentialsService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/personal/")
public class PersonalAreaController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @RequestMapping(value={""}, method = RequestMethod.GET)
    public String getProfile(Model model){
        model.addAttribute("user", getActiveUser());
        return "user/personalArea/personalArea";
    }

    @RequestMapping(value={"subscription/add"}, method = RequestMethod.GET)
    public String subscriptionIntention(Model model){
        model.addAttribute("courses", getPotentialSubscriptions(getActiveUser()));
        return "user/personalArea/addSubscription";
    }

    @RequestMapping(value = {"subscription/add/{id}"}, method = RequestMethod.POST)
    public String addSubscription(@PathVariable("id") Long idCourse, Model model){
        Course course = this.courseService.getCourseById(idCourse);
        User user = getActiveUser();
        this.courseService.addUser(course, user);
        this.userService.addCourse(course, user);
        model.addAttribute("user", user);
        return "user/personalArea/personalArea";
    }

    @RequestMapping(value={"subscription/delete/{id}"}, method = RequestMethod.GET)
    public String askForSubscriptionDelete(@PathVariable("id") Long idCourse, Model model){
        model.addAttribute("course", this.courseService.getCourseById(idCourse));
        return "user/personalArea/confirmDeleteSubscription";
    }

    @RequestMapping(value = {"subscription/delete/{id}"}, method = RequestMethod.POST)
    public String deleteSubscription(@PathVariable("id") Long idCourse, Model model){
        Course course = this.courseService.getCourseById(idCourse);
        User user = getActiveUser();
        this.courseService.removeUser(course, user);
        this.userService.removeCourse(course, user);
        model.addAttribute("user", user);
        return "user/personalArea/personalArea";
    }

    public User getActiveUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        return credentials.getUser();
    }

    public List<Course> getPotentialSubscriptions(User user){
        List<Course> potentialCourses = this.courseService.getAll();
        for(Course c : user.getCourses()){
            potentialCourses.remove(c);
        }
        return potentialCourses;
    }
}
