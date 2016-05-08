package com.glenwood.dbcomparision;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;





public class HUtil {
  public static final String CTRL_FOCUS_BLUR_EVENT  =  " onfocus='if (!document.layers)  this.style.backgroundColor=\"#FFFF80\";' onblur='if (!document.layers)  this.style.backgroundColor=\"white\";' ";
  public static final String CTRL_FOCUS_SCRIPT      =  " if (!document.layers) this.style.backgroundColor=\"#FFFF80\";";
  public static final String CTRL_BLUR_SCRIPT       =  " if (!document.layers) this.style.backgroundColor=\"white\"; ";
  public static final DecimalFormat f = new DecimalFormat("#0.##");
  public static final int heightConstant = 12;
  public static final int weightConstant = 16;
  public static final double gramToLbsConversion = 0.00220462262;
  public static final double gramToOunceConversion = 0.035274;
  public static final double gramToKgConversion = 0.001;
  public static final double cmToFeetConversion = 0.032808399;
  public static final double cmToInchesConversion = 0.393700787;
  private static final String ALPHA_NUM =  "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  
  public static String handleSymbols(String xValue){
	String newVal     = "";
	String newChar    = "";
	int initPosition  = 0;
	int finalPosition = xValue.length();
	for ( int i = initPosition; i<finalPosition; i++ ){
	    newChar = xValue.substring(i,i+1);
	    if  (newChar.equals("'") )
		newVal = newVal + "\\'";
	    else if (newChar.equals("\\") ){
		newVal = newVal + "\\";
		newVal = newVal + newChar ;
	    }
	    else
		newVal = newVal + newChar ;
	}
	return newVal;
    }


  public static String handleJSONSymbols(String xValue){
		String newVal     = "";
		String newChar    = "";
		int initPosition  = 0;
		int finalPosition = xValue.length();
		for ( int i = initPosition; i<finalPosition; i++ ){
		    newChar = xValue.substring(i,i+1);
		    if  (newChar.equals("'") )
			newVal = newVal + "\\'";
		    else if (newChar.equals("\\") ){
			newVal = newVal + "\\";
			newVal = newVal + newChar ;
		    }
		    else if(newChar.equals(">"))
		    	newVal = newVal +"";
			else
		    	newVal = newVal + newChar ;
		}
		return newVal;
	    }


  	public static String PhoneNo(String phoneno)
  	{
  		try{
  		String tokens[]=new String[10];
  		String newphoneno=new String();
  		tokens=phoneno.split("-");
  		int count=4;
  		count=tokens.length;
  		if(count>1)
  			{
  				if(tokens[0].length()>0)
  				newphoneno="("+tokens[0]+")";
  				if(tokens[1].length()>0)
  					newphoneno=newphoneno+" "+tokens[1];
  				for(int i=2;i<count;i++)
  					{
  						if(tokens[i].length()>0)
  							{
  								if(tokens[i-1].length()>0)
  									newphoneno=newphoneno+"-"+tokens[i];
  								else
  									newphoneno=newphoneno+tokens[i];
							}
  					}

  				if(count>=3){
  				if(tokens[1].length()!=3 || tokens[2].length()!=4)
  					return phoneno;
  				else
  					return newphoneno;
  				}
  				else
  					return newphoneno;
  			}
  		else
  			return phoneno;
  		}
  		catch (Exception e) {
			e.printStackTrace();
			return phoneno;
		}

  	  	  }

  	public static String[] SplitPhone(String phoneno)
  	{
  		int i=0;
  		String ph[]=new String[4];
  		String ph1[]=new String[4];
  		String s=phoneno;
  		for(i=0;i<=3;i++)
  		ph1[i]="";

		ph=s.split("-");
  		if(s.length() > 0 ){
  		if(ph.length== 4 && s.charAt(s.length()-1)=='-')
  		{
  		for(i=0;i<3;i++)
  		ph1[i]=ph[i];
  		//ph1[3]="";
  		}
  		else if(ph.length==4)
  		{
  		for(i=0;i<4;i++)
  			ph1[i]=ph[i];
  		}
  		}
  		return ph1;
  	}


    public static String Nz(String xValue1, String xValue2){
	if ( checkNull(xValue1) )
	    return xValue2;
	else
	    return xValue1;
    }

    public static String Nz(Object xValue1, String xValue2){
	if(xValue1 == null) return xValue2;
	String tmpVal = xValue1.toString();
	if ( checkNull(tmpVal) )
	    return xValue2;
	else
	    return tmpVal;
    }

    public static String ValidateSingleQuote(String xValue1,int level){
    	if(xValue1!=null && xValue1.indexOf('\'')!=-1){
	    	StringBuilder levels=new StringBuilder();
	    	levels.append("''");
	    	for(int lp=0;lp<(2*(level-1));lp++){
	    		levels.append('\'');
	    	}
	    	xValue1=xValue1.replaceAll("'",levels.toString());
    	}
    	return xValue1;
    }
    public static String ValidateSingleQuote(String xValue1){
    	return xValue1.replaceAll("'","''");
    }
    public static String EscapeSingleQuote(String xValue1){
    	return xValue1.replaceAll("'","\\\'");
    }
    public static String EscapeDoubleQuote(String xValue1){
    	return xValue1.replaceAll("\"","\\\\\"");
    }
    public static boolean checkNull(String xParam){
	xParam = (xParam != null) ? xParam.trim() : xParam  ;
	if (xParam == null || xParam.length() == 0 || xParam == "")
	    return true;
	else
	    return false;
    }

    public static Object IIF(boolean xCondn , Object xValueIfTrue , Object xValueIfFalse){
	return ( (xCondn) ? xValueIfTrue : xValueIfFalse ) ;
    }


    public static boolean IIF(boolean xCondn , boolean xValueIfTrue , boolean xValueIfFalse){
	return ( (xCondn) ? xValueIfTrue : xValueIfFalse ) ;
    }

    public static String IIF(boolean xCondn , String xValueIfTrue , String xValueIfFalse){
	return ( (xCondn) ? xValueIfTrue : xValueIfFalse ) ;
    }

    public static int IIF(boolean xCondn , int xValueIfTrue , int xValueIfFalse){
	return ( (xCondn) ? xValueIfTrue : xValueIfFalse ) ;
    }

    public static String handleSymbolsForInput(String xValue){
	String newVal     = "";
	String newChar    = "";
	int initPosition  = 0;
	int finalPosition = xValue.length();
	for ( int i = initPosition; i<finalPosition; i++ ){
	    newChar = xValue.substring(i,i+1);
	    if  (newChar.equals("\"") )
		newVal = newVal + "&quot;";
	    else
		newVal = newVal + newChar ;
	}
	return newVal;
    }

    public static String getSessionList(HttpServletRequest request)
    {
	HttpSession session = request.getSession();
	String list="<font color='blue'>Session Attributes List</font><BR>";
	Enumeration enumeration = session.getAttributeNames();
	while (enumeration.hasMoreElements()){
	    String key = (String) enumeration.nextElement();
	    list += "<B>" + key + "</B>  = " + session.getAttribute(key)+"<BR>";
	}
	return list;
    }

    public static String getRequestAttributeList(HttpServletRequest request)
    {
	String list="<font color='blue'>Request Attributes List</font><BR>";
	Enumeration enumeration = request.getAttributeNames();
	while (enumeration.hasMoreElements()){
	    String key = (String) enumeration.nextElement();
	    list += "<B>" + key + "</B>  = " + request.getAttribute(key) +"<BR>";
	}
	return list;
    }

    public static String getRequestParamList(HttpServletRequest request)
    {
	String list="<font color='blue'>Request Parameters List</font><BR>";
	Enumeration enumeration = request.getParameterNames();
	while (enumeration.hasMoreElements()){
	    String key = (String) enumeration.nextElement();
	    list += "<B>" + key + "</B>  = " + request.getParameter(key) +"<BR>";
	}
	return list;
    }

    public static String appendSpaces( String xStr ,int maxLength ){
        if (xStr == null) xStr = "";
	for (int i = xStr.length(); i <= maxLength ; i++ ){
	    xStr = xStr + "&nbsp;";
	}
        return (xStr);
    }

    public static String setTitleandWindowStatus(String statusText){
      return (" Title='" + statusText + "' onmouseover=\"window.status='" + statusText + "';return true;\" onmouseout=\"window.status='';return true;\" ");
    }

    public static Object nz(Object obj,Object nullVal){
	if(obj==null)
	    return nullVal;
	if(obj instanceof String){
	    if(((String)obj).length()==0)
		return nullVal;
	}
	return obj;
    }

    public static String Replace( String xStringValue, String xReplaceString, String xNewString ){
        int startPosition, stringLength;
        stringLength    = xReplaceString.length();
        if(xStringValue.indexOf( xReplaceString )!= -1 ){
            startPosition   = xStringValue.length();
            startPosition   = xStringValue.indexOf( xReplaceString );
            xStringValue    = xStringValue.substring(0,startPosition) + xNewString + Replace( xStringValue.substring( startPosition + stringLength, xStringValue.length()), xReplaceString, xNewString);
        }
        return xStringValue;
    }

    public static String ReplaceString( String xStringValue ){
        String[] replaceArray = new String[] { "E", "X", "T", "N", "e", "x", "t", "n", "(", ")", " ", "-" };
	for( int i=0;i < replaceArray.length ; i++ ){
	    xStringValue = Replace(xStringValue, replaceArray[i],"");
	}
	return xStringValue;
     }

    public static String FormatPhoneNumber(String xPhoneNo){
	  String formatedPhoneNo;
      formatedPhoneNo = xPhoneNo;
      if(xPhoneNo == null || xPhoneNo.equals("-1"))
	  return "";
      if (! checkNull( xPhoneNo ) ){
          xPhoneNo = ReplaceString( xPhoneNo );
	  xPhoneNo = xPhoneNo.trim();
 	  formatedPhoneNo = xPhoneNo;

	  if(xPhoneNo.indexOf('(') >= 0 || xPhoneNo.indexOf('-') >= 0 || xPhoneNo.indexOf(')') >= 0)
	      return formatedPhoneNo;

	  if ( xPhoneNo.length() == 7 )
	      formatedPhoneNo = xPhoneNo.substring(0,2) + "-" + xPhoneNo.substring(3,xPhoneNo.length()-1);
	  else if( xPhoneNo.length() > 7 && xPhoneNo.length() <= 10 )
	      formatedPhoneNo = "(" + xPhoneNo.substring(0,3) + ") " + xPhoneNo.substring(3,6) +  "-" + xPhoneNo.substring(6,xPhoneNo.length());
	  else if( xPhoneNo.length() > 10 )
	      formatedPhoneNo = "(" + xPhoneNo.substring(0,3) + ") " + xPhoneNo.substring(3,6) +  "-" + xPhoneNo.substring(6,10) +  " Extn-" + xPhoneNo.substring(10,xPhoneNo.length());
      }
      return formatedPhoneNo;
    }
/*    public static String FormatPhoneNumber(String xPhoneNo,HttpSession session){
        String formatedPhoneNo="";
    	TimeZoneContext TZContext = (TimeZoneContext)session.getAttribute("TZContext");
    	if(TZContext.getLocaltimeZone().equals("IST")){
    		if(xPhoneNo.length()>0)
    		if(xPhoneNo.indexOf("-")>=0)
    		{
    			String phoneNo[]=xPhoneNo.split("-");
    			formatedPhoneNo=phoneNo[0]+" Extn-"+phoneNo[1];
    		}
    		else
    			formatedPhoneNo=xPhoneNo;

    	}
    	else{

        formatedPhoneNo = xPhoneNo;
        if(xPhoneNo == null || xPhoneNo.equals("-1"))
  	  return "";
        if (! checkNull( xPhoneNo ) ){
            xPhoneNo = ReplaceString( xPhoneNo );
  	  xPhoneNo = xPhoneNo.trim();
   	  formatedPhoneNo = xPhoneNo;

  	  if(xPhoneNo.indexOf('(') >= 0 || xPhoneNo.indexOf('-') >= 0 || xPhoneNo.indexOf(')') >= 0)
  	      return formatedPhoneNo;

  	  if ( xPhoneNo.length() == 7 )
  	      formatedPhoneNo = xPhoneNo.substring(0,2) + "-" + xPhoneNo.substring(3,xPhoneNo.length()-1);
  	  else if( xPhoneNo.length() > 7 && xPhoneNo.length() <= 10 )
  	      formatedPhoneNo = "(" + xPhoneNo.substring(0,3) + ") " + xPhoneNo.substring(3,6) +  "-" + xPhoneNo.substring(6,xPhoneNo.length());
  	  else if( xPhoneNo.length() > 10 )
  	      formatedPhoneNo = "(" + xPhoneNo.substring(0,3) + ") " + xPhoneNo.substring(3,6) +  "-" + xPhoneNo.substring(6,10) +  " Extn-" + xPhoneNo.substring(10,xPhoneNo.length());
        }
    	}
        return formatedPhoneNo;
      }*/

/*    public static PatientDetailModel loadPatientDetail(int patientId,HttpServletRequest request)throws Exception{
	HttpSession session = request.getSession(true);
	if(patientId==-1)
	    throw new Exception("No patient found. Please select one patient");
	PatientDetailModel patientDetail = (PatientDetailModel)session.getAttribute("patientDetail");
	boolean newPatient = false;
	if(patientDetail==null)
	    newPatient = true;
	else if(patientDetail.getPatientId()!=patientId)
	    newPatient = true;
	if(newPatient){
	    patientDetail = PatientDetailModel.getInstance(patientId,session.getAttribute("connectString").toString());
	    session.setAttribute("patientDetail",patientDetail);
	}
	return patientDetail;
    }*/

/*    public static Set getReportColumns(long reportId,HttpSession session, HttpServletRequest request ) throws Exception{
    	DataBaseUtils dbUtil = null;
    	ResultSet rst;
    	Set result = new HashSet();
    	try{
    		dbUtil = new DataBaseUtils(session.getAttribute("connectString").toString());
    		rst  = dbUtil.executeQuery("SELECT report_output_field_name as fieldname from report_output_field where report_output_field_report_id="+reportId + " and report_output_field_isactive = 't'");
    		while(rst.next()){
    			result.add(rst.getString("fieldname"));
    		}rst.close();

    	}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(dbUtil!=null)
			dbUtil.destroy();
			dbUtil = null;
		}
		return result;
    }*/

    /*public static long exportReportData(String xml,String query,HttpSession session) throws Exception{
    	long insertedRowId = -1;
    	String qry = "insert into reportexport values ((select nextval('reportexport_reportexport_id_seq')),'"+xml.replaceAll("'", "''")+"','"+query.replaceAll("'", "''")+"')";
    	DataBaseUtils dbUtils = null;

    	try{
    		dbUtils = new DataBaseUtils("" + session.getAttribute("connectString"));
    		insertedRowId = dbUtils.executeAndGetGeneretedKey(qry, "reportexport_id");
    		GlaceOutLoggingStream.console("insertedRowId------->"+insertedRowId);
    	}catch (Exception e) {
    		GlaceOutLoggingStream.console("Exception in exportReportData-------->");
			e.printStackTrace();
		}finally{
			if(dbUtils != null)
				dbUtils.destroy();
			dbUtils = null;
		}
    	return insertedRowId;

    }*/


    /**
     * This Funciton is used to Generate to FileName for GenReporte
     * @author APM
     * @param  xFileName String temporary file (Logical Report Name)
     */

/*    public static String genReportFileName(String xFileName , String reportId , HttpSession session, HttpServletRequest request ) throws Exception{
    Calendar calendar = new GregorianCalendar();
	String month = "" + (calendar.get(Calendar.MONTH) + 1 );
	String year  = "" + calendar.get(Calendar.YEAR);
	String date  = "" + calendar.get(Calendar.DATE);
	String hour  = "" + calendar.get(Calendar.HOUR_OF_DAY);
	String minute= "" + calendar.get(Calendar.MINUTE);
	String second= "" + calendar.get(Calendar.SECOND);
	String tmpVariable = "" + appendZero(month) + appendZero(date) + year + appendZero(hour) + appendZero(minute) + appendZero(second) + ".html";
	String critDate=HUtil.Nz(request.getParameter("txtDate"),"");
	String dayCritDate = HUtil.Nz(request.getParameter("txtEndDate"),"");
	//[MRK
	//String fileName ="/var/tomcat4/webapps/hcareversion/shared/reports/genreports/"+ tmpVariable;
	String fileName =SharedFolderUtil.getSharedFolderLocation(request) + "/reports/genreports/"+ tmpVariable;
	//MRK]
	DataBaseUtils dbUtil = null;
	String qry = "insert into generated_reports values(-1," + reportId + ",'" + xFileName + "','" + tmpVariable + "','" + DateUtil.formatDateTime(new Date(), 3 ) + "'," + session.getAttribute("loginId") + ",true," + session.getAttribute("selPractice") + ")";
	try{
		dbUtil = new DataBaseUtils(session.getAttribute("connectString").toString());
	    dbUtil.execute(qry);
	    Hashtable accessHash = 	(Hashtable)session.getAttribute("accessCodes");
	   if(Integer.parseInt(HUtil.Nz(reportId,"0")) >= 5000 && HUtil.Nz(accessHash.get("2004"),"-1").equals("2004"))
	    {
	    String reportName=HUtil.Nz(dbUtil.tableLookUp("report_available_report_action","report_available","report_available_report_id="+(Integer.parseInt(HUtil.Nz(reportId,"0"))-5000)),"");
	    if(reportName.equals("TallySheetReport.Action") && Integer.parseInt(HUtil.Nz(reportId,"0")) >= 5000)
	    {
	    	StringBuffer upQ=new StringBuffer();
	    	upQ.append("update generated_reports set generated_reports_date_created='").append(critDate).append("'::date where generated_reports_original_report_name='").append(tmpVariable).append("' and generated_reports_owner='").append(session.getAttribute("loginId")).append("'");
	    	FileBean file=FileBean.CreateFileBean(-1, FileManagerUtil.getdateformat(),false , "reports/genreports", tmpVariable, "Tally Sheet Report", "Tally Sheet Report", 99, 97);
	    	SaveFileModel save=SaveFileModel.createModel(request);
	    	DataBaseUtils dbUtils = null;
	    	try{
	    		dbUtils = new DataBaseUtils(session.getAttribute("connectString").toString());
	       	dbUtil.executeUpdate(upQ.toString());
	       	save.savefiledetails(file, dbUtils, "-1");
	    	}
	    	catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	finally{
	    		if(dbUtils!=null)
	    		dbUtils.destroy();
	    		dbUtils=null;
	    	}
	    }
	    if(reportName.equals("DailySheetReportNew.Action") && Integer.parseInt(HUtil.Nz(reportId,"0")) >= 5000)
 	    {
 	    	StringBuffer upQ=new StringBuffer();
 	    	upQ.append("update generated_reports set generated_reports_date_created='").append(dayCritDate).append("'::date where generated_reports_original_report_name='").append(tmpVariable).append("' and generated_reports_owner='").append(session.getAttribute("loginId")).append("'");
 	    	FileBean file=FileBean.CreateFileBean(-1, FileManagerUtil.getdateformat(),false , "reports/genreports", tmpVariable, "Day Sheet Report", "Day Sheet Report", 99, 122);
 	    	SaveFileModel save=SaveFileModel.createModel(request);
 	    	DataBaseUtils dbUtils = null;
 	    	try{
 	    		dbUtil = new DataBaseUtils(session.getAttribute("connectString").toString());
 	       	dbUtil.executeUpdate(upQ.toString());
 	       	save.savefiledetails(file, dbUtils, "-1");
 	    	}
 	    	catch(Exception e){
 	    		e.printStackTrace();
 	    	}
 	    	finally{
 	    		if(dbUtil!=null)
 	    		dbUtil.destroy();
 	    		dbUtil = null;
 	    	}
 	    }
	    }
	}catch (Exception e){
	    throw e;
	}finally{
		if(dbUtil!=null)
	    dbUtil.destroy();
		dbUtil = null;
	}
	return fileName;
    }*/

/*    public static boolean insertintoGeneratedReports (String xFileName , String reportId , HttpSession session, HttpServletRequest request, String owner ) throws Exception{
       	String orgFileName = xFileName+".html";
    	DataBaseUtils dbUtil = null;
    	String qry = "insert into generated_reports values(-1," + reportId + ",'" + xFileName + "','" + orgFileName + "','" + DateUtil.formatDateTime(new Date(), 1 ) + "'," + owner + ",true," + session.getAttribute("selPractice") + ")";
    	try{
    		dbUtil = new DataBaseUtils(session.getAttribute("connectString").toString());
    	    dbUtil.execute(qry);
    	}catch (Exception e){
    	    throw e;
    	}finally{
    		if(dbUtil!=null)
    	    dbUtil.destroy();
    		dbUtil = null;
    	}

    	return true;
        }*/

/*    public static boolean insertintoGeneratedReportsPayroll (String xFileName , String reportId , HttpSession session, DataBaseUtils dbUtil, String owner ) throws Exception{
       	String orgFileName = xFileName+".html";
    	String qry = "insert into generated_reports values(-1," + reportId + ",'" + xFileName + "','" + orgFileName + "','" + DateUtil.formatDateTime(new Date(), 1 ) + "'," + owner + ",true," + session.getAttribute("selPractice") + ")";
    	try{
    	    dbUtil.execute(qry);
    	}catch (Exception e){
    	    throw e;
    	}

    	return true;
        }*/


/*    public static boolean insertintoPayrollLog (int groupNo,String fromDate,String toDate,HttpSession session, HttpServletRequest request)throws Exception{

    	DataBaseUtils dbUtil = null;
    	String qry = "insert into payroll_log (groupno, fromdate, todate, generatedon, generatedby ) values ("+groupNo+",'"+fromDate+"','"+toDate+"','"+DateUtil.formatDateTime(new Date(), 1 )+"','"+session.getAttribute("loginName")+"')";
    	GlaceOutLoggingStream.console("payroll log qry---------->"+qry);
    	try{
    		dbUtil = new DataBaseUtils(session.getAttribute("connectString").toString());
    	    dbUtil.execute(qry);
    	}catch (Exception e){
    	    throw e;
    	}finally{
    		if(dbUtil!=null)
    	    dbUtil.destroy();
    		dbUtil=null;
    	}
    	return true;
    }*/

/*    public static boolean insertintoPayrollLogNew (int groupNo,String fromDate,String toDate,HttpSession session,DataBaseUtils dbUtil)throws Exception{
    	String qry = "insert into payroll_log (groupno, fromdate, todate, generatedon, generatedby ) values ("+groupNo+",'"+fromDate+"','"+toDate+"','"+DateUtil.formatDateTime(new Date(), 1 )+"','"+session.getAttribute("loginName")+"')";
    	GlaceOutLoggingStream.console("payroll log qry---------->"+qry);
    	try{
    	    dbUtil.execute(qry);
    	}catch (Exception e){
    	    throw e;
    	}
    	return true;
    }*/





/*    public static boolean insertintoGeneratedReports (String xFileName ,String yGroupName, String reportId , HttpSession session, HttpServletRequest request, String owner ) throws Exception{
    	String orgFileName = "Group "+yGroupName.substring(1);
    	DataBaseUtils dbUtil = null;
    	String qry = "insert into generated_reports values(-1," + reportId + ",'" + xFileName + "','" + orgFileName + "','" + DateUtil.formatDateTime(new Date(), 1 ) + "'," + owner + ",true," + session.getAttribute("selPractice") + ")";
    	try{
    		dbUtil = new DataBaseUtils(session.getAttribute("connectString").toString());
    	    dbUtil.execute(qry);
    	}catch (Exception e){
    	    throw e;
    	}finally{
    		if(dbUtil!=null)
    	    dbUtil.destroy();
    		dbUtil=null;
    	}

    	return true;
        }
*/



    public static String appendZero(String xValue){
	String rValue="";
	if (xValue.length() == 1)
	    rValue = "0"+xValue;
	else
	    rValue = xValue;
	return rValue;
    }
    public static String formatDecimals( String xValue, int xNoOfDecimals){
        int dotPosition,noOfDecimalAfterDot;
	String xValueStr = xValue;
	dotPosition = xValueStr.indexOf(".");

	if(xNoOfDecimals == 0){
	    return xValueStr.substring(0 , xValueStr.indexOf(".") > 0 ? xValueStr.indexOf(".")  : xValueStr.length());

	}

	if( dotPosition == -1 ){
	    xValueStr += ".";
	    for (int i = 0 ; i < xNoOfDecimals; i++){
		xValueStr += "0";
	    }
	}
	if ( dotPosition == 0 ){
	    xValueStr   = "0" + xValueStr;
	    dotPosition = xValueStr.indexOf(".");
	}
	else{
	    noOfDecimalAfterDot = xValueStr.length() - (xValueStr.indexOf(".") + 1);
	    if(( noOfDecimalAfterDot < xNoOfDecimals ) && (noOfDecimalAfterDot > 0)){
		for(int i=noOfDecimalAfterDot;i<xNoOfDecimals;i++)   xValueStr += "0";
 	    }
	    else{
	 	xValueStr = xValueStr.substring(0, xValueStr.length() - ( noOfDecimalAfterDot - xNoOfDecimals ) );
	    }
	}
	return xValueStr;
    }

    public static double toDecimalPrecision( double xValue, int xNoOfDecimals) {

	BigDecimal bd = new BigDecimal(xValue);
	bd = bd.setScale(xNoOfDecimals,BigDecimal.ROUND_HALF_UP);
	xValue = bd.doubleValue();
	return xValue;
    }
    public static String toDecimalPrecision( double xValue, int xNoOfDecimals,int x) {
    	BigDecimal bd = new BigDecimal(xValue);
    	bd = bd.setScale(xNoOfDecimals,BigDecimal.ROUND_HALF_UP);
    	return bd.toString();
        }

    public static String formatCurrency( String xValue, int xNoOfDecimals,String xCurrency) {
    	int dotPosition,noOfDecimalAfterDot;
        double tempVal = Double.parseDouble(HUtil.Nz(xValue,"0"));
        //String xValueStr = xValue;
        String xValueStr = "" + toDecimalPrecision(tempVal,xNoOfDecimals,1);
        if (xValue.equals("") || xValue==null || xValue.equals("null") )
	    xValueStr = "0";
	dotPosition = xValueStr.indexOf(".");
	if( dotPosition == -1 ){
	    xValueStr += ".";
	    for (int i = 0 ; i < xNoOfDecimals; i++){
		xValueStr += "0";
	    }
	}
	if ( dotPosition == 0 ){
	    xValueStr   = "0" + xValueStr;
	    dotPosition = xValueStr.indexOf(".");
	}
	else{
	    noOfDecimalAfterDot = xValueStr.length() - (xValueStr.indexOf(".") + 1);
	    if(( noOfDecimalAfterDot < xNoOfDecimals ) && (noOfDecimalAfterDot > 0)){
		for(int i=noOfDecimalAfterDot;i<xNoOfDecimals;i++)   xValueStr += "0";
 	    }
	    else{
	 	xValueStr = xValueStr.substring(0, xValueStr.length() - ( noOfDecimalAfterDot - xNoOfDecimals ) );
	    }
	}
	//MMM-LIN-HCARE-001[
	String xFinalStr = "" ;
	if(xValueStr.indexOf("-") != -1){
	    xValueStr = xValueStr.replace('-',' ');
	    xValueStr = xValueStr.trim();
	    xFinalStr = xCurrency + xValueStr;
	    xFinalStr = "(" + xFinalStr + ")";
	}
	else{
	    xFinalStr = xCurrency + xValueStr;
	}
	return (xFinalStr);
	//MMM-LIN-HCARE-001]
    }

    public static String currentDate(){
	Calendar calendar = new GregorianCalendar();
	String month = "" + (calendar.get(Calendar.MONTH) + 1 );
	String year  = "" + calendar.get(Calendar.YEAR);
	String date  = "" + calendar.get(Calendar.DATE);
	String tmpVariable = "" + appendZero(month) +"/" +appendZero(date) +"/"+ year;
	return tmpVariable;
    }

    public static String currentDateTime(){
	Calendar calendar = new GregorianCalendar();
	String month = "" + (calendar.get(Calendar.MONTH) + 1 );
	String year  = "" + calendar.get(Calendar.YEAR);
	String date  = "" + calendar.get(Calendar.DATE);
	String hour  = "" + calendar.get(Calendar.HOUR);
	String min  = "" + calendar.get(Calendar.MINUTE);
	String sec  = "" + calendar.get(Calendar.SECOND);
	String am  = "" + calendar.get(Calendar.AM_PM);
	if(am.equals("0")){
	    am="AM";
	}else{
	    am="PM";
	}
	String tmpVariable = "" + appendZero(month) +"/" +appendZero(date) +"/"+ year+" "+hour+":"+min+":"+sec+" "+am;
	return tmpVariable;
    }

    public static String getAlphaNumeric(int len) {  
        StringBuffer sb = new StringBuffer(len);  
        for (int i=0;  i<len;  i++) {  
           int ndx = (int)(Math.random()*ALPHA_NUM.length());  
           sb.append(ALPHA_NUM.charAt(ndx));  
        }  
        return sb.toString();  
     }  
    
    public static String encryptSessionId(String sessionId){
    	String head = getAlphaNumeric(8);
    	String tail = getAlphaNumeric(5);
    	return head + sessionId + tail;
    }

    public static String decryptSessionId(String encryptedString){
      String sessionId = "";
      if(encryptedString.length() >= 13){
      	String tmp = encryptedString.substring(8,encryptedString.length());
      	sessionId = tmp.substring(0,tmp.length()- 5);
      }
      return sessionId;
    }

    public static String convertToAnsiFormat(double value){
		String result = "";
		String tempValue = String.valueOf(value);
		if(tempValue.contains(".")){
			result = tempValue.substring(0, tempValue.indexOf("."));
			if(result.length() > 0){
				int numeric = Integer.parseInt(result);
				if(numeric ==0){
					result = "";
				}
			}
			String temp =  tempValue.substring(tempValue.indexOf(".") + 1, tempValue.length());
			char temparr[] = temp.toCharArray();
			if(temparr.length > 1 &&  temparr.length < 2){
				if(temparr[1] != '0' &&  temparr[1] != ' '){
					result = result + "." + temp;
				}
				else if(temparr[0] != '0' &&  temparr[0] != ' '){
					result = result + "." + temparr[0];
				}
			}
			else if(temparr.length > 1){
				result = String.valueOf(round(value, 2));
				if(result.startsWith("0.")){
					result = result.substring(result.indexOf("0.")+1,result.length());
				}
			}
			else if(temparr.length > 0){
				if(temparr[0] != '0' &&  temparr[0] != ' '){
					result = result + "." + temparr[0];
				}
			}
		}else{
			result = tempValue;
		}
		if(result.equals("")){
			result="0";
		}
		return result;
	}

    public static double round(double d, int decimalPlace){
	    BigDecimal bd = new BigDecimal(Double.toString(d));
	    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
	    return bd.doubleValue();
	  }


/*    public static void deleteDirectory(String path,String fileSeparator,String file) throws Exception{
		File f = new File(path);
		String fileNames[] = f.list();
		for(int i=0;i<fileNames.length;i++){
			String folderName = fileNames[i];
			if(!folderName.equals(file)){
				File tmpf = new File(path + fileSeparator + folderName);
				if(tmpf.isDirectory())
					FileUtils.deleteDirectory(tmpf);
				else
					tmpf.delete();
			}
		}
    }*/

	public static boolean isDirExist(String foldername) throws Exception{
		File folder = new File(foldername);
		if(!folder.exists()){
			folder.mkdirs();
			return false;
		}
		return true;
	}

/*    public static FileWriter openxmlPdfFileWriter(String tempFolderPath,String fileSeparator) throws Exception{
    	FileWriter xmlPdfFileWriter = null;
    	try{
		    Calendar calendar = new GregorianCalendar();
			String month = "" + (calendar.get(Calendar.MONTH) + 1 );
			String year  = "" + calendar.get(Calendar.YEAR);
			String date  = "" + calendar.get(Calendar.DATE);
			String hour  = "" + calendar.get(Calendar.HOUR_OF_DAY);
			String minute= "" + calendar.get(Calendar.MINUTE);
			String second= "" + calendar.get(Calendar.SECOND);
			SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
	        Date toDay = new Date();
	        String toDayFolder = sdf.format(toDay);
	        deleteDirectory(tempFolderPath,fileSeparator,toDayFolder);
			String xmlPdfFile = "" + HUtil.appendZero(month) + HUtil.appendZero(date) + year + HUtil.appendZero(hour) + HUtil.appendZero(minute) + HUtil.appendZero(second) + ".xml";
			String xmlPdfFilePath= tempFolderPath +fileSeparator+toDayFolder+fileSeparator;
			isDirExist(xmlPdfFilePath);
			xmlPdfFileWriter = new FileWriter(new File(xmlPdfFilePath + xmlPdfFile));
    	}catch (Exception e) {
			e.printStackTrace();
		}
    	return xmlPdfFileWriter;
    }*/

    public static void writexmlPdfFile(FileWriter xmlPdfFileWriter,String content) throws Exception{
    	try{
    		if(xmlPdfFileWriter != null)
    			xmlPdfFileWriter.write(content,0,content.length());
    	}catch (Exception e) {
    		e.printStackTrace();
		}
    }

    public static void closexmlPdfFileWriter(FileWriter xmlPdfFileWriter)throws Exception{
    	try{
    		if(xmlPdfFileWriter != null)
    			xmlPdfFileWriter.close();
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     *
     * @param firstName
     * @param lastName
     * @param middleName
     * @param type 1: <First name> <MI>[.] <Last name> <crediantials>
     * 	      type 2: <Last name> <crediantials>[.], <First name> <MI>[.]
     * @return
     */
    public static String formatName( String firstName , String lastName, String middleName ,String crediantials ,int  type ){
    	StringBuffer name = new StringBuffer();
    	middleName = Nz(middleName, "").trim();
    	crediantials= Nz(crediantials,"").trim();
    	firstName = Nz(firstName,"");
    	lastName = Nz(lastName,"");

	switch (type){
	case 1:

		if(firstName.length()>0){
			name.append(firstName.substring(0, 1).toUpperCase()).append(firstName.substring(1).toLowerCase()).append(" ");
		}
				if(middleName.length()==1)
	    {
			name.append(middleName.substring(0, 1).toUpperCase()).append(".").append(" ");
	    }
	    else if(middleName.length()>1)
	    {
	    	name.append(middleName.substring(0, 1).toUpperCase()).append(middleName.substring(1).toLowerCase()).append(" ");
	    }
		if(lastName.length()>0){
			name.append(lastName.substring(0, 1).toUpperCase()).append(lastName.substring(1).toLowerCase()).append(" ");
		}
		if (crediantials.length()>1)
		{
		name.append(crediantials.toUpperCase());
	    if(crediantials.charAt(crediantials.length()-1)!='.')
	    	name.append(".");
		}
	    //GlaceOutLoggingStream.console(name);
	    break;

	case 2:
		if(lastName.length()>0){
			name.append(lastName.substring(0, 1).toUpperCase()).append(lastName.substring(1).toLowerCase());
		}

		if (crediantials.length()>1)
		{
			name.append(" ");
			name.append(crediantials.toUpperCase());
		if(crediantials.charAt(crediantials.length()-1)!='.')
			name.append(".");
		}
		if((firstName.length()>0 || middleName.length()>0) && (lastName.length()>0 ||crediantials.length()>0)){
			name.append(",");
		}
		if(firstName.length()>0){
			name.append(" ");
			name.append(firstName.substring(0, 1).toUpperCase()).append(firstName.substring(1).toLowerCase());
		}
			if(middleName.length()==1)
		    {
			   name.append(" ").append(middleName.substring(0, 1).toUpperCase()).append(".");
		    }
		    else if(middleName.length()>1)
		    {
		       name.append(" ").append(middleName.substring(0, 1).toUpperCase()).append(middleName.substring(1).toLowerCase()).append(" ");
		    }
	    //GlaceOutLoggingStream.console(name);
	    break;
	}
	return name.toString();
    }




public static  String formatName( String firstName , String lastName, String middleName ,String crediantials ,int  type, boolean caps ){
	//String naming=formatName("first", "last", "m","M.D", 1);
	String naming= formatName(firstName, lastName, middleName, crediantials, type)	;
		if(caps==true)
		naming=naming.toUpperCase();


	return naming;
}

public static  String formatName(  String prefix,String firstName , String lastName, String middleName ,String crediantials ,int  type){
	String formattedName="";
	if(Nz(crediantials,"").length()>0)
		formattedName=formatName(firstName, lastName, middleName, crediantials, type);
	else{
		prefix=Nz(prefix,"").trim();
		if(prefix.length()>0)
			formattedName= prefix+((prefix.charAt(prefix.length()-1)=='.')?" ":". ")+formatName(firstName, lastName,middleName,"", type);
	}
	return formattedName.trim();
}


public static String SingleTokenInitCap(String token,int len){
	if(token.length()>len)
		return (token.substring(0, 1).toUpperCase()+token.substring(1).toLowerCase());
	return token.toUpperCase();
}
private static String InitCapOnSplitter(String inputStr,String spliter){
	String[] tokenlist;
	if(spliter.equals("."))
		tokenlist=inputStr.split("\\.");
	else
		tokenlist=inputStr.split(spliter);
	StringBuffer outputStr=new StringBuffer();
	for(int i=0;i<tokenlist.length;i++){
		outputStr.append(tokenlist[i]);
		if(i!=tokenlist.length-1) outputStr.append("~").append(spliter).append("~");
	}
	return outputStr.toString();
}
public static String InitCap(String inputStr){
	String[] splitters=new String[]{".","-",",","/",":"," ",};
	inputStr+="~";
	for(int i=0;i<splitters.length;i++){
		if(inputStr.contains(splitters[i]))
			inputStr=InitCapOnSplitter(inputStr,splitters[i]);
	}
	String[] tokenlist=inputStr.split("~");
	StringBuffer outputStr=new StringBuffer();
	for(int i=0;i<tokenlist.length;i++){
		outputStr.append(SingleTokenInitCap(tokenlist[i],2));
	}
	return outputStr.toString();
	}
/*public static String getPatientName(DataBaseUtils dbUtils  ,String patientId) throws Exception{
	return HUtil.Nz(dbUtils.newTableLookUp("upper(format_name( patient_registration_first_name,patient_registration_last_name,patient_registration_mid_initial,'',1)) ","patient_registration","patient_registration_id="+patientId),"");
}*/
public static String  createErrorStackTraceToString(Exception e){
    StringBuilder sb = new StringBuilder();
    //iterate the error stack and form a single string for mail
    for (StackTraceElement element : e.getStackTrace()){
	sb.append(element.toString());
	sb.append("\n");
    }
    return sb.toString();
}

public static String transformData(String XSLFile, String XMLData)throws Exception{
	StreamSource source = new StreamSource(new ByteArrayInputStream(XMLData.getBytes("UTF-8")));
	StreamSource stylesource = new StreamSource(XSLFile);
	TransformerFactory factory = TransformerFactory.newInstance();
	Transformer transformer = factory.newTransformer(stylesource);
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	StreamResult result = new StreamResult(outputStream);
	transformer.transform(source, result);
	transformer.clearParameters();
	String htmlContent = new String(outputStream.toByteArray());
	return htmlContent;
}

/*
 * height 1-->Feet and inches{eg: X ' Y ''}
 * 		  2-->Complete inches {XX.YY in}
 * 		  display--->for vitals page display{eg: XXX ' YY.YY '' XXX.YY in XXX.YY cm}
 * 		  otherwise Feet and inches{X ' Y ''}
 * 		  flowsheet----->for flowsheet page {XXX.YYYY (complete feet)}
 */
public static String heightConversion(String cmvalue,String displayUnit,String...unit){
	String feetValue = cmvalue;
	try{
	if(cmvalue.trim().equals("")){
		return "";
	}else{
		double cmValue = Double.parseDouble(cmvalue);
		double inchesValue = (cmValue/2.54);
		double feet=inchesValue/heightConstant;
		double inches= inchesValue%heightConstant;
		if(displayUnit.equals("1")){
			if(unit.length==0)
				feetValue = (int)(feet)+"'"+" "+f.format(inches)+"''";
			else
				feetValue = feet+"";
		}else if(displayUnit.equals("2")){
			if(unit.length==0){
				feetValue = f.format(inchesValue)+" "+"''";
			}
			else{
				feetValue = inchesValue+"";}
		}else if(displayUnit.equals("display")){
			feetValue = (int)(feet)+"-"+f.format(inches)+"-"+f.format(inchesValue)+"-"+f.format(cmValue);
		}else if(displayUnit.equals("flowsheet")){
			feetValue = cmValue*cmToFeetConversion+"";
		}else{
			feetValue = inchesValue+"";
		}
	}
	}catch(Exception e){}
	return feetValue;
}
/*
 * weight 1-->Lbs and Oz {eg:XXX.YY lbs XXX.YY oz}
 * 		  2-->kgs {eg: "XXXX.YY kgs"}
 * 		  display--->for vitals page display {eg: XXX lbs YY.YY oz XXX.YY oz XXX.YY kg}
 * 		  prescription--->for prescription page display {eg: XXX lbs YY.YY oz(XXX.YY kg)}
 *        flowsheet----->for flowsheet page {XXX.YY (complete lbs)}
 */
public static String weightConversion(String gramvalue,String displayUnit,String... unit){
	String lbsAndOz = gramvalue;
	try{
	if(gramvalue.trim().equals("")){
		return "";
	}else{
		double WeightinGrams = Double.parseDouble(gramvalue);
		double ounce = WeightinGrams*gramToOunceConversion;
		double lbs = ounce/weightConstant;
		double inounce = ounce%weightConstant;
		double weightinKgs = WeightinGrams*gramToKgConversion;
		if(displayUnit.equals("1")){
			if(unit.length==0)
				lbsAndOz = (int)(lbs)+" lbs"+" "+f.format(inounce)+" oz";
			else
				lbsAndOz = lbs+"";
		}else if(displayUnit.equals("2")){
			if(unit.length==0)
				lbsAndOz =f.format(weightinKgs)+" kgs";
			else
				lbsAndOz =weightinKgs+"";
		}else if(displayUnit.equals("display")){
			lbsAndOz = (int)(lbs)+"-"+f.format(inounce)+"-"+f.format(ounce)+"-"+f.format(weightinKgs); 
		}else if(displayUnit.equals("prescription") || displayUnit.equals("3")){
			lbsAndOz = (int)(lbs)+" lbs"+" "+f.format(inounce)+" oz "+"("+f.format(weightinKgs)+" kgs)"; 
		}else if(displayUnit.equals("flowsheet")){
			lbsAndOz =(WeightinGrams*gramToLbsConversion)+""; 
		}else{
			lbsAndOz = (int)(lbs)+" lbs"+" "+f.format(inounce)+" oz";
		}
	}
	}
	catch(Exception e){}
		return lbsAndOz;
	
}
public static String ValidateSpecialQuote(String result){
	Pattern pattern = Pattern.compile("[&<>'\"]+");
    Matcher matcher = pattern.matcher(result);
    if(matcher.find()) {
	      	 if(result.contains("&")) 
	        	     {
               		    result = result.replaceAll("&","&#38;");
	        	     }
             if(result.contains("<")) 
                     {
		        	    result = result.replaceAll("<","&#60;");
		        	 }
		     if(result.contains(">"))
		        	 {
		        	    result = result.replaceAll(">","&#62;");
		        	 }   
		     if(result.contains("'")) 
		        	 {
		        	    result = result.replaceAll("'","&#39;");
		        	 }
		     if(result.contains("\""))
	 	             {
		        	    result = result.replaceAll("\"","&#34;");
	 	             }
	       }
	       return result;
}
public static String ValidateSpecialCharacters(String xValue1){
	Pattern pattern = Pattern.compile("[!%\"&<>']+");
    Matcher matcher = pattern.matcher(xValue1);
    if(matcher.find()) {
	    	 if(xValue1.contains("&")) 
	        	     {
	      		 		xValue1 = xValue1.replaceAll("&","&#38;");
	        	     }
             if(xValue1.contains("'")) 
                     {
            	 		xValue1 = xValue1.replaceAll("'","&#39;");
		        	 }
             if(xValue1.contains("\"")) 
             		 {
    	 				xValue1 = xValue1.replaceAll("\"","&#34;");
        	         }
             if(xValue1.contains("%")) 
	                 {
	        	 		xValue1 = xValue1.replaceAll("%","&#37;");
		        	 }
             if(xValue1.contains("<")) 
	          		 {
	 	 				xValue1 = xValue1.replaceAll("<","&#60;");
	          		 }
             if(xValue1.contains(">")) 
          			 {
 	 					xValue1 = xValue1.replaceAll(">","&#62;");
          			 }
        }
    return xValue1;
}
public static double calculateBMI(String weightInLbs,String HeightInInches,String BMI){
	double BMIvalue = 0;
	try{
	DecimalFormat decimalFormat = new DecimalFormat("#0.##");
	double weightInLbs1 = Double.parseDouble(weightInLbs);
	String WeightLbs = decimalFormat.format(weightInLbs1*0.00220462262);
	double HeightinInches = (int)Double.parseDouble(HeightInInches);
	double weightLbs = (int)(Double.parseDouble(WeightLbs));
	//int heightInInches = (int)(HeightinInches);
	BMIvalue = (Double.parseDouble(decimalFormat.format(( weightLbs * 703 ) / ( HeightinInches * HeightinInches ))));
	}
	catch(Exception e){}
	return BMIvalue;
}
}
