package io.nzo.web.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/Test")
public class Test extends HttpServlet 
{
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Test() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		request.setCharacterEncoding("utf-8");
		
		response.setCharacterEncoding("utf-8");
		
		response.setHeader("Expires", "Tue, 03 Jul 2001 06:00:00 GMT");
		response.setHeader("Last-Modified", new Date().toString());
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");	// HTTP 1.1
		response.setHeader("Pragma", "no-cache");	// HTTP 1.0
		
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		
		// ------------------------------------------------------------------------
		
		Connection connection = null;
		
		// db 커넥션 연결
		try
		{
			InitialContext initContext = new InitialContext();
			DataSource ds = (DataSource)initContext.lookup("java:comp/env/" + "NZOTest");
			connection = ds.getConnection();
			
			System.out.println(connection);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
		
		// Callable
		CallableStatement cstmt = null; 
		try 
		{
			cstmt = connection.prepareCall("{ CALL [dbo].[GetTestList] ( ?, ? , ? , ? ) }");
			
			cstmt.setInt("i_val1", 1);
			cstmt.setInt("i_val2", 2);
			cstmt.setInt("i_val", 3);
			cstmt.setString(4, "3");
			// cstmt setInt String형식의 매개변수 혼용해서 사용 가능
			
			
			// cstmt 실제 sql server프로시저의 @name 과 일치하지 않는 매개변수 입력시
			// 매개 변수 i_val이(가) 저장 프로시저 [dbo].[GetTestList]에 정의되지 않았습니다.
			
			
			// cstmt 형식이 다른 매개변수 입력햇을때
			// 실행이됨 (?)
			// System.out.println( cstmt.execute() );     true
			
			
			// cstmt 매개변수 하나 빠트렸을때
			// com.microsoft.sqlserver.jdbc.SQLServerException: 매개 변수 번호 4에 값이 설정되지 않았습니다.
			// exception 일어남
			
			System.out.println( cstmt.execute() );
			
			
			if( cstmt.isClosed() == false )
			{
				cstmt.close();	
			}
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		
		
		
//		PreparedStatement pstmt = null;
//		
//
//		ResultSet rs = null;
//		ResultSetMetaData rsmd = null;
		
		
		
		
		
		//pstmt = con.prepareStatement(procData.procedure);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// Closeable
		try
		{
			connection.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(connection != null)
				{
					if( connection.isClosed() == false)
					{
						connection.close(); 
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		// ------------------------------------------------------------------------
		out.print("{  \"test\" : true }");
	}

}
