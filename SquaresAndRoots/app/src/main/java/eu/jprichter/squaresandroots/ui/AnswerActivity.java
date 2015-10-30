package eu.jprichter.squaresandroots.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;

import eu.jprichter.squaresandroots.R;
import eu.jprichter.squaresandroots.kernel.IKernel;
import eu.jprichter.squaresandroots.ui.settings.SettingsActivity;
import roboguice.activity.GuiceAppCompatActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_check)
public class AnswerActivity extends GuiceAppCompatActivity {

    @InjectView(R.id.answer) TextView textView;
    @Inject IKernel kernel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int root = intent.getIntExtra(QuestionActivity.EXTRA_ROOT_QUESTION, -1);
        int solution = intent.getIntExtra(QuestionActivity.EXTRA_SOLUTION, -1);
        boolean correct = intent.getBooleanExtra(QuestionActivity.EXTRA_CORRECT, false);

        String answer;
        if(correct) {
            answer="Correct!\n" + root + " * " + root + " = " + solution;
        } else {
            answer = "The answer " + solution + " is wrong\n" +
                    root + " * " + root + " = " + root * root;
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

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigateToQuestionActivity(View v) {
        Intent intent = new Intent(this, QuestionActivity.class);
        startActivity(intent);
    }
}
