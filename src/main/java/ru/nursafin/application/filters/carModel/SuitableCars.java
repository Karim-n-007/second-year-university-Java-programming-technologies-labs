package ru.nursafin.application.filters.carModel;

import ru.nursafin.application.repositories.entitiesRepository.carModelRepository.CarModelRepository;
import ru.nursafin.domainModel.entities.car.CarModel;
import ru.nursafin.domainModel.entities.valueObjects.Money;

import java.util.List;

public class SuitableCars {
    private final CarModelRepository carModelRepository;

    public SuitableCars(CarModelRepository carModelRepository) {
        this.carModelRepository = carModelRepository;
    }

    public List<CarModel> GetSuitableCars(CarModelFilter filter) {
        return carModelRepository.findAll().stream()
                .filter(carModel -> filter.getMinBasePrice() == null ||
                        carModel.getBasePrice().compareTo(filter.getMinBasePrice()) >= 0)
                .filter(carModel -> filter.getMaxBasePrice() == null ||
                        carModel.getBasePrice().compareTo(filter.getMaxBasePrice()) <= 0)
                .filter(carModel -> filter.getMinTotalPrice() == null ||
                        CalculateTotalPrice(carModel).compareTo(filter.getMinTotalPrice()) >= 0)
                .filter(carModel -> filter.getMaxTotalPrice() == null ||
                        CalculateTotalPrice(carModel).compareTo(filter.getMaxTotalPrice()) <= 0)
                .filter(carModel -> filter.getBrand() == null ||
                        carModel.getBrand().equalsIgnoreCase(filter.getBrand()))
                .filter(carModel -> filter.getBodyType() == null ||
                        carModel.getBody().getBodyType() == filter.getBodyType())
                .filter(carModel -> filter.getFuelType() == null ||
                        carModel.getEngine().getFuelType() == filter.getFuelType())
                    .filter(carModel -> filter.getGearboxType() == null ||
                        carModel.getGearbox().getGearboxType()== filter.getGearboxType())
                .filter(carModel -> filter.getDrive() == null ||
                        carModel.getDrive() == filter.getDrive())
                .filter(carModel -> filter.getInteriorColor() == null ||
                        carModel.getInterior().getColor().equalsIgnoreCase(filter.getInteriorColor()))
                .filter(carModel -> filter.getMinPower() == null ||
                        carModel.getEngine().getPower().compareTo(filter.getMinPower()) >= 0)
                .filter(carModel -> filter.getMaxPower() == null ||
                        carModel.getEngine().getPower().compareTo(filter.getMaxPower()) <= 0)
                .toList();
    }

    private Money CalculateTotalPrice(CarModel carModel) {
        return carModel.getBasePrice()
                .plus(carModel.getBody().getPrice())
                .plus(carModel.getEngine().getPrice())
                .plus(carModel.getGearbox().getPrice())
                .plus(carModel.getInterior().getPrice())
                .plus(carModel.getSteeringWheel().getPrice())
                .plus(carModel.getWheels().getPrice());
    }
}
