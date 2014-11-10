//@author A0110649J
//andrew

package taskbuddy.logic;

import java.util.ArrayList;
import java.util.HashMap;

//Author: andrew

public class Bundle {
    public HashMap<String, Object> bundle = new HashMap<String, Object>();

    // e.g bundle.putString("Failure", "Failed to load etc");
    // note that keys are unique for each bundle
    public void putString(String key, String s) {
        this.bundle.put(key, s);
    }

    public void putObject(String key, Object o) {
        this.bundle.put(key, o);
    }

    // e.g bundle.get("success");
    public Object getItem(String key) {
        return this.bundle.get(key);
    }

    public String toString() {
        return this.bundle.toString();
    }

}