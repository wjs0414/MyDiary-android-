package com.example.mydiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

// 1.to do 작성하는 페이지 activity
// 2.날짜별로 그 날에 기록한 다른 내용의 할 일 들을 보여줌
public class TodoActivity extends AppCompatActivity {
    private RecyclerView mRv_todo;
    private ArrayList<TodoItem> mTodoItems;
    private DBHelper mDBHelper;
    private Button mBt_calender2, mBt_add_checklist;
    private TodoAdapter todoAdapter;
    private TextView tv_currentDate;// 보여지는 textview의 현재 날짜
    public String currentDate;// database에서 비교하기위해 사용한 currentdate
    public static Context mContext_todo;


    protected void onCreate(Bundle saveInstanceState) {

        super.onCreate(saveInstanceState);
        setContentView(R.layout.todo);
        setTitle("To-Do List");
        mContext_todo = this;

        setInit();
    }
    private void setInit(){

        mTodoItems = new ArrayList<>();
        mDBHelper = new DBHelper(this);
        mBt_add_checklist = (Button) findViewById(R.id.bt_add_todo);
        mBt_calender2 = (Button) findViewById(R.id.bt_calander_todo);
        mRv_todo = findViewById(R.id.checklist);
        tv_currentDate = findViewById(R.id.currentDate);

        //calander로 날짜를 받아옴
        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        tv_currentDate.setText(String.format("%d/%d/%d",cYear,cMonth+1,cDay));// 현재 날짜를 오늘 날짜로 초기화
        currentDate = tv_currentDate.getText().toString();

        loadRecentTodoDB();//이전에 있던 데이터를 조회

        //캘린더 버튼 클릭시 이벤트 처리
        mBt_calender2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //date picker dialog를 띄움
                DatePickerDialog todoDatePickerDialog = new DatePickerDialog(TodoActivity.this, R.style.DatePickerTheme,new DatePickerDialog.OnDateSetListener(){ // 내가 설정한 테마로 dialog 설정
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                        tv_currentDate.setText(String.format("%d/%d/%d",year,monthOfYear+1,dayOfMonth)); // 날짜 출력하는 텍스트에 datepicker로 선택한 날짜로 출력
                        currentDate = tv_currentDate.getText().toString();// 현재 날자를 선택한 날짜로 초기화
                        loadRecentTodoDB();// 날짜에 맞는 데이터로 다시 불러옴
                    }

                },cYear,cMonth,cDay);
                todoDatePickerDialog.show();
            }
        });
        // 추가 버튼 클릭시 이벤트 처리
        mBt_add_checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(TodoActivity.this, R.style.MyDialogTheme);// 할 일 입력하는 dialog 띄움
                //dialog에 있는 버튼과 edittext 불러옴
                dialog.setContentView(R.layout.checklist_dialog);
                EditText edt_checklist = dialog.findViewById(R.id.edt_checklist);
                Button bt_OK = dialog.findViewById(R.id.bt_ok);
                // dilaog에서 ok버튼 클릭시 발생하는 이벤트 처리
                bt_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //insert database
                        mDBHelper.insertTodo(edt_checklist.getText().toString(),currentDate,false);
                        //insert UI
                        TodoItem tItem = new TodoItem();
                        tItem.setTContent(edt_checklist.getText().toString());
                        tItem.setTWriteDate(currentDate);

                        todoAdapter.addTodoItem(tItem);//arraylist에 추가

                        mRv_todo.smoothScrollToPosition(0); //스크롤 처리
                        dialog.dismiss(); // dialog 끔
                        Toast.makeText(TodoActivity.this,"저장이 완료됨",Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.show();
            }
        });

    }
    private void loadRecentTodoDB(){
        mTodoItems = mDBHelper.selectTodoList(currentDate);// arraylist를 조회함
        if(mTodoItems !=null) { // arraylist가 비어있지 않으면
            todoAdapter = new TodoAdapter(mTodoItems, this);//어댑터 객체 생성하고 arraylist에 저장되어 있는 값들로 초기화시킴
            mRv_todo.setAdapter(todoAdapter); // recyclerview에 adapter 추가
        }
    }
}
