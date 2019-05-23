import java.sql.*;

/**
 * The Bank
 * The details of bank account(s) must be held
 *  in a relational database (typically Derby).
 */
public class Bank
{
  /**
   * Create the bank 
   *  uses a database to hold all data
   */
  private Connection con = null;    // Connection to database
  private Statement  stm = null;    // Used to talk to database

  private static final String urlDB =
                 "jdbc:derby:derby.db";
  private static final String DRIVER =
                 "org.apache.derby.jdbc.EmbeddedDriver";
                 
  private long accNo = 0;        // The current account number
  private long accPasWrd = 0;  // The current account password
  private long balance = 0;   // The current account balance
  private boolean accessOK = false;  // Can access account info.

  /*
   * Constructor sets up link to database
   * Database must exist to work
   */
  public Bank()
  {
    // Set up connection to the database
    // There is only 1 instance of a bank created
    //  for each ATM
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
   * Set the number of the selected account
   * @param accNumber The account number
   */
  public void setAcNumber( long accNumber ) 
  { 
     accNo = accNumber;
  }

  /**
   * Set the password for the selected account
   * @param accPasWrd The account password
   */
  public void setAcPasswd( long accPassWord ) 
  { 
     accPasWrd = accPassWord;
  }

  /**
   * Set the internal state to be no access 
   *  for account transactions
   */
  public void loggedOut() 
  {
    accNo = accPasWrd = 0;     // Destroy so can not be used
    accessOK = false;              // Revoke/Stop access to Account
  }

  /**
   * Check account number and password is valid.
   * if the account number and password are correct then
   * set the internal state to be valid access 
   * @return success/ failure
   */
  public boolean checkValid() 
  { 
    // Need to add code to check if User/Password is valid
   
    try{
       
     ResultSet res = null;
     
     res = stm.executeQuery("select accNo, accPassWord from Accounts");
      while ( res.next() )
      {
        long accNumber = res.getLong("accNo");
        long accPassWord = res.getLong("accPassWord");
        
        if(accNo == accNumber){
         
         if(accPassWord == accPasWrd){
             accessOK = true;
            }
         else{
             accessOK = false;
            }
        }
        
        else{
            accessOK = false;
        }
      }
     
    }
    catch ( Exception e )
     {
      System.err.println("Problems with SQL statement:\n" +
                         e.getMessage() );
                        
    }
    
    Debug.trace( "Bank: checkValid" ); 
    return accessOK;
  }

  /**
   * Withdraw money from account
   * @param amount of money to W/D
   * @return success/ failure
   */
  public boolean withdraw( long amount ) 
  { 
    // Need to add code to withdraw amount from account
    long accBalance = 0;
    
    boolean withdrawn = false;
    try{
      ResultSet res = null;
     
      res = stm.executeQuery("select balance from Accounts");
      while ( res.next() )
      {
       accBalance = res.getLong("balance");
      } 
      if(amount > accBalance){
          withdrawn = false;
      }
      else{
          stm.execute( "update Accounts set balance = balance - " + amount  );
          withdrawn = true;
        }
    }catch ( Exception e )
     {
      System.err.println("Problems with SQL statement:\n" +
                         e.getMessage() );
                        
    }
    
    Debug.trace( "Bank: withdraw %d", amount ); 
    assert ( accessOK );  // Major security issue
    return withdrawn; 
  }
  
  /**
   * Deposit money into account
   * @param amount of money to deposit
   * @return success/ failure
   */
  public boolean deposit( long amount )
  { 
    // Need to add code to deposit amount
    
     try{
       
     stm.execute( "update Accounts set balance = balance + " + amount  );
   
    }
    catch ( Exception e )
     {
      System.err.println("Problems with SQL statement:\n" +
                         e.getMessage() );
                        
    }

    Debug.trace( "Bank: Deposit " + amount ); 
    assert ( accessOK );
    return false;
  }
  
  /**
   * Get balance of account
   * @return balance of account
   */
  public long getBalance() 
  {  
    // Need to add code to get the account balance
    
    long accBalance = 0;
    try{
       
     ResultSet res = null;
     
     res = stm.executeQuery("select balance from Accounts");
      while ( res.next() )
      {
       accBalance = res.getLong("balance");
      }
     
    }
    catch ( Exception e )
     {
      System.err.println("Problems with SQL statement:\n" +
                         e.getMessage() );
                        
    }

    Debug.trace( "Bank: get balance" ); 
    assert ( accessOK );

    return accBalance;
  }
}
