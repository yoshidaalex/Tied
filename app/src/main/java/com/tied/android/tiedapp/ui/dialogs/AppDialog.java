package com.tied.android.tiedapp.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.tied.android.tiedapp.R;


/**
 * Created by Maulik.Joshi on 29-06-2015.
 */
public class AppDialog extends Dialog {
    private String message, positiveButton, negativeButton;

    public AppDialog(Context context, String message, String positiveButton, String negativeButton) {
        super(context, R.style.AppDialog);
        this.message = message;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.app_dialog);
        TextView txt_message = (TextView) findViewById(R.id.txt_message);
        TextView positive_button = (TextView) findViewById(R.id.positive_button);

        txt_message.setText(message);
        positive_button.setText(positiveButton);
        positive_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}