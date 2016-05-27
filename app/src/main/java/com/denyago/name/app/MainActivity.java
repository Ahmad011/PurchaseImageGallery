package com.denyago.name.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

import mp.MpUtils;
import mp.PaymentRequest;
import mp.PaymentResponse;

public class MainActivity extends Activity{
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private static String SERVICE_ID = "bd357a621f945a2f1e136f065c874b9c";
    private static String APP_SECRET = "4d345c7ab7f5f462ce4e5a1b87d70707";
    private AlertDialog.Builder Notify;
    String itemposition;
    static  public Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
        gridView.setAdapter(gridAdapter);



        gridView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                CheckLockStatus(position,item);

                //Create intent
//                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//                intent.putExtra("title", item.getTitle());
//                intent.putExtra("image", item.getImage());
//                startActivity(intent);
            }
        });
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }


    public void CheckLockStatus(int position, ImageItem item){


       itemposition = String.valueOf(position);

        SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_CHECK", Context.MODE_PRIVATE);
        int check = prfs.getInt(itemposition, 0);

        if(check==0){
            Toast.makeText(this,"Buy First",Toast.LENGTH_SHORT).show();
            createDialog();
        }
        else
        {

           bm = item.getImage();

            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("title", item.getTitle());
                startActivity(intent);
        }
    }



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


                        SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_CHECK", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(itemposition,1);
                        editor.commit();
                        Toast.makeText(this,"YourImage is Unlocked",Toast.LENGTH_SHORT).show();


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

    public void createDialog()

    {
        Notify = new AlertDialog.Builder(this);
        Notify.setTitle("BUY FIRST ");
        Notify.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        PaymentRequest.PaymentRequestBuilder builder = new PaymentRequest.PaymentRequestBuilder();
                        builder.setService(SERVICE_ID, APP_SECRET);
                        builder.setDisplayString("News");
                        builder.setProductName("news");  // non-consumable purchases are restored using this value
                        builder.setConsumable(true);     // non-consumable items can be later restored
                        builder.setIcon(R.drawable.ic_launcher);
                        PaymentRequest pr = builder.build();
                        makePayment(pr);

                    }

                });
        Notify.show();
    }



}