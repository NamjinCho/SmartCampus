package com.example.chaerin.smartcampus.activity;

/**
 * Created by ChaeRin on 2016-07-17.
 * 강의실찾기 내 listview
 */
import java.text.Collator;
import java.util.Comparator;

import android.graphics.drawable.Drawable;

public class ListData {
    /**
     * 리스트 정보를 담고 있을 객체 생성
     */
    // 별 (즐겨찾기 등록 여부 확인)
    public Drawable mStar;
    // 교실
    public String mClass;

    /**
     * 알파벳 이름으로 정렬
     */
    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ListData mListDate_1, ListData mListDate_2) {
            return sCollator.compare(mListDate_1.mClass, mListDate_2.mClass);
        }
    };
}
