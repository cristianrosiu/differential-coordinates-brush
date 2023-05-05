package test;

import dev6.numeric.PnMumpsSolver;
import jv.object.PsDebug;
import jv.vecmath.PdVector;
import jvx.numeric.PnSparseMatrix;


/**
* Tests whether MUMPS solver is working
*/
public class TestMumps {
	public static void main(String args[]) {
		System.out.println("Testing MUMPS JNI...");
		if (!PnMumpsSolver.isAvailable()) {
			System.out.println("Mumps not available. Check VM arguments and dll paths.");
			return;
		}
		int n=1000, k=500;
		PnSparseMatrix M = new PnSparseMatrix(n, n);
		for (int i=0; i<n; i++) {
			//Big entry on diagonal
			M.setEntry(i, i, 2 + Math.random());
			//k small entries per row at random position, while keeping symmetry
			for (int count=0; count<k; count++) {
				int j = (int) (Math.random() * n);
				if (j != i) {
					double val = 0.1;
					M.setEntry(i, j, val);
					M.setEntry(j, i, val);
				}
			}
		}
		M.validate();
		long startTime = System.currentTimeMillis();
		long factorization = -1;
		try {
			factorization = PnMumpsSolver.factor(M, PnMumpsSolver.Type.SYMMETRIC_POSITIVE_DEFINITE);
		} catch (Exception e) {
			System.out.println("Could not factorize matrix.");
			return;
		}
		System.out.println("Time for factorization: "+(System.currentTimeMillis()-startTime)+" ms");
		PdVector allOne = new PdVector(n);
		allOne.setConstant(.01);
		PdVector solution = new PdVector(n);
		startTime = System.currentTimeMillis();
		try {
			PnMumpsSolver.solve(factorization, solution, allOne);
			//PnMumpsSolver.solve(M, solution, allOne, PnMumpsSolver.Type.SYMMETRIC_POSITIVE_DEFINITE);
		} catch (Exception e) {
			System.out.println("Could not solve linear system.");
			return;
		}
		System.out.println("Time for one solve: "+(System.currentTimeMillis()-startTime)+" ms");
		PdVector tmp = new PdVector(n); 		
		M.rightMultMatrix(tmp, solution);
		double rh = allOne.length();
		System.out.println("Length right-hand side: "+rh);
		tmp.sub(allOne);
		double res = tmp.length(); 
		System.out.println("Residual: "+res);	
	}
}