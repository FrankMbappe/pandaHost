package panda.host.model.data;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import panda.host.config.database.MySQLConnection;
import panda.host.model.models.PandaFile;
import panda.host.model.models.Post;
import panda.host.model.models.filters.Filter;
import panda.host.model.models.filters.PostFilter;
import panda.host.utils.Current;
import panda.host.utils.Panda;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static panda.host.utils.Panda.generateAPandaFilePath;
import static panda.host.utils.Panda.getARandomFileId;

public class PostData implements Data<Post> {
    MySQLConnection mySQLConn;

    public PostData() {
        this.mySQLConn = Current.dbConnection;
    }

    @Override
    public void init() {
        if (mySQLConn.exists()) {
            mySQLConn.executeUpdateStatement("CREATE TABLE IF NOT EXISTS `Posts` (" +
                    "`id` INT AUTO_INCREMENT PRIMARY KEY," +
                    "`authorId` VARCHAR(255) NOT NULL," +
                    "`message` VARCHAR(500)," +
                    "`tags` VARCHAR(255)," +
                    "`fileId` VARCHAR(255)," +
                    "`fileName` VARCHAR(255)," +
                    "`fileExt` VARCHAR(255)," +
                    "`fileSize` BIGINT," +
                    "`uploadDate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                    "`lastUpdate` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                    ")", true);
            System.out.println("[PostData, init()] | Table 'Posts' created.");

        } else {
            System.out.println("[PostData, init()] | Error: The connection doesn't exist.");
        }
    }

    @Override
    public ArrayList<Post> getAll() {
        try {
            ArrayList<Post> posts = new ArrayList<>();
            ResultSet rs = mySQLConn.getStatement(true).executeQuery("SELECT * FROM posts");
            int i = 0;
            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("id"),
                        rs.getString("authorId"),
                        rs.getString("message"),
                        rs.getString("tags"),
                        rs.getString("fileId"),
                        rs.getString("fileName"),
                        rs.getString("fileExt"),
                        rs.getLong("fileSize"),
                        rs.getTimestamp("uploadDate"),
                        rs.getTimestamp("lastUpdate")
                ));
                i++;
            }
            System.out.println(String.format("[PostData, getAll()] | %d row(s) retrieved.", i));
            return posts;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<PandaFile> getAllFiles(){
        try {
            ArrayList<PandaFile> files = new ArrayList<>();
            ResultSet rs = mySQLConn.getStatement(true).executeQuery("SELECT fileId, lastUpdate, fileName, fileExt, fileSize, authorId FROM posts");
            int i = 0;
            while (rs.next()) {
                files.add(new PandaFile(
                        rs.getString("fileId"),
                        rs.getTimestamp("lastUpdate"),
                        rs.getString("fileName"),
                        rs.getString("fileExt"),
                        rs.getLong("fileSize"),
                        rs.getString("authorId")
                ));
                i++;
            }
            System.out.println(String.format("[PostData, getAllFiles()] | %d row(s) retrieved.", i));
            return files;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getTotalFilesSize(){
        long sum = 0;
        for (var file : getAllFiles()){
            sum += file.getSize();
        }
        return sum;
    }

    @Override
    public ArrayList<Post> getMatchingData(Filter filter) {
        var posts = new ArrayList<Post>();

        // I iterate each post
        for (Post post : getAll()) {
            // and I check if it matches the filter
            if (post.matchesFilter((PostFilter) filter)) {
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
    public Post getMatchingItem(Filter filter) {
        ArrayList<Post> matchingData = getMatchingData(filter);

        if(matchingData.size() > 0){
            return matchingData.get(0);
        }
        return null;
    }

    @Override
    public String getMatchingItemToJson(Filter filter) {
        return new Gson().toJson(getMatchingItem(filter));
    }

    @Override
    public Post get(Object id) { // ID is an int
        List<Post> posts = getAll();
        return posts.get(posts.indexOf(new Post((int) id)));
    }

    @Override
    public boolean add(Post post) {
        try {
            // If the post contains a file, I save the file first
            if (post.containsAFile()){
                // If the post file hasn't been actually added, I return a failure
                if(!postFileSuccessfullyAdded(post)){
                    System.out.println("[PostData, add()] | Error! The post file wasn't added.");
                    return false;
                }
            }

            // The SQL Statement to add a post in the database
            String sql = String.format("INSERT INTO posts (authorId, message, tags, fileId, fileName, fileExt, fileSize) " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %d)",
                    post.getAuthorId(),
                    post.getMessage(),
                    post.getTags(),
                    post.getFileId(),
                    post.getFileName(),
                    post.getFileExt(),
                    post.getFileSize()
            );

            // I don't use the executeUpdate() method of MySQLConnection 'cause I want to check if it throws an exception here
            mySQLConn.getStatement(true).executeUpdate(sql);
            System.out.println("[PostData, add()] | A post has been successfully added.");
            return true;

        } catch (SQLException e) {
            System.out.println("[PostData, add()] | Error! Something went wrong while adding a post.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean postFileSuccessfullyAdded(Post post){
        try {
            // I generate a random name (that will be used as an Id for the file) for the post file
            String aRandomFileId = getARandomFileId(post.getAuthorId(), post.getFileExt());

            // I set the fileId of the post, using the random file id
            post.setFileId(aRandomFileId);

            // I build a path for the post file using the random fileId
            String generatedFilePath = generateAPandaFilePath(post.getAuthorId(), post.getFileId(), post.getFileExt());

            // I create the file using the generated filePath, then I set its content
            FileUtils.writeByteArrayToFile(new File(generatedFilePath), post.getFileToBytes());

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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
