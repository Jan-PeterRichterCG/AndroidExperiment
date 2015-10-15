package eu.jprichter.squaresandroots.kernel;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * The Dagger 2 Module class for the kernel
 * Created by jrichter on 13.10.2015.
 */
@Module
public class KernelModule {

    @Provides @Singleton Kernel provideKernel () {
        return new Kernel();
    }
}
