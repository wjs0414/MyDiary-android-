package com.example.mydiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<TodoItem> mTodoItems;
    private Context mContext;
    private DBHelper mDBHelper;


    public TodoAdapter(ArrayList<TodoItem> mTodoItems, Context mContext){
        this.mTodoItems = mTodoItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext);
    }
    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list,parent,false);
        return new ViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        final TodoItem todoItem = mTodoItems.get(position);
        holder.cb_todo.setText(mTodoItems.get(position).getTContent());
        holder.cb_todo.setOnCheckedChangeListener(null);
        holder.cb_todo.setChecked(todoItem.getSelected());
        //checkbox 상태가 변경될 시 이벤트 처리
        holder.cb_todo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                todoItem.setSelected(isChecked);//arraylist에 현재 상태로 초기화

                if(isChecked==true){
                    //update database
                    mDBHelper.updateTodoChecked(true,todoItem.getIdTodo());
                    Toast.makeText(mContext,"할일이 완료됨",Toast.LENGTH_SHORT).show();
                }
                else {
                    //update database
                    mDBHelper.updateTodoChecked(false, todoItem.getIdTodo());

                    Toast.makeText(mContext, "취소", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mTodoItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb_todo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cb_todo = (CheckBox) itemView.findViewById(R.id.cb_todo);

            //아이템 롱 클릭시 이벤트 처리
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final int curPos = getAdapterPosition();// 클릭한 아이텐의 현재 포지션 받아옴
                    final TodoItem todoItem = mTodoItems.get(curPos);
                    String[] strChoiceItems = {"수정하기","삭제하기"}; // 아이템 롱 클릭시 나타나는 dialog option
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);// alertdialog 불러오기
                    builder.setTitle("원하는 작업을 선택해 주세요");
                    builder.setItems(strChoiceItems, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //update option
                            if(i == 0){
                                //dialog 띄우고 원래 기록되 있던 값 불러와서 초기화시킴
                                final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.checklist_dialog);
                                EditText edt_content = dialog.findViewById(R.id.edt_checklist);
                                Button bt_OK = dialog.findViewById(R.id.bt_ok);
                                edt_content.setText(todoItem.getTContent());
                                edt_content.setSelection(edt_content.getText().length());
                                // ok버튼 클릭시 이벤트 처리
                                bt_OK.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String currentDate = ((TodoActivity)TodoActivity.mContext_todo).currentDate; //TodoActivity의 curruentdate값 가져오기
                                        int idTodo = todoItem.getIdTodo(); // 클릭한 아이템의 id값
                                        //update database
                                        mDBHelper.updateTodo(edt_content.getText().toString(),currentDate,idTodo);// id값과 비교하여 아이템 수정
                                        //update UI
                                        todoItem.setTContent(edt_content.getText().toString());
                                        notifyItemChanged(curPos,todoItem);
                                        dialog.dismiss();
                                        Toast.makeText(mContext,"수정이 완료됨",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show();
                            }
                            //delete option
                            else if(i == 1){
                                int idTodo = todoItem.getIdTodo();
                                //delete database
                                mDBHelper.deleteTodo(idTodo);// id값과 비교하여 아이템 삭제
                                //delete UI
                                mTodoItems.remove(curPos);
                                notifyItemChanged(curPos);
                                Toast.makeText(mContext,"삭제가 완료됨",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.show();
                    return false;
                }
            });

        }
    }
    public void addTodoItem(TodoItem tItem){ 
        mTodoItems.add(0,tItem);
        notifyItemInserted(0);
    }
}
