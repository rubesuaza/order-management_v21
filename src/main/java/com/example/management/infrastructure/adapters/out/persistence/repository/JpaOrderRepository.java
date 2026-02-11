package com.example.management.infrastructure.adapters.out.persistence.repository;

import com.example.management.domain.model.Order;
import com.example.management.infrastructure.adapters.out.persistence.entity.OrderEntity;
import com.example.management.infrastructure.adapters.out.persistence.mapper.OrderMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para OrderEntity.
 */
interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {
}

/**
 * Implementación del adaptador de persistencia para OrderRepository.
 */
@Repository
public class JpaOrderRepository implements com.example.management.application.ports.out.OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final OrderMapper mapper;

    public JpaOrderRepository(OrderJpaRepository jpaRepository, OrderMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return jpaRepository.findById(orderId)
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(String orderId) {
        return jpaRepository.existsById(orderId);
    }
}
