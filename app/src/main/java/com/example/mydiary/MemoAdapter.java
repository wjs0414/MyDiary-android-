package com.example.mydiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {
    private ArrayList<MemoItem> mMemoItems;
    private Context mContext;
    private DBHelper mDBHelper;
    private MemoActivity memoActivity;


    public MemoAdapter(ArrayList<MemoItem> mMemoItems, Context mContext){
        this.mMemoItems = mMemoItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.memo_list,parent,false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.ViewHolder holder, int position) {
        holder.tv_title.setText(mMemoItems.get(position).getMTitle());
        holder.tv_writeDate.setText(mMemoItems.get(position).getMWriteDateTime());

    }

    @Override
    public int getItemCount() {
        return mMemoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;//메모 리스트에서 보여지는 제목
        private TextView tv_writeDate;//메모 리스트에서 보여지는 날짜
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            tv_writeDate = itemView.findViewById(R.id.tv_date);

            //list클릭하면 해당 메모 내용 출력 페이지로 넘어감
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int curPos = getAdapterPosition();// 클릭한 리스트의 포지션을 가져옴
                    final MemoItem memoItem = mMemoItems.get(curPos); // 리스트 중 클릭한 아이템을 가져옴
                    String memoTitle = memoItem.getMTitle();
                    String memoContent = memoItem.getMContent();
                    String memoBeforeDateTime = memoItem.getMWriteDateTime();

                    //memoActivity에 클릭한 아이템 값 가져온 것을 보냄
                    Intent intent = new Intent(mContext,MemoActivity.class);
                    intent.putExtra("title",memoTitle);
                    intent.putExtra("content",memoContent);
                    intent.putExtra("beforeDateTime",memoBeforeDateTime);
                    intent.putExtra("curPos",curPos);

                    mContext.startActivity(intent);

                }
            });

        }

    }
    //arraylist에 아이템 추가
    public void addMemoItem(MemoItem mItem){
        mMemoItems.add(0,mItem);
        notifyItemInserted(0);
    }
}
