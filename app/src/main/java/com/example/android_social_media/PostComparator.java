package com.example.android_social_media;

import com.example.android_social_media.model.post;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class PostComparator implements Comparator<post> {
    @Override
    public int compare(post o1, post o2) {


        String pattern = "yyyy-MM-dd"; // Mẫu định dạng của chuỗi ngày tháng
        if(o2.getDatetime() !=null&&o1.getDatetime() !=null&&!o2.getDatetime().isEmpty()&&!o1.getDatetime().isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                Date date = dateFormat.parse(o2.getDatetime());
                Date date1 = dateFormat.parse(o1.getDatetime());
                System.out.println(date);
                return date.compareTo(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    return 0;
    }
}
