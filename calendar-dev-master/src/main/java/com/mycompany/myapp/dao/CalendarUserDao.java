package com.mycompany.myapp.dao;

import java.sql.SQLException;
import java.util.List;

import com.mycompany.myapp.domain.CalendarUser;

public interface CalendarUserDao {
    CalendarUser getUser(int id) throws SQLException;

    CalendarUser findUserByEmail(String email) throws SQLException;

    List<CalendarUser> findUsersByEmail(String partialEmail) throws SQLException;

    int createUser(CalendarUser user) throws SQLException;
}
