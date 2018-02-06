package model;

import java.util.ArrayList;
import java.util.Observable;
import database.ListDB;
import android.content.Context;

public class AccList extends Observable {

    protected Context context;
    String parolad;

    private ArrayList<WebAccount> accountList;

    public AccList() {
        accountList = new ArrayList<WebAccount>();
    }

    public ArrayList<WebAccount> getList() {
        return accountList;
    }

    public void setList(ArrayList<WebAccount> list) {
        this.accountList = list;
    }

    public void addAccount(WebAccount acc) {
        accountList.add(acc);
        this.setChanged();
        notifyObservers(acc);
    }

    public void showData(int index) {
        String usr = accountList.get(index).getUser();
        String pswd = accountList.get(index).getPassword();
        try {
            parolad = StringEncrypter.decrypt("passwd", pswd);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ListDB.ShowData(usr, parolad);
    }

    public void deleteAccount(int index) {
        String removed = accountList.get(index).getPassword();
        String removed2 = accountList.get(index).getDomain();
        String removed3 = accountList.get(index).getUser();
        ListDB.deleteAccount(removed, removed2, removed3);
    }
}

