package it.uniroma3.siw.efitness.efitnesswebapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "personal_trainer")
public class PersonalTrainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String surname;

    @Column(length=2500)
    private String description;

    @OneToMany(mappedBy = "personalTrainer")
    private List<Course> courses;


}