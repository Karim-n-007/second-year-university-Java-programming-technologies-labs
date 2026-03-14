package ru.nursafin.application.repositories.entitiesRepository.orderRepository.orderCustomCarRepository;

import ru.nursafin.domainModel.entities.order.OrderCustomCarModel;

import java.util.List;
import java.util.UUID;

public interface OrderCustomCarRepository {
    UUID save(OrderCustomCarModel orderCustomCarModel);

    OrderCustomCarModel findById(UUID id);

    List<OrderCustomCarModel> findAll();

    void deleteById(UUID id);
}
