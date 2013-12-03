package com.shuangsetoolsserver.db;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

import com.shuangsetoolsserver.base.Log;

public class DBUtil {
    
    private final String TAG = "DBUtil";
    //Each DButil hold a Data Base connection and 
    //this connection is got from the ConnectionPool,
    //it will be released in release method;
    private Connection conn = null;
    private Statement stmt = null;
    private boolean hadErrors = false;

    public DBUtil() throws SQLException {        
        conn = ConnectionPool.getInstance().getConnection();
        //Log.i(TAG, "constructor: successfully get a database connection.");
        if(conn != null) {
            stmt = conn.createStatement();
            //Log.i(TAG, "constructor: successfully create a statement base on the database connection.");
        } else {
            Log.e(TAG, "constructor: can not get a database connection.");
            SQLException e = new SQLException("Can not get a database connection");
            throw e;
        }
    }
    
    //Begin transaction
    public void beginTransaction() throws SQLException
    {
        if(conn != null) {
            conn.setAutoCommit(false);
        }
    }
    
    //Commit transaction 
    public void commitTransaction() throws SQLException
    {
        if( !hadErrors ) {
            if(conn != null) {
                conn.commit();
            } else {
                Log.e(TAG, "conn is null in commitTransaction()");
            }
        } else {
            Log.e(TAG, "commitTransaction() fail, rollback!");
            conn.rollback();
            hadErrors = false;
        }
        
        hadErrors = false;
        if(conn != null) {
            conn.setAutoCommit(true);
        } else {
            Log.e(TAG, "conn is null in commitTransaction()");
        }
    }
    
    //Mark errors
    public void markHasError( ){
        hadErrors = true;
    }
    
    // to execute insert, remove and update
    synchronized public void execute(String sql) throws SQLException {
        try{
            if (stmt != null) {
                Log.i(TAG, "execute(sql) to execute SQL:"+sql);
                stmt.executeUpdate(sql);
            } else {                
                Log.e(TAG, "stmt is null in execute()");
            }
        } catch (SQLException e){
            Log.e(TAG, "execute(sql) to execute SQL:" + sql + " execution error!" + e.toString());
            this.markHasError();
            throw e;
        }
    }
    
    // Execute query
    synchronized public ResultSet read(String sql){
        ResultSet rs = null;
        try{
            if (stmt != null) {
                Log.i(TAG, "read(sql) to execute SQL:"+sql);
                rs = stmt.executeQuery(sql);            
                return rs;
            } else {
                Log.e(TAG, "stmt is null!");
                return null;
            }
        }catch(SQLException e){
            Log.e(TAG, "read(sql) to execute SQL:" + sql + " execution error!");
            e.printStackTrace();
            this.markHasError();
            return null;
        }
    }
    
    //Execute query single Integer value
    synchronized public int readInt( String sql ) throws SQLException{
        int nCount = 0;
        
        try {
            if( stmt != null ) {
                ResultSet rs = null;
                Log.i(TAG, "readCount(sql) to execute SQL:" + sql);
                rs = stmt.executeQuery(sql);
                
                if( rs != null && rs.next() ) {
                    nCount = rs.getInt(1);
                } else {
                    nCount = 0;
                }
            }else {
                nCount = 0;
            }
        }catch(SQLException e) {
            Log.e(TAG, "readInt(sql) to execute SQL:" + sql + " execution error!" + e.toString());
            this.markHasError();
            nCount = 0;
            throw e;
        }
        
        return nCount;
    }
    // to end the connection to database
    synchronized public void release() {
        ConnectionPool.getInstance().returnConnection(conn);
        if(stmt != null){
            try {
                stmt.close();
            } catch (SQLException e) {
                Log.e(TAG, "release() connection error!" + e.toString());
                this.markHasError();
            }
            stmt = null;
        }
    }
}