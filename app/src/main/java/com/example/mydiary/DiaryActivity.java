package com.example.mydiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

// diary 작성하는 페이지 activity
//1. 캘린더 다이알로그 불러오고 클릭 날짜 받아오는 메서드
//2. save 버튼 누르면 저장
//3. update 버튼 누르면 수정
//4. delete 버튼 누르면 삭제
//5. 캘린더에서 받은 날짜별로 다른 일기장 내용이 나옴

public class DiaryActivity extends AppCompatActivity {
    private ArrayList<DiaryItem> mDiaryItems;// db data array list
    private DBHelper mDBHelper; // database
    private EditText mEdt_diary; // 일기 작성 부분
    private TextView tv_currentDate; // 현재 날짜 표시하는 부분
    private Button mBt_calender1; // 캘린더 다이알로그 버튼
    private Button mBt_save, mBt_update, mBt_delete; // 저장,수정,삭제 버튼
    private String currentDate; // 현재 날짜를 받는 변수




    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);
        setTitle("Diary");

        setInit();
    }

    private void setInit(){

        mDBHelper = new DBHelper(this);
        mDiaryItems = new ArrayList<>();
        mEdt_diary = findViewById(R.id.edt_diary);
        mBt_calender1 = findViewById(R.id.bt_calender_diary);
        mBt_save = findViewById(R.id.bt_save_diary);
        mBt_update = findViewById(R.id.bt_update_diary);
        mBt_delete = findViewById(R.id.bt_delete_diary);
        tv_currentDate = findViewById(R.id.currentdate);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        tv_currentDate.setText(String.format("%d/%d/%d",cYear,cMonth+1,cDay));// 현재 날짜를 오늘 날짜로 초기화
        currentDate = tv_currentDate.getText().toString();
        loadRecentDiaryDB(); // 이전에 있었던 데이터 출력하기

        //calender 버튼 클릭시 이벤트 처리
        mBt_calender1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog diaryDatePickerDialog = new DatePickerDialog(DiaryActivity.this,R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener(){
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        tv_currentDate.setText(String.format("%d/%d/%d",year,monthOfYear+1,dayOfMonth)); // 날짜 출력하는 텍스트에 datepicker로 선택한 날짜로 출력
                        currentDate = tv_currentDate.getText().toString();// 현재 날자를 선택한 날짜로 초기화
                        loadRecentDiaryDB();
                    }

                },cYear,cMonth,cDay);
                diaryDatePickerDialog.show();

            }
        });



        // 저장 버튼 클릭시 이벤트 처리
        mBt_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // data base insert
                mDBHelper.insertDiary(mEdt_diary.getText().toString(),currentDate);

                Toast.makeText(DiaryActivity.this,"저장이 완료됨",Toast.LENGTH_SHORT).show();
            }
        });

        // 수정 버튼 클릭시 이벤트 처리
        mBt_update.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //data base update
                mDBHelper.updateDiary(mEdt_diary.getText().toString(),currentDate);

                Toast.makeText(DiaryActivity.this,"수정이 완료됨",Toast.LENGTH_SHORT).show();
            }
        });

        // 삭제 버튼 클릭시 이벤트 처리
        mBt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //data base delete
                mDBHelper.deleteDiary(currentDate);
                //UI delete
                mEdt_diary.setText(null);
                Toast.makeText(DiaryActivity.this,"삭제가 완료됨",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //데이터 베이스 불러오기
    private void loadRecentDiaryDB(){
        if(mDiaryItems != null){ // database data를 담아 놓은 arraylist가 비어 있지 않으면 -> 과거에 입력한 데이터가 있으면
            String diary = mDBHelper.selectDiaryContent(currentDate); // 문자열 변수 diary에 달력에서 가져온 현재 날짜에 입력된 데이터를 조회
            mEdt_diary.setText(diary); // 출력
        }

    }


}
