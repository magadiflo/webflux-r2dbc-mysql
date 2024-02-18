package dev.magadiflo.r2dbc.app.service.impl;

import dev.magadiflo.r2dbc.app.dto.ProductDTO;
import dev.magadiflo.r2dbc.app.entity.Product;
import dev.magadiflo.r2dbc.app.exception.CustomException;
import dev.magadiflo.r2dbc.app.repository.IProductRepository;
import dev.magadiflo.r2dbc.app.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Producto no encontrado")));
    }

    @Override
    @Transactional
    public Mono<Product> saveProduct(ProductDTO productDto) {
        return this.productRepository.findByName(productDto.name())
                .flatMap(productDB -> Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "El producto ya existe")))
                .switchIfEmpty(this.productRepository.save(Product.builder().name(productDto.name()).price(productDto.price()).build()))
                .cast(Product.class);
    }

    @Override
    @Transactional
    public Mono<Product> updateProduct(Long id, ProductDTO productDto) {
        return this.productRepository.findById(id)
                .flatMap(productDB -> {

                    productDB.setName(productDto.name());
                    productDB.setPrice(productDto.price());

                    return this.productRepository.repeatedName(id, productDto.name())
                            .flatMap(otherProductDB -> Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Error al actualizar, el nombre del producto ya existe")))
                            .switchIfEmpty(this.productRepository.save(productDB))
                            .cast(Product.class);
                })
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "No existe el producto para ser actualizado")));
    }

    @Override
    @Transactional
    public Mono<Boolean> deleteProduct(Long id) {
        return this.productRepository.findById(id)
                .flatMap(productDB -> this.productRepository.deleteById(id).then(Mono.just(true)))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "No existe el producto a eliminar")));
    }
}
