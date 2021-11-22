package com.example.mydiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//메모 기록하는 페이지 activity
public class MemoActivity extends AppCompatActivity {
    private ArrayList<MemoItem> mMemoItems;
    private DBHelper mDBHelper;
    private MemoAdapter memoAdapter;
    private EditText edt_title, edt_memo;
    private Button bt_save_memo,bt_update_memo, bt_delete_memo;
    private Context mContext;
    private String currentDateTime; // 현재 날짜와 시간
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_edit);

        setInit();

    }
    private void setInit(){
        mDBHelper = new DBHelper(this);
        mMemoItems = new ArrayList<>();
        edt_title = findViewById(R.id.edt_title);
        edt_memo = findViewById(R.id.edt_memo);
        bt_save_memo = findViewById(R.id.bt_memo_save);
        bt_update_memo = findViewById(R.id.bt_memo_update);
        bt_delete_memo = findViewById(R.id.bt_memo_delete);
        currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        loadRecentMDB(); // 이전에 저장됨 값을 조회함

        // 저장 버튼 클릭시 이벤트 처리
        bt_save_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //insert database
                mDBHelper.insertMemo(edt_title.getText().toString(), edt_memo.getText().toString(),currentDateTime);
                //insert UI
                MemoItem mItem = new MemoItem();
                mItem.setMTitle(edt_title.getText().toString());
                mItem.setMContent(edt_memo.getText().toString());
                mItem.setMWriteDateTime(currentDateTime);

                memoAdapter.addMemoItem(mItem);//arraylis에 아이템 추가
                Toast.makeText(MemoActivity.this,"저장이 완료됨",Toast.LENGTH_SHORT).show();
                finish();// 원래 페이지로 되돌아감

            }
        });

        //업테이트 버튼 클릭시 이벤트 처리
        bt_update_memo.setOnClickListener(new View.OnClickListener() {

            Intent mIntent = getIntent();//어뎁테 액티비티에서 값 가져오기
            String mBeforeDateTime = mIntent.getStringExtra("beforeDateTime");//클릭한 아이텐의 원래 저장되어있던 날짜와 시간을 넘겨받음

            @Override
            public void onClick(View view) {
                //update database
                mDBHelper.updateMemo(edt_title.getText().toString(),edt_memo.getText().toString(),currentDateTime,mBeforeDateTime);//클릭한 아이템의 원래 저장되어있던 날짜와 시간을 비교하여 업데이트
                //update UI
                MemoItem mItem = new MemoItem();
                mItem.setMTitle(edt_title.getText().toString());
                mItem.setMContent(edt_memo.getText().toString());
                mItem.setMWriteDateTime(currentDateTime);

                memoAdapter.addMemoItem(mItem);//arraylist에 값 수정하여 추가
                Toast.makeText(MemoActivity.this, "수정이 완료됨", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // 삭제 버튼 클릭시 이벤트 처리
        bt_delete_memo.setOnClickListener(new View.OnClickListener() {
            Intent mIntent = getIntent();
            String mBeforeDateTime = mIntent.getStringExtra("beforeDateTime");
            int curPos = mIntent.getIntExtra("curPos",-1); //클릭한 아이템의 포지션을 넘겨받음
            @Override
            public void onClick(View view) {
                //delete database
                mDBHelper.deleteMemo(mBeforeDateTime);
                //delete UI
                mMemoItems.remove(curPos);//arraylist에 아이템 삭제
                Toast.makeText(MemoActivity.this,"삭제가 완료됨",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public void loadRecentMDB(){
        if(mMemoItems != null){ //arraylist가 비어있지 않으면
            Intent mIntent = getIntent();//어뎁터에서 값을 받아옴
            edt_title.setText(mIntent.getStringExtra("title")); //클릭한 아이템의 제목을 넘겨받음
            edt_memo.setText(mIntent.getStringExtra("content")); //클릭한 아이템의 내용을 넘겨받음

        }


    }


}

