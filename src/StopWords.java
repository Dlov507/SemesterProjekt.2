import java.util.*;
import java.sql.*;
public class StopWords {
    // factory pattern for at skifte factories

    public static Collection<String> filter(List<String> unfilteredTokens) {

        Collection<String> useableStopwords = new ArrayList<>();
        String jdbcURL = "jdbc:postgresql://localhost:5432/stop_words_Eng";
        String username = "postgres";
        String password = "7872667";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);

            for(String i: unfilteredTokens) {
                String sql = "SELECT name FROM \"Words_to_remove\" WHERE name = '" + i + "';";



                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery(sql);{
                    while (resultSet.next()){
                        useableStopwords.add(resultSet.getString("name"));
                    }
                }
            }
            unfilteredTokens.removeAll(useableStopwords);


        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
        return unfilteredTokens;
    }
    public static void addStopword(String stopword) {

        String jdbcURL = "jdbc:postgresql://localhost:5432/stop_words_Eng";
        String username = "postgres";
        String password = "7872667";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);


            String sql = "INSERT INTO \"Words_to_remove\" (name)"
                    + "VALUES ('" + stopword + "') ON CONFLICT DO NOTHING";
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);
            if (rows > 0){
                System.out.println("Add command: A new stopword has been added: '" + stopword + "'");
            }
            else {
                System.out.println("Add command: The stopword: '" + stopword +  "' already exist");
            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }
    public static void removeStopword(String stopword) {
        //TreesetStopwords.remove(stopword);

        String jdbcURL = "jdbc:postgresql://localhost:5432/stop_words_Eng";
        String username = "postgres";
        String password = "7872667";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);


            String sql = "DELETE FROM \"Words_to_remove\" WHERE name = '" + stopword + "'";

            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);
            if (rows > 0){
                System.out.println("Remove command: The stopword: '" + stopword + "' Has been removed");
            }
            else {
                System.out.println("Remove command: The stopword: '" + stopword +  "' Does not exist");
            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        ArrayList<String> test = new ArrayList<>();
        removeStopword("Ali");
        test.add("throug");
        test.add("Products");
        test.add("multiple");
        test.add("tage");
        test.add("Ali");
        addStopword("Furthermore");
        addStopword("Furthermore");
        removeStopword("Furthermore");
        removeStopword("Furthermore");
        addStopword("Furthermore");
        System.out.println("Pre filtermethod: "+test);
        filter(test);
        System.out.println("Post filtermethod: "+test);

    }
}