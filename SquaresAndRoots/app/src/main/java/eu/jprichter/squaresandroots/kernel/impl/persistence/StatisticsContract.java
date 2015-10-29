package eu.jprichter.squaresandroots.kernel.impl.persistence;

import android.provider.BaseColumns;

/**
 * The SQLite contract for the success/failure statistics
 *
 * Created by jrichter on 29.10.2015.
 */
public class StatisticsContract {

    /* prevent the contract from being instantiated */
    private StatisticsContract() {}

    /* Inner class that defines the table contents */
    public static abstract class StatisticsEntry implements BaseColumns {
        public static final String TABLE_NAME = "statistics";
        public static final String COLUMN_NAME_ROOT_ID = "root_id";
        public static final String COLUMN_NAME_SUCCESSES = "successes";
        public static final String COLUMN_NAME_FAILURES = "failures";
    }

}
