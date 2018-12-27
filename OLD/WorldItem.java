/**
 * <h1>Super-class for the items that will populate World</h1>
 * Implements WorldItemCheck to allow for more elegant validation during construction of children. Sub-classes
 * are: Autonomous, Immovable and Movable Objects.
 * 
 * @see WorldItemException
 * @see WorldItemCheck
 * @see Autonomous
 * @see Moveable
 * @see Immovable
 * 
 * @author Daniel Busuttil, 260608427
 * @version 1.0
 * @since 2018-04-08
 */ 

abstract public class WorldItem implements GridItem {
  private final String name;
  private final char token;
  private int x, y;
  
  /**
   * Constructor for super-class
   * 
   * @param in_name String used to initialize 'name'
   * @param symbol Char used to initialize 'token'
   * @param in_x Int used to initialize 'x'
   * @param in_y Int used to initialize 'y'
   * @param in_grid_x Int used to validate 'x', provided by the World Object's 'x_size'
   * @param in_grid_y Int used to validate 'y', provided by the World Object's 'y_size'
   * 
   * @exception WorldItemException Thrown if constructor is called with invalid parameters
   * @see WorldItemException
   * @see Exception
   * @see World
   */ 
  
  public WorldItem(String in_name, char symbol, int in_x, int in_y, int in_grid_x, int in_grid_y) throws WorldItemException {
    // Guards against invalid inputs
    if ( in_name.length() == 0 ) throw new WorldItemException("An empty String was passed as 'name'");
    if ( !symbolValidCheck(symbol) ) throw new WorldItemException("An incorrect symbol was passed to represent this Object");
    if ( in_grid_x < 0 || in_grid_y < 0 ) throw new WorldItemException("An invalid grid-size was passed, this cannot be used to verify x or y");
    if ( in_x > in_grid_x || in_y > in_grid_y ) throw new WorldItemException("An invalid x or y value was passed");
    
    // If passed, we then initiliaze the fields of our object:
    this.name = in_name;
    this.token = symbol;
    this.x = in_x;
    this.y = in_y;
  }
  
  /**
   * Accessor for private char 'token'
   */ 
  public char getToken() {
    return this.token;
  }
  
  /**
   * Mutator for private int 'x'; 'in_grid_x' is provided by the World Object's 'x_size'
   * 
   * @see World
   */ 
  public boolean setX(int in_x, int in_grid_x) {
    if ( in_grid_x < 0 ||  in_x > in_grid_x || this.token == 'I') return false;
    
    this.x = in_x;
    return true;
  }
  
  /**
   * Mutator for private int 'y'; 'in_grid_y' is provided by the World Object's 'y_size'
   * 
   * @see World
   */ 
  public boolean setY(int in_y, int in_grid_y) {
    if ( in_grid_y < 0 ||  in_y > in_grid_y || this.token == 'I' ) return false;
    
    this.y = in_y;
    return true;
  }
  
  // TO DO: Put in safeguards:
  public void update( int new_x, int new_y, int x_spec, int y_spec ) {
    this.setX(new_x, x_spec);
    this.setY(new_y, y_spec);
  }
  
}




