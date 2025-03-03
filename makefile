install:
	javac ./com/hiro/to/Do/CalendarWithtodo/CalendarWithTodo.java
	javac ./com/hiro/to/Do/*.java
	javac ./com/hiro/to/Do/ScheduleGUI/ScheduleGUI.java
	javac ./com/hiro/to/Do/Schedule/Schedule.java
	javac ./com/hiro/to/Do/ScheduleGUI/Day.java
toDo:
	java com.hiro.to.Do.MainActivity
Calendar:
	java com.hiro.to.Do.CalendarWithtodo.CalendarWithTodo

ScheduleGUI:
	java com.hiro.to.Do.ScheduleGUI.ScheduleGUI

Schedule:
	java com.hiro.to.Do.Schedule.Schedule

DayGUI:
	java com.hiro.to.Do.ScheduleGUI.MyDay
