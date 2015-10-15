package eu.jprichter.squaresandroots.kernel;

/**
 * The App's kernel that basically holds the entire state of the application and offers the
 *  functionality that is independent of the UI
 *  * Created by jrichter on 13.10.2015.
 */
public class Kernel {

    private int maxSquare;
    private static final int MAX_SQUARE_DEFAULT = 25;

    public Kernel () {
        maxSquare = MAX_SQUARE_DEFAULT;
    }

    public int getMaxSquare() {
        return maxSquare;
    }
}
