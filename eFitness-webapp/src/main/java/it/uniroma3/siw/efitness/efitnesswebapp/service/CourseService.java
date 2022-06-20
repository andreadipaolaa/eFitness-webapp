package it.uniroma3.siw.efitness.efitnesswebapp.service;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.Room;
import it.uniroma3.siw.efitness.efitnesswebapp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;


    @Transactional
    public Course save(Course course){
        return this.courseRepository.save(course);
    }

    public List<Course> getAll(){
        return (List<Course>) this.courseRepository.findAll();
    }


    public Course getCourseById(Long id){
        Optional<Course> result = this.courseRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public void deleteById(Long id){
        this.courseRepository.deleteById(id);
    }


    public boolean AlreadyExists(Course course){
        List<Course> courses= this.courseRepository.findByNameAndTrainingType(course.getName(), course.getTrainingType());
        if (courses.size() >0)
            return true;
        else
            return false;
    }

    @Transactional
    public void modifyById(Long id, Course course){
        Course toModify = getCourseById(id);
        toModify.setName(course.getName());
        toModify.setDescription(course.getDescription());
        toModify.setRoom(course.getRoom());
        toModify.setPhoto(course.getPhoto());
        save(toModify);
    }
}
