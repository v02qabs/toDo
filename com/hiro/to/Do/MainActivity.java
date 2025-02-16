package com.hiro.to.Do;


import javax.swing.*;

public class MainActivity {
	
	public static void main(String[] args)
	{
		System.out.println("Hello World.");
		new MainActivity().init();
	}
	public MainActivity(){
	}
	private void init(){
		JFrame frame = new JFrame("Hello to do List.");
		frame.setBounds(0,0,300,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JList list = new JList();
		//list.addElement("2029-02-10 09:00 am 出社。");
		DefaultListModel model = new DefaultListModel();
		model.addElement("2024-02-16火曜日、９時出社");
		list = new JList(model);
		frame.setLayout(null);
		frame.add(list);
		list.setBounds(0,0,290,290);
		frame.setVisible(true);
	}
	
}

