package com.denyago.name.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DetailsActivity extends Activity {

    String title;
    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        title = getIntent().getStringExtra("title");


        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(MainActivity.bm);


        Button BTN_SAVE=(Button)findViewById(R.id.btn_save);
        BTN_SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(extStorageDirectory, title+".PNG");
                FileOutputStream outStream = null;
                try {
                    outStream = new FileOutputStream(file);
                    MainActivity.bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);

                  getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });



    }
}
