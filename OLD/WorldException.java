/**
 * <h1>Custom exception class, thrown during World construction</h1>
 * 
 * @see World
 * 
 * @author Daniel Busuttil, 260608427
 * @version 1.0
 * @since 2018-04-11
 */ 

// Necessary library
import java.lang.Exception;

public class WorldException extends Exception {
  private final String errMsg;
  
  public WorldException(String msg) {
    this.errMsg = msg;
  }
  
  public String getErr() {
    return this.errMsg;
  }
}