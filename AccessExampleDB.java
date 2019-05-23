import java.sql.*;
import java.util.ArrayList;

/**
 * Example of database access using the Derby database
 * @author  Michael Alexander Smith
 * All problems of concurrency are ignored
 * @version 1.1
 */

class AccessExampleDB
{
  public static void main( String args[] )
  {
    AccessExampleDB db = new AccessExampleDB();
    db.connect();   // Setup connection
    db.process();   // process transactions on the database
    db.close();     // Close down the program
  }

  private Connection con = null;    // Connection to database
  private Statement  stm = null;    // Used to talk to database

  private static final String urlDB =
                 "jdbc:derby:derby.db";
  private static final String DRIVER =
                 "org.apache.derby.jdbc.EmbeddedDriver";

  /**
   * Get a connection to the database and the Statement object.
   */
  private void connect()
  {
    try
    {
      Class.forName(DRIVER).newInstance(); //  Driver to access database
      con  = DriverManager.getConnection( urlDB, "", "" );
    }
    catch ( SQLException e )
    {
      System.err.println( "Problem with connection to " + urlDB );
      System.out.println("SQLException: " + e.getMessage());
      System.out.println("SQLState:     " + e.getSQLState());
      System.out.println("VendorError:  " + e.getErrorCode());
      System.exit( -1 );
    }
    catch ( Exception e )
    {
        // Most likely library's of Derby not made visible 
        System.err.println("Can not load JDBC driver.");
        System.exit( -1 );
    }

    try {
        //stm = con.createStatement();
        stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
		                          ResultSet.CONCUR_UPDATABLE);
    } catch (Exception e) {
        System.err.println("problems creating statement object" );
    }
  }


  /**
   * Try some very simple transactions on the table
   */
  private void process()
  {
    try
    {

      // Used to hold the table returned from an SQL command
      ResultSet res = null;

      

      // Print all of the table StockList

      res = stm.executeQuery("select * from Accounts" );
      printResultSet( "All data in Accounts ", res );           // Print



    } catch ( Exception e )
    {
      System.err.println("Problems with SQL statement:\n" +
                         e.getMessage() );
    }
  }

  /**
   * Generic print a resultset
   * Also finds column names and column types
   * @param mes The message to be printed
   * @param rs The ResultSet
   */
  private void printResultSet( String mes, ResultSet rs )
  {
    System.out.println();
    System.out.println( "printing resultset: " + mes );
    try
    {
      rs.beforeFirst();     // But result set must allow
      // Used to store column names from meta data of results set
      ArrayList<String> nameCol = new ArrayList<>();
      
      // Meta data about result(s)
      ResultSetMetaData md = rs.getMetaData();
      int cols = md.getColumnCount();

      // Extract and print column names
      for ( int j=1; j<=cols; j++ )
      {
        String name = md.getColumnName(j);
        System.out.printf( "%-14.14s ", name );
        nameCol.add( name );
      }
      System.out.println();

      // Extract and print column types
      for ( int j=1; j<=cols; j++ )
      {
        System.out.printf( "%-14.14s ",  md.getColumnTypeName(j)  );
      }
      System.out.println();

      // Now get data from results set
      while ( rs.next() )
      {
        for ( int j=0; j<cols; j++ )
        {
          String name = nameCol.get(j);
          System.out.printf( "%-14.14s ", rs.getString( name )  );
        }
        System.out.println();
      }
    } catch ( Exception e )
    {
      System.out.printf("problems with printing resultset:\n %s",
                         e.getMessage() );
    }
    System.out.println();
  }
  
  /**
   * Close the Database
   */
  private void close()
  {
    try
    {
      stm.close();     // Close the statement Object
      con.close();     // Close the connection to the Database
    } catch (SQLException e )
    {
      System.err.println( e.getMessage() );
    }
  }
}
