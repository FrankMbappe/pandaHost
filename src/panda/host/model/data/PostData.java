package panda.host.model.data;

import com.google.gson.Gson;
import panda.host.config.database.MySQLConnection;
import panda.host.model.models.Post;
import panda.host.model.models.filters.Filter;
import panda.host.model.models.filters.PostFilter;
import panda.host.utils.Panda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostData implements Data<Post> {
    MySQLConnection mySQLConn;

    public PostData(MySQLConnection mySQLConn){
        this.mySQLConn = mySQLConn;
    }

    @Override
    public void init() {
        if(mySQLConn.exists()){
            mySQLConn.executeUpdateStatement("CREATE TABLE IF NOT EXISTS `Posts` (" +
                    "`id` INT AUTO_INCREMENT PRIMARY KEY," +
                    "`authorId` VARCHAR(255) NOT NULL," +
                    "`message` VARCHAR(255)," +
                    "`fileName` VARCHAR(255)," +
                    "`fileExt` VARCHAR(255)," +
                    "`fileSize` DOUBLE," +
                    "`uploadDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "`lastUpdate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
            ")", true);
            System.out.println("[PostData, init()] | Table 'Posts' created.");

        } else{
            System.out.println("[PostData, init()] | Error: The connection doesn't exist.");
        }
    }

    @Override
    public ArrayList<Post> getAll() {
        try {
            ArrayList<Post> posts = new ArrayList<>();
            ResultSet rs = mySQLConn.getStatement(true).executeQuery("SELECT * FROM posts");
            int i = 0;
            while(rs.next()){
                posts.add(new Post(
                        rs.getInt("id"),
                        rs.getString("authorId"),
                        rs.getString("message"),
                        rs.getString("fileName"),
                        rs.getString("fileExt"),
                        rs.getDouble("fileSize"),
                        rs.getTimestamp("uploadDate"),
                        rs.getTimestamp("lastUpdate")
                )); i++;
            }
            System.out.println(String.format("[PostData, getAll()] | %d row(s) retrieved.", i));
            return posts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Post> getMatchingData(Filter filter){
        var posts = new ArrayList<Post>();

        // I iterate each post
        for (Post post : getAll()){
            // and I check if it matches the filter
            if(post.matchesFilter((PostFilter) filter)){
                posts.add(post);
            }
        }
        return posts;
    }

    @Override
    public String getMatchingDataToJson(Filter filter) {
        return new Gson().toJson(getMatchingData(filter));
    }

    @Override
    public Post get(Object id) { // ID should be an int
        List<Post> posts = getAll();
        return posts.get(posts.indexOf(new Post((int) id)));
    }

    @Override
    public void add(Post post) {
        // TODO: Add a post logic
    }

    @Override
    public String getMatchingDataFromPandaCode(String pandaCode) {
        // Here I assume that the panda code sent by the client matches the pattern of PANDAOP_REQUEST_GET_POSTS
        // Therefore I can freely create a PostFilter using the filters contained in that code
        ArrayList<String> filtersIntoStringForm = Panda.extractFiltersFromPandaCode(pandaCode);

        // I create my filter variable, using the ctor that converts these filters from String to their normal type
        PostFilter postFilter = new PostFilter(filtersIntoStringForm);

        // I return the list, applying the filters beforehand extracted
        // Notice tha
        return getMatchingDataToJson(postFilter);
    }

}
