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

    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JButton viewerButton;
    private int selectedYear;
    private int selectedMonth;

    public CalendarWithTodo() {
        setTitle("Calendar with ToDo List");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        todoMap = new HashMap<>();
        loadTodoFromFile();

        // 年と月の選択パネル
        JPanel topPanel = new JPanel();
        yearComboBox = new JComboBox<>();
        for (int year = 2000; year <= 2100; year++) {
            yearComboBox.addItem(year);
        }
        yearComboBox.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));
        selectedYear = (int) yearComboBox.getSelectedItem();

        monthComboBox = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        monthComboBox.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
        selectedMonth = monthComboBox.getSelectedIndex();

        yearComboBox.addActionListener(e -> updateSelectedYearMonth());
        monthComboBox.addActionListener(e -> updateSelectedYearMonth());

        topPanel.add(new JLabel("Year:"));
        topPanel.add(yearComboBox);
        topPanel.add(new JLabel("Month:"));
        topPanel.add(monthComboBox);

        selectedDateLabel = new JLabel("Selected Date: None", JLabel.CENTER);

        viewerButton = new JButton("View ToDo");
        viewerButton.setEnabled(false);
        viewerButton.addActionListener(e -> {
            if (selectedDate != null) {
                new ToDoViewer(this, selectedDate);
            }
        });

        topPanel.add(viewerButton);

        calendarPanel = new JPanel(new GridLayout(0, DAYS_IN_WEEK));
        updateCalendar();

        add(topPanel, BorderLayout.NORTH);
        add(selectedDateLabel, BorderLayout.CENTER);
        add(calendarPanel, BorderLayout.SOUTH);
    }

    private void updateSelectedYearMonth() {
        selectedYear = (int) yearComboBox.getSelectedItem();
        selectedMonth = monthComboBox.getSelectedIndex();
        updateCalendar();
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        for (String day : WEEK_DAYS) {
            calendarPanel.add(new JLabel(day, JLabel.CENTER));
        }

        int firstDayOfMonth = getFirstDayOfMonth(selectedYear, selectedMonth);
        int daysInMonth = getDaysInMonth(selectedYear, selectedMonth);

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
        selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + day;
        selectedDateLabel.setText("Selected Date: " + selectedDate);
        viewerButton.setEnabled(true);
        new TodoEditor(this, selectedDate);
    }

    private int getDaysInMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    private int getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
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
