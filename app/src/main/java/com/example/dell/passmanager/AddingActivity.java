package com.example.dell.passmanager;

import model.StringEncrypter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddingActivity extends Activity{
    Button btndone;
    EditText edtdomain;
    EditText edtuser;
    EditText edtpassword;
    String domain, username, pass;
    static TextView tview;
    static TextView tview2;
    static TextView tview3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);

        //identify the components
        btndone = (Button) findViewById(R.id.buttondone);
        edtdomain = (EditText) findViewById(R.id.editText1);
        edtuser = (EditText) findViewById(R.id.editText2);
        edtpassword = (EditText) findViewById(R.id.editText3);
        tview = (TextView) findViewById(R.id.textView1);
        tview2 = (TextView) findViewById(R.id.textView2);
        tview3 = (TextView) findViewById(R.id.textView3);
        AddingActivity.tview.setTextColor(Color.parseColor("#3366CC"));
        AddingActivity.tview2.setTextColor(Color.parseColor("#3366CC"));
        AddingActivity.tview3.setTextColor(Color.parseColor("#3366CC"));
        btndone.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String encryptedpass=null;
                domain = edtdomain.getText().toString();
                username = edtuser.getText().toString();
                pass = edtpassword.getText().toString();
                try {
                    encryptedpass = StringEncrypter.encrypt("passwd", pass);
                    Log.d("SimpleCrypto", "Password encrypted");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if(domain.matches("") || username.matches("") || pass.matches(""))
                {
                    Context context = getApplicationContext();
                    CharSequence text = "One or more of the fields are empty!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
                else{
                    // store in variables the text from fields

                    //going to the list activity
                    Intent intent = new Intent(AddingActivity.this, AccountsList.class);
                    intent.putExtra("domain", domain);
                    intent.putExtra("user", username);
                    intent.putExtra("password", encryptedpass);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.adding, menu);
        return true;
    }

}
