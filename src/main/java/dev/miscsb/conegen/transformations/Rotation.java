package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.Point3D;
import dev.miscsb.conegen.util.Quaternion;
import dev.miscsb.conegen.util.QuaternionUtil;

public class Rotation extends Transformation {
    private Quaternion rQuaternion;

    public Rotation(Quaternion q) {
        this.rQuaternion = q;
    }

    @Override
    public Point3D transform(Point3D p) {
        return QuaternionUtil.applyRotationQuaternion(p, rQuaternion);
    }

    @Override
    public Type getType() {
        return Type.Rotation;
    }
}
