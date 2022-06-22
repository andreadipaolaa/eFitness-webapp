package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.Credentials;
import it.uniroma3.siw.efitness.efitnesswebapp.model.User;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CourseService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CredentialsService;
import it.uniroma3.siw.efitness.efitnesswebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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

    @RequestMapping(value = {"subscription/add"}, method = RequestMethod.POST)
    public String addSubscription(@RequestParam("course") Long idCourse, Model model){
        Course course = this.courseService.getCourseById(idCourse);
        User user = getActiveUser();
        if(! user.getCourses().contains(course)) {
            this.courseService.addUser(course, user);
            this.userService.addCourse(course, user);
        }
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if(principal instanceof DefaultOidcUser){
            return this.userService.getUserByEmail(((DefaultOidcUser) principal).getEmail());
        }
        //UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = this.credentialsService.getCredentials(((UserDetails) principal).getUsername());
        //Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
        return credentials.getUser();
    }

    public List<Course> getPotentialSubscriptions(User user){
        List<Course> potentialCourses = new ArrayList<Course>();
        for(Course course : this.courseService.getAll()) {
            if (!(user.getCourses().contains(course) || course.getUsers().size() == course.getMaxSubscriptions())) {
                potentialCourses.add(course);
            }
        }
        return potentialCourses;
    }
}
