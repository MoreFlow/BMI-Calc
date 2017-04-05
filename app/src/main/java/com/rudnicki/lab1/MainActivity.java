package com.rudnicki.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String WEIGHT_KEY = "WEIGHT_KEY";
    private static final String HEIGHT_KEY = "HEIGHT_KEY";
    private static final String TOGGLE_KEY = "TOGGLE_KEY";

    private static float MIN_BMI = 18.5f;
    private static float MAX_BMI = 25f;

    @BindView(R.id.weight_label) TextView weightTextView;
    @BindView(R.id.weight_edit_text) EditText weightEditText;
    @BindView(R.id.height_label) TextView heightTextView;
    @BindView(R.id.height_edit_text) EditText heightEditText;
    @BindView(R.id.bmi_placeholder) TextView bmiPlaceholderTextView;
    @BindView(R.id.feedback_placeholder) TextView feedbackTextView;
    @BindView(R.id.toggle_button) ToggleButton toggleButton;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private SharedPreferences sharedPreferences;

    private ICountBmi counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                clearFields();
                if (isChecked) {
                    heightTextView.setText(getString(R.string.height_imperial_label));
                    weightTextView.setText(getString(R.string.weight_imperial_label));
                } else {
                    heightTextView.setText(getString(R.string.height_metric_label));
                    weightTextView.setText(getString(R.string.weight_metric_label));
                }
            }
        });
        sharedPreferences = this.getSharedPreferences("com.rudnicki.lab1", Context.MODE_PRIVATE);
        setSavedData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                onSave();
                break;
            case R.id.action_share:
                onShare();
                break;
            case R.id.action_author:
                startAuthorActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(WEIGHT_KEY, String.valueOf(weightEditText.getText()));
        outState.putString(HEIGHT_KEY, String.valueOf(heightEditText.getText()));
        outState.putBoolean(TOGGLE_KEY, toggleButton.isChecked());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        weightEditText.setText(savedInstanceState.getString(WEIGHT_KEY));
        heightEditText.setText(savedInstanceState.getString(HEIGHT_KEY));
        toggleButton.setChecked(savedInstanceState.getBoolean(TOGGLE_KEY));
    }

    private void setupToolbar() {
        if (toolbar != null) {
            toolbar.setTitle(R.string.BMI_calc);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
        }
    }

    private void onSave() {
        if (counter == null) {
            counter = toggleButton.isChecked() ? new CountBmiImperial() : new CountBmiMetrics();
        }
        float weight;
        float height;
        try {
            weight = Float.parseFloat(String.valueOf(weightEditText.getText()));
            height = Float.parseFloat(String.valueOf(heightEditText.getText()));
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.data_not_valid, Toast.LENGTH_LONG).show();
            return;
        }
        boolean toggleButtonChecked = toggleButton.isChecked();
        if (counter.isHeightValid(height)) {
            if (counter.isWeightValid(weight)) {
                sharedPreferences.edit().putString(WEIGHT_KEY, String.valueOf(weight)).apply();
                sharedPreferences.edit().putString(HEIGHT_KEY, String.valueOf(height)).apply();
                sharedPreferences.edit().putBoolean(TOGGLE_KEY, toggleButtonChecked).apply();
                Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getString(R.string.please_enter_valid_weight), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.please_enter_valid_height), Toast.LENGTH_LONG).show();
        }
        hideSoftKeyboard();
    }

    private void setSavedData() {
        toggleButton.setChecked(sharedPreferences.getBoolean(TOGGLE_KEY, false));
        heightEditText.setText(sharedPreferences.getString(WEIGHT_KEY, ""));
        weightEditText.setText(sharedPreferences.getString(HEIGHT_KEY, ""));
    }

    private void onShare() {
        if (bmiPlaceholderTextView.getText() != "") {
            String textToShare = getString(R.string.my_bmi_is) + bmiPlaceholderTextView.getText() + ". " + feedbackTextView.getText();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_your_bmi)));
        } else {
            Toast.makeText(this, R.string.please_count_first, Toast.LENGTH_LONG).show();
        }
    }

    private void startAuthorActivity() {
        startActivity(new Intent(this, AuthorActivity.class));
    }

    @OnClick(R.id.clear_button)
    public void clearFields() {
        feedbackTextView.setText(getString(R.string.please_enter_your_data));
        bmiPlaceholderTextView.setText("");
        heightEditText.setText("");
        weightEditText.setText("");
    }

    @OnClick(R.id.count_button)
    public void countBmi() {
        hideSoftKeyboard();

        counter = toggleButton.isChecked() ? new CountBmiImperial() : new CountBmiMetrics();
        float weight;
        float height;

        try {
            weight = Float.parseFloat(String.valueOf(weightEditText.getText()));
            height = Float.parseFloat(String.valueOf(heightEditText.getText()));
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.data_not_valid, Toast.LENGTH_LONG).show();
            return;
        }
        if (!counter.isWeightValid(weight)) {
            Toast.makeText(this, R.string.please_enter_valid_weight, Toast.LENGTH_LONG).show();
        } else if (!counter.isHeightValid(height)) {
            Toast.makeText(this, R.string.please_enter_valid_height, Toast.LENGTH_LONG).show();
        } else {
            float bmi = counter.countBmi(weight, height);
            bmiPlaceholderTextView.setText(String.format("%.2f", bmi));
            setFeedback(bmi);
        }
    }

    private void setFeedback(float bmi) {
        if (bmi >= MAX_BMI) {
            bmiPlaceholderTextView.setTextColor(Color.RED);
            feedbackTextView.setText(R.string.overweight_feedback);
        } else if (bmi < MIN_BMI) {
            bmiPlaceholderTextView.setTextColor(Color.RED);
            feedbackTextView.setText(R.string.underweight_feedback);
        } else {
            bmiPlaceholderTextView.setTextColor(Color.GREEN);
            feedbackTextView.setText(R.string.normal_feedback);
        }
    }

    private void hideSoftKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
