package ru.nursafin.presentation.console;

import ru.nursafin.application.filters.carModel.CarModelFilter;
import ru.nursafin.domainModel.entities.sparePart.body.BodyType;
import ru.nursafin.domainModel.entities.sparePart.engine.Drive;
import ru.nursafin.domainModel.entities.sparePart.engine.FuelType;
import ru.nursafin.domainModel.entities.sparePart.gearbox.GearboxType;

public final class CarFilterReader {
    private CarFilterReader() {
    }

    public static CarModelFilter read(ConsoleIo io) {
        io.print("Empty value means that the filter is not applied.");

        CarModelFilter filter = new CarModelFilter()
                .withMinBasePrice(io.readOptionalMoney("Price from: "))
                .withMaxBasePrice(io.readOptionalMoney("Price to: "))
                .withBrand(io.readOptionalText("Brand: "));

        if (filter.getBrand() != null) {
            filter = filter.withModel(io.readOptionalText("Model: "));
        }

        return filter
                .withColor(io.readOptionalText("Car color: "))
                .withBodyType(io.readOptionalEnum("Body type", BodyType.class))
                .withFuelType(io.readOptionalEnum("Fuel type", FuelType.class))
                .withMinPower(io.readOptionalPower("Power from: "))
                .withMaxPower(io.readOptionalPower("Power to: "))
                .withMinEngineDisplacement(io.readOptionalDisplacement("Engine displacement from (cm3): "))
                .withMaxEngineDisplacement(io.readOptionalDisplacement("Engine displacement to (cm3): "))
                .withGearboxType(io.readOptionalEnum("Gearbox type", GearboxType.class))
                .withDrive(io.readOptionalEnum("Drive", Drive.class))
                .withInteriorColor(io.readOptionalText("Interior color: "));
    }
}
