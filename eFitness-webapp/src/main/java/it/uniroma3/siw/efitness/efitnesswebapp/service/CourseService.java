package it.uniroma3.siw.efitness.efitnesswebapp.service;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course getCourseById(Long idCourse){
        return this.courseRepository.findById(idCourse).get();
    }

    public List<Course> getAll(){
        return (List<Course>) this.courseRepository.findAll();
    }

    @Transactional
    public void save(Course course){
        this.courseRepository.save(course);
    }

    @Transactional
    public void deleteById(Long idCourse){
        this.courseRepository.deleteById(idCourse);
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
