package com.example.chaerin.smartcampus.activity;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by ChaeRin on 2016-07-18.
 */
public class Building {
    Bundle favorite;
    Building()
    {
        favorite = new Bundle();
        favorite.putBoolean("가천관",false);
        favorite.putBoolean("예술대학1",false);
        favorite.putBoolean("바이오나노대학",false);
        favorite.putBoolean("공과대학1",false);
        favorite.putBoolean("중앙도서관",false);
        favorite.putBoolean("학군단",false);
        favorite.putBoolean("학생회관",false);
        favorite.putBoolean("예술대학2",false);
        favorite.putBoolean("교육대학원",false);
        favorite.putBoolean("IT대학",false);
        favorite.putBoolean("글로벌센터",false);
        favorite.putBoolean("법과대학",false);
        favorite.putBoolean("비전타워",false);
        favorite.putBoolean("바이오나노연구원",false);
        favorite.putBoolean("공과대학2",false);
        favorite.putBoolean("산학협력관",false);
        favorite.putBoolean("대학원",false);
        favorite.putBoolean("한의과대학",false);
    }
    public boolean isFavorite(String str)
    {
        return favorite.getBoolean(str);
    }
    public void  setFavorite(String str,boolean flag)
    {
        favorite.putBoolean(str,flag);
    }
}
