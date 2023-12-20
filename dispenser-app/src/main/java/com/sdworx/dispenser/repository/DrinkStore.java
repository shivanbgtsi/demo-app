package com.sdworx.dispenser.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdworx.dispenser.entity.Drink;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Component
public class DrinkStore {
    private static final ObjectMapper mapper = new ObjectMapper();
    @Value("${file.transaction}")
    private String transactionFile;

    public Map<String, Drink> updateDrinkElementsToFile(Drink drink) throws IOException {
        Map<String, Drink> loader = getDrinkElementsFromFile();

        loader.put(drink.getDrinkCode(), drink);
        mapper.writeValue(new File(transactionFile), loader);
        return loader;
    }

    public Drink saveDrinkElementsToFile(Drink drink) throws IOException {
        Map<String, Drink> loader = getDrinkElementsFromFile();

        loader.put(drink.getDrinkCode(), drink);
        mapper.writeValue(new File(transactionFile), loader);
        return drink;
    }

    public Map<String, Drink> getDrinkElementsFromFile() {
        try {
            return mapper.readValue(new File(transactionFile), new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new BeanCreationException("Bean creation exception");
        }
    }

    public void deleteDrinkElementsFromFile(String drinkCode) throws IOException {
        Map<String, Drink> loader = getDrinkElementsFromFile();
        loader.remove(drinkCode);
        mapper.writeValue(new File(transactionFile), loader);
    }
}
