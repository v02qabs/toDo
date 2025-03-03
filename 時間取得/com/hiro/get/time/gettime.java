package com.hiro.get.time;

import java.time.*;


public class gettime{
		public static void main(String[] args){
			System.out.println("Hello gettime app.");
			new gettime();
		}
		public gettime(){
			LocalDateTime now = LocalDateTime.now();
			System.out.println("現在時刻：" + now);
		}
}

