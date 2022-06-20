package it.uniroma3.siw.efitness.efitnesswebapp.validator;

import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.service.PersonalTrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PersonalTrainerValidator implements Validator {
    @Autowired
    private PersonalTrainerService personalTrainerService;

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonalTrainer.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()){
            if (this.personalTrainerService.alreadyExists((PersonalTrainer) target))
                errors.reject("personalTrainer.duplicato");
        }
    }
}
