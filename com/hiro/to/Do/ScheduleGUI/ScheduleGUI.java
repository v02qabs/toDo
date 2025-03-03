package com.hiro.to.Do.ScheduleGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ScheduleGUI extends JFrame {
    private static final String[] TIME_SLOTS = {
        "09:00", "10:00", "11:00", "12:00", "13:00", 
        "14:00", "15:00", "16:00", "17:00", "18:00"
    };

    private final Map<String, String> schedule = new HashMap<>();
    private final JComboBox<String> timeComboBox = new JComboBox<>(TIME_SLOTS);
    private final JTextField eventTextField = new JTextField(15);
    private final JTextArea scheduleTextArea = new JTextArea(10, 30);

    public ScheduleGUI() {
		
        setTitle("スケジュール管理");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLayout(new FlowLayout());

        // 予定入力エリア
        add(new JLabel("時間:"));
        add(timeComboBox);
        add(new JLabel("予定:"));
        add(eventTextField);

        JButton addButton = new JButton("追加");
        add(addButton);

        JButton showButton = new JButton("予定を表示");
        add(showButton);

        scheduleTextArea.setEditable(false);
        add(new JScrollPane(scheduleTextArea));

        // イベント処理
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSchedule();
            }
        });

        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displaySchedule();
            }
        });

        setVisible(true);
    }

    /** 予定を追加 */
    private void addSchedule() {
        String time = (String) timeComboBox.getSelectedItem();
        String event = eventTextField.getText().trim();

        if (!event.isEmpty()) {
            schedule.put(time, event);
            JOptionPane.showMessageDialog(this, time + " に「" + event + "」を追加しました！");
            eventTextField.setText(""); // 入力欄をクリア
        } else {
            JOptionPane.showMessageDialog(this, "予定を入力してください！", "エラー", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** 予定を表示 */
    private void displaySchedule() {
        StringBuilder sb = new StringBuilder("=== 予定一覧 ===\n");
        for (String time : TIME_SLOTS) {
            String event = schedule.getOrDefault(time, "（予定なし）");
            sb.append(time).append(" : ").append(event).append("\n");
        }
        scheduleTextArea.setText(sb.toString());
    }
    
    /*public static void main(String[] args) {
        
    }*/
}
