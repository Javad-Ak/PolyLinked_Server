package org.aut.dataAccessors;

import org.aut.models.Education;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class EducationAccessor {
    private static final Connection connection = DataBaseConnection.getConnection();

    private EducationAccessor() {
    }

    static void createTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS educations (" +
                    "educationId TEXT NOT NULL" +
                    ", userId TEXT NOT NULL" +
                    ", institute TEXT NOT NULL" +
                    ", field TEXT NOT NULL" +
                    ", start BIGINT NOT NULL" +
                    ", end BIGINT NOT NULL" +
                    ", grade INT NOT NULL" +
                    ", activities TEXT NOT NULL" +
                    ", about TEXT NOT NULL" +
                    ", PRIMARY KEY (educationId)" +
                    ", FOREIGN KEY (userId)" +
                    " REFERENCES users (userId)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO educations (educationId, userId, institute, field, start, end, grade, activities, about) " +
                "VALUES (?,?,?,?,?,?,?,?,?);");

        statement.setString(1, education.getEducationId());
        statement.setString(2, education.getUserId());
        statement.setString(3, education.getInstitute());
        statement.setString(4, education.getField());
        statement.setLong(5, education.getStart());
        statement.setLong(6, education.getEnd());
        statement.setInt(7, education.getGrade());
        statement.setString(8, education.getActivities());
        statement.setString(9, education.getAbout());

        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void updateEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE educations SET userId=?, institute=?, field=?, start=?, end=?, grade=?, activities=?, about=? WHERE educationId = ?;");

        statement.setString(1, education.getUserId());
        statement.setString(2, education.getInstitute());
        statement.setString(3, education.getField());
        statement.setLong(4, education.getStart());
        statement.setLong(5, education.getEnd());
        statement.setInt(6, education.getGrade());
        statement.setString(7, education.getActivities());
        statement.setString(8, education.getAbout());
        statement.setString(9, education.getEducationId());

        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void deleteEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM educations WHERE educationId = ?;");
        statement.setString(1, education.getEducationId());

        statement.executeUpdate();
        statement.close();
    }

}
