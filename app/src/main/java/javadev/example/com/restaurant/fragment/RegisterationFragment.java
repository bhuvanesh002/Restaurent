package javadev.example.com.restaurant.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.ui.SmartAppConstants;

/**
 * Created by Support on 12/22/2016.
 */
public class RegisterationFragment extends Fragment {
    EditText mFirstNameEditText, mLastNameEditText, mEmailEditText, mPhonenoEditText, mPasswordEditText, mConfirmPasswordEditText;
    Button mRegisterButton;
    String mFirstName, mLastName, mEmail, mPhoneNo, mPassword, mConfirmPassword;
    String mEmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String mMobilePattern = "[0-9]{10}";
    String mPasswordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_register, container, false);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), SmartAppConstants.BOLD_FONT);
        mFirstNameEditText = (EditText) view.findViewById(R.id.edit_firstname);
        mLastNameEditText = (EditText) view.findViewById(R.id.edit_lastname);
        mEmailEditText = (EditText) view.findViewById(R.id.edit_email);
        mPhonenoEditText = (EditText) view.findViewById(R.id.edit_phoneno);
        mPasswordEditText = (EditText) view.findViewById(R.id.edit_password);
        mConfirmPasswordEditText = (EditText) view.findViewById(R.id.edit_confirmpassword);
        mRegisterButton = (Button) view.findViewById(R.id.button_register);
        mPasswordEditText.setTypeface(face);
        mPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
        mConfirmPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
        mEmailEditText.setTypeface(face);
        mConfirmPasswordEditText.setTypeface(face);
        mPhonenoEditText.setTypeface(face);
        mLastNameEditText.setTypeface(face);
        mFirstNameEditText.setTypeface(face);
        mRegisterButton.setTypeface(face);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirstName = mFirstNameEditText.getText().toString();
                mLastName = mLastNameEditText.getText().toString();
                mEmail = mEmailEditText.getText().toString();
                System.out.println("email" + mEmail);
                mPhoneNo = mPhonenoEditText.getText().toString();
                mPassword = mPasswordEditText.getText().toString();
                mConfirmPassword = mConfirmPasswordEditText.getText().toString();
                if (!mPassword.equals(mPassword)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.matching_pass), Toast.LENGTH_LONG).show();
                    mPasswordEditText.setText("");
                    mConfirmPasswordEditText.setText("");
                } else if (mFirstName.equals("") || mLastName.equals("") || mEmail.equals("") || mPassword.equals("") || mPhoneNo.equals("") || mConfirmPassword.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.enter_all_fields), Toast.LENGTH_LONG).show();

                } else if (!mEmail.matches(mEmailPattern)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.valid_email), Toast.LENGTH_LONG).show();

                } else if (!mPhoneNo.matches(mMobilePattern)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.valid_mobile), Toast.LENGTH_LONG).show();

                } else if (!mPassword.matches(mPasswordPattern)) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.invalid_password), Toast.LENGTH_LONG).show();

                } else if (mFirstNameEditText.length() < 5) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.valid_user), Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.success_register), Toast.LENGTH_LONG).show();

                }

            }
        });

        return view;
    }

}
