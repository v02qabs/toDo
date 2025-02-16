package com.hiro.to.Do;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainActivity implements ActionListener, WindowListener {
    private DefaultListModel<String> model;
    private JList<String> list;
    private JFrame frame_add_apo;
    private JTextArea area_apo;
    private String area_get_text_apo;

    public static void main(String[] args) {
        System.out.println("Hello World.");
        new MainActivity().init();
    }

    public MainActivity() {
        model = new DefaultListModel<>();
    }

    private void init() {
        JFrame frame = new JFrame("Hello To-Do List");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLayout(new BorderLayout());
        frame.addWindowListener(this);

        // Initialize List and Model
        model.addElement("2024-02-16 火曜日、９時出社");
        list = new JList<>(model);
        JScrollPane scrollPane = new JScrollPane(list);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton add_button = new JButton("予定を追加");
        add_button.addActionListener(this);
        buttonPanel.add(add_button);

        JButton remove_button = new JButton("予定を削除");
        remove_button.addActionListener(this);
        buttonPanel.add(remove_button);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    @Override
    public void windowActivated(WindowEvent e) {
       	System.out.println("activated");
       // new Open_Apo(model); // Ensure model is initialized before this call
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("予定を追加")) {
            add_apo();
        } else if (command.equals("追加")) {
            if (frame_add_apo != null) {
                frame_add_apo.setVisible(false);
            }
            area_get_text_apo = area_apo.getText().trim();
            if (!area_get_text_apo.isEmpty()) {
                model.addElement(area_get_text_apo);
                new Write_Apo(area_get_text_apo);
            }
        } else if (command.equals("予定を削除")) {
            int index = list.getSelectedIndex();
            if (index >= 0) {
                model.remove(index);
            } else {
                JOptionPane.showMessageDialog(null, "削除する予定を選択してください。", "エラー", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void add_apo() {
        frame_add_apo = new JFrame("予定を追加");
        frame_add_apo.setSize(300, 200);
        frame_add_apo.setLayout(new FlowLayout());

        frame_add_apo.add(new JLabel("予定："));
        area_apo = new JTextArea(5, 20);
        frame_add_apo.add(new JScrollPane(area_apo));

        JButton add_apo_button = new JButton("追加");
        add_apo_button.addActionListener(this);
        frame_add_apo.add(add_apo_button);

        frame_add_apo.setVisible(true);
    }

    // Empty implementations for WindowListener
    @Override public void windowOpened(WindowEvent e) {
    	 new Open_Apo(model); 
    
    }
    @Override public void windowClosing(WindowEvent e) {}
    @Override public void windowClosed(WindowEvent e) {}
    @Override public void windowIconified(WindowEvent e) {}
    @Override public void windowDeiconified(WindowEvent e) {}
    @Override public void windowDeactivated(WindowEvent e) {}
}

