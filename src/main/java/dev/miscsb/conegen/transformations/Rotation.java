package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;
import dev.miscsb.conegen.util.Quaternion;
import dev.miscsb.conegen.util.QuaternionUtil;

public record Rotation(Quaternion rotation) implements Transformation {
    @Override
    public Point3D transform(Point3D p) {
        return QuaternionUtil.applyRotationQuaternion(p, rotation);
    }
}
