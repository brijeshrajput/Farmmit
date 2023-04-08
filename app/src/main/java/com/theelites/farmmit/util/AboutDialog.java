package com.theelites.farmmit.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bunty.mietbradmin.Utils.Helper;
import com.theelites.farmmit.R;

import java.util.Calendar;

public class AboutDialog extends Dialog {

    private final Context context;

    public AboutDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_about);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        // about text
        TextView aboutText = findViewById(R.id.about_text);
        String aboutTextIntent = null;
        try {
            aboutTextIntent = "Copyright © " + Calendar.getInstance().get(Calendar.YEAR) + " " + context.getResources().getString(R.string.app_name) + " • Version " + Helper.Companion.get_version_name(context);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        aboutText.setText(aboutTextIntent);

        // privacy policy
        TextView privacyPolicy = findViewById(R.id.privacy_policy);
        privacyPolicy.setOnClickListener(v -> {
//            Intent openPrivacyPolicy = new Intent(context, PoliciesActivity.class);
//            openPrivacyPolicy.putExtra("ACTIVITY_NAME", context.getResources().getString(R.string.privacy_and_policy));
//            openPrivacyPolicy.putExtra("REQUEST_ID", "policies/privacy/" + Config.SECRET_API_KEY + ".json");
//            context.startActivity(openPrivacyPolicy);
        });

        // terms of use
        TextView termsOfUse = findViewById(R.id.terms_of_use);
        termsOfUse.setOnClickListener(v -> {
//            Intent openTermsOfUse = new Intent(context, PoliciesActivity.class);
//            openTermsOfUse.putExtra("ACTIVITY_NAME", context.getResources().getString(R.string.terms_of_use));
//            openTermsOfUse.putExtra("REQUEST_ID", "policies/terms/" + Config.SECRET_API_KEY + ".json");
//            context.startActivity(openTermsOfUse);
        });

        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Window window = getWindow();
            WindowManager.LayoutParams WLP = window.getAttributes();
            WLP.gravity = Gravity.BOTTOM;
            window.setAttributes(WLP);
        }
    }
}
