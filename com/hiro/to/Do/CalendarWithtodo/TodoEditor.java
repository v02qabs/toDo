package com.hiro.to.Do.CalendarWithtodo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class TodoEditor extends JDialog {
    private JTextArea todoArea;
     JComboBox<String> timeComboBox;
    private JButton saveButton;
    private String selectedDate;
    private CalendarWithTodo parent;

    public TodoEditor(CalendarWithTodo parent, String selectedDate) {
        super(parent, "Edit ToDo: " + selectedDate, true);
        this.parent = parent;
        this.selectedDate = selectedDate;

        HashMap<String, String> todoMap = parent.getTodoMap();

        setSize(400, 300);
        setLayout(new BorderLayout());

        // 時間選択
        timeComboBox = new JComboBox<>(CalendarWithTodo.HOURS);
        timeComboBox.addActionListener(e -> loadTodoForSelectedTime());

        // ToDoエリア
        todoArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(todoArea);

        // 保存ボタン
        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveTodo());

        // レイアウト設定
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Time:"));
        topPanel.add(timeComboBox);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        // 初期データを表示
        loadTodoForSelectedTime();

        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void loadTodoForSelectedTime() {
        String time = (String) timeComboBox.getSelectedItem();
        if (time == null) return;

        String key = selectedDate + " " + time;
        todoArea.setText(parent.getTodoMap().getOrDefault(key, ""));
    }

    private void saveTodo() {
        String time = (String) timeComboBox.getSelectedItem();
        String key = selectedDate + " " + time;
        parent.getTodoMap().put(key, todoArea.getText());
        parent.saveTodoToFile();
        JOptionPane.showMessageDialog(this, "ToDo saved for " + key);
    }
}
