package eu.jprichter.squaresandroots.kernel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * The Dagger 2 Kernel Component Interface
 * Created by jrichter on 13.10.2015.
 */

@Singleton
@Component (modules = {KernelModule.class} )
public interface KernelComponent {

    Kernel provideKernel();

}
