package com.example.mydiary;

public class MemoItem {

    private int idMemo;
    private String mTitle;
    private String mContent;
    private String mWriteDateTime;

    public MemoItem(){

    }

    public int getIdMemo() {
        return idMemo;
    }

    public void setIdMemo(int idMemo) {
        this.idMemo = idMemo;
    }

    public String getMTitle() {
        return mTitle;
    }

    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getMContent() {
        return mContent;
    }

    public void setMContent(String mContent) {
        this.mContent = mContent;
    }

    public String getMWriteDateTime() {
        return mWriteDateTime;
    }

    public void setMWriteDateTime(String mWriteDateTime) {
        this.mWriteDateTime = mWriteDateTime;
    }
}
