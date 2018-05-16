package me.morasquad.wicard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import me.morasquad.wicard.models.MyWicard;

/**
 * Created by Sandun Isuru Niraj on 14/05/2018.
 */



public class SqliteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDatabase";

    public static final int DATABASE_VERSION = 1;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE users (email TEXT PRIMARY KEY, address Text, mobileNumber Text, fullName Text)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        onCreate(sqLiteDatabase);
    }

    public boolean saveWiCard(String email, String address, String mobileNumber, String fullName){

        Cursor cursor = getUser(email);

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("fullName", fullName);
        contentValues.put("address", address);
        contentValues.put("mobileNumber", mobileNumber);

        long result;

        if(cursor.getCount() == 0){
            contentValues.put("email", email);
            result = sqLiteDatabase.insert("users", null, contentValues);
        }else {
            result = sqLiteDatabase.update("users", contentValues, "email=?", new String[] { email });
        }

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }


    public Cursor getUser(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM users WHERE email=?";

        return db.rawQuery(sql, new String[]{email});

    }

        public List<MyWicard> getMyWiCard() {

        List<MyWicard> wicards = new ArrayList<MyWicard>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String sql = "SELECT * FROM users";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{
                MyWicard myWicard = new MyWicard();
                myWicard.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
                myWicard.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                myWicard.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                myWicard.setMobileNumber(cursor.getString(cursor.getColumnIndex("mobileNumber")));
                wicards.add(myWicard);
            }while (cursor.moveToNext());
        }

        return wicards;
    }

    public void deleteUser(String email){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete("users", "email=?", new String[] { email });
    }
}
