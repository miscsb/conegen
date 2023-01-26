package dev.miscsb.conegen;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import dev.miscsb.conegen.util.MatrixUtil;
import dev.miscsb.conegen.util.Quaternion;
import dev.miscsb.conegen.util.QuaternionUtil;

public class GeoTest {

    static Random random = new Random();

    @Test
    public void getRotationQuaternionRotatesVectorsConsistently() {
        for (int i = 0; i < 100; i++) {
            double[] original = new double[] {
                random.nextDouble(0.1, 2.0), 
                random.nextDouble(0.1, 2.0), 
                random.nextDouble(0.1, 2.0) 
            };
            double[] rotated = new double[] {
                random.nextDouble(0.1, 2.0), 
                random.nextDouble(0.1, 2.0), 
                random.nextDouble(0.1, 2.0) 
            };
            Quaternion rotation = QuaternionUtil.getRotationQuaternion(original, rotated);
            assertTrue(MatrixUtil.colinear(QuaternionUtil.applyRotationQuaternion(original, rotation), rotated));
        }
    }

}
