/**
  * The DataBaseUtils Class contains static methods to process the various 
  * database operations involved. This 
  * overloaded to split usage based on the options requirement. to 
  * generate static options, the method takes in a instance of
  * <code>DataBaseConnection</code> and to get connected with the database.
  * This Class used to connect with the DataBase Run the Query and returns the
  * the resultset or vector  as return value. This Class will also give the required
  * record for which the 
  *
*/

package com.glenwood.dbcomparision.utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;






public class DataBaseUtils{
 
	private Statement sta;  
	private String requestPage="";  
	private String tmpConnectionString="";
	  
	public static int OpenConnections=0;
  
	DataBaseConnection dbConn;

	/** * private PreparedStatement variable */
	private PreparedStatement prestatement = null;
	PreparedStatement preSt = null;

	public DataBaseUtils(String connectionString) throws Exception{	
		try{
    		dbConn  = new DataBaseConnection(connectionString);
    		tmpConnectionString = connectionString;
    		OpenConnections = OpenConnections + 1;
    		//Debug.writeToFile("/share/Glace/ConnectionLog.txt","------------------------------------" );
    		//Debug.writeToFile("/share/Glace/ConnectionLog.txt","Connection opened for "+ tmpConnectionString + " from " + requestPage);
		}catch(Exception e){
			throw e;
		} 
	}
  
  
	public DataBaseUtils() throws Exception{
    	dbConn  = new DataBaseConnection("");  
	}
	

	public void printdbUtils(){
		GlaceOutLoggingStream.console("dbConn  "+dbConn);
	}

  
	public DataBaseUtils(String connectionString,String xRequestPage) throws Exception{
	 	try{
			dbConn  = new DataBaseConnection(connectionString);
			requestPage = xRequestPage;
			tmpConnectionString = connectionString;
			OpenConnections = OpenConnections + 1;
			//Debug.writeToFile("/share/Glace/ConnectionLogTemp.txt","------------------------------------" );
			//ebug.writeToFile("/share/Glace/ConnectionLogTemp.txt","Connection opened for "+ tmpConnectionString + " from " + requestPage);
	 	}catch(Exception e){
	 		throw e;
	 	}
	}

 
	public int executeUpdate(String xQuery) throws SQLException{
		try{
			sta = dbConn.getStatement();
		}catch(Exception e){
			throw new GlaceSQLException(e.getMessage());
		}
		int zvalue;
		try{
			zvalue = sta.executeUpdate( xQuery );
		}catch(SQLException e){
			throw e;
		}
		return zvalue;
  	}
 


	public int executeUpdate(int scrollType,int cursorType,String Qry) throws SQLException{
		boolean isAnsi = true;
		try{
			sta = dbConn.getStatement(scrollType,cursorType,isAnsi);
		}catch(Exception e){
			throw new GlaceSQLException(e.getMessage());
		}
		int zvalue = sta.executeUpdate( Qry );
		return zvalue;
	}

 
	public int executeUpdate(String xQuery, boolean isNew) throws SQLException{
		sta = dbConn.getStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		int zvalue = sta.executeUpdate( xQuery );
		return zvalue;
	}
 
  //SBJ[
  /**
       This method <code>execute</code> is used to execute a query without 
       returning any result. Used to execute Insert, Update & delete Queries
  */
	
	public void execute(String xQuery) throws SQLException{
		sta = dbConn.getStatement();
		sta.execute( xQuery );
		sta = null;
	}
  //SBJ]

	public long executeAndGetGeneretedKey(String xQuery,String keyfield) throws SQLException{
		ResultSet rs = null;
		try{
			sta = dbConn.getStatement();
			rs = sta.executeQuery( xQuery + " returning "+ keyfield);
			if(rs.next())return rs.getLong(1);
		}catch (SQLException e) {
			throw e;
		}finally{
			if(rs != null)rs.close();
			rs = null;
			sta = null;
			dbConn.closeStatement();
		}
		return -1;
	}
	
	public String execute(String xQuery ,String xTableName, String xPrimaryKeyFieldName, String xPrimaryKeyFieldValue ) throws SQLException{
		sta = dbConn.getStatement();
		sta.execute( xQuery );
		if (("-1").equals(xPrimaryKeyFieldValue)){
	  		xQuery = "select h213003 from h213 where h213002 = '" + xTableName + "'";
	  		ResultSet rst = null;
	  		try{
	  			rst= sta.executeQuery( xQuery );
		  		if(rst.next()) xPrimaryKeyFieldValue = "" + rst.getInt(1);
	  		}catch(SQLException e){
	  			throw e;
	  		}finally{
	  			if(rst!=null)rst.close();
	  			rst = null;
	  		}	  	
		}
    	sta = null;
    	return xPrimaryKeyFieldValue;
	}

	public void addBatchToCurrentStatement(String xQuery) throws SQLException{
		try{
			sta = dbConn.getStatement();
			sta.addBatch( xQuery );
		}catch(GlaceSQLException e){
			sta.clearBatch();
			throw e;
		}catch(SQLException e){
			sta.clearBatch();
			throw e;	  
		}
	}
	
	public int[] executeBatch() throws SQLException{
		int rst[];
		try{
			sta = dbConn.getStatement();
			rst = sta.executeBatch();
		}catch(GlaceSQLException e){
			sta.clearBatch();
			throw e;
		}catch(SQLException e){
			sta.clearBatch();
			throw e;	  
		}
		return rst;
	}
	
	
	public ResultSet executeQuery(String xQuery) throws SQLException{
		ResultSet rst;
		try{
			sta = dbConn.getStatement();
			rst = sta.executeQuery( xQuery );
		}catch(GlaceSQLException e){
			throw e;
		}catch(SQLException e){
			dbConn.rollbackTrans();
			throw e;	  
		}
		return rst;
	}
	
  	
	public ResultSet executeQuery(String query,boolean isNew) throws SQLException{
		ResultSet rst;
		try{
			sta = dbConn.getStatement(isNew);
			rst = sta.executeQuery( query);	  
	    }catch(GlaceSQLException e){
			throw e;
		}catch(SQLException e){
			throw e;
		}
	    return rst;	
	}

  	
	public ResultSet executeQuery(String xQuery , int xScrollType, int xCursorType) throws SQLException{    
		sta = dbConn.getStatement( xScrollType, xCursorType);
    	ResultSet rst = sta.executeQuery( xQuery );
    	return rst;
  	}

	
  
	public ResultSet executeQuery_sta(String xQuery , int xScrollType, int xCursorType) throws SQLException{
	    sta = dbConn.getStatement_sta( xScrollType, xCursorType);
	    ResultSet rst = sta.executeQuery( xQuery );
	    return rst;
	}

	
	
  
	public ResultSet executeQuery(String xQuery , int xScrollType, int xCursorType,boolean isAnsi) throws SQLException{
    	sta = dbConn.getStatement( xScrollType, xCursorType,isAnsi);
    	ResultSet rst = sta.executeQuery( xQuery );
    	return rst;
  	}
  
	
	public JSONArray executeQueryToJSONArray(String xQuery) throws Exception{
	
		JSONArray result = new JSONArray();
	    JSONArray row;
	    int colIndex;
	    int rowCount = 0;
	    int colCount;
	    
	    ResultSet rst = executeQuery( xQuery );
	    
	    colCount = rst.getMetaData().getColumnCount();
	    while (rst.next()){
	      row = new JSONArray();
	      for ( colIndex=1; colIndex <= colCount; colIndex++){
	        if(rst.getString(colIndex) != null && rst.getString(colIndex) != "") 
	          row.put(colIndex-1,rst.getString(colIndex));
	        else
	          row.put(colIndex-1," ");
	      }
	      result.put(rowCount++,row);
	    }
	    rst.close();
	    rst = null;
	    return result;
	  }

	
	public JSONArray executeQueryToJSONArray(String xQuery , boolean  isNameKey , int startFrom ) throws Exception{
		  JSONArray result = new JSONArray();
	      if (startFrom>0 )
	      {
	    	    
	    		  ResultSet rst = executeQuery( xQuery );
	    		  JSONObject row;
	    		  int colIndex;
	    		  int rowCount = startFrom;
	    		  int colCount;
	    		  colCount = rst.getMetaData().getColumnCount();
	    		  
	    		  for(int i=0;i<startFrom;i++)
	    		  {
	    			  row = new JSONObject();
	    			  for ( colIndex=1; colIndex <= colCount; colIndex++){
	    				  row.put(""+(isNameKey?rst.getMetaData().getColumnName(colIndex):colIndex-1)," "); 
	    			  }
	    		  }
	    		  
	    		  
	    		  
	    		  while (rst.next()){
	    			  row = new JSONObject();
	    			  for ( colIndex=1; colIndex <= colCount; colIndex++){
	    				  if(rst.getString(colIndex) != null && rst.getString(colIndex) != "") 
	    					  row.put(""+(isNameKey?rst.getMetaData().getColumnName(colIndex):colIndex-1),rst.getString(colIndex));
	    				  else
	    					  row.put(""+(isNameKey?rst.getMetaData().getColumnName(colIndex):colIndex-1)," ");
	    			  }
	    			  result.put(rowCount++,row);
	    		  }
	    		  rst.close();
	    		  rst = null;
	    		  return result;
	    	  
	    	  
		 }
	     else
	     {
	    	 return executeQueryToJSONArray(xQuery, isNameKey); 
	     }
	    	  
	    
	}
	
	
	public JSONArray executeQueryToJSONArray(String xQuery , boolean  isNameKey ) throws Exception{
	  JSONArray result = new JSONArray();
      if (isNameKey ){
   	  
	  ResultSet rst = executeQuery( xQuery );
	  JSONObject row;
	  int colIndex;
	  int rowCount = 0;
	  int colCount;
	  colCount = rst.getMetaData().getColumnCount();
	  
	  while (rst.next()){
	      row = new JSONObject();
	      for ( colIndex=1; colIndex <= colCount; colIndex++){
		  if(rst.getString(colIndex) != null && rst.getString(colIndex) != "") 
		      row.put(rst.getMetaData().getColumnName(colIndex),rst.getString(colIndex));
		  else
		      row.put(rst.getMetaData().getColumnName(colIndex),"");
	      }
	      result.put(rowCount++,row);
	  }
	  rst.close();
	  rst = null;
	  return result;
	}else{
	    return executeQueryToJSONArray(xQuery);
	}
  }
	
public JSONArray executeQueryToDistinctJSONArray(String xQuery , boolean  isNameKey ) throws Exception{
		  JSONArray result = new JSONArray();
		  Vector resultV = new Vector();
			Vector rowV;
	      if (isNameKey ){
	   	  
		  ResultSet rst = executeQuery( xQuery );
		  JSONObject row;
		  int colIndex;
		  int rowCount = 0;
		  int rowCountV = 0;
		  int colCount;
		  colCount = rst.getMetaData().getColumnCount();
		  while (rst.next()){
		      row = new JSONObject();
		      rowV = new Vector();
		      for ( colIndex=1; colIndex <= colCount; colIndex++){
			  if(rst.getString(colIndex) != null && rst.getString(colIndex) != "")
			  {
			      row.put(rst.getMetaData().getColumnName(colIndex),rst.getString(colIndex));
			  		rowV.add(colIndex-1,rst.getString(colIndex));
			  }
			  		else
			  		{
			      row.put(rst.getMetaData().getColumnName(colIndex),"");
			      rowV.add(colIndex-1," ");
			  		}
		      }
		      if(!resultV.contains(rowV))
		      {
		    	  resultV.add(rowCountV++,rowV);  
		    	  result.put(rowCount++,row);
		      }
		      
		      
		  }
		  rst.close();
		  rst = null;
		  return result;
		}else{
		    return executeQueryToJSONArray(xQuery);
		}
	  }	
  
  public JSONArray executeQueryToJSONArray(String xQuery , int pageSize, int currentPage, boolean  isNameKey ) throws Exception{
	  xQuery = xQuery + " limit " + pageSize + " offset " + pageSize * ( currentPage - 1 ) + ";";
	  JSONArray result = new JSONArray();
      if (isNameKey ){
   	  
	  ResultSet rst = executeQuery( xQuery );
	  JSONObject row;
	  int colIndex;
	  int rowCount = 0;
	  int colCount;
	  colCount = rst.getMetaData().getColumnCount();
	  while (rst.next()){
	      row = new JSONObject();
	      for ( colIndex=1; colIndex <= colCount; colIndex++){
		  if(rst.getString(colIndex) != null && rst.getString(colIndex) != "") 
		      row.put(rst.getMetaData().getColumnName(colIndex),rst.getString(colIndex));
		  else
		      row.put(rst.getMetaData().getColumnName(colIndex)," ");
	      }
	      result.put(rowCount++,row);
	  }
	  rst.close();
	  rst = null;
	  return result;
	  }else{
	    return executeQueryToJSONArray(xQuery);
	  }
  }


  public String tableLookUp( String xFieldName , String xTableName , String xCondn){
    String completeQuery;
    if(xTableName.equals(""))
    	completeQuery = " select " + xFieldName + " as hsp001";
    else
    	completeQuery = " select " + xFieldName + " as hsp001 from " + xTableName + " where " + xCondn;
    		
    String lookUpValue = null;
    //GlaceOutLoggingStream.console(completeQuery+" ~~~~~~~~~~~~~~~from tableLookup");
    try{
	//NJK
	//	DatabaseMetaData data = dbConn.getConnectionDetails();
	//	Debug.writeToFile( "dbutil tablelook connection string :" + data.getURL() );
	//Debug.writeToFile( "dbutil tablelook qry :" + completeQuery );
	ResultSet rst = executeQuery( completeQuery );
	if ( rst.next() )  lookUpValue = rst.getString("hsp001");
	rst.close();
    }catch(Exception sExec){
	sExec.printStackTrace();
    }
    return ( lookUpValue );
  }
  
  public boolean tableCheckUp( String xFieldName , String xTableName , String xCondn){
	    String completeQuery;
	    boolean flag=false;
	    if(xTableName.equals(""))
	    	completeQuery = " select " + xFieldName + " as hsp001";
	    else
	    	completeQuery = " select " + xFieldName + " as hsp001 from " + xTableName + " where " + xCondn;
	    		
	    //GlaceOutLoggingStream.console(completeQuery);
	    try{
		ResultSet rst = executeQuery( completeQuery );
		if ( rst.next() ) 
		{
		flag=true;	
		}
		else
		{
			flag=false;
		}
		
	    }catch(Exception sExec){
		sExec.printStackTrace();
	    }
	    //GlaceOutLoggingStream.console("inside Flag--------->"+flag);
	    return flag;
	  }


  public String tableLookUp( String xFieldName , String xTableName ) {
    return ( tableLookUp( xFieldName ,  xTableName , "(1=1)" ) );
  }
  public String tableLookUp( String xFieldName) {
	    return ( tableLookUp( xFieldName ,  "" , "(1=1)" ) );
	  }
  
  public long resultCount(String query){
    long countValue = 0;
    try{
      ResultSet rst = executeQuery(" Select count(*) as hsp001 from (" + query + ")A ");
      if (rst.next()) countValue = rst.getLong(1);
      rst.close();
    }
    catch(SQLException sExec){
      sExec.printStackTrace();
    }
    return ( countValue );
  }

  public long tableCount( String xTableName , String xCondn){
    String completeQuery;
    completeQuery = " select count(*) as hsp001 from " + xTableName + " where " + xCondn;
    //GlaceOutLoggingStream.console(completeQuery);
    long countValue = 0;
    try{
      ResultSet rst = executeQuery( completeQuery );
      if ( rst.next() ) countValue = rst.getLong(1);
      rst.close();
    }
    catch(SQLException sExec){
      sExec.printStackTrace();
    }
    return ( countValue );
  }

  public long tableCount( String xTableName ){
    return ( tableCount(  xTableName , "(1=1)" ) );
  }
  public double tableSum( String xFieldName , String xTableName , String xCondn){
    String completeQuery;
    completeQuery = " select sum(" + xFieldName + ") as hsp001 from " + xTableName + " where " + xCondn;
    double lookUpValue = 0;
    try{
      ResultSet rst = executeQuery( completeQuery );
      if ( rst.next() )  lookUpValue = rst.getDouble("hsp001");
      rst.close();
    }catch(Exception sExec){
      sExec.printStackTrace();
    }
    return ( lookUpValue );
  }
  public Statement getStatement() throws SQLException{
	return dbConn.getStatement();
  }
  public double tableSum( String xFieldName , String xTableName ) {
    return ( tableSum( xFieldName ,  xTableName , "(1=1)" ) );
  }

  public void beginTrans() throws SQLException{
      dbConn.beginTrans();
  }

  public void commitTrans() throws SQLException{
      dbConn.commitTrans();
  }

  public void rollbackTrans() throws SQLException{
      dbConn.rollbackTrans();
  }

  public void destroy() throws Exception{
    dbConn.closeReferences();
    dbConn = null;
    OpenConnections = OpenConnections - 1;
    //System.gc();
    //Debug.writeToFile("/share/Glace/ConnectionLog.txt","Connection closed for "+ tmpConnectionString + " from "  + requestPage);
    //Debug.writeToFile("/share/Glace/ConnectionLog.txt","Connections left opened = " + OpenConnections );
    //Debug.writeToFile("/share/Glace/ConnectionLog.txt","------------------------------------" );
  }

  public void closeReference() throws Exception{
    dbConn.closeReferences();
    sta    = null;  
    dbConn = null;
    OpenConnections = OpenConnections - 1;
    //Debug.writeToFile("/share/Glace/ConnectionLog.txt","Connection closed for "+ tmpConnectionString + " from "  + requestPage);
    //Debug.writeToFile("/share/Glace/ConnectionLog.txt","Connections left opened = " + OpenConnections );
    //Debug.writeToFile("/share/Glace/ConnectionLog.txt","------------------------------------" );
  }

  public void closeReferences() throws Exception{
    dbConn.closeReferences();
    sta    = null;  
    dbConn = null;
    OpenConnections = OpenConnections - 1;
    //Debug.writeToFile("/share/Glace/ConnectionLog.txt","Connection closed for "+ tmpConnectionString + " from "  + requestPage);
    //Debug.writeToFile("/share/Glace/ConnectionLog.txt","Connections left opened = " + OpenConnections );
    //Debug.writeToFile("/share/Glace/ConnectionLog.txt","------------------------------------" );
  }
  
  public String executeQueryToXML(String qry)throws Exception{
	  	StringBuffer resultXML = new StringBuffer();
	  	ResultSet rst = executeQuery( qry );
	  	int colCount = rst.getMetaData().getColumnCount();
	  	resultXML.append("<table>");
	  	resultXML.append("<row>");
	  	for(int colIndex=1;colIndex<=colCount;colIndex++){
	  		resultXML.append("<field>").append(rst.getMetaData().getColumnName(colIndex));
	  		resultXML.append("</field>");
	  	}
	  	resultXML.append("</row>");
	  	while(rst.next()){
	  		resultXML.append("<row>");
	  		for(int colIndex=1;colIndex<=colCount;colIndex++){
	  	  		resultXML.append("<field>").append(rst.getString(colIndex));
	  	  		resultXML.append("</field>");
	  		}
	  		resultXML.append("</row>");
	  	}
	  	resultXML.append("</table>");
	  	return resultXML.toString();
	  }
  
    public void setPrepareParam(int index, String value) throws SQLException{
		  dbConn.setPrepareParam(index,value);
  }

  public void setPrepareParam(int index, double value) throws SQLException{
	  dbConn.setPrepareParam(index,value);
   }

  public void setPrepareParam(int index, int value) throws SQLException{
	  dbConn.setPrepareParam(index,value);
  }
  
  public void setPrepareParam(int index, Date value) throws SQLException{
	  dbConn.setPrepareParam(index,value);
  }
  
  public void setPrepareParam(int index, BigDecimal value) throws SQLException{
	  dbConn.setPrepareParam(index,value);
  }
  
  public int executePrepare() throws SQLException{
	  int updateCount = -1;
		  updateCount = dbConn.executePrepare();
//		  GlaceOutLoggingStream.console("preparestmt executed-->"+updateCount);
	  return updateCount;
  }
  public ResultSet getPrepareResults() throws Exception{
		  return dbConn.getPrepareResults();
  }
  public void beginTransaction(){
	try{
			dbConn.beginTransaction();
	}
	catch(Exception E){
//		GlaceOutLoggingStream.console("Exception in beginTransaction:"+E.getMessage());
	}
}
public void commit(){
	try{
	dbConn.commit();
	}
	catch(Exception E){
//		GlaceOutLoggingStream.console("Exception in commit:"+E.getMessage());
	}
}
public void rollback(){
	try{
			dbConn.rollback();
	}
	catch(Exception E){
//		GlaceOutLoggingStream.console("Exception in rollback:"+E.getMessage());
	}
}

/**
 * Method to create a PrepareStatement object with No generated keys
 * @param query SQL query
 * @throws SQLException
 */
public void prepareStatement(String query) throws SQLException{
	if(prestatement != null)
		prestatement.close();
	prestatement = getConnection().prepareStatement(query,Statement.NO_GENERATED_KEYS);
}
/**
 * Method to Establish connection with common database
 * @return Connection to the database
 * @throws SQLException
 */
public Connection getConnection() throws SQLException {
//	connection = getConnection(database,user); 
	return dbConn.getConnection();
}

/**
 * Method to execute PrepareStatement
 * @param String Query
 * @throws SQLException
 */


public void setDBConnPrepareStmt(String query) throws Exception{
	if(prestatement != null)
		prestatement.close();
    dbConn.prepareStatement(query);
}






/**
 * Method to set the String parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setString(int index,String value) throws SQLException{
	prestatement.setString(index,value);			
}
/**
 * Method to set the int parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setInt(int index,int value) throws SQLException{
	prestatement.setInt(index,value);
}
/**
 * Method to set the Date parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setDate(int index,Date value) throws SQLException{
	prestatement.setDate(index,value);
}
/**
 * Method to set the Time parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setTime(int index,Time value) throws SQLException{
	prestatement.setTime(index,value);
}
/**
 * Method to set the Timestamp parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setTimestamp(int index,Timestamp value) throws SQLException{
	prestatement.setTimestamp(index,value);
}
/**
 * Method to set the Double parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setDouble(int index,double value) throws SQLException{
	prestatement.setDouble(index,value);
}
/**
 * Method to set the Float parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setFloat(int index,float value) throws SQLException{
	prestatement.setFloat(index,value);
}
/**
 * Method to set the Short parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setShort(int index,short value) throws SQLException{
	prestatement.setShort(index,value);
}
/**
 * Method to set the Long parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setLong(int index,long value) throws SQLException{
	prestatement.setLong(index,value);
}
/**
 * Method to set the Boolean parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setBoolean(int index,boolean value) throws SQLException{
	prestatement.setBoolean(index,value);
}
/**
 * Method to set the Byte parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
public void setByte(int index,byte value) throws SQLException{
	prestatement.setByte(index,value);
}
/**
 * Method to set Null value at specified index
 * @param index index
 * @param type SQL Type
 * @throws SQLException
 */
public void setNull(int index , int type) throws SQLException{
	prestatement.setNull(index , type);
}

/**
 * Method to set the Object parameter to PrepareStatement
 * @param index index
 * @param value value
 * @throws SQLException
 */
//public void setObject(int index,Object value) throws SQLException{
//	callablestmt.setObject(index,value);
//}
/**
 * Method to execute the PrepareStatement
 * @return rowcount
 * @throws SQLException
 */
public int executePreparedUpdate() throws SQLException{
	int rowcount = -1;
	rowcount = prestatement.executeUpdate();
	return rowcount;
}

/**
 * Method to get the auto generated keys
 * @return ResultSet
 * @throws SQLException
 */
public ResultSet getGeneratedKeys() throws SQLException{
	ResultSet result = null;
	result = prestatement.getGeneratedKeys();
	return result;
}
/**
 * Method to get the auto generated keys with given sequence
 * @param sequence sequence name of table from table bean
 * @return auto generated key
 * @throws SQLException
 */
public int getGeneratedKeys(String sequence) throws SQLException{
	int key = -1;
	ResultSet result = executeQuery("select currval('"+ sequence +"')");
	if(result.next())
		key = Integer.parseInt(result.getString(1));
	result.close();
	return key;
}
/**
 * Method to execute the PrepareStatement
 * @return ResultSet
 * @throws SQLException
 */
public ResultSet executePreparedQuery() throws SQLException{
	ResultSet result = null;
	result = prestatement.executeQuery();
	return result;
}

//new
public Vector executeQueryToVectorMap(String xQuery) throws Exception{
	  Vector result = new Vector();
	  ResultSet rst = executeQuery( xQuery );
	  LinkedHashMap row;
	  int colIndex;
	  int rowCount = 0;
	  int colCount;
	  colCount = rst.getMetaData().getColumnCount();
	  while (rst.next()){
	      row = new LinkedHashMap();
	      for ( colIndex=1; colIndex <= colCount; colIndex++){
		  if(rst.getString(colIndex) != null && rst.getString(colIndex) != "") 
		      row.put(rst.getMetaData().getColumnName(colIndex),rst.getString(colIndex));
		  else
		      row.put(rst.getMetaData().getColumnName(colIndex)," ");
	      }
	      result.add(rowCount++,row);
	  }
	  rst.close();
	  rst = null;
	  return result;
}


public ArrayList<ArrayList> executeQueryToArrayList(String xQuery) throws Exception{
	
	ArrayList<ArrayList> result = new ArrayList<ArrayList>();
    ArrayList<String> row;
    
    int colIndex;
    int rowCount = 0;
    int colCount;
    
    ResultSet rst = null;
    try{
    	rst = executeQuery( xQuery );
    	colCount = rst.getMetaData().getColumnCount();
    
    	while (rst.next()){
    	     
    		row = new ArrayList<String>();
    	    for ( colIndex=1; colIndex <= colCount; colIndex++){
    	    	if(rst.getString(colIndex) != null && rst.getString(colIndex) != "") 
    	        	row.add(colIndex-1,rst.getString(colIndex));
    	        else
    	        	row.add(colIndex-1," ");    	    
    	    }
    	    result.add(rowCount++,row);
    	}
    }catch(Exception e){
    	throw e;
    }finally{
    	if(rst!=null)rst.close();
    	rst = null;
    }
    return result;
  }  


public ArrayList executeQueryToArrayList(String xQuery , boolean  isNameKey ) throws Exception{   
	if (isNameKey ){	  
		ArrayList<HashMap> result = new ArrayList<HashMap>();
		HashMap<String,String> row;
		int colIndex;
		int rowCount = 0;
		int colCount;		
		ResultSet rst = null;
		try{
			rst = executeQuery( xQuery );
			colCount = rst.getMetaData().getColumnCount();
			while (rst.next()){
			      row = new HashMap<String,String>();
			      for ( colIndex=1; colIndex <= colCount; colIndex++){
				  if(rst.getString(colIndex) != null && rst.getString(colIndex) != "") 
				      row.put(rst.getMetaData().getColumnName(colIndex),rst.getString(colIndex));
				  else
				      row.put(rst.getMetaData().getColumnName(colIndex)," ");
			      }
			      result.add(rowCount++,row);
			  }	  
		}catch(Exception e){
			throw e;
		}finally{
			if(rst!=null)rst.close();
	    	rst = null;
		}
		return result;
	}else{
	    return executeQueryToArrayList(xQuery);
	}
}
}