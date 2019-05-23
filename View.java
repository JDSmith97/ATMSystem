import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

/**
 * Display a view of the ATM
 */
class View extends JFrame implements Observer
{
  private static final int H = 420;       // Height of window pixels 
  private static final int W = 500;       // Width  of window pixels 

  private JLabel      message  = new JLabel();       // Message area 
  private JTextArea   output1  = new JTextArea();    // Result area 
  private JTextArea   output2  = new JTextArea();    // Result area 
  private JScrollPane scrollPane  = new JScrollPane();
  
  private Controller  controller;

  /**
   * Construct the display
   */
  public View()
  {
    Container cp     = getContentPane();   // Content Pane 
    cp.setLayout(null);                    // No layout manager 
    setSize( W, H );                       // Size of Window 
    Debug.trace("Constructor View");


    final int GAP   = 10;                  // Horizontal Gap

    final int LABH  = 20;                  // Label absolute height
    final int LAB_Y = 15;                  // Start pos Height label
    final int ELABY = LAB_Y+LABH;          // End Label Y

    final int A1H  = LAB_Y+GAP;            // Output area 1 Height
    final int A1_Y = ELABY + GAP;          // Start pos height A1
    final int EA1Y = A1_Y + A1H;           // End A1 Y

    final int A2H  = 100;                  // Output area 2 Height
    final int A2_Y = EA1Y + GAP;           // Start pos Height A2
    final int EA2Y = A2_Y + A2H;           // End A2 Y

    final int HBUT  = EA2Y + GAP;          // Buttons height

    final int BNR = 4;                     // Number of rows of buttons
    final int BNC = 6;                     // number of cols of buttons
    final int BW  = (W-10)/BNC;            // Button Width
    final int BH  = HBUT/BNR;              // Size of landscape for button
    final int SBH = EA2Y+GAP;              // Start Height Buttons

    final int WAREA = BNC*BW-20;           // Width of single component

    String labels[] = {
        "7",    "8",  "9",  "",  "Dep",  "",
        "4",    "5",  "6",  "",  "W/D",  "",
        "1",    "2",  "3",  "",  "Bal",  "Fin",
        "CLR",  "0",  "",   "",  "",     "Ent" };

    final int LABELS = labels.length;      // # Button Labels

    JButton buttons[] = new JButton[LABELS];

    for ( int i=0; i<LABELS; i++ )
    {
      String label = labels[i];
      if ( label.length() >= 1 )
      {
        buttons[i] = new JButton( label );
        final int col = i%BNC * BW, row = i/BNC * BH;
        buttons[i].setBounds( GAP+col, SBH+row, BW-20, BH-10 );
        // Set action on button press
        buttons[i].addActionListener( e -> 
          {
            String name = e.getActionCommand(); // Button name 
            if ( controller != null )
            {
              controller.process( name );       // Process
            }
          }
        );
        Font font = new Font("Serif",Font.BOLD,
                            label.length() == 1 ? 24 : 12 );
        buttons[i].setFont( font );
        cp.add( buttons[i] );
      }
    }

    Font font = new Font("Serif",Font.BOLD,14);    // Font is 

    message.setBounds( GAP, LAB_Y, WAREA, LABH );  // Message area
    message.setText( "" );                         // Blank
    output1.setFont( font );                       //  Uses font 
    output1.setEditable(false);                    //  Read only (User)
    cp.add( message );                             //  Add to canvas

    output1.setBounds(GAP, A1_Y, WAREA, A1H);      // Input Area
    output1.setText("");                           // Blank
    output1.setFont( font );                       //  Uses font 
    cp.add( output1 );                             //  Add to canvas

    font = new Font("Serif",Font.BOLD,14);         // Font is

    scrollPane.setBounds( GAP, A2_Y, WAREA, A2H ); // Scrolling pane
    output2.setText( "" );                         //  Blank
    output2.setFont( font );                       //  Uses font  
    output2.setEditable(false);                    //  Read only (User)
    cp.add( scrollPane );                          //  Add to canvas
    scrollPane.getViewport().add( output2 );       //  In TextArea
    setVisible( true );                            // Make visible

    message.setText( "Bank" );                     // Opening message
    //setDefaultCloseOperation(EXIT_ON_CLOSE);     // When window closed

    class Action extends WindowAdapter             // Do on close
    {
      public void windowClosing( WindowEvent e)
      {
        Debug.trace( "Closing" );                  // Say so
        System.exit(0);                            // Exit the program
      }
    }

    addWindowListener( new Action() ); 
    setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
  }
  
  /**
   * Update the view as the model has changed
   */
  @Override
  public void update( Observable o, Object arg )
  {
     Model model = (Model) o;
     // Method setText will update the display
     output1.setText( model.getMessage1() );
     output2.setText( model.getMessage2() );
     // We have now changed view [implemented by swing code]
  }
  
  /** 
   * Set the controller used
   * @param cont The controller
   */
  public void setController( Controller cont )
  {
    controller = cont;
  }
  
}
