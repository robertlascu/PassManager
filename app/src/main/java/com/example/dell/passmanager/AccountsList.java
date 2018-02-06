package com.example.dell.passmanager;

import java.util.Observable;
import java.util.Observer;
import model.AccList;
import model.WebAccount;
import database.ListDB;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class AccountsList extends Activity implements Observer {
    ListView list;
    Button btndetails;
    Button btnadd;
    Button btndelete;
    Button btnreset;

    //the list from the model package
    AccList model;
    ArrayAdapter<WebAccount> adapter;
    String domain, username, password;
    ListDB dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //UI objects
        list = (ListView) findViewById(R.id.listView1);
        btndetails = (Button) findViewById(R.id.button3);
        btnadd = (Button) findViewById(R.id.button1);
        btndelete = (Button) findViewById(R.id.button2);
        btnreset = (Button) findViewById(R.id.button4);

        //create the model, instantiate
        model = new AccList();

        //link model to view
        model.addObserver(this);

        //create our database helper
        dbHelper = new ListDB(this);
        //link model to database
        model.addObserver(dbHelper);
        //load the model from the database
        model.setList(dbHelper.loadList());

        //adding list header
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.listview_header,list,false);
        list.addHeaderView(header);

        //display the content from the model on the screen
        adapter = new ArrayAdapter<WebAccount>(this,
                android.R.layout.simple_list_item_single_choice,
                model.getList());

        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        btnadd.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //starting the AddingActivity
                Intent i = new Intent(AccountsList.this, AddingActivity.class);
                startActivityForResult(i, 1);

            }
        });

        btndelete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int index = list.getCheckedItemPosition();
                if (index != -1 && index != 0) {
                    model.deleteAccount(index-1);
                    adapter.remove(adapter.getItem(index-1));
                    adapter.notifyDataSetChanged();
                }
            }
        });


        btndetails.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                int index = list.getCheckedItemPosition();
                if (index != -1 && index != 0)
                    model.showData(index-1);
            }
        });

        btnreset.setOnClickListener(new OnClickListener() {
            public void onClick(View v){
                SharedPreferences pref = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit().clear();
                editor.apply();
                Intent i = new Intent(AccountsList.this, MainActivity.class);
                startActivity(i);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //used to get the results from AddingActivity
        Log.d("onActivityResult", "Called onActivityResult function!");
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                domain = data.getStringExtra("domain");
                username = data.getStringExtra("user");
                password = data.getStringExtra("password");
                model.addAccount(new WebAccount(domain, username, password));
            }
        }
    }//onActivityResult

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public void update(Observable observable, Object data) {
        //the model has changed, update view
        Log.d("AccountsList activity", "update called");
        adapter.notifyDataSetChanged();
        list.invalidateViews();
    }

}
