package eu.jprichter.squaresandroots.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import eu.jprichter.squaresandroots.R;
import roboguice.activity.GuiceAppCompatActivity;
import roboguice.inject.InjectView;

public class CheckActivity extends GuiceAppCompatActivity {

    @InjectView(R.id.answer) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Intent intent = getIntent();
        int root = intent.getIntExtra(QuestionActivity.EXTRA_ROOT_QUESTION, -1);
        int solution = intent.getIntExtra(QuestionActivity.EXTRA_SOLUTION, -1);

        String answer;
        if(root * root - solution == 0) {
            answer="Correct!\n" + root + " * " + root + " = " + solution;
        } else {
            answer = "Wrong!\n" + root + " * " + root + " = " + root * root;
        }

        textView.setText(answer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
