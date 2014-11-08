package taskbuddy.logic;

public class UserInputBundle extends Bundle {

	private String user_command = "command";
	private String user_description = "description";
	private String user_startDate = "startDate";
	private String user_endDate = "endDate";
	private String user_startTime = "startTime";
	private String user_endTime = "endTime";
	private String user_title = "title";
	private String user_id = "task ID";
	private String user_float = "floatingTask";
	private String user_comp = "completion";

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

	public void putEndTime(String endTime) {
		this.putString(user_endTime, endTime);
	}

	public void putStartDate(String startDate) {
		this.putString(user_startDate, startDate);
	}

	public void putStartTime(String startTime) {
		this.putString(user_startTime, startTime);
	}

	public void putID(String ID){
		this.putString(user_id, ID);
	}
	
	public void putFloating(boolean flt){
		this.putObject(user_float, flt);
	}
	
	public void putCompletion(boolean comp){
		this.putObject(user_comp, comp);
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
	
	public String getTaskID(){
		return (String) this.getItem(user_id);
	}
	
	public boolean getFloatingStatus(){
		return (boolean) this.getItem(user_float);
	}
	
	public boolean getCompletionStatus(){
		return (boolean) this.getCompletionStatus();
	}
}
