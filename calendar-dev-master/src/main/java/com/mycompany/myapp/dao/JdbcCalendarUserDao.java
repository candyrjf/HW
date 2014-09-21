package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.CalendarUser;

@Repository
public class JdbcCalendarUserDao implements CalendarUserDao {

	private DataSource dataSource;

	// --- constructors ---
	public JdbcCalendarUserDao() {
		// 안건들여도됨
	}

	// xml파일에 인터ㅔ이스 명 마고 클래스 네임 적어야함
	// daotest와 calendaruserdao 는 의존관계 ㄴㄴ
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// --- CalendarUserDao methods ---
	// 여기서부터 구현
	@Override
	public CalendarUser getUser(int id) throws SQLException {
		Connection c = dataSource.getConnection();
		PreparedStatement ps = c
				.prepareStatement("select * from calendar_users where id = ?");
		ps.setInt(1, id);

		ResultSet rs = ps.executeQuery();
		rs.next();
		
		CalendarUser calendarUser = new CalendarUser();
		calendarUser.setId(rs.getInt("id"));
		calendarUser.setEmail(rs.getString("email"));
		calendarUser.setPassword(rs.getString("password"));
		calendarUser.setName(rs.getString("name"));

		rs.close();
		ps.close();
		c.close();

		return calendarUser;
	}

	@Override
	public CalendarUser findUserByEmail(String email) throws SQLException {
		Connection c = dataSource.getConnection();
				
				PreparedStatement ps = c.prepareStatement( "select * from calendar_users where email = ?");
				ps.setString(1, email);
		    	
				ResultSet rs = ps.executeQuery();
				rs.next();
				
				CalendarUser user = new CalendarUser();
				
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
				
				rs.close();
				ps.close();
				c.close();
				
				return user;
	}

	@Override
	public List<CalendarUser> findUsersByEmail(String email) throws SQLException {
		List<CalendarUser> userList = new ArrayList<CalendarUser>();
		// 이메일 주소의 일부만 검색 할 수 있또록
		// sql문의 like비슷..?
		Connection c = dataSource.getConnection();

		// 부분만 들어가도 검색이 되어야 하므로 %와 like를 사용하였습니다.
		PreparedStatement ps = c
				.prepareStatement("select * from calendar_users where email like ?");
		ps.setString(1, "%" + email + "%");

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			CalendarUser user = new CalendarUser();

			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setPassword(rs.getString("password"));
			user.setEmail(rs.getString("email"));

			userList.add(user);
		}

		rs.close();
		ps.close();
		c.close();
		return userList;
	}
	@Override
	public int createUser(final CalendarUser userToAdd) throws SQLException {
		Connection c = dataSource.getConnection();
		int auto_id = -1;

		PreparedStatement ps = c.prepareStatement(
				"insert into calendar_users(email, name, password) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, userToAdd.getEmail());
		ps.setString(2, userToAdd.getName());
		ps.setString(3, userToAdd.getPassword());

		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		auto_id = (rs.next()) ? rs.getInt(1) : -1;
//		if(!rs.next()){
//			auto_id = rs.getInt("id");
//		}

		ps.close();
		c.close();	
		
		return auto_id;
		
	}
}