package com.hiro.to.Do.ScheduleGUI;

import javax.swing.*;
import java.time.*;

class MyDay extends JFrame {
    private String gtime; // 時刻
    private String gday;  // 日付

    private void setMyDay(String my_day) {
        System.out.println("day : " + my_day);
        this.gday = my_day; // return を削除
    }

    private void setMyTime(String my_time) {
        System.out.println("time : " + my_time);
        this.gtime = my_time; // return を削除
    }

    public String GetTime() {
        System.out.println("get time");
        LocalDateTime date = LocalDateTime.now();
        String now = date.toString();

        // "2025-03-03T12:34:56.789" を "T" で分割
        String[] local_now = now.split("T");
        
        // 日付と時刻をセット
        setMyDay(local_now[0]);
        setMyTime(local_now[1]);

        return now;
    }

    public void DayActivity() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // レイアウトを無効化（setBounds を使うため）
        setBounds(0, 0, 600, 600);
        setTitle("MyDay choose.");

        // ここで GetTime() を呼び出し、gtime を取得
        GetTime();

        // gtime の値をセットした後に JLabel を作成
        JLabel label_gtime = new JLabel("時間：" + gtime);
        label_gtime.setBounds(20, 20, 200, 30); // 位置とサイズを設定
        
        JLabel label_gday = new JLabel("日付：" + gday);
        label_gday.setBounds(200+10,20,200,30);

        // ラベルをフレームに追加
        add(label_gtime);
        add(label_gday);
        
        JTextArea area = new JTextArea();
        area.setBounds(10,50,200,200);
        add(area);
        setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("hello MyDay.");
        new MyDay().DayActivity();
    }
}
