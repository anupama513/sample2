package com.glenwood.dbcomparision.utils;

import java.sql.SQLException;

public class GlaceSQLException extends SQLException{
	public GlaceSQLException(){
		super();
	}

	public GlaceSQLException(String s){
		super(s);
	}


}