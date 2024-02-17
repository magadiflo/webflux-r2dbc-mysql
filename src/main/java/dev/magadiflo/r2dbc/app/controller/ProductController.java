package dev.magadiflo.r2dbc.app.controller;

import dev.magadiflo.r2dbc.app.entity.Product;
import dev.magadiflo.r2dbc.app.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/products")
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public Flux<Product> getAllProducts() {
        return this.productService.findAllProducts();
    }

    @GetMapping(path = "/{id}")
    public Mono<Product> getById(@PathVariable Long id) {
        return this.productService.findProductById(id);
    }

    @PostMapping
    public Mono<Product> saveProduct(@RequestBody Product product) {
        return this.productService.saveProduct(product);
    }

    @PutMapping(path = "/{id}")
    public Mono<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return this.productService.updateProduct(id, product);
    }

    @DeleteMapping(path = "/{id}")
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        return this.productService.deleteProduct(id);
    }
}
