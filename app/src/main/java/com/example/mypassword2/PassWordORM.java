package com.example.mypassword2;

import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class PassWordORM {


    final private static String dataBase = "Personal.db";
    final private static String email_user = "email_user";
    final private static String medium = "medium";
    final private static String pw = "pw";
    final private static String checked = "checked";

    final private String createTable = "CREATE TABLE IF NOT EXISTS pws(" + medium  +
            " VARCHAR, " + email_user + " VARCHAR, " + pw +
            " VARCHAR, " + " created_at DATETIME DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (" + medium +
            ", " + email_user + "))";



    final private String createAccessTable = "CREATE TABLE IF NOT EXISTS check_ins(id INTEGER PRIMARY KEY AUTOINCREMENT, checked DATETIME DEFAULT CURRENT_TIMESTAMP, " + medium +
            " VARCHAR, " + email_user + " VARCHAR, FOREIGN KEY(" + medium +
            ", " + email_user + ") REFERENCES pws(" + medium + ", " + email_user + ") ON DELETE CASCADE)";


    final private String updateTable = "UPDATE pws SET " + email_user + "= \"%s\" , " + pw +
            "= \"%s\" WHERE " + medium  + "= \"%s\" and " + email_user + "= \"%s\"";


    private SQLiteDatabase sqLiteDatabase;



    public PassWordORM(Context context){

        SQLiteDatabase.loadLibs(context);
        File databaseFile = new File(dataBase);

        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(context.getFilesDir() + "/" + databaseFile, "test", null);

        sqLiteDatabase.execSQL(createTable);

        sqLiteDatabase.execSQL(createAccessTable);

    }

    public boolean insert(String newMedium, String newEmailUser, String newPw){
        String insertQuery = "INSERT INTO pws(" + medium + ", " +
                email_user + ", " + pw + ") VALUES(\"%s\", \"%s\", \"%s\")";
        try {

            Log.i("insert", newMedium);
            Log.i("insert", newEmailUser);
            Log.i("insert", newPw);
            String query = String.format(insertQuery, newMedium, newEmailUser, newPw);

            Log.i("insert query", query);
            sqLiteDatabase.execSQL(query);

            testData();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public String getPw(String med, String user){
        String query = "SELECT pw FROM pws WHERE " + medium + "= '%s' AND " + email_user + "= '%s'";
        Cursor c = sqLiteDatabase.rawQuery(String.format(query, med, user), null);
        String password = "";
        if(c != null){
            if(c.moveToFirst()){
                String insertIntoAccesstable = "INSERT INTO check_ins(" + medium + ", " + email_user + ") VALUES(\"%s\", \"%s\")";
                sqLiteDatabase.execSQL(String.format(insertIntoAccesstable, med, user));
                password = c.getString(c.getColumnIndex(pw));
            }
        }
        c.close();
        return password;
    }

    public ArrayList<String> allMediums(){
        String query = "SELECT DISTINCT " + medium + " FROM pws";
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> mediums = null;
        if(c != null){
            if(c.moveToFirst()){
                mediums = new ArrayList<>();
                do {
                    mediums.add(c.getString(c.getColumnIndex(medium)));

                } while(c.moveToNext());
            }
        }
        c.close();
        return mediums;
    }

    public ArrayList<String> allUsersForMedium(String med){
        Log.i("AllUser4", med);
        String users = "SELECT " + email_user + " FROM pws WHERE " + medium + "= '%s'";
        Cursor c1 = sqLiteDatabase.rawQuery(String.format(users, med), null);
        ArrayList<String> allUsers = null;
        if(c1 != null){
            if(c1.moveToFirst()){
                allUsers = new ArrayList<>();
                do{
                    String u = c1.getString(c1.getColumnIndex(email_user));
                    allUsers.add(u);
                    Log.i("User", u);
                } while(c1.moveToNext());
            }
        }
        c1.close();
        testData();
        return allUsers;
    }

    public ArrayList<String> allUsageEntries(){
        String usage = "SELECT checked FROM check_ins ORDER BY ID DESC";
        Cursor c = sqLiteDatabase.rawQuery(usage, null);

        if(c != null){
            if(c.moveToFirst()){
                ArrayList<String> allUsage = new ArrayList<>();
                do {
                    allUsage.add(c.getString(c.getColumnIndex("checked")));
                } while(c.moveToNext());
                return allUsage;
            }
        }

        return null;
    }

    public String newestUsage(){
        String newestUsage = "SELECT checked FROM check_ins ORDER BY ID DESC LIMIT 1";
        Cursor c = sqLiteDatabase.rawQuery(newestUsage, null);
        if(c != null){
            if(c.moveToFirst()){

                return c.getString(c.getColumnIndex("checked"));
            }
        }

        return null;
    }


    public boolean deleteEntry(String med, String user){
        String deleteEntry = "DELETE FROM pws WHERE "
                + medium + "=\"%s\" and " + email_user + "= \"%s\"";
        try{
            sqLiteDatabase.execSQL(String.format(deleteEntry, med, user));
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearAccessEntries(){
        try{
            sqLiteDatabase.execSQL("DELETE FROM check_ins");
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean clearAllAccounts(){
        try{
            sqLiteDatabase.execSQL("DELETE FROM pws");
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public HashMap<String, ArrayList<String>> getUsageInfo(){
        String query = "SELECT " + checked + ", " + medium + ", " + email_user + " FROM check_ins";
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        HashMap<String, ArrayList<String>> data = new HashMap<>();
        if(c != null){
            if(c.moveToFirst()){
                do {
                    ArrayList<String> info = new ArrayList<>();
                    info.add(c.getString(c.getColumnIndex(medium)));
                    info.add(c.getString(c.getColumnIndex(email_user)));
                    data.put(c.getString(c.getColumnIndex(checked)), info);
                } while(c.moveToNext());
            }
        }
        c.close();
        return data;
    }

    private void testData(){

        String query = "SELECT * FROM pws";
        Cursor c  = sqLiteDatabase.rawQuery(query, null);
        if(c != null){
            if(c.moveToFirst()){
                do {
                    String m = c.getString(c.getColumnIndex(medium));
                    String user = c.getString(c.getColumnIndex(email_user));
                    String password = c.getString(c.getColumnIndex(pw));

                    System.out.println("\n Medium: " + m + " User: " + user + " Pw: " + password + "\n");

                } while(c.moveToNext());
            }
        }
    }


}
