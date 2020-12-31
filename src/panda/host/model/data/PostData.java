package panda.host.model.data;

import panda.host.config.databases.MySQLConnection;
import panda.host.model.models.Post;
import panda.host.model.models.User;

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
    public List<Post> getAll() {
        try {
            List<Post> posts = new ArrayList<>();
            ResultSet rs = mySQLConn.getStatement(true).executeQuery("SELECT * FROM posts");
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
                ));
            }
            return posts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Post get(String id) {
        return null;
    }

    @Override
    public void add(Post post) {

    }
}
