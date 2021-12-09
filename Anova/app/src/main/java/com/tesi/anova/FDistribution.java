package com.tesi.anova;

import DistLib.f;

public class FDistribution {

    public static double ftable(double alpha, double n1, double n2)
    {
        double step = 0.01,
                x = 0,
                test = 0,
                p = 1.0 - alpha;

        while (test < p) {
            x += step;
            test = f.cumulative(x, n1, n2);
        }

        return x;
    }
}
