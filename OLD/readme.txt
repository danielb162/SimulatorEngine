Daniel Busuttil, 260608427
12th April, 2018

I chose to implement the Singleton DP for 'World', making it a private inner-class of my 'Simulator'.

I implemented the Template DP for my 'Autonomous', 'Moveable' and 'Immovable' classes by using 'WorldItem' as my abstract class and 'GridItem' as my interface (to allow for polymorphism in my World data structure).

I implemented MVC + Observer with Simulator's 'gameWorld' as our model, the Swing UI as our view and Simulator's main method acting as the controller.

After the simulation runs 100 times, the user is prompted to click either the "Continue" or "End" button to continue (or end) the simulation.

To help the TA's test the simulation more easily (e.g. if they wanted to change the World's dimensions or increase the number of Autonomous objects), I implemented world generation and step() to work with arbitrary values. These variables are located at/from line 413 of Simulator.java.
