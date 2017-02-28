package javadev.example.com.restaurant.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class PickUpActivity extends AppCompatActivity {
    TextView mFindTextView, mOrTextView, mUsezipTextView;
    Button mConfirmButton;
    EditText mStateEditText, mCityEditText, mZipEditText;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        Typeface face = Typeface.createFromAsset(getAssets(), SmartAppConstants.BOLD_FONT);
        mStateEditText = (EditText) findViewById(R.id.editText_state);
        mCityEditText = (EditText) findViewById(R.id.editText_city);
        mZipEditText = (EditText) findViewById(R.id.editText_zip);
        mConfirmButton = (Button) findViewById(R.id.button_confirm);
        mFindTextView = (TextView) findViewById(R.id.textView_pickup);
        mOrTextView = (TextView) findViewById(R.id.textView_or);
        mUsezipTextView = (TextView) findViewById(R.id.textView_usestate);
        mStateEditText.setTypeface(face);
        mCityEditText.setTypeface(face);
        mZipEditText.setTypeface(face);
        mConfirmButton.setTypeface(face);
        mFindTextView.setTypeface(face);
        mOrTextView.setTypeface(face);
        mUsezipTextView.setTypeface(face);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_pickup);
        mToolbar.setTitle(R.string.pickup);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(PickUpActivity.this, HomeActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
