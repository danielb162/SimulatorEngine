/**
 * <h1>Sub-class to populate World</h1>
 * Child of WorldItem
 * 
 * @see WorldItem
 * 
 * @author Daniel Busuttil, 260608427
 * @version 1.0
 * @since 2018-04-08
 */ 

// Used for step()
import java.util.Random;
import java.lang.Math;

public class Autonomous extends WorldItem implements GridItem {
  
  private final Random ran_dir_gen; 
  private boolean state = false; // F -> Has not moved this iteration, T -> Has moved
  
  public Autonomous(String in_name, char symbol, int in_x, int in_y, int in_grid_x, int in_grid_y) throws WorldItemException {
    super(in_name, symbol, in_x, in_y, in_grid_x, in_grid_y);
    this.ran_dir_gen = new Random();
  }
  
  public boolean symbolValidCheck( char in_symbol ) {
    if ( in_symbol == 'A') return true;
    return false;
  }
  
  public char step() {
    int temp = Math.abs(ran_dir_gen.nextInt()) % 4;
    switch ( temp ) {
      case 0:
        return 'N';
      case 1:
        return 'E';
      case 2:
        return 'S';
      case 3:
        return 'W';
        
      // Should never be executed  
      default:
        return 'e'; // Acts as error flag, but should never be reached 
    }
  }
  
  public void setMode( boolean state){
    this.state = state;
  }
  public boolean getMode(){
    return this.state;
  }
  
  
}




