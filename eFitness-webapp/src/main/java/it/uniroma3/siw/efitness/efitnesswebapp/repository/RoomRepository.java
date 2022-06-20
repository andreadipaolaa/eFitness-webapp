package it.uniroma3.siw.efitness.efitnesswebapp.repository;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room,Long> {
    List<Room> findByName(String name);
}
