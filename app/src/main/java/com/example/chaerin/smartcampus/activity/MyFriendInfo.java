package com.example.chaerin.smartcampus.activity;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by 남지니 on 2016-08-12.
 */
public class MyFriendInfo {
    public String Name;
    public String No;
    public String Phone;
    public String dept;
    public String room;
    public String Time;
    public String building;
    public boolean favorite = false;
    /**
     * 리스트 정보를 담고 있을 객체 생성
     */
    // 별 (즐겨찾기 등록 여부 확인)
    public Drawable mStar;
    /**
     * 알파벳 이름으로 정렬
     */
    public static final Comparator<MyFriendInfo> ALPHA_COMPARATOR = new Comparator<MyFriendInfo>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(MyFriendInfo mListDate_1, MyFriendInfo mListDate_2) {
            return sCollator.compare(mListDate_1.Name, mListDate_2.Name);
        }
    };
}