package eu.jprichter.squaresandroots.kernel.impl;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.SparseIntArray;

import com.google.inject.Singleton;

import eu.jprichter.squaresandroots.SquaresRootsApp;
import eu.jprichter.squaresandroots.kernel.IKernel;
import eu.jprichter.squaresandroots.R;

import roboguice.util.Ln;

/**
 * The App's kernel that basically holds the entire state of the application and offers the
 *  functionality that is independent of the UI
 *
 *  Created by jrichter on 13.10.2015.
 */

@Singleton
public class Kernel implements IKernel {

    // the max number of successful guesses until the root will not be asked any more.
    private static final int MAX_SUCCESS = 3;

    private int maxRoot;

    private SparseIntArray successes;
    private SparseIntArray failures;

    /**
     * Constructor to be used by RoboGuice only!
     */
    Kernel() {
        Ln.d("XXXXXXXXXXXXXXXXXX Kernel instantiation started");
        // read the persistent value of the maxRoot preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SquaresRootsApp.getStaticContext());
        String keyPrefMaxRootString =SquaresRootsApp.getStaticResources().getString(R.string.key_pref_max_root);
        String maxRootPrefString = sharedPref.getString(keyPrefMaxRootString, "");
        setMaxRoot(Integer.valueOf(maxRootPrefString));
        Ln.d("XXXXXXXXXXXXXXXXXX maxRoot set to '" + maxRootPrefString + "'");

        resetStatistics();
    }

    public int getMaxSuccess() { return MAX_SUCCESS; }

    public int getMaxRoot() {
        return maxRoot;
    }

    public void setMaxRoot(int maxRoot) {

        Ln.d("XXXXXXXXXXXXXXXXXX Kernel: set maxRoot to " + maxRoot);

        if(maxRoot < MIN_MAX_ROOT || maxRoot > MAX_MAX_ROOT)
            throw new IllegalArgumentException("maxRoot must be in [" + MIN_MAX_ROOT + " ... " +
            MAX_MAX_ROOT + "].");

        this.maxRoot = maxRoot;
    }

    public int getRandomRoot() {

        int sumSuccesses = 0;
        for (int i=0; i<=maxRoot; i++) {
            sumSuccesses += successes.get(i);
        }

        if(sumSuccesses == maxRoot * MAX_SUCCESS) {
            Ln.d("XXXXXXXXXXXXXXXXXX sumSuccesses: " + sumSuccesses + " equals maxRoot * MAX_SUCCESS!");
            return 0;
        }

        int triesLeft = maxRoot * MAX_SUCCESS - sumSuccesses;

        int pick = (Double.valueOf(Math.random()*triesLeft)).intValue()+1;

        Ln.d("XXXXXXXXXXXXXXXXXX sumSuccesses: " + sumSuccesses + " triesLeft: " + triesLeft + " pick: " + pick);

        int root=0;
        while(pick > 0) {
            root++;
            pick -= (MAX_SUCCESS - successes.get(root));
            Ln.d("XXXXXXXXXXXXXXXXXX root: " + root + " reduced pick: " + pick);
        }

        return  (root);

    }

    public void resetStatistics() {
        Ln.d("XXXXXXXXXXXXXXXXXX resetting statistics!");

        successes = new SparseIntArray(maxRoot);
        failures = new SparseIntArray(maxRoot);
    }

    @Override
    public boolean checkRootSquare(int root, int square) {

        if (root * root == square) {
            successes.put(root,(successes.get(root) + 1));
            return true;
        } else {
            failures.put(root,(failures.get(root) + 1));
            return false;
        }
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
