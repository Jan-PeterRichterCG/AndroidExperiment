package eu.jprichter.squaresandroots.kernel;

import com.google.inject.Inject;

/**
 * The component-public interface of the application's kernel
 * Created by jrichter on 16.10.2015.
 */
public interface IKernel {

    /**
     *  The minimum for the maxRoot parameter
     */
    public static int MIN_MIN_ROOT = 1;
    /**
     * The maximum for the maxRoot parameter
     */
    public static int MAX_MIN_ROOT = 10;
    /**
     *  The minimum for the maxRoot parameter
     */
    public static int MIN_MAX_ROOT = 10;
    /**
     * The maximum for the maxRoot parameter
     */
    public static int MAX_MAX_ROOT = 30;

    // public static final String KEY_PREF_MAX_ROOT = "pref_max_root";

    /** Get the maximum number of successful attempts for a root
     * @return the maximum number of successful attempts for a root
     */
    public int getMaxSuccess();

    /**
     * Get the minimum of randomly created roots.
     * @return the minimum of randomly created roots
     */
    public int getMinRoot();
    /**
     * Set the minimum of randomly created roots.
     * @param minRoot the minimum of randomly created roots
     */
    public void setMinRoot(int minRoot);

    /**
     * Get the maximumm of randomly created roots.
     * @return the maximumm of randomly created roots
     */
    public int getMaxRoot();

    /**
     * Set the maximumm of randomly created roots.
     * @param maxRoot the maximumm of randomly created roots
     */
    public void setMaxRoot(int maxRoot);

    /**
     * Get a random root value 1 <= x <= MaxRoot. The odds for a root decrease as the root and
     * its square are guessed / known correctly. If all roots and squares have been guessed /
     * known MAX_SUCCESS times, the probability for that root is 0.
     *
     * Attention: In case there are no more roots left to be guessed, getRandomRoot returns 0.
     *
     * @return the maximumm of randomly created roots
     */
    public int getRandomRoot();

    /**
     * Reset the success/failure statistics.
     */
    public void resetStatistics();

    /**
     * Checks whether a guessed root / square pair is correct. Also updates the statistics
     * on guesses.
     * @return true if root / square pair is correct
     */
    public boolean checkRootSquare(int root, int square);

    /**
     * A simple data container (resultset) for statistics queries
     */
    public class StatisticsEntry {
        public int root = 0;
        public int successes = 0;
        public int failures = 0;

        public StatisticsEntry(int root) {
            this.root = root;
        }
    }
    /**
     * get the statistics for one root
     * @param root
     * @return  the statistics entry from the database - or an entry containing 0 values for
     * success and failure
     */
    public StatisticsEntry getStatistics(int root);

}
