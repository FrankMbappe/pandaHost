package panda.host.model.models;

/**
 * Set of necessary properties to properly establish a connection with MySQL
 */
public class MySQLConfig {
    static final String DB_URL_FORMAT = "jdbc:mysql://localhost:3306/%s?" +
            "useSSL=false&&allowPublicKeyRetrieval=true&&useUnicode=true" +
            "&characterEncoding=UTF-8" +
            "&serverTimezone=UTC";
    private String jdbcDriver = "com.mysql.cj.jdbc.Driver";
    private String dbUrl = DB_URL_FORMAT;
    private String dbName = "";
    private String username;
    private String password;


    public MySQLConfig() {
    }

    public MySQLConfig(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public MySQLConfig(String username, String password, String dbName) {
        this.username = username;
        this.password = password;
        this.dbName = dbName;
        // And since I don't need to specify the db name at the beginning:
        this.dbUrl = String.format(DB_URL_FORMAT, "");
    }

    public MySQLConfig(String jdbcDriver, String dbUrl, String username, String password, String dbName) {
        this.dbName = dbName;
        this.jdbcDriver = jdbcDriver;
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" + "\n" +
                "\tjdbcDriver: \"" + jdbcDriver + "\",\n" +
                "\tdbName: \"" + dbName + "\",\n" +
                "\tdbUrl: \"" + dbUrl + "\"" + ",\n" +
                "\tusername: \"" + username + "\",\n" +
                "\tpassword: \"" + password + "\"\n" +
                '}';
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * Alternative to getDbUrl() method.
     * @return The database URL, with the database name specified.
     */
    public String existingDbUrl(){
        // Sometimes, like when creating a table in a db, we must specify the db name
        return String.format(DB_URL_FORMAT, dbName);
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
