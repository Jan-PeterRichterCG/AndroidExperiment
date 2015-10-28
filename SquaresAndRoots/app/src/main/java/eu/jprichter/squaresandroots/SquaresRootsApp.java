package eu.jprichter.squaresandroots;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import roboguice.util.Ln;

/**
 * The main Application class of the Squares&Roots App
 * Created by jrichter on 18.10.2015.
 */
public class SquaresRootsApp extends Application {

    private static Context context;
    private static Resources resources;

    @Override
    public void onCreate() {
        super.onCreate();

        Ln.d("XXXXXXXXXXXXXXXXXX SquaresRootsApp creation started");
        context = getApplicationContext();
        resources = getResources();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, true);
    }

    public static Context getStaticContext() {
        return context;
    }

    public static Resources getStaticResources() {
        return resources;
    }
}
