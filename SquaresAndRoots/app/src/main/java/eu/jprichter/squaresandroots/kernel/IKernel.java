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

}
