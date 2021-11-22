package com.example.mydiary;

public class DiaryItem {
    private int idDiary;
    private String dContent;
    private String dWriteDate;

    public DiaryItem(){

    }

    public int getIdDiary() {
        return idDiary;
    }

    public void setIdDiary(int idDiary) {
        this.idDiary = idDiary;
    }

    public String getDContent() {
        return dContent;
    }

    public void setDContent(String dContent) {
        this.dContent = dContent;
    }

    public String getDWriteDate() {
        return dWriteDate;
    }

    public void setDWriteDate(String dWriteDate) {
        this.dWriteDate = dWriteDate;
    }
}
