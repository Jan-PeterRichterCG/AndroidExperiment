package eu.jprichter.squaresandroots.kernel;

import com.google.inject.Inject;

/**
 * The component-public interface of the application's kernel
 * Created by jrichter on 16.10.2015.
 */
public interface IKernel {

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
     * Note a successful attempt to guess a square from a root in the statistics.
     * @param root the root for which the square was successfully guessed/known
     */
    public void noteSuccess(int root);

    /**
     * Note a failed attempt to guess a square from a root in the statistics.
     * @param root the root for which the square was unsuccessfully guessed
     */
    public void noteFailure(int root);

    /**
     * Get the statistics for successful attempts to guess/know the square of a given root
     * @param root the given root
     */
    public int getSucessful(int root);

    /**
     * Get the statistics for failed attempts to guess/know the square of a given root
     * @param root the given root
     */
    public int getFailed(int root);

}
