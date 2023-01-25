package dev.miscsb.conegen.util;

public class Geo2DUtil {
    public static double[] rotate(double x, double y, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new double[] {
            x * cos - y * sin,
            y * cos + x * sin
        };
    }
}
