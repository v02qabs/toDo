package com.hiro.DiaryGUI;


import java.awt.event.*;
import javax.swing.*;

public class MainActivity extends JFrame implements ActionListener{
    
    private JPanel jpanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainActivity().init());
    }
    public String command;
	public void actionPerformed(ActionEvent e){
		command = e.getActionCommand();
			if(command.equals("write")){
				System.out.println("write");
			}
			
			
	}
	
    private JPanel createMainPanel() {
        jpanel = new JPanel();
        jpanel.setLayout(null); // Set to null so we can use setBounds()
        
        JButton write_button = new JButton("write");
        write_button.setBounds(20, 20, 100, 30);
		write_button.addActionListener(this);
        jpanel.add(write_button);
        return jpanel;
    }

    private void init() {
        System.out.println("Initializing GUI...");
        
        setTitle("ようこそ、DiaryGUIへ。");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centers the window

        jpanel = createMainPanel();
        add(jpanel);

        setVisible(true);
    }
}
