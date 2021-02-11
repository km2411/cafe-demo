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
        return "Initialized Coffee Point with - " + machine.getTotalItemsQuantity().toString();
    }

    @GetMapping(value = "/serve")
    @ResponseBody
    public String ServeBeverages() {
        return machine.serve().toString();
    }

    @GetMapping(value = "/status")
    @ResponseBody
    public String GetStatus() {
        return machine.getTotalItemsQuantity().toString();
    }

    @PostMapping(value = "/refill")
    @ResponseBody
    public boolean Refill(@RequestBody String request) {
        try {
            machine.refill(request);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}
