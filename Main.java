
class Main
{
  public static void main( String args[] )
  {
    Debug.set(true);
    Debug.trace("ATM");
    Model model = new Model();
    View  view  = new View();
    Controller   cont  = new Controller( model, view );
    
    view.setController( cont );

    model.addObserver( view );       // Add observer to the model
    
    view.setVisible( true );         // Show window on screen
    model.display();                 // Initial display 
  }
}
