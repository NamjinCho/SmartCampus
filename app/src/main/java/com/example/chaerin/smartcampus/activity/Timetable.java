package com.example.chaerin.smartcampus.activity;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 남지니 on 2016-07-20.
 */
public class Timetable {
    private static ArrayList<com.example.chaerin.smartcampus.activity.Class> ClassesOfMon=new ArrayList<>();
    private static ArrayList<Class> ClassesOfTue=new ArrayList<>();
    private static ArrayList<Class> ClassesOfWed=new ArrayList<>();
    private static ArrayList<Class> ClassesOfThu=new ArrayList<>();
    private static ArrayList<Class> ClassesOfFri=new ArrayList<>();
    private static ArrayList<AttendanceInfo> AttInfo = new ArrayList<>();
    private static ArrayList<ClassroomInfo> ClassInfo = new ArrayList<>();
    public static HashMap<String,String> mapForConvertFromTimeSlot = new HashMap<>();
    public static ArrayList<Class> getMonAllClasses(){return ClassesOfMon;}
    public static ArrayList<Class> getTueAllClasses(){return ClassesOfTue;}
    public static ArrayList<Class> getWedAllClasses(){return ClassesOfWed;}
    public static ArrayList<Class> getThuAllClasses(){return ClassesOfThu;}
    public static ArrayList<Class> getFriAllClasses(){return ClassesOfFri;}
    public static void putMonClasses(Class input){ClassesOfMon.add(input);}
    public static void putTueClasses(Class input){ClassesOfTue.add(input);}
    public static void putWedClasses(Class input){ClassesOfWed.add(input);}
    public static void putThuClasses(Class input){ClassesOfThu.add(input);}
    public static void putFriClasses(Class input){ClassesOfFri.add(input);}
    public static void putAttinfo(AttendanceInfo input){AttInfo.add(input);}
    public static void putClassinfo(ClassroomInfo input){ClassInfo.add(input);}
    public static ArrayList<AttendanceInfo> getAttinfo(){return AttInfo;}
    public static ArrayList<ClassroomInfo> getClassinfo(){return ClassInfo;}
    public static void initHashMap()
    {
        mapForConvertFromTimeSlot.put("1","09:00/09:50");
        mapForConvertFromTimeSlot.put("2","10:00/10:50");
        mapForConvertFromTimeSlot.put("3","11:00/11:50");
        mapForConvertFromTimeSlot.put("4","12:00/12:50");
        mapForConvertFromTimeSlot.put("5","13:00/13:50");
        mapForConvertFromTimeSlot.put("6","14:00/14:50");
        mapForConvertFromTimeSlot.put("7","15:00/15:50");
        mapForConvertFromTimeSlot.put("8","16:00/16:50");
        mapForConvertFromTimeSlot.put("9","17:30/18:20");
        mapForConvertFromTimeSlot.put("10","18:25/19:15");
        mapForConvertFromTimeSlot.put("11","19:20/20:10");
        mapForConvertFromTimeSlot.put("12","20:15/21:05");
        mapForConvertFromTimeSlot.put("13","21:10/22:00");
        mapForConvertFromTimeSlot.put("14","22:05/22:55");
        mapForConvertFromTimeSlot.put("A","09:30/10:45");
        mapForConvertFromTimeSlot.put("B","11:00/12:15");
        mapForConvertFromTimeSlot.put("C","12:30/13:45");
        mapForConvertFromTimeSlot.put("D","14:00/15:15");
        mapForConvertFromTimeSlot.put("E","15:30/16:45");
    }
    public static void insertData(String timeslot,String Building,String classroom,String title,String prof)
    {
        //String []subTimeSlot = timeslot.split(" ,");
        Log.d("디벅스",timeslot);
        String period = timeslot.substring(3,4);
        Log.d("period","_"+mapForConvertFromTimeSlot.get(period));
        String []subTime = mapForConvertFromTimeSlot.get(period).split("/");
        int StartHour = Integer.parseInt(subTime[0].split(":")[0]);
        int StartMinute = Integer.parseInt(subTime[0].split(":")[1]);
        int EndHour = Integer.parseInt(subTime[1].split(":")[0]);
        int EndMinute = Integer.parseInt(subTime[1].split(":")[1]);
        Class newClass = new Class();
        newClass.startHour=StartHour;
        newClass.startMinute=StartMinute;
        newClass.endHour=EndHour;
        newClass.endMinute=EndMinute;
        newClass.Building=Building;
        newClass.ClassName=title;
        newClass.Room=classroom;
        newClass.Period=period;
        newClass.Day=timeslot.substring(0,3);
        newClass.Profesor=prof;
        if(timeslot.startsWith("Mon"))
        {
            putMonClasses(newClass);
        }
        else if(timeslot.startsWith("Tue"))
        {
            putTueClasses(newClass);
        }
        else if(timeslot.startsWith("Wed"))
        {
            putWedClasses(newClass);
        }
        else if(timeslot.startsWith("Thu"))
        {
            putThuClasses(newClass);
        }
        else if(timeslot.startsWith("Fri"))
        {
            putFriClasses(newClass);
        }

    }
    public static void sort_Class()
    {
        Class[]sort_list = new Class[ClassesOfMon.size()];
        for(int i =0;i<ClassesOfMon.size();i++)
        {
            sort_list[i] = ClassesOfMon.get(i);
        }
        sort_list = list_sort(sort_list);
        for(int i=ClassesOfMon.size()-1;i>=0;i--)
        {
            ClassesOfMon.remove(i);
        }
        for(int i = 0;i<sort_list.length;i++)
            ClassesOfMon.add(sort_list[i]);

        sort_list = new Class[ClassesOfTue.size()];
        for(int i =0;i<ClassesOfTue.size();i++)
        {
            sort_list[i] = ClassesOfTue.get(i);
        }
        sort_list = list_sort(sort_list);
        for(int i=ClassesOfTue.size()-1;i>=0;i--)
        {
            ClassesOfTue.remove(i);
        }
        for(int i = 0;i<sort_list.length;i++)
            ClassesOfTue.add(sort_list[i]);

        sort_list = new Class[ClassesOfWed.size()];
        for(int i =0;i<ClassesOfWed.size();i++)
        {
            sort_list[i] = ClassesOfWed.get(i);
        }
        sort_list = list_sort(sort_list);
        for(int i=ClassesOfWed.size()-1;i>=0;i--)
        {
            ClassesOfWed.remove(i);
        }
        for(int i = 0;i<sort_list.length;i++)
            ClassesOfWed.add(sort_list[i]);

        sort_list = new Class[ClassesOfThu.size()];
        for(int i =0;i<ClassesOfThu.size();i++)
        {
            sort_list[i] = ClassesOfThu.get(i);
        }
        sort_list = list_sort(sort_list);
        for(int i=ClassesOfThu.size()-1;i>=0;i--)
        {
            ClassesOfThu.remove(i);
        }
        for(int i = 0;i<sort_list.length;i++)
            ClassesOfThu.add(sort_list[i]);

        sort_list = new Class[ClassesOfFri.size()];
        for(int i =0;i<ClassesOfFri.size();i++)
        {
            sort_list[i] = ClassesOfFri.get(i);
        }
        sort_list = list_sort(sort_list);
        for(int i=ClassesOfFri.size()-1;i>=0;i--)
        {
            ClassesOfFri.remove(i);
        }
        for(int i = 0;i<sort_list.length;i++)
            ClassesOfFri.add(sort_list[i]);

    }
    private static Class[] list_sort(Class [] sort_list)
    {
        int start1=0;
        int start2=0;
        for(int i=0;i<sort_list.length-1;i++)
        {
            for(int j=0;j<sort_list.length-1;j++)
            {
                start1=sort_list[j].startHour*100 + sort_list[j].startMinute;
                start2=sort_list[j+1].startHour*100 + sort_list[j+1].startMinute;
                if(start1 > start2)
                {
                    Class temp = sort_list[j];
                    sort_list[j]=sort_list[j+1];
                    sort_list[j+1]=temp;
                }
            }
        }
        return sort_list;
    }
}
