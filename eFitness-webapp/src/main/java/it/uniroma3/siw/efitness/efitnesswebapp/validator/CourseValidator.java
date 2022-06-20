package it.uniroma3.siw.efitness.efitnesswebapp.validator;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CourseValidator implements Validator {

    @Autowired
    private CourseService courseService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Course.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()){
            if (this.courseService.AlreadyExists((Course) target))
                errors.reject("course.duplicated");
        }
    }
}
