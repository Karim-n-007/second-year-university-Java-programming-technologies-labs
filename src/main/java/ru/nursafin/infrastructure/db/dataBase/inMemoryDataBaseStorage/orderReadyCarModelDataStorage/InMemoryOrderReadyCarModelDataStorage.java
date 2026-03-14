package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.orderReadyCarModelDataStorage;

import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryOrderReadyCarModelDataStorage {
    public final HashMap<UUID, OrderReadyCarModel> orders = new HashMap<>();
}
