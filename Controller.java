
public class Controller
{
  private Model model;
  private View  view;
  
  public Controller( Model ma, View va )
  {
     model = ma; view = va;
  }
  
  public void process( String action )
  {
    model.process( action );
  }
 
}
