package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;

public record CompositeTransformation(Transformation before, Transformation after) implements Transformation {
    @Override
    public Point3D transform(Point3D p) {
        return after.transform(before.transform(p));
    }
}
