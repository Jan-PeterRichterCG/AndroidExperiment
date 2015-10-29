package eu.jprichter.squaresandroots.kernel.impl.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A DB helper for the entire database of the Squares&Roots app.
 *
 * Created by jrichter on 29.10.2015.
 */
public class DbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SquaresRoots.db";

    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + StatisticsContract.StatisticsEntry.TABLE_NAME + " (" +
                    StatisticsContract.StatisticsEntry._ID + " INTEGER PRIMARY KEY," +
                    StatisticsContract.StatisticsEntry.COLUMN_NAME_ROOT_ID + INT_TYPE + COMMA_SEP +
                    StatisticsContract.StatisticsEntry.COLUMN_NAME_SUCCESSES + INT_TYPE + COMMA_SEP +
                    StatisticsContract.StatisticsEntry.COLUMN_NAME_FAILURES + INT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + StatisticsContract.StatisticsEntry.TABLE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for the App's state-of-game data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}