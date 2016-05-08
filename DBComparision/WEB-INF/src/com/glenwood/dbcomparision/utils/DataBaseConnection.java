/**
  * The <Code>DataBaseConnection</Code> Class is used to open DataBase Connection and 
  * to get the opened Statement or opened Connection.
  * This internally open the DSN Connection and passes the required references 
  * such as Connection or Statement as per the requirement.

*/

package com.glenwood.dbcomparision.utils;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



public class DataBaseConnection {
  /** Maintains the DataSource  **/
  private DataSource   dsn;
  /** Maintains the DataBase Connection **/
  private Connection   con;
  /** Maintains the Connection Statement  **/
  private Statement    sta;

  private PreparedStatement preSt = null;
  
    public static final String PGDRIVERURL="jdbc:postgresql://";
    public static final String USERNAME="anup";
    public static final String PASSWORD="anup513";
  /**
   * Initializes the DataSource in the Contructor
   **/
  public DataBaseConnection(String connectionString ) throws Exception{
    if ( connectionString == null || connectionString == "" ){
      ResourceBundle connrc = ResourceBundle.getBundle("com.glenwood.hcare.resources.Connection");
      if ( connrc == null ) 
        throw new Exception("Unable to load the resource");
      connectionString = connrc.getString("Connection.connString");
      connrc = null;
    }
    //Class.forName("org.postgresql.Driver");
    //con = DriverManager.getConnection(connectionString,USERNAME,PASSWORD);

    Context initCtx = new InitialContext();
    dsn = (DataSource)initCtx.lookup(connectionString);
  }

  public DataBaseConnection() throws Exception{
      this("");
  }

  /** Returns the Connection object for the DataSource
   **/
  public Connection getConnection() throws SQLException{
	try{
		if( con == null ) con = dsn.getConnection();
	}catch(Exception e){
	    e.printStackTrace(); GlaceOutLoggingStream.console("\n\nMESSAGE "+e.getMessage());
		throw new GlaceSQLException();
	}
    return con;
  }

  /** Returns the Statement of the Connection Object
   **/
  public Statement getStatement() throws SQLException{
	 try{
		 if (sta == null) sta = getConnection().createStatement();
	 }catch(Exception e){
		 GlaceOutLoggingStream.console("\n\n\nException in DataBaseConnections.java Excetion ");
		 throw new GlaceSQLException();
	 }
    return sta;
  }

  public Statement getStatement(boolean isNew ) throws SQLException{
    	sta = getConnection().createStatement();
    return sta;
  }
  
  public Statement createStatement() throws SQLException{
  return getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
  }
  
  /** Returns the Statement of the Connection Object
   *@param1 scroll type of the ResultSet   
   *@param2 cursor type of the ResultSet   
  **/
  public Statement getStatement(int xScrollType, int xCursorType) throws SQLException{
    if (sta == null) sta = getConnection().createStatement( xScrollType ,xCursorType);
    return sta;
  }
  public Statement getStatement_sta(int xScrollType, int xCursorType) throws SQLException{
	if(sta!=null)	sta.close();
	sta = getConnection().createStatement( xScrollType ,xCursorType);
	return sta;
  }
  public Statement getStatement(int xScrollType, int xCursorType,boolean isAnsi) throws SQLException{
    //if (sta == null)
    sta = getConnection().createStatement( xScrollType ,xCursorType);
    return sta;
  }
  /** Closes the Statement of the Connection Object
   **/
  public void closeStatement() throws SQLException{
    sta.close();
    sta = null;
  }

  /** Closes the Connection with the DataSource
   **/
  public void closeConnection()  throws SQLException{
    con.close();
    con = null;
  }

  /** Closes the Connection and and the Statement
   **/
  public void closeReferences() throws SQLException{
    if (sta != null) closeStatement();
    if (con != null) closeConnection();
  }

  public void beginTrans() throws SQLException{
    if( con == null ) con = dsn.getConnection();
    con.setAutoCommit( false );
  }

  public void commitTrans() throws SQLException{
      con.commit();
      con.setAutoCommit( false ); //false
  }

  public void rollbackTrans() throws SQLException{
	  con.setAutoCommit( false ); //falsecon.setAutoCommit( false ); //false
	  con.rollback();
      con.setAutoCommit( false ); //false
  }

  public PreparedStatement prepareStatement(String query) throws SQLException{
	  if(con != null)
		  preSt = con.prepareStatement(query);
	  return preSt;
  }
  public void setPrepareParam(int index, String value) throws SQLException{
	  if(preSt != null)
		  preSt.setString(index, value);

  }
  public void setPrepareParam(int index, double value) throws SQLException{
	  if(preSt != null)
		  preSt.setDouble(index, value);
  }
  public void setPrepareParam(int index, int value) throws SQLException{
	  if(preSt != null)
		  preSt.setInt(index, value);
  }
  public void setPrepareParam(int index, Date value) throws SQLException{
	  if(preSt != null)
		  preSt.setDate(index, value);
  }
  public void setPrepareParam(int index, BigDecimal value) throws SQLException{
	  if(preSt != null)
		  preSt.setBigDecimal(index, value);
  }

  
  
  public int executePrepare() throws SQLException{
	  int updateCount = -1;
	  if(preSt != null)
		  updateCount = preSt.executeUpdate();
	  return updateCount;
  }
  public ResultSet getPrepareResults() throws Exception{
	  if(preSt != null)
		  return preSt.getGeneratedKeys();
	  return null;
  }

  public void beginTransaction(){
	try{
		if(con != null)
			con.setAutoCommit(false);
	}
	catch(Exception E){
//		GlaceOutLoggingStream.console("Exception in beginTransaction:"+E.getMessage());
	}

}

public void commit(){
	try{
		if(con != null){
			con.commit();
			con.setAutoCommit(true);
		}
	}
	catch(Exception E){
//		GlaceOutLoggingStream.console("Exception in commit:"+E.getMessage());
	}
}

public void rollback(){
	try{
		if(con != null){
			con.rollback();
			con.setAutoCommit(false);
		}
	}
	catch(Exception E){
//		GlaceOutLoggingStream.console("Exception in rollback:"+E.getMessage());
	}
}

}
