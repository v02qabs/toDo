import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import java.io.*;

class MainActivity extends JFrame {
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private Calendar calendar;

    public static void main(String[] args) {
        new MainActivity();
    }
    private String getSelectedDate(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/");
        return sdf.format(calendar.getTime()) + day + "";
    }
    //private String time;
    public String[] time = {"00:00", "01:00", "02:00","03:00","04:00","05:00","06:00", "07:00", "08:00", "09:00","10:00","11:00","12:00","13:00","14:00", "15:00", "16:00","17:00", "18:00", "19:00", "20:00", "21:00","22:00","23:00"};
    public MainActivity() {
        setLayout(new BorderLayout());
        setBounds(100, 100, 400, 400);
        setTitle("スケジュール帳");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        calendar = Calendar.getInstance();
        
        // 上部パネル
        JPanel topPanel = new JPanel();
        JButton prevButton = new JButton("前月");
        JButton nextButton = new JButton("次月");
        monthLabel = new JLabel(getCurrentMonth(), JLabel.CENTER);
        
        prevButton.addActionListener(e -> changeMonth(-1));
        nextButton.addActionListener(e -> changeMonth(1));
	  
        topPanel.add(prevButton);
        topPanel.add(monthLabel);
        topPanel.add(nextButton);

        add(topPanel, BorderLayout.NORTH);

        // カレンダー表示パネル
        calendarPanel = new JPanel(new GridLayout(0, 7));
        updateCalendar();
        add(calendarPanel, BorderLayout.CENTER);
	  addWindowListener(new WindowAdapter() {
            @Override
           public void windowActivated(WindowEvent e) {
                openTodoFile();
            }
        });

        setVisible(true);
    }

    private String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(calendar.getTime()).toString();
    }

    private void changeMonth(int delta) {
        calendar.add(Calendar.MONTH, delta);
        monthLabel.setText(getCurrentMonth());
        updateCalendar();
    }
    public void openning(){
    	try{
    		BufferedReader br = new BufferedReader(new FileReader(new File("todo.csv")));
    		String line;
    		while((line = br.readLine()) != null){
    			//System.out.println(line);
    			String array_comma[]= line.split(",");
    		}
    		
    		    		
    	}
    	catch(Exception error){}
   } 	
    public void openTodoFile(){
	System.out.println("ファイルを開きます。");
	if(new File("todo.csv").exists()){
		System.out.println("ファイル [ todo.csv ] は存在します。");
		openning();
	}
	else{
		System.out.println("ファイル [ todo.csv ] は不在です。");
	}
	}

    private void updateCalendar() {
        calendarPanel.removeAll();
        String[] weekDays = {"日", "月", "火", "水", "木", "金", "土"};
        
        for (String day : weekDays) {
            calendarPanel.add(new JLabel(day, JLabel.CENTER));
        }

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < firstDayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }

        for (int day = 1; day <= daysInMonth; day++) {
        	final int today = day;
        
            JButton dayButton = new JButton(String.valueOf(today));
            dayButton.addActionListener(e -> showAppointments(today));
            calendarPanel.add(dayButton);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
	public JTextArea area;
	JComboBox<String> comboBox;
    private void showAppointments(int day) {
        JOptionPane.showMessageDialog(this, "選択した日付：" + day, "予定", JOptionPane.INFORMATION_MESSAGE);
       	   JFrame edit_schedule = new JFrame("day: " + day);
       	   edit_schedule.setLayout(null);
      	   	    area = new JTextArea();
       	   	    area.setBounds(0,0,300,400);
       	   	    	edit_schedule.add(area);
       	   	    edit_schedule.setBounds(0,0,500,500);
       	   	    	JButton save_button = new JButton("save");
       	   	    	save_button.setBounds(0,410,100,30);
       	   	    	edit_schedule.add(save_button);
       	   	    	 comboBox = new JComboBox<>(time);
			        comboBox.setBounds(400,0,100,30);	
			        edit_schedule.add(comboBox);

     	   	    	save_button.addActionListener(e -> save_schedule(day));
       	  edit_schedule.setVisible(true);
    }
        public String getCurrentYearMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/DD");
        return sdf.format(calendar.getTime());
    }
   // String yearMonth;
    public void save_schedule(int day){
    
    	try{
    	
	    	String line;
	    	if(new File("todo.csv").exists()){
	    		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("todo.csv"),true));
	    		 String selectedDate = getSelectedDate(day);
			String selected = (String) comboBox.getSelectedItem();
		    	bw.write(selectedDate + "_" + selected + "," + area.getText().toString() + "\n");
    			bw.close();
    		}
    		else if ( new File("todo.csv").createNewFile()){
    			System.out.println("新しく作りました。");
    			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("todo.csv")));
    			bw.write("date,contents\n");
    			String selectedDate = getSelectedDate(day);
			String selected = (String) comboBox.getSelectedItem();
    			bw.write(selectedDate + "_" + selected + "," + area.getText().toString() + "\n");
    			bw.close();
    		}
    		
    	}
    	catch(Exception error)
	{
	}
	}
}

