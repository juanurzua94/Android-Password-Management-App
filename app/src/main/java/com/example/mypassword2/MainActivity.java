package com.example.mypassword2;

import android.app.ActionBar;
import android.app.KeyguardManager;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LifecycleObserver {

    private FingerprintManager fingerPrintManager;
    private KeyguardManager keyguardManager;
    private boolean endProgram = false;
    private FingerprintAuth fingerprintAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        setContentView(R.layout.activity_main);



    }

    private void openSignInDialogue(String header, String info, String text, String buttonText, int image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.sign_in_dialog, null);

        TextView headerText = (TextView) mView.findViewById(R.id.headerText);
        TextView infoText = (TextView) mView.findViewById(R.id.info);
        TextView subInfoText = (TextView) mView.findViewById(R.id.sub_info);
        ImageView imageIcon = (ImageView) mView.findViewById(R.id.icon);
        Button displayButton = (Button) mView.findViewById(R.id.exit_button);

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        headerText.setText(header);
        infoText.setText(info);
        subInfoText.setText(text);
        displayButton.setText(buttonText);
        imageIcon.setImageResource(image);

        builder.setView(mView);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        dialog.getWindow().setLayout(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        fingerprintAuth = new FingerprintAuth();
        fingerprintAuth.start(fingerPrintManager, this);

    }

    private void chooseAppropriateDisplay(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerPrintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (!fingerPrintManager.isHardwareDetected()) {
                openSignInDialogue("Error", "Device does not support fingerprint authentication", "", "Exit", R.mipmap.infoicon);
                endProgram = true;
            }

            if (!fingerPrintManager.hasEnrolledFingerprints()) {
                openSignInDialogue("Notice", "Enroll a fingerprint into device to continue", "", "Exit", R.mipmap.padlockicon);
                endProgram = true;
            }

            if (!keyguardManager.isKeyguardSecure()) {
                openSignInDialogue("Notice", "Secure device lockscreen to continue", "", "Exit", R.mipmap.infoicon);
                endProgram = true;
            }

            if (endProgram == false) {
                openSignInDialogue("Sign in", "Confirm fingerprint to continue", "Touch Sensor", "Cancel", R.mipmap.fingerprint);

            }


        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(){
        super.onResume();
        chooseAppropriateDisplay();
    }

}
