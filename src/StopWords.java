import java.util.*;
import java.sql.*;
public class StopWords {
   private static Collection<String> useableStopwords;
    public static StopWords getInstance() {
        return Instance;
    }


    private StopWords() {
        useableStopwords =new ArrayList<>();
    }

    private static StopWords Instance = new StopWords();

    public Collection<String> filter(List<String> unfilteredTokens) {
        String jdbcURL = "jdbc:postgresql://localhost:5432/stop_words_Eng";
        String username = "postgres";
        String password = "7872667";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);

            for(String i: unfilteredTokens) {
                String sql = "SELECT name FROM \"Words_to_remove\" WHERE name = " + i + "";

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
    public void addStopWord(String stopWord) {

        String jdbcURL = "jdbc:postgresql://localhost:5432/stop_words_Eng";
        String username = "postgres";
        String password = "7872667";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);


            String sql = "INSERT INTO \"Words_to_remove\" (name)"
                    + "VALUES ('" + stopWord + "') ON CONFLICT DO NOTHING";
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);
            if (rows > 0){
                System.out.println("Add command: A new stop-word has been added: " + stopWord + "");
            }
            else {
                System.out.println("Add command: The stop-word: " + stopWord +  " already exist");
            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }
    public void removeStopWord(String stopWord) {
        String jdbcURL = "jdbc:postgresql://localhost:5432/stop_words_Eng";
        String username = "postgres";
        String password = "7872667";

        try {
            Connection connection = DriverManager.getConnection(jdbcURL,username,password);


            String sql = "DELETE FROM \"Words_to_remove\" WHERE name = " + stopWord + "";

            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(sql);
            if (rows > 0){
                System.out.println("Remove command: The stop-word: " + stopWord + " Has been removed");
            }
            else {
                System.out.println("Remove command: The stop-word: " + stopWord +  " Does not exist");
            }

            connection.close();

        } catch (SQLException e) {
            System.out.println("Error connecting to database");
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        ArrayList<String> test = new ArrayList<>();
        StopWords.getInstance().removeStopWord("Ali");
        test.add("throug");
        test.add("Products");
        test.add("multiple");
        test.add("tage");
        test.add("Ali");
        StopWords.getInstance().addStopWord("Furthermore");
        StopWords.getInstance().addStopWord("Furthermore");
        StopWords.getInstance().removeStopWord("Furthermore");
        StopWords.getInstance().removeStopWord("Furthermore");
        StopWords.getInstance().addStopWord("Furthermore");
        System.out.println("Pre filtermethod: "+test);
        StopWords.getInstance().filter(test);
        System.out.println("Post filtermethod: "+test);

    }
}