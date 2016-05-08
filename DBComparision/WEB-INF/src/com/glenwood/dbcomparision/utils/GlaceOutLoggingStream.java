package com.glenwood.dbcomparision.utils;

import java.io.PrintStream;
import java.util.Vector;

 
public class GlaceOutLoggingStream extends PrintStream{


	public GlaceOutLoggingStream(final PrintStream realPrintStream){
		super(realPrintStream);
	}

	@Override
	public void print(String s) {
		super.print(s);
		try{
		}catch(Exception e){
		}
	}


	/**
	 * A dummy method to replace all the GlaceOutLoggingStream.console
	 * @param str
	 */
	public static void console(String str){}
	public static void console(Object obj){}
	public static void console(StringBuffer strbuff){}
	public static void console(Vector vec){}
	public static void console(){}
}
