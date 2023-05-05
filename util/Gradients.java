package util;

import jv.vecmath.PdMatrix;
import jvx.numeric.PnSparseMatrix;

import java.util.ArrayList;
import java.util.List;

public class Gradients {
    PnSparseMatrix S, G, M, GTM;

    public Gradients() {
        S = new PnSparseMatrix();
        G = new PnSparseMatrix();
        M = new PnSparseMatrix();
        GTM = new PnSparseMatrix();

    }

    public PnSparseMatrix getG() {
        return G;
    }
    public PnSparseMatrix getS() {
        return S;
    }

    public PnSparseMatrix getM() {
        return M;
    }

    public PnSparseMatrix getGTM() {
        return GTM;
    }

    public void setG(PnSparseMatrix g) {
        G = g;
    }

    public void setS(PnSparseMatrix S) {
        this.S = S;
    }

    public void setM(PnSparseMatrix m) {
        M = m;
    }

    public void setGTM(PnSparseMatrix GTM) {
        this.GTM = GTM;
    }
}
