package workshop;

import dev6.numeric.PnMumpsSolver;
import jv.geom.PgElementSet;
import jv.object.PsObject;
import jv.project.PgGeometry;
import jv.vecmath.PcMatrix;
import jv.vecmath.PdMatrix;
import jv.vecmath.PdVector;
import jv.vecmath.PiVector;
import jvx.numeric.PnBiconjugateGradient;
import jvx.numeric.PnSparseMatrix;
import jvx.project.PjWorkshop;
import util.Gradients;
import jv.object.PsDebug;

import java.util.*;


public class DifferentialCoordinates extends PjWorkshop {
    PgElementSet m_geom;
    PgElementSet m_geomSave;

    PiVector[] f;
    PdVector[] v;
    int F, V;

    public DifferentialCoordinates(PiVector[] f, PdVector[] v, int F, int V) {
        super("Differential Coordinates");
        if (getClass() == DifferentialCoordinates.class)
            init();

        this.f = f;
        this.v = v;
        this.F = F;
        this.V = V;
    }

    @Override
    public void setGeometry(PgGeometry geom) {
        super.setGeometry(geom);
        m_geom 		= (PgElementSet)super.m_geom;
        m_geomSave 	= (PgElementSet)super.m_geomSave;

    }

    public void reset()
    {
        m_geom = (PgElementSet)m_geomSave.clone();
    }

    /** Initialization */
    public void init() {
        super.init();
    }

    public void applyBrush(PdMatrix A, boolean test) throws Exception {

        PdVector originalC = getCentroid();

        // Compute S
        Gradients gradients = getGradients();

        PnSparseMatrix S = gradients.getS();
        PnSparseMatrix G = gradients.getG();
        PnSparseMatrix M = gradients.getM();

        // Compute Factorization
        PnBiconjugateGradient solver = new PnBiconjugateGradient();

        // Compute embeddings
        PdVector[] embeddings = getEmbeddings();

        // Compute gradients
        PdVector[] Gv = new PdVector[] {
                new PdVector(new double[3*F]),
                new PdVector(new double[3*F]),
                new PdVector(new double[3*F]),
        };
        for (int i = 0; i < 3; i++)
            G.leftMultMatrix(Gv[i], embeddings[i]);

        // Update gradients of selected triangles
        for (int i = 0; i < F; i++)
        {
            if (f[i].hasTag(PsObject.IS_SELECTED))
            {
                for (int j = 0; j < 3; j++)
                {
                    PdVector g = new PdVector(
                            Gv[j].getEntry(i*3),
                            Gv[j].getEntry(i*3+1),
                            Gv[j].getEntry(i*3+2)
                    );

                    PdVector newGradient = new PdVector(3);
                    A.leftMultMatrix(newGradient, g);

                    Gv[j].setEntry(i*3, newGradient.getEntry(0));
                    Gv[j].setEntry(i*3 + 1, newGradient.getEntry(1));
                    Gv[j].setEntry(i*3 + 2, newGradient.getEntry(2));
                }
            }
        }

        // Solve the systems
        PdVector[] solutions = new PdVector[] {
                new PdVector(new double[V]),
                new PdVector(new double[V]),
                new PdVector(new double[V]),
        };
        for (int i = 0; i < 3; i++)
        {
            PdVector b = new PdVector(V);
            gradients.getGTM().leftMultMatrix(b, Gv[i]);
            solver.solve(S, solutions[i], b);
        }

        // Update the mesh
        for(int i = 0 ; i < V; i++)
        {
            PdVector solution = new PdVector(
              solutions[0].getEntry(i),
                    solutions[1].getEntry(i),
                    solutions[2].getEntry(i)
            );
            if(!test)
                m_geom.setVertex(i, solution);
        }
        PdVector newC = getCentroid();
        m_geom.translate(PdVector.subNew(originalC, newC));
    }

    public PdVector[] getEmbeddings() {
        PdVector[] embeddings = new PdVector[] {
                new PdVector(),
                new PdVector(),
                new PdVector(),
        };

        for (PdVector vertex : v)
            for (int j = 0; j < 3; j++)
                embeddings[j].addEntry(vertex.getEntry(j));

        return embeddings;
    }
    public Gradients getGradients()
    {
        Gradients gradients = new Gradients();

        PnSparseMatrix G = getMatrixG();
        PnSparseMatrix M = getMatrixM();
        PnSparseMatrix GTM =  PnSparseMatrix.multMatrices(G.transposeNew(), M, null);

        gradients.setG(G);
        gradients.setS(PnSparseMatrix.multMatrices(GTM, G, null));
        gradients.setM(M);
        gradients.setGTM(GTM);

        return gradients;
    }

    public PnSparseMatrix getMatrixG()
    {
        PnSparseMatrix G = new PnSparseMatrix(3*F, V, 3);

        // Iterate over triangles
        int i = 0;
        for (int t = 0 ; t < F; t++) {
            // Get triangle vertices
            int[] p = f[t].getEntries();
            // Compute triangle normal
            PdVector N = getTriangleNormal(p);
            PdMatrix g = getGradientMatrix(N, p, getAreaOfElement(new PdVector[]{v[p[0]], v[p[1]], v[p[2]]}));
            // Add values rows of the matrix to final matrix S
            for(int j = 0; j < 3; j++)
                for (int k = 0; k < 3; k++)
                    G.setEntry(i+k, p[j], g.getEntry(k, j));
            i+=3;
        }

        return G;
    }

    public PnSparseMatrix getMatrixM()
    {
        PnSparseMatrix M = new PnSparseMatrix(F*3, F*3);
        int i = 0;
        for (int j = 0; j < F; j++) {
            int[] p = f[j].getEntries();
            // Add area of triangle to the diagonal
            double area = getAreaOfElement(new PdVector[]{v[p[0]], v[p[1]], v[p[2]]});
            M.addEntry(i, i, area);
            M.addEntry(i+1, i+1, area);
            M.addEntry(i+2, i+2, area);
            i+=3;
        }
        return M;
    }

    public PdMatrix getGradientMatrix(PdVector N, int[] p, double triangleArea)
    {
        PdMatrix G = new PdMatrix(3, 3);

        // Compute edges opposite to each vertex of a triangle
        PdVector[] e = new PdVector[3];
        e[0] = PdVector.subNew(v[p[2]], v[p[1]]);
        e[1] = PdVector.subNew(v[p[0]], v[p[2]]);
        e[2] = PdVector.subNew(v[p[1]], v[p[0]]);

        // Compute Gradient matrix G
        for (int i = 0; i < 3; i++)
            G.setColumn(i, PdVector.crossNew(N, e[i]));
        G.multScalar(1.0/(2*triangleArea));

        return G;
    }

    public double getAreaOfElement(PdVector[] p) {
        return  PdVector.area(p);
    }

    public PdVector getTriangleNormal(int[] p)
    {
        PdVector[] e = new PdVector[2];
        e[0] = PdVector.subNew(v[p[1]], v[p[0]]);
        e[1] =PdVector.subNew(v[p[2]], v[p[0]]);

        PdVector N = PdVector.crossNew(e[0], e[1]);
        N.normalize();
        return N;
    }

    public PdVector getCentroid()
    {
        double areaSum= 0.0;
        PdVector centroid= new PdVector(0.0,0.0,0.0);
        for (int i = 0; i < F; i++)
        {
            PdVector p0 = v[f[i].getEntry(0)];
            PdVector p1 = v[f[i].getEntry(1)];
            PdVector p2 = v[f[i].getEntry(2)];
            PdVector  center = PdVector.addNew(PdVector.addNew(p0, p1), p2);
            center.multScalar(1.0/3.0);
            double area = getAreaOfElement(new PdVector[]{p0, p1, p2});
            center.multScalar(area);

            centroid = PdVector.addNew(centroid, center);
            areaSum += area;
        }
        centroid.multScalar(1.0/areaSum);

        return centroid;
    }

}
