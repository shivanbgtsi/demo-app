package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.exception.NotFoundException;
import com.sdworx.dispenser.model.ProductModel;
import com.sdworx.dispenser.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductsService {

    @Autowired
    private ProductRepository productRepository;
    
    public Drink getProductByDrinkCode(String drinkCode) {
        return productRepository.findByDrinkCode(drinkCode)
                .orElseThrow(() -> new NotFoundException("Drink code not found"));

    }

    public Drink saveProduct(ProductModel productModel) {

        Drink product = new Drink(productModel.getDrinkCode(), productModel.getDrinkName(),
                productModel.getProductPrice(), productModel.getMaxLimit(), productModel.getMaxLimit());

        return productRepository.saveProduct(product);
    }

    public void updateProduct(Drink drink) {
        productRepository.updateProduct(drink);
    }

    public Set<ProductModel> getProducts() {
        Map<String, Drink> stringDrinkHashMap = productRepository.findAll().orElseThrow(() ->
                new NotFoundException("Product Not available"));
        Collection<Drink> values = stringDrinkHashMap.values();
        return values.stream().map(product -> new ProductModel(product.getDrinkCode(), product.getDrinkName(),
                product.getPrice(), product.getMaxLimit(), product.getAvailableQuantity())).collect(Collectors.toSet());
    }

}
