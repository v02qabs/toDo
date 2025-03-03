package com.hiro.to.Do.CalendarWithtodo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class CalendarWithTodo extends JFrame {
    private static final int DAYS_IN_WEEK = 7;
    private static final String[] WEEK_DAYS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    public static final String[] HOURS = { 
        "00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
        "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
        "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
        "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
    };

    private JPanel calendarPanel;
    private JLabel selectedDateLabel;
    private HashMap<String, String> todoMap;
    private String selectedDate;
    private final String FILE_PATH = "todo.txt";

    private JButton viewerButton;  // Viewer ボタン

    public CalendarWithTodo() {
        setTitle("Calendar with ToDo List");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        todoMap = new HashMap<>();
        loadTodoFromFile();

        calendarPanel = new JPanel(new GridLayout(0, DAYS_IN_WEEK));
        updateCalendar();

        selectedDateLabel = new JLabel("Selected Date: None", JLabel.CENTER);

        // Viewer ボタンの追加
        viewerButton = new JButton("View ToDo");
        viewerButton.setEnabled(false);  // 初期状態では無効
        viewerButton.addActionListener(e -> {
            if (selectedDate != null) {
                new ToDoViewer(this, selectedDate);
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(selectedDateLabel, BorderLayout.CENTER);
        topPanel.add(viewerButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        for (String day : WEEK_DAYS) {
            calendarPanel.add(new JLabel(day, JLabel.CENTER));
        }

        int firstDayOfMonth = getFirstDayOfMonth();
        int daysInMonth = getDaysInMonth();

        for (int i = 1; i < firstDayOfMonth; i++) {
            calendarPanel.add(new JLabel(""));
        }

        for (int i = 1; i <= daysInMonth; i++) {
            JButton dayButton = new JButton(String.valueOf(i));
            dayButton.setBackground(Color.WHITE);
            int day = i;
            dayButton.addActionListener(e -> selectDate(day));
            calendarPanel.add(dayButton);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void selectDate(int day) {
        selectedDate = getMonth() + " " + day;
        selectedDateLabel.setText("Selected Date: " + selectedDate);

        viewerButton.setEnabled(true); // Viewer ボタンを有効化
        new TodoEditor(this, selectedDate);
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

    public void saveTodoToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, String> entry : todoMap.entrySet()) {
                writer.write(entry.getKey() + "###" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTodoFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("###", 2);
                if (parts.length == 2) {
                    todoMap.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getTodoMap() {
        return todoMap;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalendarWithTodo frame = new CalendarWithTodo();
            frame.setVisible(true);
        });
    }
}
