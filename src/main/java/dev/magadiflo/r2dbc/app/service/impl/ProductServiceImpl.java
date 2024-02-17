package dev.magadiflo.r2dbc.app.service.impl;

import dev.magadiflo.r2dbc.app.entity.Product;
import dev.magadiflo.r2dbc.app.repository.IProductRepository;
import dev.magadiflo.r2dbc.app.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements IProductService {

    private final IProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Flux<Product> findAllProducts() {
        return this.productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Product> findProductById(Long id) {
        return this.productRepository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("Producto no encontrado")));
    }

    @Override
    @Transactional
    public Mono<Product> saveProduct(Product product) {
        return this.productRepository.findByName(product.getName())
                .flatMap(productDB -> Mono.error(new Exception("El producto ya existe")))
                .switchIfEmpty(this.productRepository.save(product))
                .cast(Product.class);
    }

    @Override
    @Transactional
    public Mono<Product> updateProduct(Long id, Product product) {
        return this.productRepository.findById(id)
                .flatMap(productDB -> {

                    productDB.setName(product.getName());
                    productDB.setPrice(product.getPrice());

                    return this.productRepository.repeatedName(id, product.getName())
                            .flatMap(otherProductDB -> Mono.error(new Exception("Error al actualizar, el nombre del producto ya existe")))
                            .switchIfEmpty(this.productRepository.save(productDB))
                            .cast(Product.class);
                })
                .switchIfEmpty(Mono.error(new Exception("No existe el producto para ser actualizado")));
    }

    @Override
    @Transactional
    public Mono<Boolean> deleteProduct(Long id) {
        return this.productRepository.findById(id)
                .flatMap(productDB -> this.productRepository.deleteById(id).then(Mono.just(true)))
                .switchIfEmpty(Mono.error(new Exception("No existe el producto a eliminar")));
    }
}
