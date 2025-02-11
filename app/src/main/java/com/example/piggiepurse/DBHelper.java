package com.example.piggiepurse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table authentication (userid INTEGER primary key, username TEXT, email TEXT, password TEXT)");
        db.execSQL("create Table UserBalance (id INTEGER PRIMARY KEY, userid INTEGER, type TEXT, amount TEXT, category TEXT, date TEXT, time TEXT, description TEXT, timestamp INTEGER, FOREIGN KEY (userid) REFERENCES authentication(userid))");
        db.execSQL("create Table UserBudget (id INTEGER PRIMARY KEY, userid INTEGER, budget TEXT, description TEXT, toggle TEXT, FOREIGN KEY (userid) REFERENCES authentication(userid))");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists authentication;");
        db.execSQL("drop Table if exists UserBalance");
        db.execSQL("drop Table if exists UserBudget");
        db.execSQL("create Table UserBalance (id INTEGER primary key, type TEXT, amount TEXT, category TEXT, date TEXT, time TEXT, description TEXT, timestamp INTEGER)");
        db.execSQL("create Table UserBudget (id INTEGER primary key, budget TEXT, description TEXT, toggle TEXT)");
    }


    //userAccount
    //insert new user
    public Boolean insertUser(String username, String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = MyDB.insert("authentication", null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    //update existing user details
    public Boolean updateUser(String username, String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("password", password);

        //specify WHERE clause to update specific record
        String whereClause = "username = ? AND email = ?";
        String[] whereArgs = new String[]{username, email};

        long result = MyDB.update("authentication", contentValues, whereClause, whereArgs);
        return result != -1;
    }

    //check if username or email already exists in database
    public Boolean checkUser1(String username, String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM authentication WHERE username = ? OR email = ?",
                new String[] {username, email});
        if ((cursor.getCount() > 0))
            return true;
        else
            return false;
    }

    //check if combination of username and email already exists in  database
    public Boolean checkUser2(String username, String email) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM authentication WHERE username = ? AND email = ?",
                new String[] {username, email});
        //if user exists, return true, else return false
        if ((cursor.getCount() > 0))
            return true;
        else
            return false;
    }

    //check if username and password matches
    public Boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM authentication WHERE username = ? AND password = ?", new String[] {username, password});
        if ((cursor.getCount() > 0))
            return true;
        else
            return false;
    }

    //retrieve userid
    public Cursor getUserId(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM authentication WHERE username = ? AND password = ?", new String[]{username, password});
    }

    //retrieve user details
    public Cursor getUserDetails(int userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM authentication WHERE userid = ?", new String[]{String.valueOf(userid)});
    }

    //delete existing user
    public Boolean deleteUser(int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM authentication WHERE userid = ?", new String[]{String.valueOf(userid)});

        //check if record with certain userid exists
        if (cursor.getCount() > 0) {
            //delete record with certain userid from UserBudget
            long budgetResult = db.delete("UserBudget", "userid = ?", new String[]{String.valueOf(userid)});

            //delete record with certain userid from UserBalance
            long balanceResult = db.delete("UserBalance", "userid = ?", new String[]{String.valueOf(userid)});

            //delete record with certain userid from authentication
            long authenticationResult = db.delete("authentication", "userid = ?", new String[]{String.valueOf(userid)});

            if (budgetResult == -1 || balanceResult == -1 || authenticationResult == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //userBalance
    //insert income or expense record
    public Boolean insertBalance (int userid, String type, String amount, String category, String date, String time, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("userid", userid);
        contentValues.put("type", type);
        contentValues.put("amount", amount);
        contentValues.put("category", category);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("description", description);

        //convert date to timestamp
        Date dDate;
        try {
            dDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            dDate = null;
        }
        if (dDate != null) {
            contentValues.put("timestamp", dDate.getTime());
        }

        long result = db.insert("UserBalance", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //delete income or expense record
    public Boolean deleteBalance (int userid, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM UserBalance WHERE userid = ? AND id = ?", new String[]{String.valueOf(userid), String.valueOf(id)});

        //check if record with certain id exists
        if (cursor.getCount() > 0) {
            long result = db.delete("UserBalance", "userid = ? AND id = ?", new String[]{String.valueOf(userid), String.valueOf(id)});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //get all income or expense record ordered by date and time in descending order
    public Cursor getBalanceDataOrderedByDateDesc(int userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM UserBalance WHERE userid = ? ORDER BY date DESC, time DESC, id DESC", new String[]{String.valueOf(userid)});
    }

    ///get all income or expense ordered by date and time in descending order within selected date range
    public Cursor getBalanceDataOrderedByDateDesc(int userid, long startDate, long endDate) {
        String where;
        if (startDate > 0) {
            where = " AND timestamp >= " + startDate;
            if (endDate > 0) {
                where += " AND timestamp < " + endDate;
            }
        } else if (endDate > 0) {
            where = " AND timestamp < " + endDate;
        } else {
            where = "";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM UserBalance WHERE userid = ?" + where + " ORDER BY date DESC, time DESC, id DESC", new String[]{String.valueOf(userid)});
    }


    //userBudget
    //insert or update userBudget
    public Boolean insertOrUpdateBudget(int userid, String budget, String description, String toggle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Cursor cursor = db.rawQuery("SELECT * FROM UserBudget WHERE userid = ?", new String[]{String.valueOf(userid)});
        if (cursor.getCount() > 0) {
            // Entry with the specified id and userid exists, update it
            return updateBudget(userid, budget, description, toggle);
        } else {
            // Entry does not exist, insert a new one
            contentValues.put("userid", userid);
            contentValues.put("budget", budget);
            contentValues.put("description", description);
            contentValues.put("toggle", toggle);

            long result = db.insert("UserBudget", null, contentValues);
            return result != -1;
        }
    }

    //update userBudget based on userid
    public Boolean updateBudget(int userid, String budget, String description, String toggle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("budget", budget);
        contentValues.put("description", description);
        contentValues.put("toggle", toggle);

        //specify WHERE clause to update specific record based on userid
        String whereClause = "userid = ?";
        String[] whereArgs = new String[]{String.valueOf(userid)};

        long result = db.update("UserBudget", contentValues, whereClause, whereArgs);
        return result != -1;
    }

    //retrieve budget data
    public Cursor getBudgetData(int userid) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM UserBudget WHERE userid = ?",  new String[]{String.valueOf(userid)});
    }
}
