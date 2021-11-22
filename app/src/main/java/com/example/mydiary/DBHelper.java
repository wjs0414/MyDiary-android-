package com.example.mydiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MyDiary.db"; // database 이름 정의
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // database 생성될 때 호출
        //Table 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS DiaryList (idDiary INTEGER PRIMARY KEY AUTOINCREMENT , dContent TEXT , dWriteDATE TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS TodoList (idTodo INTEGER PRIMARY KEY AUTOINCREMENT , tContent TEXT , tWriteDate TEXT NOT NULL, isChecked BOOLEAN NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS MemoList (idMemo INTEGER PRIMARY KEY AUTOINCREMENT ,mTitle TEXT NOT NULL, mContent TEXT NOT NULL, mWriteDateTime TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public String selectDiaryContent(String _dWriteDate){
        String dContent = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiaryList  WHERE dWriteDate = '" +_dWriteDate+"' ORDER BY dWriteDate DESC",null);
        if(cursor.getCount()!=0){
            while(cursor.moveToNext()){
                dContent = cursor.getString(cursor.getColumnIndexOrThrow("dContent"));
            }
        }
        cursor.close();
        return dContent;
    }

    //각 테이블 마다 select 문(데이터 베이스 값 조회,출력)
    public ArrayList<DiaryItem> selectDiaryList(String _dWriteDate){
        ArrayList<DiaryItem> diaryItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DiaryList  WHERE dWriteDate = '" +_dWriteDate+"' ORDER BY dWriteDate DESC",null); // 달력에서 클릭한 년/월/일이 모두 포함된 row의 내용 select
        if(cursor.getCount() !=0){
            while(cursor.moveToNext()){
                //실제 값 가져오기
                int idDiary = cursor.getInt(cursor.getColumnIndexOrThrow("idDiary"));
                String dContent = cursor.getString(cursor.getColumnIndexOrThrow("dContent"));
                String dWriteDate = cursor.getString(cursor.getColumnIndexOrThrow("dWriteDate"));
                //DiaryItem 클래스를 이용해 가져온 값들을 초기화 시킴
                DiaryItem diaryItem = new DiaryItem();
                diaryItem.setIdDiary(idDiary);
                diaryItem.setDContent(dContent);
                diaryItem.setDWriteDate(dWriteDate);
                // arraylist diaryItems에 diaryItem 추가
                diaryItems.add(diaryItem);
            }
        }
        cursor.close();
        return diaryItems;
    }

    public ArrayList<TodoItem> selectTodoList(String _tWriteDate){
        ArrayList<TodoItem> todoItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TodoList WHERE tWriteDate = '"+_tWriteDate+"' ORDER BY tWriteDate DESC",null); // 달력에서 클릭한 년/월/일이 모두 포함된 row의 내용 select
        if(cursor.getCount() !=0){
            while(cursor.moveToNext()){
                int idTodo = cursor.getInt(cursor.getColumnIndexOrThrow("idTodo"));
                String tContent = cursor.getString(cursor.getColumnIndexOrThrow("tContent"));
                String tWriteDate = cursor.getString(cursor.getColumnIndexOrThrow("tWriteDate"));
                boolean isChecked = cursor.getInt(cursor.getColumnIndexOrThrow("isChecked"))>-1;
                TodoItem todoItem = new TodoItem();
                todoItem.setIdTodo(idTodo);
                todoItem.setTContent(tContent);
                todoItem.setTWriteDate(tWriteDate);
                todoItem.setSelected(isChecked);
                todoItems.add(todoItem);


            }
        }
        cursor.close();
        return todoItems;
    }


    public ArrayList<MemoItem> selectMemoList(){
        ArrayList<MemoItem> memoItems = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MemoList ORDER BY mWriteDateTime DESC",null);
        if(cursor.getCount() !=0){
            while(cursor.moveToNext()){
                int idMemo = cursor.getInt(cursor.getColumnIndexOrThrow("idMemo"));
                String mTitle = cursor.getString(cursor.getColumnIndexOrThrow("mTitle"));
                String mContent = cursor.getString(cursor.getColumnIndexOrThrow("mContent"));
                String mWriteDateTime = cursor.getString(cursor.getColumnIndexOrThrow("mWriteDateTime"));

                MemoItem memoItem = new MemoItem();
                memoItem.setIdMemo(idMemo);
                memoItem.setMContent(mContent);
                memoItem.setMTitle(mTitle);
                memoItem.setMWriteDateTime(mWriteDateTime);

                memoItems.add(memoItem);
            }
        }
        cursor.close();
        return memoItems;
    }


    // 각 테이블 마다 insert 문(데이터 베이스에 값 넣기)
    public void insertDiary(String _dContent, String _dWriteDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DiaryList(dContent,dWriteDate) VALUES('"+_dContent+"','"+_dWriteDate+"');");
    }
    public void insertTodo(String _tContent, String _tWriteDate, boolean _isChecked){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO TodoList(tContent,tWriteDate,isChecked) VALUES('"+_tContent+"','"+_tWriteDate+"','"+_isChecked+"');");
    }
    public void insertMemo(String _mTitle, String _mContent, String _mWriteDateTime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO MemoList(mTitle,mContent,mWriteDateTime) VALUES('"+_mTitle+"','"+_mContent+"','"+_mWriteDateTime+"');");
    }

    //각 테이블 마다 Update 문( 데이터 베이스 값 수정)
    //각 content의 시 분 초를 이용하여 값 수정
    public void updateDiary(String _dContent, String _dWriteDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DiaryList SET dContent='"+_dContent+"',dWriteDate= '"+_dWriteDate+"'  WHERE dWriteDate ='" +_dWriteDate+"'");

    }
    public void updateTodo(String _tContent, String _tWriteDate, int _idTodo){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET tContent='"+_tContent+"',tWriteDate= '"+_tWriteDate+"'  WHERE idTodo ='" +_idTodo+"'");

    }
    public void updateTodoChecked(boolean _isChecked, int _idTodo){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE TodoList SET isChecked='"+_isChecked+"'  WHERE idTodo ='" +_idTodo+"'");
    }

    public void updateMemo(String _mTitle, String _mContent, String _mWriteDateTime, String _mBeforeDateTime){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE MemoList SET mTitle='"+_mTitle+"',mContent='"+_mContent+"',mWriteDateTime= '"+_mWriteDateTime+"'  WHERE mWriteDateTime ='" +_mBeforeDateTime+"'");

    }

    //각 테이블 마다 Delete 문(데이터 베이스 값 삭제)
    //각 content의 시 분 초를 이용하여 값 삭제
    public void deleteDiary(String _dWriteDate){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DiaryList WHERE dWriteDate = '"+_dWriteDate+"'");

    }
    public void deleteTodo(int _idTodo){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM TodoList WHERE idTodo = '"+_idTodo+"'");

    }
    public void deleteMemo(String _mBeforeDateTime) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM MemoList WHERE mWriteDateTime = '" + _mBeforeDateTime + "'");

    }
}
