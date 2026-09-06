package ru.nursafin.application.filters.carModel;

import ru.nursafin.domainModel.entities.sparePart.body.BodyType;
import ru.nursafin.domainModel.entities.sparePart.engine.Drive;
import ru.nursafin.domainModel.entities.sparePart.engine.FuelType;
import ru.nursafin.domainModel.entities.sparePart.gearbox.GearboxType;
import ru.nursafin.domainModel.entities.valueObjects.EngineDisplacement;
import ru.nursafin.domainModel.entities.valueObjects.Money;
import ru.nursafin.domainModel.entities.valueObjects.Power;
import ru.nursafin.domainModel.exceptions.DomainValidationException;

public class CarModelFilter {
    private Money minBasePrice;
    private Money maxBasePrice;
    private Money minTotalPrice;
    private Money maxTotalPrice;
    private String brand;
    private String model;
    private String color;
    private BodyType bodyType;
    private FuelType fuelType;
    private GearboxType gearboxType;
    private Drive drive;
    private String interiorColor;
    private Power minPower;
    private Power maxPower;
    private EngineDisplacement minEngineDisplacement;
    private EngineDisplacement maxEngineDisplacement;

    public CarModelFilter withMinBasePrice(Money minBasePrice) {
        this.minBasePrice = minBasePrice;
        return this;
    }

    public CarModelFilter withMaxBasePrice(Money maxBasePrice) {
        this.maxBasePrice = maxBasePrice;
        return this;
    }

    public CarModelFilter withMinTotalPrice(Money minTotalPrice) {
        this.minTotalPrice = minTotalPrice;
        return this;
    }


    public CarModelFilter withMaxTotalPrice(Money maxTotalPrice) {
        this.maxTotalPrice = maxTotalPrice;
        return this;
    }

    public CarModelFilter withBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public CarModelFilter withModel(String model) {
        this.model = model;
        return this;
    }

    public CarModelFilter withColor(String color) {
        this.color = color;
        return this;
    }

    public CarModelFilter withBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
        return this;
    }

    public CarModelFilter withFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
        return this;
    }

    public CarModelFilter withGearboxType(GearboxType gearboxType) {
        this.gearboxType = gearboxType;
        return this;
    }

    public CarModelFilter withDrive(Drive drive) {
        this.drive = drive;
        return this;
    }

    public CarModelFilter withInteriorColor(String interiorColor) {
        this.interiorColor = interiorColor;
        return this;
    }

    public CarModelFilter withMinPower(Power minPower) {
        this.minPower = minPower;
        return this;
    }

    public CarModelFilter withMaxPower(Power maxPower) {
        this.maxPower = maxPower;
        return this;
    }

    public CarModelFilter withMinEngineDisplacement(EngineDisplacement minEngineDisplacement) {
        this.minEngineDisplacement = minEngineDisplacement;
        return this;
    }

    public CarModelFilter withMaxEngineDisplacement(EngineDisplacement maxEngineDisplacement) {
        this.maxEngineDisplacement = maxEngineDisplacement;
        return this;
    }

    public void validate() {
        if (model != null && brand == null) {
            throw new DomainValidationException("Model filter is available only when brand is chosen");
        }
    }

    public Money getMinBasePrice() {
        return minBasePrice;
    }

    public Money getMaxBasePrice() {
        return maxBasePrice;
    }

    public Money getMinTotalPrice() {
        return minTotalPrice;
    }

    public Money getMaxTotalPrice() {
        return maxTotalPrice;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getColor() {
        return color;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public GearboxType getGearboxType() {
        return gearboxType;
    }

    public Drive getDrive() {
        return drive;
    }

    public String getInteriorColor() {
        return interiorColor;
    }

    public Power getMinPower() {
        return minPower;
    }

    public Power getMaxPower() {
        return maxPower;
    }

    public EngineDisplacement getMinEngineDisplacement() {
        return minEngineDisplacement;
    }

    public EngineDisplacement getMaxEngineDisplacement() {
        return maxEngineDisplacement;
    }
}
