package eu.jprichter.squaresandroots.kernel.impl;

import com.google.inject.Singleton;

import eu.jprichter.squaresandroots.kernel.IKernel;

/**
 * The App's kernel that basically holds the entire state of the application and offers the
 *  functionality that is independent of the UI
 *  * Created by jrichter on 13.10.2015.
 */

@Singleton
public class Kernel implements IKernel {

    private static final int MAX_ROOT_DEFAULT = 25;
    private int maxRoot = MAX_ROOT_DEFAULT;

    public int getMaxRoot() {
        return maxRoot;
    }

    public void setMaxRoot(int maxRoot) {
        this.maxRoot = maxRoot;
    }
}
