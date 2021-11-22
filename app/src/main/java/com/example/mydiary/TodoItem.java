package com.example.mydiary;

import android.widget.CheckBox;

public class TodoItem {
    private int idTodo;
    private String tContent;
    private String tWriteDate;
    private  boolean isChecked;


    public TodoItem(){

    }

    public int getIdTodo() {
        return idTodo;
    }

    public void setIdTodo(int idTodo) {
        this.idTodo = idTodo;
    }

    public String getTContent() {
        return tContent;
    }

    public void setTContent(String tContent) {
        this.tContent = tContent;
    }

    public String getTWriteDate() {
        return tWriteDate;
    }

    public void setTWriteDate(String tWriteDateTime) {
        this.tWriteDate = tWriteDateTime;
    }

    public boolean getSelected(){
        return isChecked;
    }

    public void setSelected(boolean isSelected){
        this.isChecked = isSelected;
    }



}
