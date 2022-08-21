package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class IntroductionToDBAppsEx1 {
    //TODO
    // WRITE YOUR PASSWORD IN FIELD: properties.setProperty("password", "").
    // RUN THE PROGRAM AND WRITE IN THE CONSOLE THE NUMBER OF THE EXERCISE AFTER THAT THE INPUT - YOU WILL RECEIVE THE RESULT.
    // FOR EXERCISES №: 4, 5, 6, 7, 8 AND 9 USE FRESH DATABASE.
    // FOR EX9 FIRST CREATE THE PROCEDURE IN MINIONS_DB AND AFTER THAT EXECUTE

    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/";
    public static final String DB_NAME = "minions_db";
    public static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static Connection connection;

    public static void main(String[] args) throws SQLException, IOException {
        connection = getConnection();

        System.out.println("Enter ex number:");
        int exNum = Integer.parseInt(reader.readLine());

        switch (exNum) {
            case 2:
                exTwo();
                break;
            case 3:
                exThree();
                break;
            case 4:
                exFour();
                break;
            case 5:
                exFive();
                break;
            case 6:
                exSix();
                break;
            case 7:
                exSeven();
                break;
           case 8:
                exEight();
               break;
            case 9:
                exNine();
                break;
        }
    }

    private static void exNine() throws SQLException, IOException {
        System.out.println("Enter minion id:");
        int minion_id = Integer.parseInt(reader.readLine());
               // TODO FIRST CRATE THE PROCEDURE IN MINIONS_DB AND AFTER THAT EXECUTE EX 9
                /* DELIMITER $$
                CREATE PROCEDURE `usp_get_older`(minion_id INT)
                    DETERMINISTIC
                BEGIN
                    UPDATE minions
                    SET age = age + 1
                    WHERE id = minion_id;
                    END$$
                DELIMITER ;*/
        CallableStatement callableStatement = connection.prepareCall("CALL usp_get_older(?)");
        callableStatement.setInt(1, minion_id);

        int affects = callableStatement.executeUpdate();

        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT name, age FROM minions");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d %n", resultSet.getString("name"),
                    resultSet.getInt("age"));
        }
    }

    private static void exEight() throws SQLException, IOException {
        System.out.println("Enter minion id array:");
        String inputStr = reader.readLine();
        if (!inputStr.isEmpty()) {
            String[] input = inputStr.split(" ");
            for (int i = 0; i < input.length; i++) {
                int minionId = Integer.parseInt(input[i]);

                PreparedStatement preparedStatement = connection
                        .prepareStatement("UPDATE minions SET NAME = LOWER(name), age = age + 1 WHERE id = ?;");

                preparedStatement.setInt(1, minionId);

                int affectedRows = preparedStatement.executeUpdate();
            }
        }


        PreparedStatement preparedStatementMinions = connection
                .prepareStatement("SELECT name, age FROM minions");

        ResultSet resultSet = preparedStatementMinions.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d %n", resultSet.getString("name"),
                    resultSet.getInt("age"));
        }

    }

    private static void exSeven() throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT name FROM minions");

        ResultSet resultSet = preparedStatement.executeQuery();

        List<String> allMinionsNames = new ArrayList<>();
        while (resultSet.next()) {
            allMinionsNames.add(resultSet.getString("name"));
        }
        int start = 0;
        int end = allMinionsNames.size() - 1;
        for (int i = 0; i < allMinionsNames.size(); i++) {
            System.out.println(i % 2 == 0
                    ? allMinionsNames.get(start++)
                    : allMinionsNames.get(end--));

        }
    }

    private static void exSix() throws IOException {
        System.out.println("Enter villain id:");
        int villainId = Integer.parseInt(reader.readLine());

        try {
            int affectedEntities = deleteMinionsByVillainId(villainId);
            String villainName = findEntityNameById("villains", villainId);
            deleteVillainById(villainId);

            System.out.printf("%s was deleted%n" +
                    "%d minions released%n", villainName, affectedEntities);
        } catch (Exception e) {
            System.out.println("No such villain was found");
        }
    }

    private static void deleteVillainById(int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM villains WHERE id = ?;");
        preparedStatement.setInt(1, villainId);
        preparedStatement.executeUpdate();
    }

    private static String findEntityNameById(String villains, int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement(String.format("SELECT name FROM %s WHERE id = ?;", villains));

        preparedStatement.setInt(1, villainId);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }

    private static int deleteMinionsByVillainId(int villainId) throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("DELETE FROM minions_villains WHERE villain_id = ?;");

        preparedStatement.setInt(1, villainId);

        return preparedStatement.executeUpdate();
    }


    private static void exFive() throws IOException, SQLException {
        System.out.println("Enter country name:");
        String countryName = reader.readLine();
        PreparedStatement preparedStatement = connection
                .prepareStatement("UPDATE towns SET NAME = UPPER(name) WHERE country = ?;");

        preparedStatement.setString(1, countryName);

        int affectedRows = preparedStatement.executeUpdate();

        if (affectedRows == 0) {
            System.out.println("No town names were affected.");
            return;
        }
        System.out.printf("%d town names were affected.%n", affectedRows);
        PreparedStatement preparedStatementTowns = connection
                .prepareStatement("SELECT name FROM towns WHERE country = ?;");

        preparedStatementTowns.setString(1, countryName);

        ResultSet resultSet = preparedStatementTowns.executeQuery();

        List<String> townsArr = new ArrayList<>();
        while (resultSet.next()) {
            townsArr.add(resultSet.getString("name"));
        }
        System.out.println(townsArr);
    }

    private static void exFour() throws IOException, SQLException {
        System.out.println("Enter minion info:");
        String[] minionInput = reader.readLine().split(" ");
        String minionName = minionInput[1];
        int minionAge = Integer.parseInt(minionInput[2]);
        String minionTown = minionInput[3];


        System.out.println("Enter villain name:");
        String villainName = reader.readLine().split(" ")[1];

        int townId = getOrInsertTown(minionTown);
        int villainId = getOrInsertVillain(villainName);

        PreparedStatement insertMinion = connection
                .prepareStatement("INSERT INTO minions(name, age, town_id) VALUES (?, ?, ?)");

        insertMinion.setString(1, minionName);
        insertMinion.setInt(2, minionAge);
        insertMinion.setInt(3, townId);
        insertMinion.executeUpdate();

        System.out.printf("Successfully added %s to be minion of %s.%n", minionName, villainName);
    }

    private static int getOrInsertVillain(String villainName) throws SQLException {
        PreparedStatement selectVillain = connection
                .prepareStatement("SELECT id from villains WHERE name = ?;");
        selectVillain.setString(1, villainName);
        ResultSet villainSet = selectVillain.executeQuery();

        int villainId = 0;
        if (!villainSet.next()) {
            PreparedStatement insertVillain = connection
                    .prepareStatement("INSERT into villains(name, evilness_factor )Values(?, ?);");
            insertVillain.setString(1, villainName);
            insertVillain.setString(2, "evil");
            insertVillain.executeUpdate();

            ResultSet newVillainSet = selectVillain.executeQuery();
            newVillainSet.next();
            villainId = newVillainSet.getInt("id");
            System.out.printf("Villain %s was added to the database.%n", villainName);
        } else {
            villainId = villainSet.getInt("id");
        }
        return villainId;
    }

    private static int getOrInsertTown(String minionTown) throws SQLException {
        PreparedStatement selectTown = connection
                .prepareStatement(" SELECT id from towns WHERE name = ?;");
        selectTown.setString(1, minionTown);
        ResultSet townSet = selectTown.executeQuery();

        int townId = 0;
        if (!townSet.next()) {
            PreparedStatement insertTown = connection
                    .prepareStatement("INSERT into towns(name)Values(?);");
            insertTown.setString(1, minionTown);
            insertTown.executeUpdate();

            ResultSet newTownSet = selectTown.executeQuery();
            newTownSet.next();
            townId = newTownSet.getInt("id");
            System.out.printf("Town %s was added to the database.%n", minionTown);
        } else {
            townId = townSet.getInt("id");
        }
        return townId;
    }

    private static void exThree() throws IOException {
        System.out.println("Enter villain id:");
        int villainId = Integer.parseInt(reader.readLine());

        try {
            String villainName = findVillainNameByID(villainId);

            System.out.println("Villain: " + villainName);

            Set<String> result = new LinkedHashSet<>();
            PreparedStatement preparedStatement = connection
                    .prepareStatement("    SELECT m.name, m.age FROM minions AS m " +
                            "JOIN minions_villains mv on m.id = mv.minion_id " +
                            "WHERE mv.villain_id = ?; ");

            preparedStatement.setInt(1, villainId);
            ResultSet resultSet = preparedStatement.executeQuery();

            int counter = 1;
            while (resultSet.next()) {
                System.out.printf("%d. %s %d%n", counter++, resultSet.getString("name"),
                        resultSet.getInt("age"));
            }
        } catch (Exception e) {
            System.out.println("No villain with ID 10 exists in the database.");
        }

    }

    private static String findVillainNameByID(int villainId) throws SQLException, IOException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT name FROM villains WHERE id = ?;");

        preparedStatement.setInt(1, villainId);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString(1);
    }

    private static void exTwo() throws SQLException {
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT v.name, COUNT(DISTINCT MV.minion_id) AS m_count FROM villains AS v\n" +
                        "JOIN minions_villains mv on v.id = mv.villain_id " +
                        "GROUP BY v.name " +
                        "HAVING m_count > ? ;");

        preparedStatement.setInt(1, 15);


        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.printf("%s %d %n", resultSet.getString(1),
                    resultSet.getInt(2));
        }
    }


    private static Connection getConnection() throws IOException, SQLException {
//        System.out.println("Enter user");
//        String user = reader.readLine();
//        System.out.println("Enter password");
//        String password = reader.readLine();

//      TODO  Please enter your password
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "");

        return DriverManager
                .getConnection(CONNECTION_STRING + DB_NAME, properties);
    }
}


