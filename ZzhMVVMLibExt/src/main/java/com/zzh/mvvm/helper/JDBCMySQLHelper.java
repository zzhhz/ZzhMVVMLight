package com.zzh.mvvm.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * @date: 2018/4/16 下午2:14
 * @email: zzh_hz@126.com
 * @QQ: 1299234582
 * @author: zzh
 * @description: JDBCMySQLHelper.java
 * @version 1
 */
public class JDBCMySQLHelper {
	// 数据库url格式："jdbc:mysql://IP:端口/数据库的名字"
	private static String url = "jdbc:mysql://127.0.0.1:3306/1310";
	private static String user = "root";// 访问数据库的用户名
	private static String password = "root";// 访问数据库的密码
	private Connection conn = null;
	private Statement statement = null;

	public JDBCMySQLHelper() {
		getConnection();
	}

	/**
	 * 作用：建立数据库连接
	 */
	private void getConnection() {
		// 1.加载数据库驱动；
		// 2.建立数据库连接。Connection对象。
		// 3.建立Statement对象。Statement对象的作用是传送sql语句到数据库服务器。
		// 4.执行sql语句。
		// 5.处理结果。
		try {
			// 利用反射原理加载数据库驱动程序
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("加载驱动失败！");
		}

		try {
			// 建立数据库连接
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("数据库连接失败！");
		}
	}

	/**
	 * 作用：执行sql select语句
	 * 
	 * @param sql
	 * @return ResultSet结果集
	 */
	public ResultSet selectResultSet(String sql) {
		try {
			// statement的作用是传递sql语句，让服务器对sql执行操作。
			statement = conn.createStatement();
			// 执行sql语句
			ResultSet rs = statement.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 作用：根据条件查找符合条件的条数
	 * 
	 * @param sql语句一定是
	 *            select count(*) from 表名 where 条件
	 * @return 查询到的条数
	 */
	public int selectResultSetCount(String sql) {
		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			// 默认游标在数据集的前方。只有next之后才能指到第一条数据
			rs.next();
			// columnIndex the first column is 1, the second is 2
			return rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 作用：对数据库进行update ,insert ,delete。CRUD操作（增删查改操作）
	 * 
	 * @param sql
	 * @return
	 */
	public boolean updateData(String sql) {
		try {
			statement = conn.createStatement();
			int result = statement.executeUpdate(sql);
			return (result > 0 ? true : false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
