package com.denyago.name.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import mp.MpUtils;
import mp.PaymentRequest;
import mp.PaymentResponse;

public class SdkMainActivity extends Activity implements OnClickListener {
    private static String SERVICE_ID = "bd357a621f945a2f1e136f065c874b9c";
    private static String APP_SECRET = "4d345c7ab7f5f462ce4e5a1b87d70707";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdk_activity_main);
        ((Button)findViewById(R.id.buy_button)).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buy_button: {

                PaymentRequest.PaymentRequestBuilder builder = new PaymentRequest.PaymentRequestBuilder();
                builder.setService(SERVICE_ID, APP_SECRET);
                builder.setDisplayString("News");
                builder.setProductName("news");  // non-consumable purchases are restored using this value
                builder.setConsumable(true);     // non-consumable items can be later restored
                builder.setIcon(R.drawable.ic_launcher);
                PaymentRequest pr = builder.build();
                makePayment(pr);

            } break;
        }
    }

    // Fortumo related glue-code
    private static final int REQUEST_CODE = 1234; // Can be anything
    protected final void makePayment(PaymentRequest payment) {
        startActivityForResult(payment.toIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if(data == null) {
                return;
            }

            // OK
            if (resultCode == RESULT_OK) {
                PaymentResponse response = new PaymentResponse(data);

                switch (response.getBillingStatus()) {
                    case MpUtils.MESSAGE_STATUS_BILLED:
                        // ...
                        break;
                    case MpUtils.MESSAGE_STATUS_FAILED:
                        // ...
                        break;
                    case MpUtils.MESSAGE_STATUS_PENDING:
                        // ...
                        break;
                }
                // Cancel
            } else {
                // ..
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
