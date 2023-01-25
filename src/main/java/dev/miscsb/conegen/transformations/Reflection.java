package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;

public class Reflection extends Transformation {
    private int xf, yf, zf;

    public Reflection(boolean x, boolean y, boolean z) {
        this.xf = x? -1 : 1;
        this.yf = y? -1 : 1;
        this.zf = z? -1 : 1;
    }

    @Override
    public Point3D transform(Point3D p) {
        return new Point3D(
            p.x * this.xf,
            p.y * this.yf,
            p.z * this.zf
        );
    }

    @Override
    public Type getType() {
        return Type.Reflection;
    }
}
