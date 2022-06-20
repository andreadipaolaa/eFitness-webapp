package it.uniroma3.siw.efitness.efitnesswebapp.repository;

import it.uniroma3.siw.efitness.efitnesswebapp.model.TrainingType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TrainingTypeRepository extends CrudRepository<TrainingType, Long> {
    List<TrainingType> findByName(String name);
}
