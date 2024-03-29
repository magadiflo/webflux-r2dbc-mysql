package dev.magadiflo.r2dbc.app.service;

import dev.magadiflo.r2dbc.app.dto.ProductDTO;
import dev.magadiflo.r2dbc.app.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {
    Flux<Product> findAllProducts();

    Mono<Product> findProductById(Long id);

    Mono<Product> saveProduct(ProductDTO productDto);

    Mono<Product> updateProduct(Long id, ProductDTO productDto);

    Mono<Boolean> deleteProduct(Long id);
}
