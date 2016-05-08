package com.glenwood.dbcomparision;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.glenwood.dbcomparision.utils.DataBaseUtils;

/**
 * Servlet implementation class SendRequest
 */

public class SendRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SendRequest() {
		super();
	
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processResponse(request, response);
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processResponse(request, response);
	}
	public void processResponse(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		DataBaseUtils dbUtils =null;
		try {
			dbUtils = new DataBaseUtils("java://comp/env/jdbc/glace");

			String filter = HUtil.Nz(request.getParameter("filter"), "");
			String search = HUtil.Nz(request.getParameter("search"), "");
			int  limit = Integer.parseInt(HUtil.Nz(request.getParameter("limit"), "-1"));
			int pageNum = Integer.parseInt(HUtil.Nz(request.getParameter("pageNum"), "-1"));
			System.out.println("filter-->"+filter+"<<search>>"+search);
			FlightsService flightsService = new FlightsService();
			JSONArray json = flightsService.getAllFlights(dbUtils,search,filter,limit,pageNum);
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try{
			dbUtils.destroy();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}

	}



}