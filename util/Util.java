package util;

import jv.vecmath.PdVector;
import jv.vecmath.PdMatrix;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
* Class containing static methods that can be helpful for the assignments. 
*/
public class Util {
	
	/** Singular Value Decomposition.
   <P>
   For an m-by-n matrix M with m >= n, the singular value decomposition is
   an m-by-n orthogonal matrix U, an n-by-n diagonal matrix D, and
   an n-by-n orthogonal matrix V so that M = U*D*V'. V' is the transpose of V.
   <P>
   The singular values, sigma[k] = S[k][k], are ordered so that
   sigma[0] >= sigma[1] >= ... >= sigma[n-1].
   <P>
   The method write the factors U,D and V into the provided PdMatrices, which should not be null. 
   <P>
   The singular value decompostion always exists, so the constructor will
   never fail.  The matrix condition number and the effective numerical
   rank can be computed from this decomposition.
   */
	public static void computeSVD(PdMatrix M, PdMatrix U, PdMatrix D, PdMatrix V){
		Matrix MJama = new Matrix(M.getEntries());
		SingularValueDecomposition svd = new SingularValueDecomposition(MJama);
		U.set(svd.getU().getArray());
		V.set(svd.getV().getArray());
		D.set(svd.getS().getArray());
	}
	
}