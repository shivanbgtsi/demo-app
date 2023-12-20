package com.sdworx.dispenser.controller;

import com.sdworx.dispenser.entity.Drink;
import com.sdworx.dispenser.model.DrinkModel;
import com.sdworx.dispenser.services.ProductsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController {

    private ProductsService productsService;

    @PostMapping
    public ResponseEntity<Drink> create(@RequestBody DrinkModel drinkModel) {
        return new ResponseEntity<>(productsService.saveProduct(drinkModel), HttpStatus.CREATED);
    }

    @GetMapping
    public Set<DrinkModel> getAllProducts() {
        return productsService.getProducts();
    }

    @DeleteMapping("/{drinkCode}")
    public ResponseEntity<Void> delete(@PathVariable("drinkCode") String drinkCode) {
        productsService.deleteProducts(drinkCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    public ResponseEntity<Void> update(@RequestBody DrinkModel drinkModel) {
        productsService.updateProduct(drinkModel);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
