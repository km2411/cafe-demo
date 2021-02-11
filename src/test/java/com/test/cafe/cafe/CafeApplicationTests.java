package com.test.cafe.cafe;

import com.test.cafe.cafe.service.CoffeeMachine;

import org.junit.jupiter.api.Test;

class CafeApplicationTests {
	private CoffeeMachine machine = new CoffeeMachine();

	@Test
	public void coffeeMachineTest() {
		String payload = getPayload();
		machine.init(payload);
		System.out.println(machine.getTotalItemsQuantity());
		for (int i = 0; i < 2; i++) {
			System.out.println(machine.prepare());
			System.out.println(machine.getTotalItemsQuantity());
		}
	}

	private String getPayload() {
		return "{ \"machine\": { \"outlets\": { \"count_n\": 3 }, \"total_items_quantity\": { \"hot_water\": 500, \"hot_milk\": 500, \"ginger_syrup\": 100, \"sugar_syrup\": 100, \"tea_leaves_syrup\": 100 }, \"beverages\": { \"hot_tea\": { \"hot_water\": 200, \"hot_milk\": 100, \"ginger_syrup\": 10, \"sugar_syrup\": 10, \"tea_leaves_syrup\": 30 }, \"hot_coffee\": { \"hot_water\": 100, \"ginger_syrup\": 30, \"hot_milk\": 400, \"sugar_syrup\": 50, \"tea_leaves_syrup\": 30 }, \"black_tea\": { \"hot_water\": 300, \"ginger_syrup\": 30, \"sugar_syrup\": 50, \"tea_leaves_syrup\": 30 }, \"green_tea\": { \"hot_water\": 100, \"ginger_syrup\": 30, \"sugar_syrup\": 50, \"green_mixture\": 30 } } } }";
	}

}
