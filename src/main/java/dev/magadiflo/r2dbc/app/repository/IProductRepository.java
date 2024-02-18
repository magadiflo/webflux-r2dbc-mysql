package dev.magadiflo.r2dbc.app.repository;

import dev.magadiflo.r2dbc.app.entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IProductRepository extends ReactiveCrudRepository<Product, Long> {
    Mono<Product> findByName(String name);

    @Query("""
            SELECT *
            FROM products
            WHERE id <> :id AND name = :name
            """)
    Mono<Product> repeatedName(Long id, String name);
}
