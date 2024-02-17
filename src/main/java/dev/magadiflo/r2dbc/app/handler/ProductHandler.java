package dev.magadiflo.r2dbc.app.handler;


import dev.magadiflo.r2dbc.app.entity.Product;
import dev.magadiflo.r2dbc.app.service.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProductHandler {

    private final IProductService productService;

    public Mono<ServerResponse> getAllProducts(ServerRequest request) {
        Flux<Product> productFlux = this.productService.findAllProducts();
        return ServerResponse.ok().body(productFlux, Product.class);
    }

    public Mono<ServerResponse> getProduct(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return this.productService.findProductById(id)
                .flatMap(productDB -> ServerResponse.ok().bodyValue(productDB))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> saveProduct(ServerRequest request) {
        String path = request.requestPath().value();
        Mono<Product> productMono = request.bodyToMono(Product.class);
        return productMono
                .flatMap(this.productService::saveProduct)
                .flatMap(productDB -> ServerResponse.created(URI.create(path + "/" + productDB.getId())).bodyValue(productDB));
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        Mono<Product> productRequestMono = request.bodyToMono(Product.class);
        return this.productService.findProductById(id)
                .zipWith(productRequestMono, (productDB, productRequest) -> {
                    productDB.setName(productRequest.getName());
                    productDB.setPrice(productRequest.getPrice());
                    return productDB;
                })
                .flatMap(product -> this.productService.updateProduct(id, product))
                .flatMap(productDB -> ServerResponse.ok().bodyValue(productDB))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return this.productService.findProductById(id)
                .flatMap(productDB -> this.productService.deleteProduct(id).then(Mono.just(true)))
                .flatMap(wasDeleted -> ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }


}
