package ru.nursafin.infrastructure.repositories.inMemoryOrderRepository.inMemoryOrderCustomCarModelRepository;

import ru.nursafin.domainModel.entities.order.OrderCustomCarModel;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderCustomCarRepository.OrderCustomCarRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.orderCustomCarModelDataStorage.InMemoryOrderCustomCarModelDataStorage;

import java.util.*;

public class InMemoryOrderCustomCarModelRepository implements OrderCustomCarRepository {
    private final InMemoryOrderCustomCarModelDataStorage dataStorage;

    public InMemoryOrderCustomCarModelRepository(InMemoryOrderCustomCarModelDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(OrderCustomCarModel orderCustomCarModel) {
        UUID orderId = madeCorrectId(dataStorage.orders);
        dataStorage.orders.put(orderId, orderCustomCarModel);

        return orderId;
    }

    @Override
    public OrderCustomCarModel findById(UUID id) {
        OrderCustomCarModel orderCustomCarModel = dataStorage.orders.get(id);
        if (orderCustomCarModel == null) {
            throw new EntityNotFoundException("Order with id " + id + " not found");
        }

        return orderCustomCarModel;
    }

    @Override
    public List<OrderCustomCarModel> findAll() {
        Collection<OrderCustomCarModel> orders = dataStorage.orders.values();

        return new ArrayList<>(orders);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.orders.remove(id);
    }

    private UUID madeCorrectId(HashMap<UUID, OrderCustomCarModel> OrderCustomCarModelsMap) {
        while (true) {
            UUID orderId = UUID.randomUUID();

            if (!OrderCustomCarModelsMap.containsKey(orderId)) {
                return orderId;
            }
        }
    }
}
