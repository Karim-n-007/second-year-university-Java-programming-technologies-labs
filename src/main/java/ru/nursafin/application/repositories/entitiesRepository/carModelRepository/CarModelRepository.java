package ru.nursafin.application.repositories.entitiesRepository.carModelRepository;

import ru.nursafin.domainModel.entities.car.CarModel;

import java.util.List;
import java.util.UUID;

public interface CarModelRepository {
    UUID save(CarModel carModel);

    CarModel findById(UUID id);

    List<CarModel> findAll();

    void deleteById(UUID id);
}
