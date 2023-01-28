package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;

@FunctionalInterface
public interface Transformation {

    Point3D transform(Point3D p);

    public static final Transformation NONE = new Transformation() {
        @Override
        public Point3D transform(Point3D p) {
            return p;
        }
    };

    default Transformation then(Transformation then) {
        return new CompositeTransformation(this, then);
    }

    default Transformation about(Point3D point) {
        return new Translation(-point.x, -point.y, -point.z)
            .then(this)
            .then(new Translation(point.x, point.y, point.z));
    }
}
