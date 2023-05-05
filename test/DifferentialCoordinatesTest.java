package test;

import jv.geom.PgElementSet;
import jv.loader.PjImportModel;
import jv.vecmath.PdMatrix;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.numeric.PnSparseMatrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import workshop.DifferentialCoordinates;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class DifferentialCoordinatesTest {

    DifferentialCoordinates dc;
    PiVector[] f;
    PdVector[] v;
    @BeforeEach
    void setUp() {
        f = new PiVector[]{new PiVector(0, 1, 2), new PiVector(0, 2, 3)};
        v = new PdVector[]{
                new PdVector(0, 1 ,0),
                new PdVector(1, 1, 0),
                new PdVector(0, 0 ,0),
                new PdVector(-1, 0, 0)
        };

        dc = new DifferentialCoordinates(f, v, 2, 4);
    }

    @Test
    void getTriangleNormal() {
        PdVector N = dc.getTriangleNormal(new int[]{0, 1, 2});
        assertEquals(N.getEntry(0), 0);
        assertEquals(N.getEntry(1), 0);
        assertEquals(N.getEntry(2), -1);
    }
    @Test
    void getGradientMatrix() {
        PdVector N = new PdVector(0, 0, -1);
        PdVector[] p = new PdVector[] { v[0], v[1], v[2] };

        PdMatrix g = dc.getGradientMatrix(N, f[0].getEntries(), PdVector.area(p));
        // Assert column 0
        assertEquals(g.getColumn(0).getEntry(  0), -1);
        assertEquals(g.getColumn(0).getEntry(1), 1 );
        assertEquals(g.getColumn(0).getEntry(2), 0);

        // Assert column 1
        assertEquals(g.getColumn(1).getEntry(  0), 1);
        assertEquals(g.getColumn(1).getEntry(1), -0.0);
        assertEquals(g.getColumn(1).getEntry(2), 0);

        // Assert column 2
        assertEquals(g.getColumn(2).getEntry(  0), 0);
        assertEquals(g.getColumn(2).getEntry(1), -1);
        assertEquals(g.getColumn(2).getEntry(2), 0);
    }

    @Test
    void getMatrixG() {
        PnSparseMatrix actual = dc.getMatrixG();
        PnSparseMatrix expected = new PnSparseMatrix(3*2, 4, 3);
        expected.setEntry(0,0, -1);
        expected.setEntry(0,1, 1);
        expected.setEntry(0,2, 0);
        expected.setEntry(0,3, 0);

        expected.setEntry(1,0, 1);
        expected.setEntry(1,1, -0.0);
        expected.setEntry(1,2, -1);
        expected.setEntry(1,3, 0);

        expected.setEntry(2,0, 0);
        expected.setEntry(2,1, 0);
        expected.setEntry(2,2, 0);
        expected.setEntry(2,3, 0);

        expected.setEntry(3,0, 0);
        expected.setEntry(3,1, 0);
        expected.setEntry(3,2, 1);
        expected.setEntry(3,3, -1);

        expected.setEntry(4,0, 1);
        expected.setEntry(4,1, 0);
        expected.setEntry(4,2, -1);
        expected.setEntry(4,3, -0.0);

        expected.setEntry(5,0, 0);
        expected.setEntry(5,1, 0);
        expected.setEntry(5,2, 0);
        expected.setEntry(5,3, 0);

        assertEquals(actual.toShortString(), expected.toShortString());
    }

    @Test
    void getMatrixM() {
        PnSparseMatrix actual = dc.getMatrixM();
        PnSparseMatrix expected = new PnSparseMatrix(2*3, 2*3);

        PdVector[] t1 = new PdVector[] { v[0], v[1], v[2] };
        PdVector[] t2 = new PdVector[] { v[0], v[2], v[3] };
        double area1 = PdVector.area(t1);
        double area2 = PdVector.area(t2);

        for(int i = 0; i < 3; i++) {
            expected.setEntry(i, i, area1);
            expected.setEntry(i+3, i+3, area2);
        }

        assertEquals(actual.toShortString(), expected.toShortString());
    }
}