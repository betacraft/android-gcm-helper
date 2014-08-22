package com.rc.gcmhelper.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.rc.gcmhelper.GCMListener;
import com.rc.gcmhelper.GCMRegistrar;




public class GCMHelperAppDemoActivity extends ActionBarActivity {
    private static final String TAG = "###GCMHelperAppDemoActivity###";
    private Handler mHandler;
    private Context mContext;
    private TextView mRegIdTextView;
    private EditText mSenderIdEditText;
    private TextView mMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gcmhelper_app_demo);
        mHandler = new Handler();
        mContext = getApplicationContext();
        mRegIdTextView = (TextView)findViewById(R.id.reg_id);
        mSenderIdEditText = (EditText)findViewById(R.id.sender_id_edit_text);
        mMessageView = (TextView)findViewById(R.id.message);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String senderId = mSenderIdEditText.getText().toString();
                final ProgressDialog progressDialog = ProgressDialog.show(GCMHelperAppDemoActivity.this, "Registering",
                        "Please wait", true);
                GCMRegistrar.RegisterIfNotRegistered(GCMHelperAppDemoActivity.this, senderId,
                        new GCMRegistrar.GCMRegistrarListener() {
                            @Override
                            public void registrationDone(final String regId) {
                                Log.d(TAG,"Reg id " + regId);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        mRegIdTextView.setText(regId);
                                    }
                                });
                            }

                            @Override
                            public void registering() {

                            }

                            @Override
                            public void errorWhileRegistering(Throwable exception) {
                                Log.e(TAG,"Error while registering",exception);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        });

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gcmhelper_app_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
