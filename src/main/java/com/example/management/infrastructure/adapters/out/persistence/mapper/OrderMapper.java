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
            .collect(Collectors.toList());
        
        entity.setItems(itemEntities);
        return entity;
    }

    /**
     * Convierte una OrderEntity a entidad de dominio Order.
     */
    public Order toDomain(OrderEntity entity) {
        List<OrderItem> items = entity.getItems().stream()
            .map(this::toDomainItem)
            .collect(Collectors.toList());

        // Reconstruir la orden con todos sus atributos usando reflexión
        return reconstructOrder(entity, items);
    }

    /**
     * Reconstruye una orden desde la entidad JPA usando reflexión para establecer campos finales.
     */
    private Order reconstructOrder(OrderEntity entity, List<OrderItem> items) {
        try {
            // Crear una nueva instancia usando el constructor
            Order order = new Order(entity.getCustomerId(), items);
            
            // Usar reflexión para establecer campos finales y estado
            java.lang.reflect.Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, entity.getId());
            
            java.lang.reflect.Field statusField = Order.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(order, toDomainStatus(entity.getStatus()));
            
            java.lang.reflect.Field createdAtField = Order.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(order, entity.getCreatedAt());
            
            java.lang.reflect.Field updatedAtField = Order.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(order, entity.getUpdatedAt());
            
            return order;
        } catch (Exception e) {
            throw new RuntimeException("Error al reconstruir la orden desde la entidad JPA", e);
        }
    }

    private OrderItemEntity toItemEntity(OrderItem item, OrderEntity orderEntity) {
        return new OrderItemEntity(
            orderEntity,
            item.getProductId(),
            item.getProductName(),
            item.getUnitPrice().getValue(),
            item.getQuantity().getValue()
        );
    }

    private OrderItem toDomainItem(OrderItemEntity entity) {
        return new OrderItem(
            entity.getProductId(),
            entity.getProductName(),
            new Money(entity.getUnitPrice()),
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
