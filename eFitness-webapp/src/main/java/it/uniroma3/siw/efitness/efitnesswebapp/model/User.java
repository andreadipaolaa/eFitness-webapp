package it.uniroma3.siw.efitness.efitnesswebapp.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity @Getter @Setter
@Table(name="users") //in postgres user is a reserved key
public class User {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	private String surname;

	private String email;

	@ManyToMany
	private List<Course> courses;
}
