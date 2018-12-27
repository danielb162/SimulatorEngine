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

public class Immovable extends WorldItem implements GridItem {
  
  public Immovable(String in_name, char symbol, int in_x, int in_y, int in_grid_x, int in_grid_y) throws WorldItemException {
    super(in_name, symbol, in_x, in_y, in_grid_x, in_grid_y);
  }
  
  public boolean symbolValidCheck( char in_symbol ) {
    if ( in_symbol == 'I') return true;
    return false;
  }
  
  public char step() {
    return 'e';
  }
  
  public void setMode( boolean state){}
  public boolean getMode(){
    return false;
  }
}
