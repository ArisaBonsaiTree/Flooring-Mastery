package com.av.flooringmastery.ui;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

public interface UserIO {

    void print(String msg);

    void error(String msg);

    void printF(String format, Object... args);

    void printEmptyLine();
    double readDouble(String prompt);

    double readDouble(String prompt, double min, double max);

    float readFloat(String prompt);

    float readFloat(String prompt, float min, float max);

    int readInt(String prompt);

    int readInt(String prompt, int min, int max);

    long readLong(String prompt);

    long readLong(String prompt, long min, long max);

    String readString(String prompt);

    BigDecimal readBigDecimal(String prompt);

    String printHashSet(Set<String> set);


    void placeAfterNumber();
}
