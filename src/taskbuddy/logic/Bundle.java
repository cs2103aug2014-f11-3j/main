package taskbuddy.logic;

import java.util.ArrayList;
import java.util.HashMap;

//Author: andrew

class Bundle{
	HashMap<String, Object> bundle;
	
	//e.g bundle.putString("Failure", "Failed to load etc");
	public void putString(String key,String s){
		this.bundle.put(key, s);
	}
	
	public void putObject(String key, Object o){
		this.bundle.put(key, o);
	}
	
	public Object getItem(String key){
		return this.bundle.get(key);
	}
	
	public String toString(){
		return this.bundle.toString();
	}
}