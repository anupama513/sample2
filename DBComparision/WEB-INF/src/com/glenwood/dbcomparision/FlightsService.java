package com.glenwood.dbcomparision;

import org.json.JSONArray;

import com.glenwood.dbcomparision.utils.DataBaseUtils;

public class FlightsService {

	public enum FilterConstants{
		all("Airports"),civil("Civil Airports"),milit("Military Airport"),sea_based("Sea Plane Base"),harb("Harbours");
		public final String desc;
		private FilterConstants(String desc){
			this.desc =desc;
		}
		
		
	}
	public JSONArray getAllFlights(DataBaseUtils dbUtils,String search,String filter,int limit,int pageNum) throws Exception{
		
		JSONArray flightsJSON = new JSONArray();
		try{
		StringBuilder query = new StringBuilder();
		
		query.append("select id as is, code as code,lat as lat, lon as lon,name as name,city as city, rating as rating,state as state,"
				+ "country as country,tz as tz,type as type, elev as elev, direct_flights as direct, "
				+ "url as url");
		query.append(" from mmt ");
		query.append(" where 1=1");
		if(search.trim() != ""){
			String[] criteria = search.split(",");
			if(criteria[0].trim() != "")
				query.append(" and code ilike '%"+criteria[0]+"%'");
			if(criteria.length >1)
				query.append(" and city || country ilike '%"+criteria[1]+"%'");

		}
		if(filter.trim() != ""){
			for(FilterConstants eachType:FilterConstants.values()){
				if(eachType.toString().equals(filter)){
					filter = eachType.desc;
					break;
				}
			}
			query.append(" and type ilike '%"+filter+"%'");
		}
		query.append(" limit "+limit);
//		query.append(" offset "+ limit*pageNum+1);
		System.out.println("here in query-->"+query);
		flightsJSON = dbUtils.executeQueryToJSONArray(query.toString(), true);
		
		}catch(Exception ex){
			
			ex.printStackTrace();
		}
		return flightsJSON;
	}
	
	public long totalCount(DataBaseUtils dbUtils){
	
		return dbUtils.tableCount("mmt");
		
	}
}
