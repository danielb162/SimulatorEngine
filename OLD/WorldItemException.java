/**
 * <h1>Custom exception class, thrown during WorldItem construction</h1>
 * 
 * @see WorldItem
 * 
 * @author Daniel Busuttil, 260608427
 * @version 1.0
 * @since 2018-04-08
 */ 

// Necessary library
import java.lang.Exception;

public class WorldItemException extends Exception {
  private final String errMsg;
  
  public WorldItemException(String msg) {
    this.errMsg = msg;
  }
  
  public String getErr() {
    return this.errMsg;
  }
}