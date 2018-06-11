package jp.thesaurus.acctmanager.jp.thesaurus.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    private Map<Date, String> activities = new LinkedHashMap<>();
    static final String DB = "acct_manager.db";
    static final int DB_VERSION = 7;
    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS acct (" +
            "  uid text not null PRIMARY KEY" +
            " ,id text not null" +
            " ,password text not null" +
            " ,remarks text"+
            " ,service_index text" +
            " ,service_name text" +
            " ,regist_date datetime" +
            " ,updata_date datetime" +
            " );";
    static final String DROP_TABLE = "drop table acct;";

    public DBHelper(Context c) {
        super(c, DB, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    /****
     * 一覧用全アカウント情報取得
     * @param target キャッシュ用
     * @return lmap
     */
    public List<Map<String, String>> getAllAccountList(Date target) {
        List<Map<String, String>> lmap = new ArrayList<Map<String, String>>();
        if (!activities.containsKey(target)) {
            activities.put(target, target.toString());
            //SQL作成
            String query = "SELECT uid,id,password,service_index FROM acct ORDER BY service_index ASC ,regist_date DESC;";
            //rawQueryメソッドでデータを取得
            SQLiteDatabase db = getReadableDatabase();
            try {
                Cursor cursor = db.rawQuery(query, null);
                boolean isEof = cursor.moveToFirst();
                //String result = cursor.getString(0);
                //Log.d("result", result);
                while (isEof) {
                    Map<String, String> map = new LinkedHashMap<String, String>();
                    map.put("uid", cursor.getString(cursor.getColumnIndex("uid")));
                    map.put("id", cursor.getString(cursor.getColumnIndex("id")));
                    map.put("password", cursor.getString(cursor.getColumnIndex("password")));
                    map.put("service_index", cursor.getString(cursor.getColumnIndex("service_index")));
                    lmap.add(map);
                    isEof = cursor.moveToNext();
                }

            } finally {
                db.close();
            }
        }
        return lmap;
    }

    /****
     * 詳細画面用アカウント情報取得
     * @param target キャッシュ用
     * @param uid 検索用uid
     * @return map
     */
    public Map<String, String> getAccountData(Date target,String uid) {
        Map<String, String> map = new LinkedHashMap<String, String>(){
            {
                put("uid", "");
                put("id", "");
                put("password", "");
                put("service_index", "");
                put("remarks", "");
            }
        };
        if (!activities.containsKey(target)) {
            activities.put(target, target.toString());
            //SQL作成
            String query = "SELECT uid,id,password,service_index,remarks FROM acct Where uid = ?;";
            //rawQueryメソッドでデータを取得
            SQLiteDatabase db = getReadableDatabase();
            try {
                Cursor cursor = db.rawQuery(query, new String[]{uid});
                if(( cursor != null ) && cursor.getCount() > 0 ) {
                    cursor.moveToFirst();
                    map.put("uid", cursor.getString(cursor.getColumnIndex("uid")));
                    map.put("id", cursor.getString(cursor.getColumnIndex("id")));
                    map.put("password", cursor.getString(cursor.getColumnIndex("password")));
                    map.put("service_index", cursor.getString(cursor.getColumnIndex("service_index")));
                    map.put("remarks", cursor.getString(cursor.getColumnIndex("remarks")));
                }
            } finally {
                db.close();
            }
        }
        return map;
    }

    /**
     * idのみ変更の場合column 1
     * passwordのみ変更の場合column 2
     * id,password変更の場合column 3
     * 備考のみの場合column 4
     * 全て変更の場合column 7
     * 8
     * @param db データベースクラス
     * @param uid 更新用uid
     * @param id 更新用アカウントid
     * @param password 更新用パスワード
     * @param remarks 更新用備考
     * @param columnList 更新カラム
     * @return res 更新結果
     * */
    public int updateData(SQLiteDatabase db, String uid, String id,String password,String remarks,String spinnerPosition,List<String> columnList) {
        ContentValues values = new ContentValues();
        for(String s :columnList){
            switch (s) {
                case "id":
                    values.put("id", id);
                    break;
                case "password":
                    values.put("password", password);
                    break;
                case "remarks":
                    values.put("remarks", remarks);
                    break;
                case "service_index":
                    values.put("service_index", spinnerPosition);
                    break;
            }
        }
        String whereClause = "uid = ?";
        String whereArgs[] = new String[1];
        whereArgs[0] = uid;
        int res;
        try {
            res = db.update("acct", values, whereClause, whereArgs);
        } finally {
            db.close();
        }
        return res;
    }

    /**
     * @param db データベースクラス
     * @param id 更新用アカウントid
     * @param password 更新用パスワード
     * @param remarks 更新用備考
     * @return res 登録結果
     * */
    public long insertData(SQLiteDatabase db, String id,String password,String remarks,String serviceIndex,String serviceName) {
        db.beginTransaction();
        long res = 0;
        try {
            final SQLiteStatement statement = db.compileStatement("INSERT INTO acct VALUES (?,?,?,?,?,?, datetime('now', 'localtime'), datetime('now', 'localtime'))");
            try {
                statement.bindString(1, RandGeneratUtil.get());
                statement.bindString(2, id);
                statement.bindString(3, password);
                statement.bindString(4, remarks);
                statement.bindString(5, serviceIndex);
                statement.bindString(6, serviceName);
                res = statement.executeInsert();
            } catch (SQLException e) {
                e.printStackTrace();
                return res;
            } finally {
                statement.close();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return res;
    }
}
