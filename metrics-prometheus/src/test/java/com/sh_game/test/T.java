package com.sh_game.test;

import com.sh_game.metrics.core.Metrics;

public class T {
    public static void main(String[] args) {

        System.out.println("Aa");

        Metrics.meter().counter("aaad", "dd", "ddd");
    }
}
