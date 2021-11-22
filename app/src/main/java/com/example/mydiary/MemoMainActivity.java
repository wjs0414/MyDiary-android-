package com.example.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

// add 버튼을 누르면 메모장 적는 페이지로 넘어가는 기능
public class MemoMainActivity extends AppCompatActivity {
    private RecyclerView mRv_memo; // 메모 리스트를 보여주는 리사이클러뷰
    private Button mBt_add_memo; // 메모 추가하는 버튼
    private DBHelper mDBHelper;
    private ArrayList<MemoItem> mMemoItems;
    private MemoAdapter memoAdapter; // 리스트 어뎁터
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo);

        setInit();
    }
    private void setInit(){
        mDBHelper = new DBHelper(this);
        mRv_memo = findViewById(R.id.rv_memo);
        mBt_add_memo = findViewById(R.id.bt_add_memo);
        mMemoItems = new ArrayList<>();
        loadRecentMemoDB(); // 이전에 데이터가 있으면 불러옴

        // 추가 버튼을 누르면 메모장 적는 페이지로 넘어감
        mBt_add_memo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MemoActivity.class);
                startActivity(intent);

            }
        });
    }

    private void loadRecentMemoDB() {
        mMemoItems = mDBHelper.selectMemoList();//database를 조회하여 arraylist에 초기화
        if(memoAdapter==null){
            memoAdapter = new MemoAdapter(mMemoItems, this);
            mRv_memo.setHasFixedSize(true);
            mRv_memo.setAdapter(memoAdapter);//어뎁터를 이용하여 리사이클러뷰에 아이템 추가
        }
    }
}
