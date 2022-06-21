package it.uniroma3.siw.efitness.efitnesswebapp.validator;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Room;
import it.uniroma3.siw.efitness.efitnesswebapp.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RoomValidator implements Validator {
    @Autowired
    private RoomService roomService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Room.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()){
            if (this.roomService.AlreadyExists((Room)target)){
                errors.reject("room.duplicated");
            }
        }
    }
}
