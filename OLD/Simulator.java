// Used during step():
import java.util.HashMap;
// Used in buildWorld():
import java.util.Random;
import java.lang.Math;
import java.lang.System;
// Used for Swing/display:
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Thread;

public class Simulator {
  // Private class to follow Singleton DP
  private static class World {
    private final GridItem[][] item_arr;
    private final int max_y;
    private final int max_x;
    
    private World( int in_x_dim, int in_y_dim ) {
      item_arr = new GridItem[in_y_dim][in_x_dim];
      max_y = item_arr.length;
      max_x = item_arr[0].length;
    }
    
    // Allows Autonomous objects to move again, called after step() in Simulator's main
    public void resetMode() {
      for ( int y = 0; y < max_y; y++ ){
        for ( int x = 0; x < max_x; x++ ){
          if ( item_arr[y][x] != null && item_arr[y][x].getToken() == 'A' && item_arr[y][x].getMode() == true ) {
            item_arr[y][x].setMode(false);
          }
        }
      }
    }
    
    public char getSym(int x_coord, int y_coord) {
      if ( item_arr[y_coord][x_coord] == null ) return ' ';
      else return item_arr[y_coord][x_coord].getToken();
    }
    
    public void display() {
      Simulator.getInstance().passGUI( WorldGUI.init(max_x, max_y) );
    }
    
    // Iterates through cells changing state of the world
    public void step() {      
      for ( int y = 0; y < max_y; y++ ){
        for ( int x = 0; x < max_x; x++ ){
          if ( item_arr[y][x] != null && item_arr[y][x].getToken() == 'A' && item_arr[y][x].getMode() == false ) {
            // Want to step & see if chain reaction occurs
            char dir = item_arr[y][x].step();
            if ( dir != 'e' ) { // Protect against calls on improper objects
              switch ( dir ) {
                case 'N': // Want to move 'up', y - 1
                  if ( y == 0 ) break;
                  if ( item_arr[y - 1][x] == null ) {
                    item_arr[y - 1][x] = item_arr[y][x];
                    item_arr[y - 1][x].update(x, y - 1, max_x, max_y);
                    item_arr[y - 1][x].setMode(true); // to prevent further, autonomous movement
                    item_arr[y][x] = null;
                    
                    break;
                  }
                  else if ( item_arr[y - 1][x].getToken() == 'A' || item_arr[y - 1][x].getToken() == 'M' ) {
                    int row_count = y - 1;
                    // HM related temp variables:
                    HashMap<Integer, GridItem> bumpingHM = new HashMap<Integer, GridItem>();
                    bumpingHM.put( 0, item_arr[y][x]);
                    
                    int count = 1;
                    while ( row_count >= 0 ) {
                      if ( item_arr[row_count][x] == null ) {
                        // Collapse & broadcast
                        while ( count > 0 ) {
                          // Count is one greater than the last key in the HM, so we -1:
                          GridItem temp = bumpingHM.remove(count - 1);
                          item_arr[row_count][x] = temp;
                          item_arr[row_count][x].update(x, row_count, max_x, max_y);
                          if ( count == 1 ) item_arr[row_count][x].setMode(true); // to prevent further, autonomous movement
                          item_arr[row_count + 1][x] = null;
                          
                          row_count++;
                          count--;
                        }
                        break;
                      }
                      else if ( item_arr[row_count][x].getToken() == 'I' ) {
                        // Want to stop as we've hit an immovable object
                        break;
                      }
                      
                      // Want to put current Object onto bumpingHm and continue looping
                      bumpingHM.put(count, item_arr[row_count][x]);
                      count++;
                      row_count--;
                    }
                    
                  }
                  // Nothing happens as our object can't move up into an Immovable object
                  break;
                  
                case 'E': // Want to move 'right', x + 1
                  if ( x == max_x - 1 ) break;
                  else if ( item_arr[y][x + 1] == null ) {
                    item_arr[y][x + 1] = item_arr[y][x];
                    item_arr[y][x + 1].update(x + 1, y, max_x, max_y);
                    item_arr[y][x + 1].setMode(true);
                    item_arr[y][x] = null;
                    
                    break;
                  }
                  else if ( item_arr[y][x + 1].getToken() == 'A' || item_arr[y][x + 1].getToken() == 'M' ) {
                    int col_count = x + 1;
                    HashMap<Integer, GridItem> bumpingHM = new HashMap<Integer, GridItem>();
                    bumpingHM.put( 0, item_arr[y][x]);
                    
                    int count = 1;
                    while ( col_count < max_x ) {
                      if ( item_arr[y][col_count] == null ) {
                        while ( count > 0 ) {
                          GridItem temp = bumpingHM.remove(count - 1);
                          item_arr[y][col_count] = temp;
                          item_arr[y][col_count].update(col_count, y, max_x, max_y);
                          if ( count == 1 ) item_arr[y][col_count].setMode(true);
                          item_arr[y][col_count - 1] = null;
                          
                          col_count--;
                          count--;
                        }
                        break;
                      }
                      else if ( item_arr[y][col_count].getToken() == 'I' ) {
                        break;
                      }
                      
                      bumpingHM.put(count, item_arr[y][col_count]);
                      count++;
                      col_count++;
                    }
                    
                  }
                  break;
                  
                case 'S': // Want to move 'down', y + 1
                  if ( y == max_y - 1) break;
                  else if ( item_arr[y + 1][x] == null ) {
                    item_arr[y + 1][x] = item_arr[y][x];
                    item_arr[y + 1][x].update(x, y + 1, max_x, max_y);
                    item_arr[y + 1][x].setMode(true);
                    item_arr[y][x] = null;
                    
                    break;
                  }
                  else if ( item_arr[y + 1][x].getToken() == 'A' || item_arr[y + 1][x].getToken() == 'M' ) {
                    int row_count = y + 1;
                    HashMap<Integer, GridItem> bumpingHM = new HashMap<Integer, GridItem>();
                    bumpingHM.put( 0, item_arr[y][x]);
                    
                    int count = 1;
                    while ( row_count < max_y ) {
                      if ( item_arr[row_count][x] == null ) {
                        while ( count > 0 ) {
                          GridItem temp = bumpingHM.remove(count - 1);
                          item_arr[row_count][x] = temp;
                          item_arr[row_count][x].update(x, row_count, max_x, max_y);
                          if ( count == 1 ) item_arr[row_count][x].setMode(true);
                          item_arr[row_count - 1][x] = null;
                          
                          row_count--;
                          count--;
                        }
                        break;
                      }
                      else if ( item_arr[row_count][x].getToken() == 'I' ) {
                        break;
                      }
                      
                      bumpingHM.put(count, item_arr[row_count][x]);
                      count++;
                      row_count++;
                    }
                    
                  }
                  break;
                  
                case 'W': // Want to move 'left', x - 1
                  if ( x == 0 ) break;
                  else if ( item_arr[y][x - 1] == null ) {
                    item_arr[y][x - 1] = item_arr[y][x];
                    item_arr[y][x - 1].update(x - 1, y, max_x, max_y);
                    item_arr[y][x - 1].setMode(true); // to prevent further, autonomous movement
                    item_arr[y][x] = null;
                    
                    break;
                  }
                  else if ( item_arr[y][x - 1].getToken() == 'A' || item_arr[y][x - 1].getToken() == 'M' ) {
                    int col_count = x - 1;
                    HashMap<Integer, GridItem> bumpingHM = new HashMap<Integer, GridItem>();
                    bumpingHM.put( 0, item_arr[y][x]);
                    
                    int count = 1;
                    while ( col_count >= 0 ) {
                      if ( item_arr[y][col_count] == null ) {
                        while ( count > 0 ) {
                          GridItem temp = bumpingHM.remove(count - 1);
                          item_arr[y][col_count] = temp;
                          item_arr[y][col_count].update(col_count, y, max_x, max_y);
                          if ( count == 1 ) item_arr[y][col_count].setMode(true);
                          item_arr[y][col_count + 1] = null;
                          
                          col_count++;
                          count--;
                        }
                        break;
                      }
                      else if ( item_arr[y][col_count].getToken() == 'I' ) {
                        break;
                      }
                      
                      bumpingHM.put(count, item_arr[y][col_count]);
                      count++;
                      col_count--;
                    }
                  }
                  break;
              }
            }
          }
        } // End of inner loop
      } // End of outer loop
    }
    
    // ASCII/text-only display for debugging
    public void displayASCII() {
      System.out.println("          0   1   2   3   4");
      for ( int y = 0; y < max_y; y++ ){
        System.out.print("Row #" + y + ": ");
        for ( int x = 0; x < max_x; x++ ){
          if ( x == max_x - 1 ) {
            if ( item_arr[y][x] == null ) System.out.println("|   |");
            else System.out.println("| " + item_arr[y][x].getToken() + " |");
          }
          else {
            if ( item_arr[y][x] == null ) System.out.print("|   ");
            else System.out.print("| " + item_arr[y][x].getToken() + " ");
          }
          
        }
      }
    }
    
    // Method to create and add a GridItem, returns false if -for any reason- the GridItem can't be added
    public boolean add( String in_name, char in_symbol, int in_x, int in_y ) {
      if ( item_arr[in_y][in_x] != null ) return false;
      switch ( in_symbol ) {
        case 'I':
          try {
          item_arr[in_y][in_x] = new Immovable(in_name, 'I', in_x, in_y, item_arr[in_y].length, item_arr.length);
          return true;
        }
          catch ( WorldItemException e ) {
            return false;
          }
        case 'A':
          try {
          item_arr[in_y][in_x] = new Autonomous(in_name, 'A', in_x, in_y, item_arr[in_y].length, item_arr.length);
          return true;
        }
          catch ( WorldItemException e ) {
            return false;
          }
        case 'M':
          try {
          item_arr[in_y][in_x] = new Moveable(in_name, 'M', in_x, in_y, item_arr[in_y].length, item_arr.length);
          return true;
        }
          catch ( WorldItemException e ) {
            return false;
          }
      }
      return true;
    }
  }
  
  // Private class to, with the Swing library, create a custom GUI to display state of 'gameWorld'
  private static class WorldGUI {
    private class ButtonClickListener implements ActionListener {
      public void actionPerformed( ActionEvent e) {
        String command = e.getActionCommand();
        if( command.equals( "Continue" ) ) {
          cont = 1; // Want to continue the sim
        }
        else cont = 2; // Want to terminate
      } 
    }
    
    // General purpose variables:
    private JFrame mainFrame;
    private JLabel headerLabel;
    private JPanel controlPanel;
    private JPanel buttonPanel;
    private final Simulator active_sim = Simulator.getInstance();
    // Variables used to continue/terminate the current sim:
    private JButton contButton;
    private JButton termButton;
    private int cont = 0;
    // Variables used to interface with the World instance in active_sim
    private JLabel[][] world_cells; 
    private final int x_dim;
    private final int y_dim;
    
    // Constructor
    public WorldGUI(int y_size, int x_size){
      x_dim = x_size;
      y_dim = y_size;
      world_cells = new JLabel[y_dim][x_dim];
      prepareGUI();
    }
    
    // Begins to populate components of the window
    private void prepareGUI(){
      mainFrame = new JFrame("World Simulation");
      mainFrame.setSize(400, 400);
      mainFrame.setLayout(new BorderLayout()); // Have mainFrame have 3 rows, 1 row
      headerLabel = new JLabel("", JLabel.CENTER);
      
      // Anonymous WindowEvent to instruct mainFrame what to do on close
      mainFrame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent wEvent){
          System.exit(0);
        }
      });
      
      // Properly initialize the various Components:
      contButton = new JButton("Continue");
      contButton.setActionCommand("Continue");
      contButton.addActionListener( new ButtonClickListener() );
      
      termButton = new JButton("End");
      termButton.setActionCommand("End");
      termButton.addActionListener( new ButtonClickListener() );
      
      controlPanel = new JPanel();
      controlPanel.setLayout(new GridLayout(y_dim, x_dim));
      controlPanel.setSize(350,350);
      
      buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(1, 2));
      
      // Populate the Containers
      mainFrame.add(headerLabel, BorderLayout.NORTH);
      mainFrame.add(controlPanel, BorderLayout.CENTER);
      
      buttonPanel.add(contButton);
      buttonPanel.add(termButton);        
      mainFrame.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Show initial state
    public void showGUI(){
      headerLabel.setText("Active simulation: ");
      
      for ( int y = 0; y < y_dim; y++ ){
        for ( int x = 0; x < x_dim; x++ ) {
          world_cells[y][x] = new JLabel( Character.toString(active_sim.getSym(x, y)) );
          world_cells[y][x].setFont(new Font("Serif", Font.PLAIN, 25));
          controlPanel.add(world_cells[y][x]);
        }
      }
      
      // 'Activate' window so that it is visible
      mainFrame.setVisible(true);
    }
    
    // Updates GUI
    public void updateGUI(int iterate_num){
      headerLabel.setText("Active simulation: " + iterate_num);
      
      for ( int y = 0; y < y_dim; y++ ){
        for ( int x = 0; x < x_dim; x++ ) {
          world_cells[y][x].setText( Character.toString(active_sim.getSym(x, y)) );
        }
      }
    }
    
    
    // Manner by which Simulator (the controller) can communicate to the View
    public static WorldGUI init(int x_dim, int y_dim){
      WorldGUI swingUI = new WorldGUI(y_dim, x_dim);
      swingUI.showGUI();
      return swingUI;
    }
    
    // Methods for Simulator to interface with the GUI/change it's state:
    public int getCont(){
      return this.cont;
    }
    public void resetCont(){
      this.cont = 0;
    }
    public void prompt(){
      headerLabel.setText("Would you like to run the simulation again?");
    }
    
    // Called by Simulator to System.exit(0)
    public void term(){
      System.exit(0); 
    }
  }
  
  // Specifications used to populate item_arr, can be changed by TA's to make testing easier
  private final static int imove_num = 5;
  private final static int move_num = 3;
  private final static int auto_num = 2;
  private final static int x_size = 1;
  private final static int y_size = 1;
  private static final int loop_num = 100;
  // Instances of World (Model) and WorldGUI (View) used by our Simulator (Controller)
  private static World gameWorld;
  private static WorldGUI swingUI;
  
  // Creating Simulator using Singleton DP
  private static Simulator simInstance = new Simulator();
  private Simulator() {}
  public static Simulator getInstance() {return simInstance;}
  
  // Builds a 'World' of a particular size and then populates with the provided specs
  private static void buildWorld() {
    // To make sure we don't get stuck in an infinite loop
    if ( imove_num + move_num + auto_num <= x_size * y_size ) {
      gameWorld = new World(x_size, y_size);
      Random rand_gen = new Random();
      
      for ( int i = 1; i <= imove_num; ) {
        if ( gameWorld.add("I" + i, 'I', ( Math.abs(rand_gen.nextInt()) % x_size ), ( Math.abs(rand_gen.nextInt()) % y_size )) ) i++;
      }
      for ( int i = 1; i <= move_num; ) {
        if ( gameWorld.add("M" + i, 'M', ( Math.abs(rand_gen.nextInt()) % x_size ), ( Math.abs(rand_gen.nextInt()) % y_size )) ) i++;
      }
      for ( int i = 1; i <= auto_num; ) {
        if ( gameWorld.add("A" + i, 'A', ( Math.abs(rand_gen.nextInt()) % x_size ), ( Math.abs(rand_gen.nextInt()) % y_size )) ) i++;
      }
    }
    // Need to make sure that our simulation doesn't continue if sizes don't allow
    else {
      System.err.println("The world sizes you provided was too small to accomodate the number of items you requested");
      gameWorld = null;
    }
  }
  
  public char getSym(int in_x, int in_y) {
    return gameWorld.getSym(in_x, in_y);
  }
  
  // Called by WorldGUI.display() to hand over control of Swing to Simulator
  public void passGUI( WorldGUI in_GUI ){
    swingUI = in_GUI;
  }
  
  // Puts our thread to sleep for 'millis' ms' to allow enough time for a person to observe the simulation
  public static void sleep(long millis){
    try {
      Thread.sleep(millis);
    }
    catch(InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
  }
  
  public static void main(String[] args) throws WorldItemException {
    // Some variables we'll during our loops
    int cont = 0;
    int cur_iterate = 0;
    
    // 0th loop:
    buildWorld();
    if (gameWorld == null) System.exit(-1);
    gameWorld.display();
    
    // Nth loop:
    do {
      swingUI.resetCont();
      cont = 0;
      int temp = cur_iterate;
      int lim = temp + loop_num;
      for (; temp < lim; temp++, cur_iterate++ ){
        swingUI.updateGUI(cur_iterate + 1);
        gameWorld.step();
        gameWorld.resetMode();
        // Pads our simulation's loops by 0.2s to allow for us (humans) to see the changes on the GUI:
        sleep(200); 
      }
      swingUI.prompt(); // Changes swingUI.headerLabel to prompt user
      while(true) {
        cont = swingUI.getCont();
        // Arbitrary library call to allow enough time for Swing/'ButtonClickListener' to change 'swingUI.cont'
        System.out.println(""); // Meaningless 'padding'
        if (cont != 0) break;
      }
    } while ( cont == 1 );
    
    // Cleanup:
    swingUI.term();
    System.exit(0);
  }
  
}