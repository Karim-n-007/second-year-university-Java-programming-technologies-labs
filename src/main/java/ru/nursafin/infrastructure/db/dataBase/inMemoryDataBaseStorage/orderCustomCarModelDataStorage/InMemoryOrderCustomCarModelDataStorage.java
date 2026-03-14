package ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.orderCustomCarModelDataStorage;

import ru.nursafin.domainModel.entities.order.OrderCustomCarModel;

import java.util.HashMap;
import java.util.UUID;

public class InMemoryOrderCustomCarModelDataStorage {
    public final HashMap<UUID, OrderCustomCarModel> orders = new HashMap<>();
}
