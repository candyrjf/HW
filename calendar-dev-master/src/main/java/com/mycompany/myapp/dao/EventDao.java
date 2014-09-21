package com.mycompany.myapp.dao;

import java.sql.SQLException;
import java.util.List;

import com.mycompany.myapp.domain.Event;

public interface EventDao {

    Event getEvent(int eventId) throws SQLException;

    int createEvent(Event event) throws SQLException;

    List<Event> findForUser(int userId) throws SQLException;

    List<Event> getEvents() throws SQLException;
}