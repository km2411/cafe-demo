package com.test.cafe.cafe.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.cafe.cafe.interfaces.ServingMachine;
import com.test.cafe.cafe.models.Beverage;

public class CoffeeMachine implements ServingMachine {
    private Integer outlets;
    private ConcurrentHashMap<String, Integer> totalItemsQuantity = new ConcurrentHashMap<>();
    private List<Beverage> beverages = new ArrayList<>();
    private List<String> response = new ArrayList<>();

    public Map<String, Integer> getTotalItemsQuantity() {
        return totalItemsQuantity;
    }

    @Override
    public void init(String request) {
        Map<String, Object> info = parse(request, "machine");
        if (info != null) {
            Map<String, Object> outlets = (Map<String, Object>) info.getOrDefault("outlets", null);
            Map<String, Object> totalItems = (Map<String, Object>) info.getOrDefault("total_items_quantity", null);
            Map<String, Object> beverages = (Map<String, Object>) info.getOrDefault("beverages", null);
            if (outlets == null || totalItems == null || beverages == null) {
                throw new IllegalArgumentException();
            }
            extractValues(outlets, totalItems, beverages);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public synchronized List<String> serve() {
        int count = this.outlets;
        Collections.shuffle(this.beverages);
        Iterator<Beverage> iter = this.beverages.iterator();
        while (iter.hasNext() && count > 0) {
            Beverage bv = iter.next();
            if (canBeServed(bv)) {
                count -= 1;
                adjustQuantities(bv);
            }
        }
        List<String> res = this.response;
        this.response = new ArrayList<>();
        return res;
    }

    @Override
    public synchronized boolean refill(String request) {
        Map<String, Object> info = parse(request, "total_items_quantity");
        if (info != null) {
            ConcurrentHashMap<String, Integer> itemQuMap = new ConcurrentHashMap<>();
            for (Entry<String, Object> item : info.entrySet()) {
                itemQuMap.put(item.getKey(), (Integer) item.getValue());
            }
            refillItems(itemQuMap);
        } else {
            throw new IllegalArgumentException();
        }
        return true;
    }

    private Map<String, Object> parse(String request, String root) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(request, new TypeReference<Map<String, Object>>() {
            });
            return (Map<String, Object>) map.getOrDefault(root, null);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void extractValues(Map<String, Object> outlets, Map<String, Object> totalItems,
            Map<String, Object> beverages) {
        ConcurrentHashMap<String, Integer> itemQuMap = new ConcurrentHashMap<>();
        List<Beverage> bvs = new ArrayList<>();

        for (Entry<String, Object> item : totalItems.entrySet()) {
            itemQuMap.put(item.getKey(), (Integer) item.getValue());
        }

        for (Entry<String, Object> beverage : beverages.entrySet()) {
            Beverage bv = new Beverage(beverage.getKey());
            Map<String, Integer> ing = new HashMap<>();
            for (Entry<String, Object> item : ((Map<String, Object>) beverage.getValue()).entrySet()) {
                ing.put(item.getKey(), (Integer) item.getValue());
            }
            bv.setIngredients(ing);
            bvs.add(bv);
        }

        this.outlets = (Integer) outlets.get("count_n");
        this.totalItemsQuantity = itemQuMap;
        this.beverages = bvs;
    }

    private boolean canBeServed(Beverage bv) {
        for (Entry<String, Integer> item : bv.getIngredients().entrySet()) {
            Integer amnt = this.totalItemsQuantity.getOrDefault(item.getKey(), null);
            if (amnt == null) {
                this.response.add(bv.getName() + " cannot be prepared because " + item.getKey() + " is not present");
                return false;
            }
            if (amnt < item.getValue()) {
                this.response.add(bv.getName() + " cannot be prepared because " + item.getKey() + " is not sufficient");
                return false;
            }
        }
        this.response.add(bv.getName() + " is prepared");
        return true;
    }

    private void adjustQuantities(Beverage bv) {
        for (Entry<String, Integer> item : bv.getIngredients().entrySet()) {
            update(item, false);
        }
    }

    private void refillItems(ConcurrentHashMap<String, Integer> itemQuMap) {
        for (Entry<String, Integer> item : itemQuMap.entrySet()) {
            update(item, true);
        }
    }

    private void update(Entry<String, Integer> item, boolean add) {
        int factor = add ? 1 : -1;
        Integer amnt = this.totalItemsQuantity.get(item.getKey());
        Integer val = factor * item.getValue();
        this.totalItemsQuantity.put(item.getKey(), amnt + val);
    }

}
