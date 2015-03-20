package SQL;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

public class DBServlet implements Servlet{
	private Connection conn = null;
	private static final String VALIDATOR_CODE = "validator_code";
	private static final String CODE_ERROR = "code_error";
	
	protected ResultSet execSQL(String sql,Object...args) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(sql);
		for(int i = 0;i < args.length;++i){
			ps.setObject(i + 1, args[i]);
		}
		ps.execute();
		return ps.getResultSet();
	}
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		if(conn == null){
			try {
				Context context = new InitialContext();
				DataSource ds = (DataSource) context.lookup("java:/comp/env/jdbc/world");
				conn = ds.getConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	protected boolean checkValidationCode(ServletRequest request,String validatorCode){
		String systemCode = (String) ((HttpServletRequest) request).getSession().getAttribute(VALIDATOR_CODE);
		if(systemCode == null){
			request.setAttribute(CODE_ERROR, "验证码过期");
			return false;
		}
		if(!systemCode.equalsIgnoreCase(validatorCode)){
			request.setAttribute(CODE_ERROR, "验证码错误");
			return false;
		}
		return true;
	}
	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		
	}

}
