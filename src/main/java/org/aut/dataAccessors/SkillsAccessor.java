package org.aut.dataAccessors;

import org.aut.models.Education;
import org.aut.models.Skill;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SkillsAccessor {
    private static final Connection connection = DataBaseConnection.getConnection();

    private SkillsAccessor() {
    }

    static void createTable() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS skills (" +
                    "skillId TEXT NOT NULL " +
                    ", userId TEXT NOT NULL" +
                    ", educationId TEXT NOT NULL" +
                    ", text TEXT NOT NULL" +
                    ", PRIMARY KEY (skillId)" +
                    ", FOREIGN KEY (userId, educationId)" +
                    " REFERENCES educations (userId, educationId)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addSkill(Skill skill) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO skills (skillId, userId, educationId, text) " +
                "VALUES (?,?,?,?);");

        statement.setString(1, skill.getSkillId());
        statement.setString(2, skill.getUserId());
        statement.setString(3, skill.getEducationId());
        statement.setString(4, skill.getText());

        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void updateSkill(Skill skill) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("Update skills SET userId=?, educationId=?, text=? where skillId=?;");

        statement.setString(1, skill.getUserId());
        statement.setString(2, skill.getEducationId());
        statement.setString(3, skill.getText());
        statement.setString(4, skill.getSkillId());

        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void deleteSkill(Skill skill) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM skills WHERE skillId = ?;");
        statement.setString(1, skill.getSkillId());

        statement.executeUpdate();
        statement.close();
    }
}
