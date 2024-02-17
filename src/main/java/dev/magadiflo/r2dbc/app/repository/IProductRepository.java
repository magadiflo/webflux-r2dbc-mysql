package dev.magadiflo.r2dbc.app.repository;

import dev.magadiflo.r2dbc.app.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IProductRepository extends ReactiveCrudRepository<Product, Long> {
}
