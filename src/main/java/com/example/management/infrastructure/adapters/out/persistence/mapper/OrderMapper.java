package com.example.management.infrastructure.adapters.out.persistence.mapper;

import com.example.management.domain.model.*;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderEntity;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderItemEntity;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderStatusEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y entidades JPA.
 */
@Component
public class OrderMapper {

    /**
     * Convierte una entidad de dominio Order a OrderEntity.
     */
    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity(
            order.getId(),
            order.getCustomerId(),
            toEntityStatus(order.getStatus()),
            order.getCreatedAt(),
            order.getUpdatedAt()
        );

        List<OrderItemEntity> itemEntities = order.getItems().stream()
            .map(item -> toItemEntity(item, entity))
            .toList();
        
        entity.setItems(itemEntities);
        return entity;
    }

    /**
     * Convierte una OrderEntity a entidad de dominio Order.
     */
    public Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
            .map(this::toDomainItem)
            .toList();

        return new Order(
            entity.getId(),
            entity.getCustomerId(),
            items,
            toDomainStatus(entity.getStatus()),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    private OrderItemEntity toItemEntity(OrderItem item, OrderEntity orderEntity) {
        return new OrderItemEntity(
            orderEntity,
            item.getProductId(),
            item.getProductName(),
            item.getUnitPrice().getValue().doubleValue(),
            item.getQuantity().getValue()
        );
    }

    private OrderItem toDomainItem(OrderItemEntity entity) {
        return new OrderItem(
            entity.getProductId(),
            entity.getProductName(),
            new Money(java.math.BigDecimal.valueOf(entity.getUnitPrice())),
            new Quantity(entity.getQuantity())
        );
    }

    private OrderStatusEntity toEntityStatus(OrderStatus status) {
        return OrderStatusEntity.valueOf(status.name());
    }

    private OrderStatus toDomainStatus(OrderStatusEntity status) {
        return OrderStatus.valueOf(status.name());
    }
}
