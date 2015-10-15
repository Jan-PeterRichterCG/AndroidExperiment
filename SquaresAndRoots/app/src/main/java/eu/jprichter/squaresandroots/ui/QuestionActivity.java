package eu.jprichter.squaresandroots.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import roboguice.activity.GuiceAppCompatActivity;
import eu.jprichter.squaresandroots.R;
import eu.jprichter.squaresandroots.kernel.Kernel;
import roboguice.inject.InjectView;

public class QuestionActivity extends GuiceAppCompatActivity {

    @InjectView(R.id.question) TextView questionText;
    @InjectView(R.id.solution) EditText editText;
    @InjectView(R.id.check_button) Button checkButton;


    public final static String EXTRA_ROOT_QUESTION = "eu.jprichter.squaresandroots.ui.QuestionActivity.EXTRA_ROOT_QUESTION";
    public final static String EXTRA_SOLUTION = "eu.jprichter.squaresandroots.ui.QuestionActivity.EXTRA_SOLUTION";
    public final static String STATE_ROOT_QUESTION = "eu.jprichter.squaresandroots.ui.QuestionActivity.STATE_ROOT_QUESTION";

    int root = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        if(savedInstanceState != null)
            root = savedInstanceState.getInt(STATE_ROOT_QUESTION, 0);

        if(root == 0) {

            root =  (Double.valueOf(Math.random()*Kernel.getInstance().getMaxSquare())).intValue()+1;

        }

        questionText.setText(root + " * " + root + " = ?");

        editText.addTextChangedListener(new ButtonEnablerTextWatcher(checkButton));
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

        Intent intent = new Intent(this, CheckActivity.class);
        intent.putExtra(EXTRA_ROOT_QUESTION, root);
        intent.putExtra(EXTRA_SOLUTION, solution);

        startActivity(intent);

        root = 0; // signals that a new question has to be generated
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(STATE_ROOT_QUESTION, root);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
