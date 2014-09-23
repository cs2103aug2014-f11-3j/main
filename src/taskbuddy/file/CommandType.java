/**
 * 
 */
package taskbuddy.file;

/**
 * Describes the type of command in the <code>Command</code> object. This class
 * is to be included as a field in the <code>Command</code> class. More commands
 * may subsequently be added by future developers.
 * 
 * @author Soh Yong Sheng
 *
 */
public enum CommandType {
    // Logic can add more commands as he deems fit
    ADD, EDIT, DELETE, CLEAR, DISPLAY
}
