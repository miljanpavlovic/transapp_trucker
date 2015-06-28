package com.transapp.trucker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.transapp.trucker.widgets.SignatureView;

import java.io.File;


public class ActivityPrototyping extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototyping);

        final SignatureView signatureView = (SignatureView)findViewById(R.id.signature);
        Button btnSave = (Button)findViewById(R.id.btnSave);
        Button btnClear = (Button)findViewById(R.id.btnClear);
        final ImageView imgTemp = (ImageView)findViewById(R.id.imgTemp);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.setDrawingCacheEnabled(true);
                File file = signatureView.save(getApplicationContext());
                Picasso.with(getApplicationContext()).load(file).centerInside().resize(signatureView.getWidth(), signatureView.getHeight()).into(imgTemp);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clear();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_prototyping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
