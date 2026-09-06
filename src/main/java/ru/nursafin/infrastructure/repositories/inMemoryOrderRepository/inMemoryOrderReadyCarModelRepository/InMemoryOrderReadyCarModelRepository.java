package ru.nursafin.infrastructure.repositories.inMemoryOrderRepository.inMemoryOrderReadyCarModelRepository;

import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;
import ru.nursafin.domainModel.exceptions.EntityNotFoundException;
import ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderReadyCarModelRepository.OrderReadyCarRepository;
import ru.nursafin.infrastructure.db.dataBase.inMemoryDataBaseStorage.orderReadyCarModelDataStorage.InMemoryOrderReadyCarModelDataStorage;

import java.util.*;

public class InMemoryOrderReadyCarModelRepository implements OrderReadyCarRepository {
    private final InMemoryOrderReadyCarModelDataStorage dataStorage;

    public InMemoryOrderReadyCarModelRepository(InMemoryOrderReadyCarModelDataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public UUID save(OrderReadyCarModel orderReadyCarModel) {
        UUID orderId = orderReadyCarModel.getId();
        dataStorage.orders.put(orderId, orderReadyCarModel);

        return orderId;
    }

    @Override
    public OrderReadyCarModel findById(UUID id) {
        OrderReadyCarModel orderReadyCarModel = dataStorage.orders.get(id);
        if (orderReadyCarModel == null) {
            throw new EntityNotFoundException("Order with id " + id + " not found");
        }

        return orderReadyCarModel;
    }

    @Override
    public List<OrderReadyCarModel> findAll() {
        Collection<OrderReadyCarModel> orderReadyCarModels = dataStorage.orders.values();

        return new ArrayList<>(orderReadyCarModels);
    }

    @Override
    public void deleteById(UUID id) {
        dataStorage.orders.remove(id);
    }
}
