package com.example.chapter3.homework.recycler;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet {

    public static List<TestData> getData() {
        List<TestData> result=  new ArrayList();
        result.add(new TestData("Plankson","快去自习"));
        result.add(new TestData("Harry","快去肝ddl"));
        result.add(new TestData("Yang Min","快去调bug"));
        result.add(new TestData("John","Hello"));
        result.add(new TestData("Richard","Expelliarmus"));
        result.add(new TestData("Ant", "Sectumsempra"));
        result.add(new TestData("Ghost", "Morsmordre"));
        result.add(new TestData("Canyon", "Expecto patronum"));
        result.add(new TestData("Rookie", "Imperio"));
        result.add(new TestData("Theshy", "Crucio"));
        result.add(new TestData("Ning", "Silencio"));
        return result;
    }

}
