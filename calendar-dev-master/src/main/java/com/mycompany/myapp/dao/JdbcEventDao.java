package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;

@Repository
public class JdbcEventDao implements EventDao {
    private DataSource dataSource;
    private JdbcCalendarUserDao jdbceventDao;

    // --- constructors ---
    public JdbcEventDao(JdbcCalendarUserDao jdbceventDao) {
    	this.jdbceventDao = jdbceventDao;
    }

	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

    // --- EventService ---
    @Override
    public Event getEvent(int eventId) throws SQLException {
    	Connection c = dataSource.getConnection();
		PreparedStatement ps = c.prepareStatement("select * from events where id = ?");
		ps.setInt(1, eventId);
		ResultSet rs = ps.executeQuery();
		rs.next();
	
		Event event = new Event();
		ApplicationContext context = 
				new GenericXmlApplicationContext("com/mycompany/myapp/applicationContext.xml");
 		
 		CalendarUserDao calendarUserDao = 
 				context.getBean("calendarUserDao", JdbcCalendarUserDao.class);

		event.setId(rs.getInt("id"));	   	
		event.setDescription(rs.getString("description"));
		event.setSummary(rs.getString("summary"));
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(rs.getTimestamp("when").getTime());
		event.setWhen(time);		
		event.setOwner(calendarUserDao.getUser(rs.getInt("owner")));
		event.setAttendee(calendarUserDao.getUser(rs.getInt("attendee")));
		
		rs.close();
		ps.close();
		c.close();

		return event;
    }

    @Override
    public int createEvent(final Event event) throws SQLException {
    	int auto_id = -1;
			Connection c = dataSource.getConnection();
			// 사용자에게 id값을 입력받지 않고 DB에서 저절로 증가되기 때문에 이 값을 알기 위한 코드
			PreparedStatement ps = c
					.prepareStatement(
							"insert into events (`when`, summary, description, owner, attendee) values (?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			
			java.util.Date date = event.getWhen().getTime();
			Timestamp timestamp = new Timestamp(date.getTime());
			
			ps.setTimestamp(1, timestamp);
			ps.setString(2, event.getSummary());
			ps.setString(3, event.getDescription());
			ps.setInt(4, event.getOwner().getId());
			ps.setInt(5, event.getAttendee().getId());

			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			auto_id = (rs.next()) ? rs.getInt(1) : -1;

			rs.close();
			ps.close();
			c.close();
		
		return auto_id;
    }

    @Override
    public List<Event> findForUser(int userId) throws SQLException {
    	List<Event> eventList = new ArrayList<Event>();
		Calendar cal = Calendar.getInstance();
		CalendarUser user;

			Connection c = dataSource.getConnection();
			PreparedStatement ps = c
					.prepareStatement("select * from events where id ?");
			ps.setInt(1, userId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Event event = new Event();

				// ---- get Owner of event ----
				user = jdbceventDao.getUser(rs.getInt("owner"));

				// ---- setting ----

				cal.clear();
				cal.setTimeInMillis(rs.getTimestamp("when").getTime());

				event.setId(rs.getInt("id"));
				event.setWhen(cal);
				event.setSummary(rs.getString("summary"));
				event.setOwner(user);

				// ---- get Attendee of event ----
				user = jdbceventDao.getUser(rs.getInt("attendee"));

				event.setAttendee(user);
				event.setDescription(rs.getString("description"));

				eventList.add(event);
			}

			rs.close();
			ps.close();
			c.close();
	
		return eventList;
    }

    @Override
    public List<Event> getEvents() throws SQLException {
    	List<Event> eventList = new ArrayList<Event>();
		Calendar cal = Calendar.getInstance();
		CalendarUser user;

			Connection c = dataSource.getConnection();
			PreparedStatement ps = c.prepareStatement("select * from events");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Event event = new Event();

				// ---- get Owner of event ----
				user = jdbceventDao.getUser(rs.getInt("owner"));

				// ---- setting ----

				cal.clear();
				cal.setTimeInMillis(rs.getTimestamp("when").getTime());

				event.setId(rs.getInt("id"));
				event.setWhen(cal);
				event.setSummary(rs.getString("summary"));
				event.setOwner(user);

				// ---- get Attendee of event ----
				user = jdbceventDao.getUser(rs.getInt("attendee"));

				event.setAttendee(user);
				event.setDescription(rs.getString("description"));

				eventList.add(event);
			}

			rs.close();
			ps.close();
			c.close();
	
		return eventList;
    }
    

    /*
    private static final String EVENT_QUERY = "select e.id, e.summary, e.description, e.when, " +
            "owner.id as owner_id, owner.email as owner_email, owner.password as owner_password, owner.name as owner_name, " +
            "attendee.id as attendee_id, attendee.email as attendee_email, attendee.password as attendee_password, attendee.name as attendee_name " +
            "from events as e, calendar_users as owner, calendar_users as attendee " +
            "where e.owner = owner.id and e.attendee = attendee.id";
     */
}
