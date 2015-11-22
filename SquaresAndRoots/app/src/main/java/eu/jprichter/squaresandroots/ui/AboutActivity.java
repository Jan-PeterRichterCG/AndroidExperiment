package eu.jprichter.squaresandroots.ui;

import android.os.Bundle;
import android.view.View;

import eu.jprichter.squaresandroots.R;
import roboguice.activity.GuiceAppCompatActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_about)
public class AboutActivity extends GuiceAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void navigateBack(View v) {
        super.finish();
    }

}
