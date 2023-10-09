package com.sdworks.dispenser.services;

import com.sdworks.dispenser.entity.Product;
import com.sdworks.dispenser.exception.InsufficientFunds;
import com.sdworks.dispenser.exception.NotFoundException;
import com.sdworks.dispenser.model.DrinksModel;
import com.sdworks.dispenser.model.ProductModel;
import com.sdworks.dispenser.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductsService {

    @Autowired
    private ProductRepository productRepository;

    public DrinksModel dispense(String drinkCode, Double coin, int noOfUnits) {
        Product product = productRepository.findByDrinkCode(drinkCode)
                .orElseThrow(() -> new NotFoundException("Drink code not found"));

        if (product.getAvailableLimit() < noOfUnits) {
            throw new RuntimeException("Quantity not available");
        }

        Double totalAmount = product.getProductPrice() * noOfUnits;
        if (coin < totalAmount) {
            throw new InsufficientFunds("Insufficient funds");
        }
        product.setAvailableLimit(product.getMaxLimit() - noOfUnits);
        if (product.getAvailableLimit() == product.getMaxLimit()) {
            product.setAvailable(false);
        }
        productRepository.updateProduct(product);
        return new DrinksModel(drinkCode, product.getDrinkName(), noOfUnits, coin - totalAmount);
    }

    public Product saveProduct(ProductModel productModel) {
        Product product = new Product(productModel.getDrinkCode(), productModel.getDrinkName(),
                productModel.getProductPrice(), productModel.getMaxLimit(), productModel.isAvailable());

        return productRepository.saveProduct(product);
    }

    public Set<ProductModel> getDrinksStatus() {
        Set<Product> productNotAvailable = productRepository.findAll().orElseThrow(() -> new NotFoundException("Product Not available"));
        return productNotAvailable.stream().map(product -> new ProductModel(product.getDrinkCode(), product.getDrinkName(),
                product.getProductPrice(), product.getMaxLimit(), product.isAvailable(), product.getAvailableLimit())).collect(Collectors.toSet());
    }

}
