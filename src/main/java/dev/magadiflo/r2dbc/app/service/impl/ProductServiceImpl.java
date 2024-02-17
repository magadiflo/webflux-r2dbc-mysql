package dev.magadiflo.r2dbc.app.service.impl;

import dev.magadiflo.r2dbc.app.entity.Product;
import dev.magadiflo.r2dbc.app.repository.IProductRepository;
import dev.magadiflo.r2dbc.app.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;

    @Override
    public Flux<Product> findAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    public Mono<Product> findProductById(Long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public Mono<Product> updateProduct(Long id, Product product) {
        return this.productRepository.save(new Product(id, product.getName(), product.getPrice()));
    }

    @Override
    public Mono<Void> deleteProduct(Long id) {
        return this.productRepository.deleteById(id);
    }
}
