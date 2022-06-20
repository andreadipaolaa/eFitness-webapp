package it.uniroma3.siw.efitness.efitnesswebapp.validator;

import it.uniroma3.siw.efitness.efitnesswebapp.model.TrainingType;
import it.uniroma3.siw.efitness.efitnesswebapp.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class TrainingTypeValidator implements Validator {

    @Autowired
    private TrainingTypeService trainingTypeService;

    @Override
    public boolean supports(Class<?> clazz) {
        return TrainingType.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()){
            if (this.trainingTypeService.AlreadyExists((TrainingType) target))
                errors.reject("trainingType.duplicated");
        }
    }
}

