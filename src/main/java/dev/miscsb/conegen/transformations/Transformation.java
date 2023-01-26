package dev.miscsb.conegen.transformations;

import dev.miscsb.conegen.util.MatrixUtil;
import dev.miscsb.conegen.util.Point3D;

public abstract class Transformation {
    public static final Transformation NONE = new Transformation() {
        @Override
        public Point3D transform(Point3D p) {
            return p;
        }

        @Override
        public Type getType() {
            return Type.None;
        }
    };

    public abstract Point3D transform(Point3D p);
    public abstract Type getType();
    public Transformation then(Transformation then) {
        Transformation first = this;
        Type firstType = this.getType();
        Type thenType  = then.getType();
        Type transType = (firstType == Type.None || firstType == thenType) ? thenType : Type.Combined;
        return new Transformation() {
            @Override
            public Point3D transform(Point3D p) {
                return then.transform(first.transform(p));
            }

            @Override
            public Type getType() {
                return transType;
            }
        };
    }

    public enum Type {
        Rotation,
        Reflection,
        Translation,
        Dilation,
        Combined,
        None
    }

    public Transformation about(Point3D point) {
        double[] forwardVector = point.toArray();
        double[] reverseVector = MatrixUtil.vectorScale(forwardVector, -1);
        return new Translation(reverseVector)
                    .then(this)
                    .then(new Translation(forwardVector));
    }
}
