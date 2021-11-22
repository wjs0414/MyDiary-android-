package com.example.mydiary;
// 버튼 클릭하면 화면 넘어가는 기능
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private ImageButton goToDiary;// 다이어리 페이지로 넘어가는 이미지 버튼
    private ImageButton goToTodo; // 투두 리스트 페이지로 넘어가는 이미지 버튼
    private ImageButton goToMemo; // 메모장 페이지로 넘어가는 이미지 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MyDiary");

        goToDiary = (ImageButton) findViewById(R.id.bt_diary);
        goToTodo = (ImageButton) findViewById(R.id.bt_Todo);
        goToMemo = (ImageButton) findViewById(R.id.bt_memo);

        // 다이어리 페이지로 넘어가기
        goToDiary.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),DiaryActivity.class);
                startActivity(intent);
            }
        });

        // 투두리스트 페이지로 넘어가기
        goToTodo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TodoActivity.class);
                startActivity(intent);
            }
        });

        // 메모장 페이지로 넘어가기
        goToMemo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MemoMainActivity.class);
                startActivity(intent);
            }
        });

    }
}