package it.uniroma3.siw.efitness.efitnesswebapp.repository;

import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonalTrainerRepository extends CrudRepository<PersonalTrainer, Long> {
    List<PersonalTrainer> findByNameAndSurname(String name, String surname);
}
