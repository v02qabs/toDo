package com.hiro.to.Do;


import javax.swing.*;
import java.awt.event.*;

public class MainActivity implements ActionListener{
	
	public static void main(String[] args)
	{
		System.out.println("Hello World.");
		new MainActivity().init();
	}
	public MainActivity(){
	}
	private	DefaultListModel model;
	private void init(){
		JFrame frame = new JFrame("Hello to do List.");
		frame.setBounds(0,0,300,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JList list = new JList();
		//list.addElement("2029-02-10 09:00 am 出社。");
		model = new DefaultListModel();
		model.addElement("2024-02-16火曜日、９時出社");
		list = new JList(model);
		frame.setLayout(null);
		frame.add(list);
		list.setBounds(0,0,290,150);
		JButton add_button = new JButton("予定を追加。"); //2025-02-16 10:10 am
		add_button.setBounds(0,160,100,30);
		add_button.addActionListener(this);
		frame.add(add_button);
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		String command = e.getActionCommand();
		
		//System.out.println("command: " + command);
		if(command.equals("予定を追加。")){
			System.out.println("予定を追加。");
			add_apo();
		}
		else if(command.equals("追加")){
			System.out.println("追加");
			frame_add_apo.setVisible(false);
			String area_get_text_apo = area_apo.getText().toString();
			model.addElement(area_get_text_apo);
		}
		
	}
	private JTextArea area_apo;
	private JFrame frame_add_apo;
	private void add_apo(){
		System.out.println("add apo");
		frame_add_apo = new JFrame("予定を追加。");
		frame_add_apo.setBounds(100,100,400,400);
		frame_add_apo.setLayout(null);
		JLabel label_apo = new JLabel("予定：");
		label_apo.setBounds(0,0,100,30);
		frame_add_apo.add(label_apo);
		area_apo = new JTextArea();
		area_apo.setBounds(0,31,200,200);
		frame_add_apo.add(area_apo);
		JButton add_apo_abutton = new JButton("追加");
		add_apo_abutton.setBounds(0,31+201, 100,30);
		add_apo_abutton.addActionListener(this);
		frame_add_apo.add(add_apo_abutton);
		frame_add_apo.setVisible(true);
		//frame_add_apo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}

