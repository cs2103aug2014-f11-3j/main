package taskbuddy.logic;

public class UserInputBundle extends Bundle {

	private String user_command = "command";
	private String user_description = "description";
	private String user_startDate = "startDate";
	private String user_endDate = "endDate";
	private String user_startTime = "startTime";
	private String user_endTime = "endTime";
	private String user_title = "title";

	//Setter methods
	
	public void putTitle(String title) {
		this.putString(user_title, title);
	}

	public void putCommand(String cmd) {
		this.putString(user_command, cmd);
	}

	public void putDescription(String desc) {
		this.putString(user_description, desc);
	}

	public void putEndDate(String endDate) {
		this.putString(user_endDate, endDate);
	}

	public void putEndT0ime(String endTime) {
		this.putString(user_endTime, endTime);
	}

	public void putStartDate(String startDate) {
		this.putString(user_startDate, startDate);
	}

	public void putStartTime(String startTime) {
		this.putString(user_startTime, startTime);
	}

	//Access methods
	
	public String getTitle() {
		return (String) this.getItem(user_title);
	}
	
	public String getCommand() {
		return (String) this.getItem(user_command);
	}
	
	public String getDescription() {
		return (String) this.getItem(user_description);
	}
	
	public String getEndDate(){
		return (String) this.getItem(user_endDate);
	}
	
	public String getEndTime(){
		return (String) this.getItem(user_endTime);
	}
	
	public String getStartDate(){
		return (String) this.getItem(user_startDate);
	}
	
	public String getStartTime(){
		return (String) this.getItem(user_startTime);
	}
}
