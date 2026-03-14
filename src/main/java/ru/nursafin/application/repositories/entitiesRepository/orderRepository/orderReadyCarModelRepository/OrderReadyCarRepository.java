package ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderReadyCarModelRepository;

import ru.nursafin.domainModel.entities.order.OrderReadyCarModel;

import java.util.List;
import java.util.UUID;

public interface OrderReadyCarRepository {
    UUID save(OrderReadyCarModel orderReadyCarModel);

    OrderReadyCarModel findById(UUID id);

    List<OrderReadyCarModel> findAll();

    void deleteById(UUID id);
}
