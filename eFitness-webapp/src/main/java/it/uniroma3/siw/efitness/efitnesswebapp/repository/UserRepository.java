package it.uniroma3.siw.efitness.efitnesswebapp.repository;


import it.uniroma3.siw.efitness.efitnesswebapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    public Optional<User> findByEmail(String email);
}