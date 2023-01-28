package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;

public record Reflection(boolean reflectedX, boolean reflectedY, boolean reflectedZ) implements Transformation {
    @Override
    public Point3D transform(Point3D p) {
        return new Point3D(
            this.reflectedX ? -p.x : p.x,
            this.reflectedX ? -p.y : p.y,
            this.reflectedX ? -p.z : p.z
        );
    }
}
