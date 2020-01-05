package kz.altel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;

/**
 * @author vazhenin
 */
public class DButilities {

    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Driver myDriver = null;
    String user, password, database_url, class_name, logFileName;

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public void setRs(ResultSet rs) {
        this.rs = rs;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase_url(String database_url) {
        this.database_url = database_url;
    }

    public Connection getConn() {
        return conn;
    }

    public Statement getStmt() {
        return stmt;
    }

    public ResultSet getRs() {
        return rs;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase_url() {
        return database_url;
    }

    String read_sql(String file) {
        BufferedReader br = null;
        File f = new File(file);
        String line = "-1", Result = "";
        // if file does not exist , file error
        if (!f.exists()) {
            return new FileNotFoundException().getMessage();
        }
        try {
            br = new BufferedReader(new FileReader(f));
            while ((line = br.readLine()) != null) {
                //System.out.println(i + "; " + line);
                Result += line;
            }
        } catch (Exception ex) {
            Logging.put_log(ex.getMessage());
        }
        return Result;
    }

    void load_class(String name) {
        setClass_name(name);
        try {
            this.myDriver = new oracle.jdbc.OracleDriver();
            DriverManager.registerDriver(this.myDriver);
        } catch (Exception ex) {
            Logging.put_log("Error: unable to load driver class! (Details below)");
            Logging.put_log(ex.getMessage());
            System.exit(1);
        }
    }

    void open_conn() {
        try {
            this.conn = DriverManager.getConnection(this.database_url,
                    this.user,
                    this.password);
        } catch (SQLException ex) {
            Logging.put_log(ex.getMessage());
            System.exit(1);
        }
    }

    ResultSet executeQuery(String sql) {
        try {
            // create statement
            this.stmt = this.conn.createStatement();
            // execute query and return result set
            return this.stmt.executeQuery(sql);
        } catch (Exception ex) {
            Logging.put_log(ex.getMessage());
            System.exit(1);
        }
        return this.rs;
    }

    ResultSet executeQuery(String sql, String parameters) {
        try {
            // create statement
            //System.out.println(sql);
            System.out.println(parameters);
            this.pstmt = this.conn.prepareStatement(sql);
            // map parameters
            this.pstmt.setString(1, parameters);
            // execute query and return Result set
            return this.pstmt.executeQuery();
        } catch (Exception ex) {
            Logging.put_log("executeQuery : " + ex.getMessage());
            System.exit(1);
        }
        return this.rs;
    }

    ResultSet GetResultSetQuery(String sql) {
        try {
            // create statement
            this.stmt = this.conn.createStatement();
            // execute query
            this.rs = this.stmt.executeQuery(sql);
        } catch (Exception ex) {
            Logging.put_log(ex.getMessage());
            System.exit(1);
        }
        return rs;
    }

    void close_statement() {
        if (this.stmt != null) {
            try {
                this.stmt.close();
            } catch (Exception e) {
                Logging.put_log(e.getMessage());
                System.exit(1);
            }
        }
    }

    void close_rs() {
        if (this.rs != null) {
            try {
                this.rs.close();
            } catch (Exception e) {
                Logging.put_log(e.getMessage());
                System.exit(1);
            }
        }
    }

    void close_conn() {
        close_rs();
        close_statement();

        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (Exception e) {
                Logging.put_log(e.getMessage());
                System.exit(1);
            }
        }
    }

    void set_property(String property, String value) {
        System.setProperty(property, value);
    }
}
