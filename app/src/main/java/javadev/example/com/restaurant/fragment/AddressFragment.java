package javadev.example.com.restaurant.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.activity.AddressActivity;
import javadev.example.com.restaurant.model.Addresses;
import javadev.example.com.restaurant.ui.SmartAppConstants;

/**
 * Created by Support on 12/27/2016.
 */
public class AddressFragment extends Fragment {
    Addresses address;
    RecyclerView mRecyclerView;
    TextView mAddressTextView;
    Button mAddAddressButton, mSubmitButton;
    LinearLayoutManager mLayoutManager;
    boolean mstatus;
    int flag;
    Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_address, container, false);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar_add);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), SmartAppConstants.BOLD_FONT);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_address);
        mAddressTextView = (TextView) view.findViewById(R.id.textView_add);
        mAddAddressButton = (Button) view.findViewById(R.id.button_addaddress);
        mSubmitButton = (Button) view.findViewById(R.id.button_submitaddr);
        mAddAddressButton.setTypeface(face);
        mSubmitButton.setTypeface(face);
        mAddressTextView.setTypeface(face);

        mAddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressActivity myActivity = (AddressActivity) getActivity();
                NewAddressFragment fragment_address = new NewAddressFragment();
                FragmentTransaction mAddMemberFragmentTransaction = myActivity.getSupportFragmentManager().beginTransaction();
                mAddMemberFragmentTransaction.replace(R.id.frame, fragment_address);
                mAddMemberFragmentTransaction.commit();
                Bundle args = new Bundle();
                args.putInt("flag", 2);
                fragment_address.setArguments(args);

            }
        });
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("flags" + mstatus);
                if (AddressAdapter.mstatus) {
                    StoresFragment fragment_address = new StoresFragment();
                    FragmentTransaction mMemberFragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    mMemberFragmentTransaction.replace(R.id.frame, fragment_address);
                    mMemberFragmentTransaction.commit();
                } else if (!mstatus) {
                    Toast.makeText(getActivity(), R.string.any_address, Toast.LENGTH_LONG).show();
                }

            }
        });
        final ArrayList<Addresses> mAddressList = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.address);
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
            JSONArray jArray = jObject.getJSONArray("address");
            for (int i = 0; i < jArray.length(); i++) {
                address = new Addresses();
                address.nickname = jArray.getJSONObject(i).getString("Nickname");
                address.address = jArray.getJSONObject(i).getString("addr");
                address.suiteno = jArray.getJSONObject(i).getString("suite");
                address.city = jArray.getJSONObject(i).getString("city");
                address.state = jArray.getJSONObject(i).getString("state");
                address.zipcode = jArray.getJSONObject(i).getString("zipcode");
                mAddressList.add(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AddressAdapter mAddressAdapter;
        mAddressAdapter = new AddressAdapter(getActivity(), mAddressList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAddressAdapter);


        return view;
    }


    public static class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
        ArrayList<Addresses> data = new ArrayList<>();
        static Context mContext;
        public static boolean mstatus = false;
        private static int lastCheckedPos = 0;
        private static RadioButton lastChecked = null;
        int clickedPos;


        static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mNickNameTextView, mAddressTextView;
            RadioButton mRadioButton = null;
            boolean status = false;

            ViewHolder(View v) {
                super(v);
                Typeface face = Typeface.createFromAsset(mContext.getAssets(), SmartAppConstants.BOLD_FONT);
                mNickNameTextView = (TextView) v.findViewById(R.id.text_nickname);
                mAddressTextView = (TextView) v.findViewById(R.id.address);
                mRadioButton = (RadioButton) v.findViewById(R.id.radioButton);
                mNickNameTextView.setTypeface(face);
                mAddressTextView.setTypeface(face);

            }


        }


        public AddressAdapter(Context context, ArrayList<Addresses> myDataset) {
            mContext = context;
            data = myDataset;
        }

        @Override
        public AddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int position) {
            // create a new view
            View v = null;
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.address_adapter, parent, false);

            return new ViewHolder(v);

        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            holder.mNickNameTextView.setText(data.get(position).getNickname());
            holder.mRadioButton.setChecked(data.get(position).isSelected());
            holder.mRadioButton.setTag(new Integer(position));
            holder.mAddressTextView.setText(data.get(position).getSuiteno() + "," + data.get(position).getAddress() + "\n" + data.get(position).getCity() + "," + data.get(position).getState() + "\n" + data.get(position).getZipcode());
            if (position == 0 && data.get(0).isSelected() && holder.mRadioButton.isChecked()) {
                lastChecked = holder.mRadioButton;
                lastCheckedPos = 0;
            }
            holder.mRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.status = true;
                    mstatus = true;
                    RadioButton rb = (RadioButton) v;
                    clickedPos = ((Integer) rb.getTag()).intValue();
                    System.out.println("posi" + clickedPos);
                    if (rb.isChecked()) {
                        if (lastChecked != null) {
                            lastChecked.setChecked(false);
                            data.get(lastCheckedPos).setSelected(false);
                        }
                        lastChecked = rb;
                        lastCheckedPos = clickedPos;
                    } else
                        lastChecked = null;
                    data.get(clickedPos).setSelected(rb.isChecked());
                }
            });
            SharedPreferences.Editor editor = mContext.getSharedPreferences("addresscart", mContext.MODE_PRIVATE).edit();
            editor.putString("address_nick", data.get(clickedPos).getNickname());
            editor.putString("address_add", data.get(clickedPos).getAddress());
            editor.putString("address_suit", data.get(clickedPos).getSuiteno());
            editor.putString("address_state", data.get(clickedPos).getState());
            editor.putString("address_city", data.get(clickedPos).getCity());
            editor.putString("address_zipcode", data.get(clickedPos).getZipcode());
            editor.commit();


        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}

