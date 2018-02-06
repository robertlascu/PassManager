package com.example.dell.passmanager;

import android.os.Bundle;
import java.io.UnsupportedEncodingException;
import model.StringEncrypter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("WorldWriteableFiles")
public class MainActivity extends Activity {

    TextView textview1;
    TextView textview2;
    EditText password;
    Button buttonOK;
    String pw;
    String typed = "";
    String pwd;
    String crypto;
    String seed = "This is my key!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        password = (EditText) findViewById(R.id.editText1);
        textview1 = (TextView) findViewById(R.id.textView1);
        textview2 = (TextView) findViewById(R.id.textView2);
        buttonOK = (Button) findViewById(R.id.buttonok);
        textview1.setTextColor(Color.WHITE);
        textview2.setTextColor(Color.WHITE);

        SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        pw = preferences.getString("login8","").trim();

        if(pw!="")
            textview1.setVisibility(View.VISIBLE);
        else
            textview2.setVisibility(View.VISIBLE);
        Log.d("Pass loaded:", pw);
    }

    public void sendMessage(View view) throws UnsupportedEncodingException
    {
        password = (EditText) findViewById(R.id.editText1);
        typed = password.getText().toString().trim();

        if(pw!=""){
            Log.d("MesajPw", "Parola diferita de null");
            try {
                pwd = StringEncrypter.encrypt(seed, typed);
                Log.d("Encrypted",pwd);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(pwd.equals(pw))
            {
                Intent intent = new Intent(MainActivity.this, AccountsList.class);
                startActivity(intent);
            }
            else {
                Context context = getApplicationContext();
                CharSequence text = "Wrong password! The password is:"+pwd;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
        else
        {
            SharedPreferences sharedPref = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            try {
                crypto = StringEncrypter.encrypt(seed, typed);
                Log.d("Encrypted", crypto);
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.putString("login8", crypto);
            editor.apply();
            Log.d("Pass saved", "Your password has been saved");
            Intent intent = new Intent(MainActivity.this, AccountsList.class);
            startActivity(intent);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}


