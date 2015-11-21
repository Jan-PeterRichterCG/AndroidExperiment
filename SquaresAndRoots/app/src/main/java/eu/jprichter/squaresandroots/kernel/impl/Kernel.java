package eu.jprichter.squaresandroots.kernel.impl;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.SparseIntArray;

import com.google.inject.Singleton;

import eu.jprichter.squaresandroots.SquaresRootsApp;
import eu.jprichter.squaresandroots.kernel.IKernel;
import eu.jprichter.squaresandroots.R;

import eu.jprichter.squaresandroots.kernel.impl.persistence.DbHelper;
import eu.jprichter.squaresandroots.kernel.impl.persistence.StatisticsContract;
import roboguice.util.Ln;

/**
 * The App's kernel that basically holds the entire state of the application and offers the
 *  functionality that is independent of the UI
 *
 *  Created by jrichter on 13.10.2015.
 */

@Singleton
public class Kernel implements IKernel {

    // the max number of successful guesses until the root will not be asked any more.
    private static final int MAX_SUCCESS = 3;

    private int minRoot;
    private int maxRoot;

    private DbHelper dbHelper = new DbHelper(SquaresRootsApp.getStaticContext());
    private SQLiteDatabase db;

    /**
     * Constructor to be used by RoboGuice only!
     */
    Kernel() {
        Ln.d("XXXXXXXXXXXXXXXXXX Kernel instantiation started");

        // read the persistent value of the maxRoot preference
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SquaresRootsApp.getStaticContext());

        String keyPrefMaxRootString =SquaresRootsApp.getStaticResources().getString(R.string.key_pref_max_root);
        String maxRootPrefString = sharedPref.getString(keyPrefMaxRootString, "");
        setMaxRoot(Integer.valueOf(maxRootPrefString));
        Ln.d("XXXXXXXXXXXXXXXXXX maxRoot set to '" + maxRootPrefString + "'");

        String keyPrefMinRootString =SquaresRootsApp.getStaticResources().getString(R.string.key_pref_min_root);
        String minRootPrefString = sharedPref.getString(keyPrefMinRootString, "");
        setMinRoot(Integer.valueOf(minRootPrefString));
        Ln.d("XXXXXXXXXXXXXXXXXX minRoot set to '" + minRootPrefString + "'");

         /* TODO: check whether this is a performance risk and mitigate */
        db = dbHelper.getWritableDatabase();
    }

    public int getMaxSuccess() { return MAX_SUCCESS; }

    public int getMaxRoot() {
        return maxRoot;
    }

    public void setMaxRoot(int maxRoot) {

        Ln.d("XXXXXXXXXXXXXXXXXX Kernel: set maxRoot to " + maxRoot);

        if(maxRoot < MIN_MAX_ROOT || maxRoot > MAX_MAX_ROOT)
            throw new IllegalArgumentException("maxRoot must be in [" + MIN_MAX_ROOT + " ... " +
            MAX_MAX_ROOT + "].");

        this.maxRoot = maxRoot;
    }

    public int getMinRoot() {
        return minRoot;
    }

    public void setMinRoot(int minRoot) {

        Ln.d("XXXXXXXXXXXXXXXXXX Kernel: set minRoot to " + minRoot);

        if(minRoot < MIN_MIN_ROOT || minRoot > MAX_MIN_ROOT)
            throw new IllegalArgumentException("minRoot must be in [" + MIN_MIN_ROOT + " ... " +
                    MAX_MIN_ROOT + "].");

        this.minRoot = minRoot;
    }

    public int getRandomRoot() {

        int sumSuccesses = 0;
        for (int i=0; i<=maxRoot; i++) {
            sumSuccesses += getStatistics(i).successes;
        }

        if(sumSuccesses == maxRoot * MAX_SUCCESS) {
            Ln.d("XXXXXXXXXXXXXXXXXX sumSuccesses: " + sumSuccesses + " equals maxRoot * MAX_SUCCESS!");
            return 0;
        }

        int triesLeft = maxRoot * MAX_SUCCESS - sumSuccesses;

        int pick = (Double.valueOf(Math.random()*triesLeft)).intValue()+1;

        Ln.d("XXXXXXXXXXXXXXXXXX sumSuccesses: " + sumSuccesses + " triesLeft: " + triesLeft + " pick: " + pick);

        int root=0;
        while(pick > 0) {
            root++;
            pick -= (MAX_SUCCESS - getStatistics(root).successes);
        }
        return  (root);

    }

    public void resetStatistics() {
        Ln.d("XXXXXXXXXXXXXXXXXX resetting statistics!");

         // empty database
        long rowAffected = db.delete(StatisticsContract.StatisticsEntry.TABLE_NAME, "1", null);
        Ln.d("XXXXXXXXXXXXXXXXXX Database cleared - rows affected: " + rowAffected);
    }

    @Override
    public boolean checkRootSquare(int root, int square) {

        StatisticsEntry statistics = getStatistics(root);

        boolean result = false;

        if (root * root == square) {
            statistics.successes++;
            result = true;
        } else {
            statistics.failures++;
            result = false;
        }

        // update database
        Ln.d("XXXXXXXXXXXXXXXXXX update Database");

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StatisticsContract.StatisticsEntry.COLUMN_NAME_SUCCESSES, statistics.successes);
        values.put(StatisticsContract.StatisticsEntry.COLUMN_NAME_FAILURES, statistics.failures);
        long rowId;
        if(statistics.successes + statistics.failures == 1) {
            values.put(StatisticsContract.StatisticsEntry.COLUMN_NAME_ROOT_ID, root);
            // Insert the new row, returning the primary key value of the new row
            rowId = db.insert(
                    StatisticsContract.StatisticsEntry.TABLE_NAME,
                    null,
                    values);
        } else {
            rowId = db.update(
                    StatisticsContract.StatisticsEntry.TABLE_NAME,
                    values,
                    StatisticsContract.StatisticsEntry.COLUMN_NAME_ROOT_ID + " LIKE ? ",
                    new String[] {String.valueOf(root)}
                    );
        }
        Ln.d("XXXXXXXXXXXXXXXXXX Database updated in row " + rowId);

        return result;
    }

    public StatisticsEntry getStatistics(int root) {

        String[] columns = {
                StatisticsContract.StatisticsEntry._ID,
                StatisticsContract.StatisticsEntry.COLUMN_NAME_ROOT_ID,
                StatisticsContract.StatisticsEntry.COLUMN_NAME_SUCCESSES,
                StatisticsContract.StatisticsEntry.COLUMN_NAME_FAILURES
        };

        Ln.d("XXXXXXXXXXXXXXXXXX Read statistics from database");

        String[] selectionArgs = {String.valueOf(root)};
        Cursor cursor = db.query(StatisticsContract.StatisticsEntry.TABLE_NAME,
                columns,
                StatisticsContract.StatisticsEntry.COLUMN_NAME_ROOT_ID + " LIKE ?",
                selectionArgs, null, null, null, null);

        StatisticsEntry statisticsEntry = new StatisticsEntry(root);
        if (cursor.moveToFirst()) {
            do {
                long itemId = cursor.getLong(
                        cursor.getColumnIndex(StatisticsContract.StatisticsEntry._ID));
                statisticsEntry.root = cursor.getInt(
                        cursor.getColumnIndex(StatisticsContract.StatisticsEntry.COLUMN_NAME_ROOT_ID));
                statisticsEntry.successes = cursor.getInt(
                        cursor.getColumnIndex(StatisticsContract.StatisticsEntry.COLUMN_NAME_SUCCESSES));
                statisticsEntry.failures = cursor.getInt(
                        cursor.getColumnIndex(StatisticsContract.StatisticsEntry.COLUMN_NAME_FAILURES));
                Ln.d("XXXXXXXXXXXXXXXXXX read from DB: id: " + itemId + " root: " + statisticsEntry.root +
                        " succ: " + statisticsEntry.successes + " fail: " + statisticsEntry.failures);
            } while (cursor.moveToNext());
        } else {
            Ln.d("XXXXXXXXXXXXXXXXXX Database does not contain a row for root " + root);
        }

        return statisticsEntry;

    }

}
