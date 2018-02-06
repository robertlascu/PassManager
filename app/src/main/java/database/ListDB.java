package database;

import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import model.WebAccount;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ListDB extends SQLiteOpenHelper implements Observer {
    public static final String DATABASE_NAME = "accounts_datab";
    public static final int DATABASE_VERSION = 1;

    private static class Table {
        private static final String TAB_NAME = "account_table";
        private static final String COL_ID = "id";
        private static final String COL_DOMAIN = "domain";
        private static final String COL_USER = "username";
        private static final String COL_PASSWORD = "password";
    }
    private final String sql_create = "CREATE TABLE IF NOT EXISTS %s ("
            + "%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT)";

    protected static Context context;
    private static SQLiteDatabase db;

    public ListDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ListDB.context = context;
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creates the database
        db.execSQL(String.format(sql_create, Table.TAB_NAME, Table.COL_ID,
                Table.COL_DOMAIN, Table.COL_USER, Table.COL_PASSWORD));

    }

    public void addAccount(WebAccount account) {
        Log.d("DBHelper", "adding to DB " + account.toString());
        ContentValues args = new ContentValues();
        args.put(Table.COL_DOMAIN, account.getDomain());
        args.put(Table.COL_USER, account.getUser());
        args.put(Table.COL_PASSWORD, account.getPassword());
        db.insert(Table.TAB_NAME, null, args);

    }

    public static void deleteAccount(String password, String domain, String user) {
		db.delete(Table.TAB_NAME, Table.COL_PASSWORD + "="
				+ "'" + password + "'" + " " + "AND" +" " + Table.COL_DOMAIN + "=" + "'" + domain + "'"
				+ " AND " + Table.COL_USER + "=" + "'" + user + "'", null);
		int affectedRows = db.delete(Table.TAB_NAME, Table.COL_PASSWORD + "="
				+ "'" + password + "'", null);
		Log.d("DBHelper", "delete from DB, pass: " + password + " affected rows = "
				+ affectedRows);
    }


    public ArrayList<WebAccount> loadList() {
        ArrayList<WebAccount> AccList = new ArrayList<WebAccount>();
        // interogate the DB
        Cursor cursor = db.query(Table.TAB_NAME, new String[] { Table.COL_ID,
                        Table.COL_DOMAIN, Table.COL_USER, Table.COL_PASSWORD }, null,
                null, null, null, null);
        // iterate cursor and load the data
        while (cursor.moveToNext()) {
            WebAccount account = new WebAccount(cursor.getString(1),
                    cursor.getString(2), cursor.getString(3));
            AccList.add(account);
            Log.d("DBHelper", "Loaded password : " + cursor.getString(3));
        }
        cursor.close();
        return AccList;
    }

    @Override
    public void update(Observable observable, Object data) {
        // the model has changed, save data
        if (data instanceof WebAccount) {
            // add operation
            WebAccount account = (WebAccount) data;
            addAccount(account);
        } else {
            // delete operation
            String removed = (String) data;
            deleteAccount(removed, removed, removed);
        }

    }

    public static void ShowData(String user, String password)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Username:" + user + '\n' + "Password:" + password);
        builder1.setCancelable(true);
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
