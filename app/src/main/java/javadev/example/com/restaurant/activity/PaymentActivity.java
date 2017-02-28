package javadev.example.com.restaurant.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javadev.example.com.restaurant.R;
import javadev.example.com.restaurant.model.Rewards;
import javadev.example.com.restaurant.model.Tax;
import javadev.example.com.restaurant.ui.SmartAppConstants;

public class PaymentActivity extends AppCompatActivity {
    private static final String TAG = "";
    RadioGroup mPaymentRadioGroup;
    Cursor taxCursor;
    TextView mTotalTextView, mTotalcountTextView, mSubtotalTextView, mServiceTextView, mVatTextView, mPromoTextView, mCartPromoTextView, mCartLoyaltyTextView, mPromoPriceTextView, mLoyaltyPriceTextView, mLoyaltyTextView, mSubcarttotalTextView, mcartServiceTextView, mcartVatTextView, mBillpaymentTextView, mPaymentoptionTextView, mAddcouponTextView;
    EditText mPromocodeEditText, mLoyaltyEditText;
    Button mPromoButton, mLoyaltyButton, mPaymentButton, mCancelpromoButton, mCancelLoyaltybButton;
    String tax_type, promo_code, mLoyalty, couponValue, couponType;
    float subtotal;
    RadioButton mRadioButton, mCashRadioButton, mCardRadioButton, mNetbankRadioButton;
    HashMap<String, Integer> mTaxMap = new HashMap<>();
    int percentage, service_percentage, vat_percentage, mLoyaltyPoints, loyalty;
    String mSubtotal;
    Toolbar mToolbar;
    DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Typeface face = Typeface.createFromAsset(getAssets(), SmartAppConstants.BOLD_FONT);
        mPaymentRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        SharedPreferences prefs_store = getSharedPreferences("loyalty", MODE_PRIVATE);
        mLoyaltyPoints = prefs_store.getInt("loyalty_points", 0);
        System.out.println("loy" + mLoyaltyPoints);
        ArrayList<Rewards> mCouponList = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.coupon);
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
            // Parse the data into jsonobject to get original data in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());
            couponType = jObject.getString("type");
            couponValue = jObject.getString("value");
        } catch (Exception e) {

        }
        mCashRadioButton = (RadioButton) findViewById(R.id.radioButton2);
        mCardRadioButton = (RadioButton) findViewById(R.id.radioButton);
        mNetbankRadioButton = (RadioButton) findViewById(R.id.radioButton3);
        mTotalTextView = (TextView) findViewById(R.id.text_carttotal);
        mTotalcountTextView = (TextView) findViewById(R.id.text_carttotaltext);
        mSubtotalTextView = (TextView) findViewById(R.id.text_cartbillsubtotal);
        mCartPromoTextView = (TextView) findViewById(R.id.text_cartpromotext);
        mPromoPriceTextView = (TextView) findViewById(R.id.text_cartpromo);
        mCartLoyaltyTextView = (TextView) findViewById(R.id.text_cartloyaltytext);
        mLoyaltyPriceTextView = (TextView) findViewById(R.id.text_cartloyalty);
        mSubcarttotalTextView = (TextView) findViewById(R.id.text_cartbillsubtotaltext);
        mcartServiceTextView = (TextView) findViewById(R.id.text_cartservicetext);
        mcartVatTextView = (TextView) findViewById(R.id.text_cartvattext);
        mBillpaymentTextView = (TextView) findViewById(R.id.textView_billsummary);
        mAddcouponTextView = (TextView) findViewById(R.id.textView_addcoupon);
        mPaymentoptionTextView = (TextView) findViewById(R.id.textView_paymentoption);
        mVatTextView = (TextView) findViewById(R.id.text_cartvat);
        mServiceTextView = (TextView) findViewById(R.id.text_cartservice);
        mPromoTextView = (TextView) findViewById(R.id.textview_promo);
        mLoyaltyTextView = (TextView) findViewById(R.id.textview_loyalty);
        mPaymentButton = (Button) findViewById(R.id.button_billpayment);
        mCancelpromoButton = (Button) findViewById(R.id.cancel_promo);
        mCancelLoyaltybButton = (Button) findViewById(R.id.cancel_loyalty);
        mPromocodeEditText = (EditText) findViewById(R.id.edit_promocode);
        mLoyaltyEditText = (EditText) findViewById(R.id.edit_loyalty);
        mPromoButton = (Button) findViewById(R.id.button_promo);
        mLoyaltyButton = (Button) findViewById(R.id.button_loyalty);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_payment);
        mToolbar.setTitle(R.string.payment);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTotalTextView.setTypeface(face);
        mCardRadioButton.setTypeface(face);
        mCashRadioButton.setTypeface(face);
        mNetbankRadioButton.setTypeface(face);
        mTotalcountTextView.setTypeface(face);
        mBillpaymentTextView.setTypeface(face);
        mAddcouponTextView.setTypeface(face);
        mPaymentoptionTextView.setTypeface(face);
        mCartPromoTextView.setTypeface(face);
        mPromoPriceTextView.setTypeface(face);
        mLoyaltyPriceTextView.setTypeface(face);
        mCartLoyaltyTextView.setTypeface(face);
        mServiceTextView.setTypeface(face);
        mSubtotalTextView.setTypeface(face);
        mVatTextView.setTypeface(face);
        mPromoTextView.setTypeface(face);
        mSubcarttotalTextView.setTypeface(face);
        mcartServiceTextView.setTypeface(face);
        mcartVatTextView.setTypeface(face);
        mLoyaltyTextView.setTypeface(face);
        mPromoButton.setTypeface(face);
        mLoyaltyButton.setTypeface(face);
        mPaymentButton.setTypeface(face);
        Intent i = getIntent();
        mSubtotal = i.getStringExtra("sub_total");
        subtotal = Float.parseFloat(mSubtotal);
        mSubtotalTextView.setText("$ " + String.valueOf(subtotal));
        db = new DataBaseHandler(this);
        taxCursor = db.query(Tax.TABLE_TAX, Tax.TAX_COLUMNS, null, null, null);
        if (taxCursor != null) {
            System.out.println("countssssme" + taxCursor.getCount());
            if (taxCursor.getCount() > 0 && taxCursor.moveToFirst()) {
                do {
                    percentage = taxCursor.getInt(taxCursor.getColumnIndex(Tax.TAXPERCENTAGE));
                    tax_type = taxCursor.getString(taxCursor.getColumnIndex(Tax.TAXNAME));
                    mTaxMap.put(tax_type, percentage);
                } while (taxCursor.moveToNext());

            }
        }
        ArrayList<Integer> taxList = new ArrayList<>();
        Iterator myVeryOwnIterator = mTaxMap.keySet().iterator();
        while (myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            int value = (Integer) mTaxMap.get(key);
            taxList.add(value);
        }
        vat_percentage = taxList.get(0);
        service_percentage = taxList.get(1);
        System.out.println("vat_percentage" + vat_percentage);
        System.out.println("service_percentage" + service_percentage);
        float mvattotal = (subtotal / 100.0f) * vat_percentage;
        float mservicetotal = (subtotal / 100.0f) * service_percentage;
        System.out.println("mvattotal" + mvattotal);
        System.out.println("mservicetotal" + mservicetotal);
        String svattotal = String.format("%.2f", mvattotal);
        String sservicetotal = String.format("%.2f", mservicetotal);
        mServiceTextView.setText("$ " + String.valueOf(sservicetotal));
        mVatTextView.setText("$ " + String.valueOf(svattotal));
        float mlasttotal = subtotal + (mvattotal + mservicetotal);
        String slasttotal = String.format("%.2f", mlasttotal);
        mTotalTextView.setText("$ " + slasttotal);
        mPaymentButton.setText("pay $ " + slasttotal);
        mPromocodeEditText.addTextChangedListener(mTextWatcher);
        mLoyaltyEditText.addTextChangedListener(mTextWatcher);
        // run once to disable if empty
        checkFieldsForEmptyValues();
        mPromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promo_code = mPromocodeEditText.getText().toString();
                if (promo_code.equals("HAPPY20")) {
                    float sTotal = Float.parseFloat(couponValue);
                    int flag = 2;
                    mPromoButton.setVisibility(View.GONE);
                    mPromocodeEditText.setVisibility(View.GONE);
                    mCancelpromoButton.setVisibility(View.VISIBLE);
                    mPromoTextView.setVisibility(View.VISIBLE);
                    mPromoTextView.setText("thanks for using this promo code");
                    mCartPromoTextView.setVisibility(View.VISIBLE);
                    mPromoPriceTextView.setVisibility(View.VISIBLE);
                    mCartPromoTextView.setText("promo - (" + promo_code + "-" + couponValue + " off)");
                    mPromoPriceTextView.setText("$ " + couponValue);
                    payment(sTotal, flag);
                } else {
                    System.out.println("valid");
                    mPromocodeEditText.setError("enter valid promo code");
                }
            }
        });
        mPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = 0;
                selectedId = mPaymentRadioGroup.getCheckedRadioButtonId();
                mRadioButton = (RadioButton) findViewById(selectedId);


                if (selectedId < 0) {
                    Toast.makeText(PaymentActivity.this, getResources().getString(R.string.select_any_option), Toast.LENGTH_SHORT).show();



                } else if (selectedId!=0&&!mRadioButton.getText().equals("cash on delivery")){
                    makePayment(v);

                }

            }
        });
        mLoyaltyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("validpro" + mLoyalty);
                mLoyalty = mLoyaltyEditText.getText().toString();
                loyalty = Integer.parseInt(mLoyalty);
                if (mLoyaltyPoints > loyalty) {
                    float sTotal = 5;
                    int flag = 2;
                    mLoyaltyButton.setVisibility(View.GONE);
                    mLoyaltyEditText.setVisibility(View.GONE);
                    mCancelLoyaltybButton.setVisibility(View.VISIBLE);
                    mLoyaltyTextView.setVisibility(View.VISIBLE);
                    mLoyaltyTextView.setText(loyalty + " points used for this order");
                    mCartLoyaltyTextView.setVisibility(View.VISIBLE);
                    mLoyaltyPriceTextView.setVisibility(View.VISIBLE);
                    mCartLoyaltyTextView.setText("loyalty - (" + mLoyalty + "- off)");
                    mLoyaltyPriceTextView.setText("$ " + sTotal);
                    payment(sTotal, flag);
                } else {
                    mLoyaltyEditText.setError("enter valid loyalty points");
                }
            }
        });
        mCancelpromoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float sTotal = Float.parseFloat(couponValue);
                int flag = 1;
                mCancelpromoButton.setVisibility(View.GONE);
                mPromoButton.setVisibility(View.VISIBLE);
                mPromocodeEditText.setVisibility(View.VISIBLE);
                mCartPromoTextView.setVisibility(View.GONE);
                mPromoPriceTextView.setVisibility(View.GONE);
                mPromoTextView.setVisibility(View.GONE);
                payment(sTotal, flag);
            }
        });
        mCancelLoyaltybButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float sTotal = 5;
                int flag = 1;
                mCartLoyaltyTextView.setVisibility(View.GONE);
                mLoyaltyPriceTextView.setVisibility(View.GONE);
                mCancelLoyaltybButton.setVisibility(View.GONE);
                mLoyaltyButton.setVisibility(View.VISIBLE);
                mLoyaltyEditText.setVisibility(View.VISIBLE);
                mLoyaltyTextView.setVisibility(View.GONE);
                payment(sTotal, flag);
            }
        });
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            checkFieldsForEmptyValues();
        }


    };

    void checkFieldsForEmptyValues() {
        promo_code = mPromocodeEditText.getText().toString();
        if (promo_code.length() == 0) {
            mPromoButton.setEnabled(false);

        } else if (promo_code.length() > 0) {
            mPromoButton.setEnabled(true);
            mPromoButton.setBackgroundColor(getResources().getColor(R.color.red));
        }
        mLoyalty = mLoyaltyEditText.getText().toString();
        if (mLoyalty.length() == 0) {
            mLoyaltyButton.setEnabled(false);
        } else if (promo_code.length() > 0) {
            mLoyaltyButton.setEnabled(true);
            mLoyaltyButton.setBackgroundColor(getResources().getColor(R.color.red));
        }

    }

    public void payment(float total, int flag) {
        String price = mPaymentButton.getText().toString().substring(6);
        float mprice = 0;
        mprice = Float.parseFloat(price);
        System.out.println("mprice" + mprice);
        float mtotal = 0, totalprice = 0;
        mtotal = total;
        String mTotal = String.format("%.2f", mtotal);
        totalprice = Float.parseFloat(mTotal);
        System.out.println("mtotal" + mtotal);
        int mflag = 0;
        mflag = flag;
        System.out.println("mflag" + mflag);
        float roundtotal = 0;
        if (mflag == 1) {
            roundtotal = mprice + totalprice;
            String roundedtotal = String.format("%.2f", roundtotal);
            mPaymentButton.setText("pay $ " + roundedtotal);
            mTotalTextView.setText("$ " + roundedtotal);
        } else {
            roundtotal = mprice - totalprice;
            String roundedtotal = String.format("%.2f", roundtotal);
            mPaymentButton.setText("pay $ " + roundedtotal);
            mTotalTextView.setText("$ " + roundedtotal);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private double getAmount() {


        Double amount = 10.0;

        if (isDouble(mPaymentButton.getText().toString().substring(6))) {
            amount = Double.parseDouble(mPaymentButton.getText().toString().substring(6));
            return amount;
        } else {
            Toast.makeText(getApplicationContext(), "Paying Default Amount ₹10", Toast.LENGTH_LONG).show();
            return amount;
        }
    }

    public void makePayment(View view) {

        String phone = "8882434664";
        String productName = "product_name";
        String firstName = "piyush";
        String txnId = "0nf7" + System.currentTimeMillis();
        String email = "piyush.jain@payu.in";
        String sUrl = "https://www.android.com/intl/en_in/";
        String fUrl = "https://en.wikipedia.org/wiki/Microsoft_Windows";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";
        boolean isDebug = true;
        String key = "dRQuiA";
        String merchantId = "4928174";

        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new PayUmoneySdkInitilizer.PaymentParam.Builder();


        builder.setAmount(getAmount())
                .setTnxId(txnId)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(sUrl)
                .setfUrl(fUrl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setIsDebug(isDebug)
                .setKey(key)
                .setMerchantId(merchantId);

        PayUmoneySdkInitilizer.PaymentParam paymentParam = builder.build();


//             server side call required to calculate hash with the help of <salt>
//             <salt> is already shared along with merchant <key>
     /*        serverCalculatedHash =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|<salt>)

             (e.g.)

             sha512(FCstqb|0nf7|10.0|product_name|piyush|piyush.jain@payu.in||||||MBgjYaFG)

             9f1ce50ba8995e970a23c33e665a990e648df8de3baf64a33e19815acd402275617a16041e421cfa10b7532369f5f12725c7fcf69e8d10da64c59087008590fc
*/

        // Recommended
        calculateServerSideHashAndInitiatePayment(paymentParam);

//        testing purpose

       /* String salt = "";0
        String serverCalculatedHash=hashCal(key+"|"+txnId+"|"+getAmount()+"|"+productName+"|"
                +firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+salt);

        paymentParam.setMerchantHash(serverCalculatedHash);

        PayUmoneySdkInitilizer.startPaymentActivityForResult(MyActivity.this, paymentParam);*/

    }

    public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }

    private void calculateServerSideHashAndInitiatePayment(final PayUmoneySdkInitilizer.PaymentParam paymentParam) {

        // Replace your server side hash generator API URL
        String url = "https://test.payumoney.com/payment/op/calculateHashForTest";

        Toast.makeText(this, "Please wait... Generating hash from server ... ", Toast.LENGTH_LONG).show();
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.has(SdkConstants.STATUS)) {
                        String status = jsonObject.optString(SdkConstants.STATUS);
                        if (status != null || status.equals("1")) {
                            System.out.println("mtotalpayyyyyyyaaaaaa");
                            String hash = jsonObject.getString(SdkConstants.RESULT);
                            Log.i("app_activity", "Server calculated Hash :  " + hash);

                            paymentParam.setMerchantHash(hash);
                            System.out.println("mtotalpayyyyyyybbbbbb");
                            PayUmoneySdkInitilizer.startPaymentActivityForResult(PaymentActivity.this, paymentParam);
                            System.out.println("mtotalpayyyyyyysssssss");
                        } else {

                            Toast.makeText(PaymentActivity.this,
                                    jsonObject.getString(SdkConstants.RESULT),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    Toast.makeText(PaymentActivity.this,
                            PaymentActivity.this.getString(R.string.connect_to_internet),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaymentActivity.this,
                            error.getMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return paymentParam.getParams();
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {

            /*if(data != null && data.hasExtra("result")){
              String responsePayUmoney = data.getStringExtra("result");
                if(SdkHelper.checkForValidString(responsePayUmoney))
                    showDialogMessage(responsePayUmoney);
            } else {
                showDialogMessage("Unable to get Status of Payment");
            }*/


            if (resultCode == RESULT_OK) {
                Log.i(TAG, "Success - Payment ID : " + data.getStringExtra(SdkConstants.PAYMENT_ID));
                String paymentId = data.getStringExtra(SdkConstants.PAYMENT_ID);
                showDialogMessage("Payment Success Id : " + paymentId);
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "failure");
                showDialogMessage("cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                Log.i("app_activity", "failure");

                if (data != null) {
                    if (data.getStringExtra(SdkConstants.RESULT).equals("cancel")) {

                    } else {
                        showDialogMessage("failure");
                    }
                }
                //Write your code if there's no result
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
                Log.i(TAG, "User returned without login");
                showDialogMessage("User returned without login");
            }
        }
    }

    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }
}
