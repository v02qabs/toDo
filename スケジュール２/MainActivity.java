import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.Set;
import java.util.HashSet;

class MainActivity extends JFrame {
    private JPanel calendarPanel;
    private JLabel monthLabel;
    private Calendar calendar;

    public static void main(String[] args) {
        new MainActivity();
    }
    // getSelectedDate内の修正
private String getSelectedDate(int day) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/");
    return sdf.format(calendar.getTime()) + String.format("%02d", day); // ← ここで2桁に
}
private void showAppointments(int day) {
    String selectedDate = getSelectedDate(day);
    
    JFrame edit_schedule = new JFrame("day: " + day);
    edit_schedule.setLayout(null);
    
    area = new JTextArea();
    area.setBounds(0, 0, 300, 400);
    edit_schedule.add(area);

    comboBox = new JComboBox<>(time);
    comboBox.setBounds(400, 0, 100, 30);
    edit_schedule.add(comboBox);

    JButton save_button = new JButton("save");
    save_button.setBounds(0, 410, 100, 30);
    edit_schedule.add(save_button);
    save_button.addActionListener(e -> save_schedule(day));

    edit_schedule.setBounds(0, 0, 500, 500);

    // ✅ 追加：CSVから既存の予定を検索
    try (BufferedReader br = new BufferedReader(new FileReader("todo.csv"))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("date,")) continue; // ヘッダーをスキップ

            String[] parts = line.split(",");
            if (parts.length < 2) continue;

            String datePart = parts[0];  // yyyy/MM/dd_HH:mm
            String content = parts[1];

            if (datePart.startsWith(selectedDate + "_")) {
                area.setText(content); // 予定をテキストエリアにセット
                String timeStr = datePart.substring(datePart.indexOf("_") + 1);
                comboBox.setSelectedItem(timeStr); // 時間もセット
                break;
            }
        }
    } catch (IOException ex) {
        ex.printStackTrace();
    }

    edit_schedule.setVisible(true);
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
                openning();
            }
        });

        setVisible(true);
    }

    private String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(calendar.getTime()).toString();
    }
private Set<String> scheduledDays = new HashSet<>();

    private void changeMonth(int delta) {
        calendar.add(Calendar.MONTH, delta);
        monthLabel.setText(getCurrentMonth());
        updateCalendar();
    }
 private void updateCalendar() {
 loadScheduledDays();
    calendarPanel.removeAll();
    Calendar tempCal = (Calendar) calendar.clone();
    tempCal.set(Calendar.DAY_OF_MONTH, 1);
    int firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1;
    int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

    for (int i = 0; i < firstDayOfWeek; i++) {
        calendarPanel.add(new JLabel(""));
    }

    for (int day = 1; day <= daysInMonth; day++) {
        String displayText = String.valueOf(day);
        if (scheduledDays.contains(String.valueOf(day))) {
            displayText = "<E>";  // ←予定ありマーク
        }

        JButton dayButton = new JButton(displayText);
        int selectedDay = day;
        dayButton.addActionListener(e -> showAppointments(selectedDay));
        calendarPanel.add(dayButton);
    }

    calendarPanel.revalidate();
    calendarPanel.repaint();
}


public void openning() {
    scheduledDays.clear();  // 一度リセット
    try (BufferedReader br = new BufferedReader(new FileReader("todo.csv"))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("date")) continue; // ヘッダー行をスキップ

            String[] parts = line.split(",");
            if (parts.length < 1) continue;

            String dateTime = parts[0];  // 例: 2025/04/19_15:00

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm");
                LocalDate date = LocalDate.parse(dateTime, formatter);
                int day = date.getDayOfMonth();
                scheduledDays.add(String.valueOf(day)); // 例: "19"
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
	public void selected_date(String date, String contents){
		System.out.println("date: " + date);
		if(date != null){
			System.out.println(date + " : " + contents);
    if (date != null) {
        //System.out.println(date + " : " + contents);
        // ボタンのテキストを変更
        for (Component comp : calendarPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (button.getText().equals(date)) {
                    button.setText("E"); // ボタンのテキストを更新
                    break;
                }
            }
        }
        }
        }
        	
}	
   public void save_schedule(int day){
    try {
        BufferedWriter bw;
        File file = new File("todo.csv");
        String selectedDate = getSelectedDate(day);
        String selected = (String) comboBox.getSelectedItem();

        if (file.exists()) {
            bw = new BufferedWriter(new FileWriter(file, true));
        } else {
            file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file));
            bw.write("date,contents\n");
        }

        bw.write(selectedDate + "_" + selected + "," + area.getText().toString() + "\n");
        bw.close();

        // カレンダーを更新して "E" 表示
                loadScheduledDays();
        updateCalendar();
        openning(); // <-- ここを追加
    } catch(Exception error) {
        error.printStackTrace();
    }
    }
    private void loadScheduledDays() {
    scheduledDays.clear();
    try (BufferedReader br = new BufferedReader(new FileReader("todo.csv"))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.startsWith("date,")) continue;

            String[] parts = line.split(",");
            if (parts.length < 2) continue;

            String datePart = parts[0]; // 例: "2025/04/20_10:00"
            String dayStr = datePart.substring(8, 10); // "20"
            if (dayStr.startsWith("0")) dayStr = dayStr.substring(1); // "01" -> "1"
            scheduledDays.add(dayStr);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    

	public JTextArea area;
	JComboBox<String> comboBox;
   /* private void showAppointments(int day) {
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
    }*/
   // String yearMonth;
/*    public void save_schedule(int day){
    
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
	}*/
}

