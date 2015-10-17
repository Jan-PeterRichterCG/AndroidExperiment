package eu.jprichter.squaresandroots.kernel.impl;

import android.util.SparseIntArray;

import com.google.inject.Singleton;

import eu.jprichter.squaresandroots.kernel.IKernel;

/**
 * The App's kernel that basically holds the entire state of the application and offers the
 *  functionality that is independent of the UI
 *  * Created by jrichter on 13.10.2015.
 */

@Singleton
public class Kernel implements IKernel {

    private static final int MAX_ROOT_DEFAULT = 10;
    private int maxRoot = MAX_ROOT_DEFAULT;

    private SparseIntArray successes = new SparseIntArray(MAX_ROOT_DEFAULT);
    private SparseIntArray failures = new SparseIntArray(MAX_ROOT_DEFAULT);


    public int getMaxRoot() {
        return maxRoot;
    }

    public void setMaxRoot(int maxRoot) {
        this.maxRoot = maxRoot;
    }

    public int getRandomRoot() {
        return  (Double.valueOf(Math.random()*maxRoot)).intValue()+1;
    }

    public void resetStatistics() {
        successes = new SparseIntArray(MAX_ROOT_DEFAULT);
        failures = new SparseIntArray(MAX_ROOT_DEFAULT);
    }

    @Override
    public void noteSuccess(int root) {
        successes.put(root,(successes.get(root) + 1));
    }

    @Override
    public  void noteFailure(int root) {
        failures.put(root,(successes.get(root) + 1));
    }

    @Override
    public int getSucessful(int root) {
        return successes.get(root);
    }

    @Override
    public int getFailed(int root) {
        return failures.get(root);
    }

}
