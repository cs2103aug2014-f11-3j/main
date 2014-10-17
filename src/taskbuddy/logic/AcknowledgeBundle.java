package taskbuddy.logic;

public class AcknowledgeBundle extends Bundle{
	
	private String status = "Status";
	private String success = "Success";
	private String failure = "Failure";
	private String message = "Message";
	private String task = "Task";
	
	public void putStatus(String stat){
		this.putString(status, stat);
	}
	
	public void putSuccess(){
		this.putString(status, success);
	}
	
	public void putFailure(){
		this.putString(status, failure);
	}
	
	public void putMessage(String msg){
		this.putString(message, msg);
	}
	
	public void putTask(Task t){
		this.putObject(task, t);
	}
	
	public String getStatus(){
		return (String) this.getItem(status);
	}
	
	public String getMessage(){
		return (String) this.getItem(message);
	}
	
	public Task getTask(){
		return (Task) this.getItem(task);
	}
}
