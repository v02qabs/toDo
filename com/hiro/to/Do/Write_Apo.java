package com.hiro.to.Do;


import javax.swing.*;
import java.awt.event.*;
import java.io.*;

public class Write_Apo{
	public String Apo;

	public Write_Apo(String GetApo2){
		Write_Apo(GetApo2);
	}
	private void Write_Apo(String GetApo){
		System.out.println("G: " + GetApo);
		try{
		System.out.println("G2: " + GetApo);
			this.Apo = GetApo;
			BufferedWriter br = new BufferedWriter(new FileWriter(new File("./toDo.txt"),true));
			br.write(this.Apo + "\n");
			br.close();
		}
		catch(Exception error){
			System.out.println("Writting Error");
		}

	}
}

