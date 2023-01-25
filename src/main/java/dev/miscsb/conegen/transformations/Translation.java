package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;

public class Translation extends Transformation {
    private double[] vector;

    public Translation(double[] vector) {
        this.vector = vector;
    }

    public Translation(double xd, double yd, double zd) {
        this.vector = new double[] {xd, yd, zd};
    }

    @Override
    public Point3D transform(Point3D p) {
        return new Point3D(
            p.x + this.vector[0],
            p.y + this.vector[1],
            p.z + this.vector[2]
        );
    }

    @Override
    public Type getType() {
        return Type.Translation;
    }
}
