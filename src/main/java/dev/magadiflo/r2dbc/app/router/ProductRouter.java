package dev.magadiflo.r2dbc.app.router;

import dev.magadiflo.r2dbc.app.handler.ProductHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Slf4j
@Configuration
public class ProductRouter {

    private static final String PRODUCTS_PATH = "/api/v2/products";

    @Bean
    public RouterFunction<ServerResponse> routes(ProductHandler productHandler) {
        return RouterFunctions.route()
                .GET(PRODUCTS_PATH, productHandler::getAllProducts)
                .GET(PRODUCTS_PATH + "/{id}", productHandler::getProduct)
                .POST(PRODUCTS_PATH, productHandler::saveProduct)
                .PUT(PRODUCTS_PATH + "/{id}", productHandler::updateProduct)
                .DELETE(PRODUCTS_PATH + "/{id}", productHandler::deleteProduct)
                .build();
    }
}
