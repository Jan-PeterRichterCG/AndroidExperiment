package eu.jprichter.squaresandroots.kernel.impl;

import com.google.inject.AbstractModule;

import eu.jprichter.squaresandroots.kernel.IKernel;

/**
 * The Module to provide the RoboGuice Bindings for the injectable POJOs of the kernel module.
 *
 * Created by jrichter on 16.10.2015.
 */
public class KernelModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IKernel.class).to(Kernel.class);
    }

}
