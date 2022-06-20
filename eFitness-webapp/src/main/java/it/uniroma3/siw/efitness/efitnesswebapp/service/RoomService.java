package it.uniroma3.siw.efitness.efitnesswebapp.service;

import it.uniroma3.siw.efitness.efitnesswebapp.model.PersonalTrainer;
import it.uniroma3.siw.efitness.efitnesswebapp.model.Room;
import it.uniroma3.siw.efitness.efitnesswebapp.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

   @Transactional
    public Room save(Room room){
        return this.roomRepository.save(room);
    }

    public List<Room> getAll() {
        return (List<Room>) this.roomRepository.findAll();
    }

    public Room getRoomById(Long id){
        Optional<Room> result = this.roomRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public void deleteById(Long id){
       this.roomRepository.deleteById(id);
    }

    @Transactional
    public void modifyById(Long id, Room room){
        Room toModify = getRoomById(id);
        toModify.setName(room.getName());
        toModify.setDescription(room.getDescription());
        toModify.setPhoto(room.getPhoto());
        save(toModify);
    }

    public boolean AlreadyExists(Room room){
       List<Room> rooms= this.roomRepository.findByName(room.getName());
       if (rooms.size() >0)
           return true;
       else
           return false;
    }


}
