package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;

public record Dilation(double xf, double yf, double zf) implements Transformation {
    @Override
    public Point3D transform(Point3D p) {
        return new Point3D(
            p.x * this.xf,
            p.y * this.yf,
            p.z * this.zf
        );
    }
}
