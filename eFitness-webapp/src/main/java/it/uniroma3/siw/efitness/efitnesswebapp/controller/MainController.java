package it.uniroma3.siw.efitness.efitnesswebapp.controller;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.Credentials;
import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.model.User;
import it.uniroma3.siw.efitness.efitnesswebapp.service.*;
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
@RequestMapping("/user/")
public class MainController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private PersonalTrainerService personalTrainerService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = {"home"}, method = RequestMethod.GET)
    public String getHome(Model model){
        return "index.html";
    }

    @RequestMapping(value = {"courses"}, method = RequestMethod.GET)
    public String getCourses(Model model){
        model.addAttribute("courses", this.courseService.getAll());
        return "user/course/course-list.html";
    }

    @RequestMapping(value = {"rooms"}, method = RequestMethod.GET)
    public String getRooms(Model model){
        model.addAttribute("rooms", this.roomService.getAll());
        return "user/room/room-list.html";
    }

    @RequestMapping(value = {"trainers"}, method = RequestMethod.GET)
    public String getTrainers(Model model){
        model.addAttribute("trainers", this.personalTrainerService.getAll());
        return "user/trainer/trainer-list.html";
    }

    @RequestMapping(value={"trainer/{id}"}, method = RequestMethod.GET)
    public String getTrainer(@PathVariable("id") Long idTrainer, Model model){
        PersonalTrainer trainer = this.personalTrainerService.getPersonalTrainerById(idTrainer);
        model.addAttribute("trainer", trainer);
        return "user/trainer/detailTrainer";
    }

    @RequestMapping(value = {"course/{id}"}, method = RequestMethod.GET)
    public String getCourse(@PathVariable("id") Long idCourse, Model model){
        Course course = this.courseService.getCourseById(idCourse);
        model.addAttribute("course", course);
        return "user/course/detailCourse";
    }

    @RequestMapping(value = {"room/{id}"}, method = RequestMethod.GET)
    public String getRoom(@PathVariable("id") Long idRoom, Model model){
        model.addAttribute("room", this.roomService.getRoomById(idRoom));
        return "user/room/detailRoom";
    }

    @RequestMapping(value={"personal"}, method = RequestMethod.GET)
    public String getProfile(Model model){
        model.addAttribute("user", getActiveUser());
        return "user/personalArea/personalArea";
    }

    @RequestMapping(value={"personal/subscription"}, method = RequestMethod.GET)
    public String subscriptionIntention(Model model){
        model.addAttribute("courses", getPotentialSubscriptions(getActiveUser()));
        return "user/personalArea/addSubscription";
    }

    @RequestMapping(value = {"personal/subscription/add/{id}"}, method = RequestMethod.POST)
    public String addSubscription(@PathVariable("id") Long idCourse, Model model){
        Course course = this.courseService.getCourseById(idCourse);
        User user = getActiveUser();
        this.courseService.addUser(course, user);
        this.userService.addCourse(course, user);
        model.addAttribute("user", user);
        return "user/personalArea/personalArea";
    }

    @RequestMapping(value = {"personal/subscription/delete/{id}"}, method = RequestMethod.POST)
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
