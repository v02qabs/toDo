package com.hiro.to.Do.CalendarWithtodo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;

public class CalendarWithTodo extends JFrame {
    private static final int DAYS_IN_WEEK = 7;
    private static final String[] WEEK_DAYS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private JPanel calendarPanel;
    private JTextArea todoArea;
    private HashMap<String, String> todoMap;  // 日付に対するToDoリスト

    public CalendarWithTodo() {
        setTitle("Calendar with ToDo List");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        todoMap = new HashMap<>();

        // カレンダーを表示するパネル
        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(0, DAYS_IN_WEEK));
        updateCalendar();

        // ToDoリストを表示するエリア
        todoArea = new JTextArea();
        todoArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(todoArea);
        
        // クリックされた日付を表示するラベル
        JLabel selectedDateLabel = new JLabel("Selected Date: None", JLabel.CENTER);
        
        // 日付をクリックしたときの処理
        calendarPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // クリックされた座標から日付を計算
                int x = e.getX() / 50;
                int y = e.getY() / 50;
                
                int day = (y * DAYS_IN_WEEK + x) - (getFirstDayOfMonth() - 1);
                
                if (day > 0 && day <= getDaysInMonth()) {
                    String date = getMonth() + " " + day;
                    selectedDateLabel.setText("Selected Date: " + date);
                    
                    // ToDoリスト表示
                    String todoList = todoMap.getOrDefault(date, "");
                    todoArea.setText(todoList);
                }
            }
        });

        // フレームにコンポーネントを追加
        add(selectedDateLabel, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void updateCalendar() {
        // カレンダーをクリア
        calendarPanel.removeAll();

        // 曜日を表示
        for (String day : WEEK_DAYS) {
            calendarPanel.add(new JLabel(day, JLabel.CENTER));
        }

        // 月の最初の日の曜日
        int firstDayOfMonth = getFirstDayOfMonth();

        // 空白を表示
        for (int i = 1; i < firstDayOfMonth; i++) {
            calendarPanel.add(new JLabel("", JLabel.CENTER));
        }

	// 日付を表示
	for (int i = 1; i <= getDaysInMonth(); i++) {
		int day = i;  // Create a final variable to hold the value of 'i'
		JButton dayButton = new JButton(String.valueOf(i));
		dayButton.setBackground(Color.WHITE);
		dayButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String selectedDate = getMonth() + " " + day; // Use the final 'day' variable
			String todoList = todoMap.getOrDefault(selectedDate, "");
			todoArea.setText(todoList);
		}
		});
		calendarPanel.add(dayButton);
	}


        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private String getMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
    }

    private int getDaysInMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private int getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalendarWithTodo frame = new CalendarWithTodo();
            frame.setVisible(true);
        });
    }
}

