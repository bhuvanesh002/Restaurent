package javadev.example.com.restaurant.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.activity.HomeActivity;
import javadev.example.com.restaurant.ui.SmartAppConstants;

/**
 * Created by Support on 12/22/2016.
 */
public class LoginFragment extends Fragment {
    EditText mEmailEditText, mPasswordEditText;
    Button mLoginButton;
    String mEmail, mPassword, mValidUserName, mValidPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login, container, false);
        String username = "";
        String password = "";
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), SmartAppConstants.BOLD_FONT);
        mEmailEditText = (EditText) view.findViewById(R.id.edit_loginemail);
        mPasswordEditText = (EditText) view.findViewById(R.id.edit_loginpassword);
        mLoginButton = (Button) view.findViewById(R.id.button_login);
        mPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
        mPasswordEditText.setTypeface(face);
        mEmailEditText.setTypeface(face);
        mLoginButton.setTypeface(face);
        InputStream inputStream = getResources().openRawResource(R.raw.myfile);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.v("Text Data", byteArrayOutputStream.toString());
        try {
            // Parse the mMenuList into jsonobject to get original mMenuList in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());

            JSONArray jArray = jObject.getJSONArray("Users");
            for (int i = 0; i < jArray.length(); i++) {
                username = jArray.getJSONObject(i).getString("email");
                password = jArray.getJSONObject(i).getString("password");
                Log.v("Cat ID", username);
                Log.v("Cat Name", password);
                mValidUserName = username;
                mValidPassword = password;
                Log.v("Cat user", mValidUserName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmailEditText.getText().toString().trim();
                mPassword = mPasswordEditText.getText().toString().trim();
                if (mEmail.equals(mValidUserName) && mPassword.equals(mValidPassword)) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("address", getActivity().MODE_PRIVATE).edit();
                    editor.putString("name", mEmail);
                    editor.commit();
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                    Toast.makeText(getActivity(), getActivity().getString(R.string.success), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.valid_username), Toast.LENGTH_LONG).show();
                    mEmailEditText.setText("");
                    mPasswordEditText.setText("");
                }
            }
        });
        return view;

    }
}
