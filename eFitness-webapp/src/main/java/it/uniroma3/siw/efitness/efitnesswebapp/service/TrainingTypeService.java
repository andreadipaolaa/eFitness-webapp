package it.uniroma3.siw.efitness.efitnesswebapp.service;

import it.uniroma3.siw.efitness.efitnesswebapp.model.Course;
import it.uniroma3.siw.efitness.efitnesswebapp.model.TrainingType;
import it.uniroma3.siw.efitness.efitnesswebapp.repository.TrainingTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingTypeService {
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Transactional
    public TrainingType save(TrainingType trainingType){
        return this.trainingTypeRepository.save(trainingType);
    }

    public List<TrainingType> getAll(){
        return (List<TrainingType>) this.trainingTypeRepository.findAll();
    }

    public TrainingType getTrainingTypeById(Long id){
        Optional<TrainingType> result = this.trainingTypeRepository.findById(id);
        return result.orElse(null);
    }

    @Transactional
    public void deleteById(Long id){
        this.trainingTypeRepository.deleteById(id);
    }


    public boolean AlreadyExists(TrainingType trainingType){
        List<TrainingType> trainingTypes= this.trainingTypeRepository.findByName(trainingType.getName());
        if (trainingTypes.size() >0)
            return true;
        else
            return false;
    }
}
