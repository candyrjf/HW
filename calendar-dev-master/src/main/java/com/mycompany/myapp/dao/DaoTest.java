package com.mycompany.myapp.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;

public class DaoTest {
	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {

		ApplicationContext context = new GenericXmlApplicationContext(
				"com/mycompany/myapp/applicationContext.xml");

		JdbcCalendarUserDao calendarUserDao = context.getBean(
				"calendarUserDao", JdbcCalendarUserDao.class);
		JdbcEventDao eventDao = context.getBean("eventDao", JdbcEventDao.class);

		// 1. 디폴트로 등록된 CalendarUser 3명 출력 (패스워드 제외한 모든 내용 출력)
		for (int i = 1; i < 4; i++) {
			CalendarUser user1 = calendarUserDao.getUser(i);
			System.out.print(user1.getId() + "  ");
			System.out.print(user1.getEmail() + "  ");
			System.out.println(user1.getName());
		}
		System.out.println("\n");

		// 2. 디폴트로 등록된 Event 3개 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력)
		for (int i = 100; i < 103; i++) {
			Event event1 = eventDao.getEvent(i);
			System.out.print(event1.getId() + "  ");
			System.out.print(event1.getWhen().getTime() + "  ");
			System.out.print(event1.getSummary() + "  ");
			System.out.print(event1.getDescription() + "  ");
			System.out.print(event1.getOwner().getName() + "  ");
			System.out.println(event1.getAttendee().getEmail());
		}
		System.out.println("\n");

		// 3. 새로운 CalendarUser 2명 등록 및 각각 id 추출
		 CalendarUser caluser1 = new CalendarUser();
		 caluser1.setEmail("candyrjf@kut.ac.kr");
		 caluser1.setName("최소라");
		 caluser1.setPassword("chlthfk");

		 CalendarUser caluser2 = new CalendarUser();
		 caluser2.setEmail("skypk0916@kut.ac.kr");
		 caluser2.setName("박순호");
		 caluser2.setPassword("qkrtnsgh");	
		 
		int id1 = calendarUserDao.createUser(caluser1);
		int id2 = calendarUserDao.createUser(caluser2);
			
		caluser1.setId(id1);
		caluser2.setId(id2);
		
		System.out.println("test id 1 : " + id1);
		System.out.println("test id 2 : " + id2);
		System.out.println("\n");

		
		// 4. 추출된 id와 함께 새로운 CalendarUser 2명을 DB에서 가져와 (getUser 메소드 사용) 방금 등록된
		// 2명의 사용자와 내용 (이메일, 이름, 패스워드)이 일치하는 지 비교
		CalendarUser nuser1 = calendarUserDao.getUser(id1);
		CalendarUser nuser2 = calendarUserDao.getUser(id2);
		
		if((caluser1.getName().equals(nuser1.getName()) && caluser1.getEmail().equals(nuser1.getEmail()) && caluser1.getPassword().equals(nuser1.getPassword())
				&& caluser2.getName().equals(nuser2.getName()) && caluser2.getEmail().equals(nuser2.getEmail()) && caluser2.getPassword().equals(nuser2.getPassword())
				)){
				System.out.print("추출된 id와 새로운 Event가 같습니다.");			
			}
			
		System.out.println("\n");

		// 5. 5명의 CalendarUser 모두 출력 (패스워드 제외한 모든 내용 출력)
		List<CalendarUser> userList = new ArrayList<CalendarUser>();
		
		for(int i = 1; i <= 5; i++){
			userList.add(calendarUserDao.getUser(i));
			CalendarUser calList = userList.get(i - 1);
			System.out.print("id : " + calList.getId() + "  ");
			System.out.print("name : " + calList.getName() + "  ");
			System.out.println("email : " + calList.getEmail());
		}
		System.out.println("\n");

		// 6. 새로운 Event 2개 등록 및 각각 id 추출		
		Event event1 = new Event();
		Event event2 = new Event();
		
		event1.setAttendee(caluser1);
		event1.setDescription("안녕하세요  ");
		event1.setOwner(caluser1);
		event1.setSummary("웹서비스 첫번 째 과제");
		event1.setWhen(new GregorianCalendar(2014, 9, 19));
		
		event2.setAttendee(caluser2);
		event2.setDescription("교수님  ");
		event2.setOwner(caluser2);
		event2.setSummary("과제는 조금만 내주세요..");
		event2.setWhen(new GregorianCalendar(2014, 9, 19));
		
		int eventId1 = eventDao.createEvent(event1);
		int eventId2 = eventDao.createEvent(event2);
		
		event1.setId(eventId1);
		event2.setId(eventId2);
		
		System.out.println("newEvent1 id : " + eventId1);
		System.out.println("newEvent2 id : " + eventId2);
		System.out.println("\n");
		
		
		// 7. 추출된 id와 함께 새로운 Event 2개를 DB에서 가져와 (getEvent 메소드 사용) 방금 추가한 2개의
		// 이벤트와 내용 (when, summary, description, owner, attendee)이 일치하는 지 비교
		Event event3 = eventDao.getEvent(eventId1);
		Event event4 = eventDao.getEvent(eventId2);
		if((event1.getAttendee().equals(event3.getAttendee()) && event1.getSummary().equals(event3.getSummary()) && event1.getSummary().equals(event3.getSummary())
			&& event1.getDescription().equals(event3.getDescription()) && event1.getOwner().equals(event3.getOwner())
			&& (event2.getWhen().compareTo(event4.getWhen()) == 0)) && (event2.getAttendee().equals(event4.getAttendee()) && event2.getSummary().equals(event4.getSummary()) && event2.getSummary().equals(event4.getSummary())
					&& event2.getDescription().equals(event4.getDescription()) && event2.getOwner().equals(event4.getOwner())
					&& (event2.getWhen().compareTo(event4.getWhen()) == 0))){
			System.out.print("추출된 id와 새로운 Event가 같습니다.");			
		}
		
		System.out.println("\n");
		
		// 8. 5개의 Event 모두 출력 (owner와 attendee는 해당 사용자의 이메일과 이름을 출력)
		List<Event> eventList = eventDao.getEvents();
		
		for(int i = 0; i < eventList.size(); i++){
			Event list = eventList.get(i);			
			System.out.print(list.getOwner().getEmail()+ "  ");
			System.out.print(list.getAttendee().getName()+ "  ");
			System.out.print(list.getWhen().getTime()+ "  ");
			System.out.print(list.getDescription()+ "  ");
			System.out.println(list.getSummary());
		}
	}
}
