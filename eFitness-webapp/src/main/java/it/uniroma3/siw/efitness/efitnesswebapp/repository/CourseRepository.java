package it.uniroma3.siw.efitness.efitnesswebapp.repository;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.TrainingType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long> {
    List<Course> findByNameAndTrainingType(String name, TrainingType trainingType);
}
