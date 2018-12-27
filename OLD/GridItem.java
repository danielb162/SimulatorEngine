/**
 * <h1>Interface to allow for polymorphic handling of WorldItem sub-classes</h1>
 * This interface is used so that, in World, we can create data structures of type 'GridItem' (i.e
 * take advantage of polymorphism). Furthermore it is used so that, at construction of a WorldItem,
 * only a char which correctly represents the WorldItem Object it is trying to construct will be
 * accepted. Otherwise a WorldItemException is thrown.
 * 
 * @see WorldItem
 * @see WorldItemException
 * @see Autonomous
 * @see Moveable
 * @see Immovable
 * 
 * @author Daniel Busuttil, 260608427
 * @version 1.0
 * @since 2018-04-11
 */ 

public interface GridItem {
  public boolean symbolValidCheck( char in_symbol ); // Used during construction of WorldItems
  public char step(); // Chars of 'E' or 'e' are returned by non-autonomous objects
  public char getToken(); // Exists so that we can invoke this from our GridItem 2D array in World 
  public void update( int new_x, int new_y, int x_spec, int y_spec );
  // These allow us to stop autonomous objects from moving more than once:
  public void setMode( boolean state);
  public boolean getMode();
  
}