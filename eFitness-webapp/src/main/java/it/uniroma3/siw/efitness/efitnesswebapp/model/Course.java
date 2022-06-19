package it.uniroma3.siw.efitness.efitnesswebapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique=true)
    private String name;

    @Column(length=2500)
    private String description;

    @ManyToOne
    private TrainingType trainingType;

    @ManyToOne
    private PersonalTrainer personalTrainer;

    @ManyToMany (mappedBy = "courses")
    private List<User> users;

    @OneToOne
    private Room room;

}