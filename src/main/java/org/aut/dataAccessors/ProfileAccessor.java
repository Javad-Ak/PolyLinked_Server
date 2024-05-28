package org.aut.dataAccessors;

import org.aut.models.CallInfo;
import org.aut.models.Education;
import org.aut.models.Profile;
import org.aut.models.Skill;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ProfileAccessor {
    private static final Connection connection = DataBaseConnection.getConnection();

    private ProfileAccessor() {
    }

    static void createUserTables() throws IOException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS profiles (" +
                    "userId TEXT NOT NULL" +
                    ", bio TEXT" +
                    ", pathToPic TEXT" +
                    ", pathToBG TEXT" +
                    ", country TEXT" +
                    ", city TEXT" +
                    ", status TEXT" +
                    ", profession TEXT" +
                    ", FOREIGN KEY (userId)" +
                    " REFERENCES users (userId)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS educations (" +
                    "educationId TEXT NOT NULL" +
                    ", userId TEXT NOT NULL" +
                    ", institute TEXT NOT NULL" +
                    ", field TEXT NOT NULL" +
                    ", start BIGINT NOT NULL" +
                    ", end BIGINT NOT NULL" +
                    ", grade INT NOT NULL" +
                    ", profession TEXT NOT NULL" +
                    ", activities TEXT NOT NULL" +
                    ", about TEXT NOT NULL" +
                    ", PRIMARY KEY (educationId)" +
                    ", FOREIGN KEY (userId)" +
                    " REFERENCES users (userId)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");
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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS callInfo (" +
                    "userId TEXT NOT NULL" +
                    ", email TEXT" +
                    ", mobileNumber TEXT" +
                    ", homeNumber TEXT" +
                    ", workNumber TEXT" +
                    ", Address TEXT" +
                    ", birthDay BIGINT" +
                    ", privacyPolitics TEXT" +
                    ", socialMedia TEXT" +
                    ", PRIMARY KEY (userId, email)" +
                    ", FOREIGN KEY (userId, email)" +
                    " REFERENCES users (userId, email)" +
                    " ON UPDATE CASCADE" +
                    " ON DELETE CASCADE" +
                    ");");
        } catch (Exception e) {
            throw new RemoteException(e.getMessage());
        }
    }

    public synchronized static void addProfile(Profile profile) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO profiles (userId, bio, pathToPic, pathToBG, country, city, status, profession) " +
                "VALUES (?,?,?,?,?,?,?,?);");

        statement.setString(1, profile.getUserId());
        statement.setString(2, profile.getBio());
        statement.setString(3, profile.getPathToPic());
        statement.setString(4, profile.getPathToBG());
        statement.setString(5, profile.getCountry());
        statement.setString(6, profile.getCity());
        statement.setString(7, profile.getStatus());
        statement.setString(8, profile.getProfession());

        statement.executeUpdate();
        statement.close();
    }

    public synchronized static void addEducation(Education education) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO educations (educationId, userId, institute, field, start, end, grade, profession, activities, about) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?);");

        statement.setString(1, education.getEducationId());
        statement.setString(2, education.getUserId());
        statement.setString(3, education.getInstitute());
        statement.setString(4, education.getField());
        statement.setLong(5, education.getStart());
        statement.setLong(6, education.getEnd());
        statement.setInt(7, education.getGrade());
        statement.setString(8, education.getEducationId());

        statement.executeUpdate();
        statement.close();
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


    public synchronized static void addCallInfo(CallInfo callInfo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO callInfo(userId, email, mobileNumber, homeNumber, workNumber, Address, birthDay, privacyPolitics, socialMedia) " +
                "VALUES (?,?,?,?,?,?,?,?,?);");

        statement.setString(1, callInfo.getUserId());
        statement.setString(2, callInfo.getEmail());
        statement.setString(3, callInfo.getMobileNumber());
        statement.setString(4, callInfo.getHomeNumber());
        statement.setString(5, callInfo.getWorkNumber());
        statement.setString(6, callInfo.getAddress());
        statement.setLong(7, callInfo.getBirthDay());
        statement.setString(8, callInfo.getPrivacyPolitics());
        statement.setString(9, callInfo.getSocialMedia());

        statement.executeUpdate();
        statement.close();
    }
}
