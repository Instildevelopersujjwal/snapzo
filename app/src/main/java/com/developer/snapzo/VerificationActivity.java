package com.developer.snapzo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

public class VerificationActivity extends ActionBarActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback, VerificationListener {

    private static final String TAG = Verification.class.getSimpleName();
    private Verification mVerification;
    TextView resend_timer;
    String phone_no;

    Button killer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        resend_timer = (TextView) findViewById(R.id.resend_timer);
        resend_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResendCode();
            }
        });

        Intent intent = getIntent();
        phone_no = intent.getExtras().getString("phone");
        startTimer();
        initiateVerification();
        killer=(Button)findViewById(R.id.codeInputButton);
        killer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = ((EditText) findViewById(R.id.inputCode)).getText().toString();
                if (!code.isEmpty()) {
                    if (mVerification != null) {
                        mVerification.verify(code);
                        //     showProgress();
                        TextView messageText = (TextView) findViewById(R.id.textView);
                        //    messageText.setText("Verification in progress");

                        Toast.makeText(VerificationActivity.this, "text verification in progress", Toast.LENGTH_LONG)
                                .show();
                        enableInputField(false);
                    }
                }    //   hideKeypad();
            }
        });
    }

  //  public void onSubmitClicked(View view) {
       /* String code = ((EditText) findViewById(R.id.inputCode)).getText().toString();
        if (!code.isEmpty()) {
            if (mVerification != null) {
                mVerification.verify(code);
                //     showProgress();
                TextView messageText = (TextView) findViewById(R.id.textView);
                //    messageText.setText("Verification in progress");

                Toast.makeText(VerificationActivity.this, "text verification in progress", Toast.LENGTH_LONG)
                        .show();
                enableInputField(false);
            }
        }    //   hideKeypad();
     */
    //}


    void enableInputField(boolean enable) {
        View container = findViewById(R.id.inputContainer);
        if (enable) {
            container.setVisibility(View.VISIBLE);
            EditText input = (EditText) findViewById(R.id.inputCode);
            input.requestFocus();
        } else {
            container.setVisibility(View.GONE);
        }
        TextView resend_timer = (TextView) findViewById(R.id.resend_timer);
        resend_timer.setClickable(false);
    }


    void createVerification(String phoneNumber, boolean skipPermissionCheck, String countryCode) {
        if (!skipPermissionCheck && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 0);

        } else {

            Log.d(TAG, "ELSE in progress");
            mVerification = SendOtpVerification.createSmsVerification
                    (SendOtpVerification
                            .config(countryCode + phoneNumber)
                            .context(this)
                            .autoVerification(true)
                            .build(), this);
            mVerification.initiate();
            Toast.makeText(VerificationActivity.this, "else end", Toast.LENGTH_SHORT)
                    .show();
            Log.d(TAG, "ELSE in end");
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "This application needs permission to read your SMS to automatically verify your "
                        + "phone, you may disable the permission once you have been verified.", Toast.LENGTH_LONG)
                        .show();
            }
        }
        initiateVerificationAndSuppressPermissionCheck();
    }

    void initiateVerification() {

        //COMMENTING this part for testing

     //   initiateVerification(false);
        //* uncomment above

        //skip verification

        Toast.makeText(VerificationActivity.this,"User Verified",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(VerificationActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //killer code starts
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("session", 1);
        editor.commit();
        //killer code ends
        startActivity(intent);
        finish();


        //



    }

    void initiateVerificationAndSuppressPermissionCheck() {
        initiateVerification(true);
    }

    void initiateVerification(boolean skipPermissionCheck) {
        Intent intent = getIntent();
        if (intent != null) {
            String phoneNumber = ""+phone_no;
            String countryCode = "91";
          //  TextView phoneText = (TextView) findViewById(R.id.numberText);
         //   phoneText.setText("+" + countryCode + phoneNumber);
            createVerification(phoneNumber, skipPermissionCheck, countryCode);
            Toast.makeText(VerificationActivity.this, " initiateVerification", Toast.LENGTH_SHORT)
                    .show();
            Log.d(TAG, "initiateVerification started");
        }
    }


    private void startTimer() {
        resend_timer.setClickable(false);
        resend_timer.setTextColor(ContextCompat.getColor(VerificationActivity.this, R.color.light_gray));
        new CountDownTimer(30000, 1000) {
            int secondsLeft = 0;

            public void onTick(long ms) {
                if (Math.round((float) ms / 1000.0f) != secondsLeft) {
                    secondsLeft = Math.round((float) ms / 1000.0f);
                    resend_timer.setText("Resend via call ( " + secondsLeft + " )");
                }
            }

            public void onFinish() {
                resend_timer.setClickable(true);
                resend_timer.setText("Resend via call");
                resend_timer.setTextColor(ContextCompat.getColor(VerificationActivity.this, R.color.light_gray));
            }
        }.start();
    }
    public void ResendCode() {
        startTimer();
        mVerification.resend("voice");
    }




    @Override
    public void onInitiated(String response) {
        Log.d(TAG, "Initialized!" + response);
        Toast.makeText(VerificationActivity.this,"OTP Sent",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitiationFailed(Exception exception) {
        Log.e(TAG, "Verification initialization failed: " + exception.getMessage());
        Toast.makeText(VerificationActivity.this,"something went wrong:"+exception.getMessage(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(VerificationActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onVerified(String response) {
        Log.d(TAG, "Verified!\n" + response);
      Toast.makeText(VerificationActivity.this,"User Verified",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(VerificationActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //killer code starts
        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("session", 1);
        editor.commit();
        //killer code ends
        startActivity(intent);
        finish();
    }

    @Override
    public void onVerificationFailed(Exception exception) {
        Log.e(TAG, "Verification failed: " + exception.getMessage());
        Toast.makeText(VerificationActivity.this,"Failed:"+exception.getMessage(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(VerificationActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }



}