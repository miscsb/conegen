package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;

public class Dilation extends Transformation {
    private double xf, yf, zf;
    private boolean combined;

    public Dilation(double xf, double yf, double zf) {
        this.xf = xf;
        this.yf = yf;
        this.zf = zf;
        this.combined = (xf < 0) || (yf < 0) || (zf < 0);
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
        return this.combined ? Type.Combined : Type.Dilation;
    }
}
