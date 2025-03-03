package com.hiro.to.Do.CalendarWithtodo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class ToDoViewer extends JDialog {
    private JTextArea todoArea;
    private JComboBox<String> timeComboBox;
    private String selectedDate;
    private CalendarWithTodo parent;

    public ToDoViewer(CalendarWithTodo parent, String selectedDate) {
        super(parent, "View ToDo: " + selectedDate, true);
        this.parent = parent;
        this.selectedDate = selectedDate;

        HashMap<String, String> todoMap = parent.getTodoMap();

        setSize(400, 300);
        setLayout(new BorderLayout());

        timeComboBox = new JComboBox<>(CalendarWithTodo.HOURS);
        timeComboBox.addActionListener(e -> loadTodoForSelectedTime());

        todoArea = new JTextArea();
        todoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(todoArea);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Time:"));
        topPanel.add(timeComboBox);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadTodoForSelectedTime();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void loadTodoForSelectedTime() {
        String time = (String) timeComboBox.getSelectedItem();
        if (time == null) return;

        String key = selectedDate + " " + time;
        todoArea.setText(parent.getTodoMap().getOrDefault(key, "No ToDo for this time."));
    }
}
