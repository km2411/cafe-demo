package com.test.cafe.cafe.interfaces;

import java.util.List;

public interface ServingMachine {

    public void init(String request);

    public List<String> serve();

    public boolean refill(String request);
}
