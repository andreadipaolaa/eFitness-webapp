package it.uniroma3.siw.efitness.efitnesswebapp.service;


import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.repository.PersonalTrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PersonalTrainerService {
    @Autowired
    private PersonalTrainerRepository personalTrainerRepository;

    @Transactional
    public PersonalTrainer save(PersonalTrainer personalTrainer){
        return this.personalTrainerRepository.save(personalTrainer);
    }

    public List<PersonalTrainer> getAll(){
        return (List<PersonalTrainer>) this.personalTrainerRepository.findAll();
    }

    public PersonalTrainer getPersonalTrainerById(Long id){
        Optional<PersonalTrainer> result = this.personalTrainerRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public void deleteById(Long id){
        this.personalTrainerRepository.deleteById(id);
    }

    @Transactional
    public void modifyById(Long id, PersonalTrainer trainer){
        PersonalTrainer toModify = getPersonalTrainerById(id);
        toModify.setName(trainer.getName());
        toModify.setSurname(trainer.getSurname());
        toModify.setDescription(trainer.getDescription());
        toModify.setPhoto(trainer.getPhoto());
        save(toModify);
    }

    public boolean alreadyExists(PersonalTrainer personalTrainer){
        List<PersonalTrainer> personalTrainers= this.personalTrainerRepository.findByNameAndSurname(personalTrainer.getName(), personalTrainer.getSurname());
        if (personalTrainers.size() >0)
            return true;
        else
            return false;
    }
}
