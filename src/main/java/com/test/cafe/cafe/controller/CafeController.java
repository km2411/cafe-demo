package com.test.cafe.cafe.controller;

import com.test.cafe.cafe.service.CoffeeMachine;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping(value = "/cafe")
public class CafeController {
    private CoffeeMachine machine = new CoffeeMachine();

    @PostMapping(value = "/init")
    @ResponseBody
    public String Initialize(@RequestBody String coffePointInfo) {
        try {
            machine.init(coffePointInfo);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return machine.getTotalItemsQuantity().toString();
    }

    @GetMapping(value = "/serve")
    @ResponseBody
    public String ServeBeverages() {
        return machine.prepare().toString();
    }
}
