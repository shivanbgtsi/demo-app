package com.sdworx.dispenser.services;

import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.exception.NotFoundException;
import com.sdworx.dispenser.model.DrinkModel;
import com.sdworx.dispenser.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductsService {

    private ProductRepository productRepository;

    public Drink getProductByDrinkCode(String drinkCode) {
        return productRepository.findByDrinkCode(drinkCode)
                .orElseThrow(() -> new NotFoundException("Drink code not found"));
    }

    public Drink saveProduct(DrinkModel drinkModel) {
        Optional<Drink> byDrinkCode = productRepository.findByDrinkCode(drinkModel.getDrinkCode());
        if (byDrinkCode.isPresent()) {
            throw new RuntimeException("Drink exists in product catalog");
        }
        validateAvailableAndMaxQuantity(drinkModel.getAvailableQuantity(), drinkModel.getMaxLimit());
        Drink drink = new Drink(drinkModel.getDrinkCode(), drinkModel.getDrinkName(), drinkModel.getProductPrice(),
                drinkModel.getMaxLimit(), drinkModel.getAvailableQuantity());
        return productRepository.save(drink);
    }

    public void updateProduct(DrinkModel drinkModel) {
        getProductByDrinkCode(drinkModel.getDrinkCode());
        validateAvailableAndMaxQuantity(drinkModel.getAvailableQuantity(), drinkModel.getMaxLimit());
        Drink drink = convertDrinkModelToDrink(drinkModel);
        productRepository.update(drink);
    }

    private Drink convertDrinkModelToDrink(DrinkModel drinkModel) {
        return new Drink(drinkModel.getDrinkCode(), drinkModel.getDrinkName(), drinkModel.getProductPrice(),
                drinkModel.getMaxLimit(), drinkModel.getAvailableQuantity());
    }

    public Set<DrinkModel> getProducts() {
        Map<String, Drink> stringDrinkHashMap = productRepository.findAll().orElseThrow(() ->
                new NotFoundException("Product Not available"));
        Collection<Drink> values = stringDrinkHashMap.values();
        return values.stream().map(product -> new DrinkModel(product.getDrinkCode(), product.getDrinkName(),
                product.getPrice(), product.getMaxLimit(), product.getAvailableQuantity())).collect(Collectors.toSet());
    }

    public void deleteProducts(String drinkCode) {
        getProductByDrinkCode(drinkCode);
        productRepository.delete(drinkCode);
    }

    private void validateAvailableAndMaxQuantity(int availableQuantity, int maximumQuantity) {
        if (availableQuantity > maximumQuantity) {
            throw new RuntimeException("Available quantity should be less than maximum quantity");
        }
    }
}
