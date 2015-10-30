package eu.jprichter.squaresandroots.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.inject.Inject;

import eu.jprichter.squaresandroots.R;
import eu.jprichter.squaresandroots.kernel.IKernel;
import eu.jprichter.squaresandroots.ui.settings.SettingsActivity;
import roboguice.activity.GuiceAppCompatActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

@ContentView(R.layout.activity_question)
public class QuestionActivity extends GuiceAppCompatActivity {

    @Inject IKernel kernel;

    @InjectView(R.id.question) TextView questionText;
    @InjectView(R.id.solution) EditText editText;
    @InjectView(R.id.check_button) Button checkButton;
    @InjectView(R.id.statisticsView) StatisticsDrawableView statisticsView;
    @Inject CongratulationsDialogFragment congratulations;


    public final static String EXTRA_ROOT_QUESTION = "eu.jprichter.squaresandroots.ui.QuestionActivity.EXTRA_ROOT_QUESTION";
    public final static String EXTRA_SOLUTION = "eu.jprichter.squaresandroots.ui.QuestionActivity.EXTRA_SOLUTION";
    public final static String EXTRA_CORRECT = "eu.jprichter.squaresandroots.ui.QuestionActivity.EXTRA_CORRECT";
    public final static String STATE_ROOT_QUESTION = "eu.jprichter.squaresandroots.ui.QuestionActivity.STATE_ROOT_QUESTION";

    public final static String CONGRATULATIONS_POPUP_FRAGMENT = "eu.jprichter.squaresandroots.ui.QuestionActivity.CONGRATULATIONS_POPUP";

    private int rootQuestion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Ln.d("XXXXXXXXXXXXXXXXXX Question Activity created.");

        if(savedInstanceState != null)
            rootQuestion = savedInstanceState.getInt(STATE_ROOT_QUESTION, 0);

        editText.addTextChangedListener(new ButtonEnablerTextWatcher(checkButton));

        /* trigger change to the check activity if OK button on keyboard is pressed. */
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
                        (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Ln.d("XXXXXXXXXXXXXXXXXXXXX Enter pressed");
                    if(v.getText().length() > 0) // only if non-empty solution given
                        checkSolution(v);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Ln.d("XXXXXXXXXXXXXXXXXX Resume QuestionActivity - maxRoot: " + kernel.getMaxRoot() +
                " rootQuestion:" + rootQuestion);

        if(kernel.getMaxRoot() < rootQuestion)
            rootQuestion = 0;

        if(rootQuestion == 0) {
            /* this resume is not just a re-activation; something fundamental has happened. */
            Ln.d("XXXXXXXXXXXXXXXXXX Generate new question.");
            rootQuestion =  kernel.getRandomRoot();
            if (rootQuestion == 0) {
                // some kind of "Congratulations - you've done it!" screen is shown
                congratulations.show(getSupportFragmentManager(), CONGRATULATIONS_POPUP_FRAGMENT);
                kernel.resetStatistics();
                rootQuestion =  kernel.getRandomRoot();
            }

            editText.setText("");

            statisticsView.invalidateDiagramBlocks();
        }

        questionText.setText(rootQuestion + " * " + rootQuestion + " = ?");
    }

    private class ButtonEnablerTextWatcher implements TextWatcher {
        Button button = null;

        ButtonEnablerTextWatcher(Button buttonToEnable) {
            this.button = buttonToEnable;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // don't do anything
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length() > 0)
                button.setEnabled(true);
            else
                button.setEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // don't do anything
        }
    }

    public void checkSolution(View view) {
        int solution = Integer.parseInt(editText.getText().toString());
        // kernel.checkRootSquare also updates the statistics
        boolean correct = kernel.checkRootSquare(rootQuestion, solution);

        Intent intent = new Intent(this, AnswerActivity.class);
        intent.putExtra(EXTRA_ROOT_QUESTION, rootQuestion);
        intent.putExtra(EXTRA_SOLUTION, solution);
        intent.putExtra(EXTRA_CORRECT, correct);

        startActivity(intent);

        rootQuestion = 0; // signals that a new question has to be generated
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question , menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(STATE_ROOT_QUESTION, rootQuestion);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
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
}
