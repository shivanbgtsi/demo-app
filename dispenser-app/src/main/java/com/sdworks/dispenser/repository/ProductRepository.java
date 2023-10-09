package com.sdworks.dispenser.repository;

import com.sdworks.dispenser.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class ProductRepository {

    private static final Set<Product> products = new HashSet<>();

    static {
        Product coca = new Product("CO", "Coca", 1d, 100, true);
        Product redBull = new Product("RB", "Red Bull", 1.25d, 50, true);
        Product water = new Product("WH", "Water", 0.05d, 100, true);
        Product orangeJuice = new Product("OJ", "Orange Juice", 1.95d, 60, true);
        products.add(coca);
        products.add(redBull);
        products.add(water);
        products.add(orangeJuice);
    }

    // Optional
    public Optional<Product> findByDrinkCode(String drinkCode) {
        return products
                .stream().filter(product -> product.getDrinkCode().equalsIgnoreCase(drinkCode))
                .findAny();
    }

    public void updateProduct(Product product) {
        products.add(product);
    }

    public Product saveProduct(Product product) {
        products.add(product);
        return findByDrinkCode(product.getDrinkCode()).get();
    }

    public Optional<Set<Product>> findAll() {
        return Optional.of(products);
    }
}
