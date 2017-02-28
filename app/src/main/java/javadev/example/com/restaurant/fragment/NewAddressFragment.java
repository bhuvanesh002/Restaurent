package javadev.example.com.restaurant.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.activity.HomeActivity;
import javadev.example.com.restaurant.ui.SmartAppConstants;

/**
 * Created by Support on 12/27/2016.
 */
public class NewAddressFragment extends Fragment {
    Toolbar mToolbar;
    Button mSubmitButton;
    EditText mNicknameEditText, mAddressEditText, mSuitenoEditText, mCityEditText, mStateEditText, mZIpcodeEditText;
    String mNickname, mAddress, mSuiteno, mCity, mState, mZipcode;
    int flag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_newaddress, container, false);
        Bundle b = getArguments();
        try {
            flag = b.getInt("flag");
            System.out.println("fla" + flag);
        } catch (Exception e) {

        }
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), SmartAppConstants.BOLD_FONT);
        mSubmitButton = (Button) view.findViewById(R.id.button_submitadd);
        mNicknameEditText= (EditText) view.findViewById(R.id.edit_nickname);
        mAddressEditText = (EditText) view.findViewById(R.id.edit_address);
        mSuitenoEditText = (EditText) view.findViewById(R.id.edit_suiteno);
        mCityEditText = (EditText) view.findViewById(R.id.edit_city);
        mStateEditText = (EditText) view.findViewById(R.id.edit_state);
        mZIpcodeEditText= (EditText) view.findViewById(R.id.edit_zipcode);
        mSubmitButton.setTypeface(face);

        Bundle c=getArguments();
        String city=c.getString("locality");
        String state=c.getString("state");
        String address=c.getString("address");
        String zipcode=c.getString("zipcode");
        mAddressEditText.setText(address);
        mCityEditText.setText(city);
        mStateEditText.setText(state);
        mZIpcodeEditText.setText(zipcode);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNickname = mNicknameEditText.getText().toString();
                mAddress = mAddressEditText.getText().toString();
                mSuiteno = mSuitenoEditText.getText().toString();
                mCity = mCityEditText.getText().toString();
                mState = mStateEditText.getText().toString();
                mZipcode = mZIpcodeEditText.getText().toString();
                if (mNickname.equals("") || mAddress.equals("") || mSuiteno.equals("") || mCity.equals("") || mState.equals("") || mZipcode.equals("")) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.enter_all_fields), Toast.LENGTH_LONG).show();
                } else {
                    StoresFragment fragment_address = new StoresFragment();
                    FragmentTransaction mMemberFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    mMemberFragmentTransaction.replace(R.id.frame, fragment_address);
                    mMemberFragmentTransaction.commit();
                }

            }
        });
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("flassssssssss" + flag);
                if (flag == 2) {
                    NewAddressFragment currentFragment = new NewAddressFragment();
                    AddressFragment fragment_members = new AddressFragment();
                    String backStateName = fragment_members.getClass().getName();
                    System.out.println("backstck" + backStateName);
                    FragmentTransaction mMemberFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    mMemberFragmentTransaction.replace(R.id.frame, fragment_members);
                    mMemberFragmentTransaction.hide(currentFragment);
                    mMemberFragmentTransaction.show(fragment_members);
                    mMemberFragmentTransaction.addToBackStack(backStateName);
                    mMemberFragmentTransaction.commit();
                } else {
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    startActivity(i);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
