package com.shuangsetoolsserver.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import com.shuangsetoolsserver.base.Log;
 
/**
* ConnectionPool 类创建了一个对特定数据库指定大小的连接池。连接池对象 允许客户端指定 JDBC
* 驱动程序，数据库，使用数据库的用户名和密码。而且， 客户端能指定连接池的在初始创建是产生
* 数据 库连接的数量，和指定当连接不够时每次自动增加连接的数量及连接池最多的数据库连接的数量。
* 
* 对外提供的方法有： 
* ConnectionPool ：构造函数 
* getInitialConnections: 返回连接池初始化大小
* setInitialConnections: 设置连接池初始化大小 
* getIncrementalConnections: 返回连接池自动增加的增量
* setIncrementalConnections: 设置连接池自动增加的大小 
* getMaxConnections ：获得连接池的最大可允许的连接数
* setMaxConnections ：设置连接池的最大可允许的连接数 
* getTestTable ：获得测试表的名字 
* setTestTable ：设置测试表的名字 
* createPool: 创建连接池 , 线程己同步 
* getConnection: 从连接池中获得一个数据库连接
* returnConnection: 返回一个连接到连接池中 
* refreshConnections: 刷新连接池 
* closeConnectionPool: 关闭连接池
*/



public class ConnectionPool {
    private final static String TAG = "ConnectionPool";
    private static ConnectionPool instance = null;

    private String jdbcDriver = "com.mysql.jdbc.Driver"; // 数据库驱动
    private String dbServer = "localhost";
    private String dbPort = "3306";
    private String dbName = "shuangse";
    private String dbUsername = "java"; // 数据库用户名
    private String dbPassword = "100200"; // 数据库用户密码
    private String dbUrl = "jdbc:mysql://" + 
                            dbServer + ":" + 
                            dbPort + "/" + 
                            dbName + "?autoReconnect=true" + 
                            "&useUnicode=true" + 
                            "&characterEncoding=UTF-8" +
                            "&characterSetResults=UTF-8" + 
                            "&user=" + dbUsername + 
                            "&password=" + dbPassword; // 数据 URL
    
    //标识是否在返回一个连接的时候测试每一个连接的可用性
    private boolean ifTestEachConnectionWhenUseIt = true;
    
    private String testTable = "testconn"; // 测试连接是否可用的测试表名，默认测试表为""-不使用测试表
    private int initialConnections = 20; // 连接池的初始大小

    private int incrementalConnections = 10; // 连接池自动增加的大小
    private int maxConnections = 50; // 连接池最大的大小

    private Vector<PooledConnection> connections = null; // 存放连接池中数据库连接的向量 ,
                                                            // 初始时为 null

    // 它中存放的对象为 PooledConnection 型
    /**
     * 
     * 构造函数
     * 
     * @param jdbcDriver
     *            String JDBC 驱动类串
     * 
     * @param dbUrl
     *            String 数据库 URL
     * 
     * @param dbUsername
     *            String 连接数据库用户名
     * 
     * @param dbPassword
     *            String 连接数据库用户的密码
     * 
     * 
     */

    private ConnectionPool() {
        FileInputStream inputFile = null;
        try {
            inputFile = new FileInputStream("/etc/shuangsetool.conf");
            Properties pro = new Properties();
            pro.load(inputFile);
            inputFile.close();
            
            dbServer = pro.getProperty("DBServer");
            dbPort = pro.getProperty("DBPort");
            dbName = pro.getProperty("DBName");  
            dbUsername = pro.getProperty("DBusername");  
            dbPassword = pro.getProperty("DBpassword");
            
            Log.i(TAG,"ConnectionPool::ConnectionPool() - DBServer: " + dbServer);
            Log.i(TAG,"ConnectionPool::ConnectionPool() - dbPort: " + dbPort);
            Log.i(TAG,"ConnectionPool::ConnectionPool() - dbName: " + dbName);
            Log.i(TAG,"ConnectionPool::ConnectionPool() - dbUsername: " + dbUsername);
            Log.i(TAG,"ConnectionPool::ConnectionPool() - dbPassword: " + dbPassword);
            
            //update the DB URL
            dbUrl = "jdbc:mysql://" + 
                    dbServer + ":" + 
                    dbPort + "/" + 
                    dbName + "?autoReconnect=true" + 
                    "&characterEncoding=UTF-8" + 
                    "&characterSetResults=UTF-8" + 
                    "&user=" + dbUsername + 
                    "&password=" + dbPassword; // 数据 URL
            
            Log.i(TAG,"ConnectionPool::ConnectionPool() - " + dbUrl);
            
        } catch (FileNotFoundException e) {
          
            Log.w(TAG,"ConnectionPool::ConnectionPool() /etc/shuangsetool.conf doesn't exist, will use default configuration.");
            
        } catch (IOException e) {
            Log.w(TAG,"ConnectionPool::ConnectionPool() read /etc/shuangsetool.conf got exception, will use default configuration" + e.toString());
        } 
    }
    
    public static synchronized ConnectionPool getInstance() {
        if (instance==null) {            
            instance = new ConnectionPool();
        }
            
        try {
            instance.createPool();
        } catch(Exception e) {
            Log.e(TAG,"got exception during createPool()" + e.toString());
        }
        
        return instance; 
    }

    

    /**
     * 
     * 返回连接池的初始大小
     * 
     * @return 初始连接池中可获得的连接数量
     */

    public int getInitialConnections() {
        return this.initialConnections;
    }

    public boolean getIfTestEachConnectionWhenUseIt() {
        return this.ifTestEachConnectionWhenUseIt;
    }
    
    public void setIfTestEachConnectionWhenUseIt(boolean flag) {
        this.ifTestEachConnectionWhenUseIt = flag;
    }
    /**
     * 
     * 设置连接池的初始大小
     * 
     * @param 用于设置初始连接池中连接的数量
     */

    public void setInitialConnections(int initialConnections) {
        this.initialConnections = initialConnections;
    }

    /**
     * 
     * 返回连接池自动增加的大小
     * 
     * @return 连接池自动增加的大小
     */

    public int getIncrementalConnections() {
        return this.incrementalConnections;
    }

    /**
     * 
     * 设置连接池自动增加的大小
     * 
     * @param 连接池自动增加的大小
     */

    public void setIncrementalConnections(int incrementalConnections) {
        this.incrementalConnections = incrementalConnections;
    }

    /**
     * 
     * 返回连接池中最大的可用连接数量
     * 
     * @return 连接池中最大的可用连接数量
     */

    public int getMaxConnections() {
        return this.maxConnections;
    }

    /**
     * 
     * 设置连接池中最大可用的连接数量
     * 
     * @param 设置连接池中最大可用的连接数量值
     */

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    /**
     * 
     * 获取测试数据库表的名字
     * 
     * @return 测试数据库表的名字
     */

    public String getTestTable() {
        return this.testTable;
    }

    /**
     * 
     * 设置测试表的名字
     * 
     * @param testTable
     *            String 测试表的名字
     */
    public void setTestTable(String testTable) {
        this.testTable = testTable;
    }

    /**
     * 
     * 创建一个数据库连接池，连接池中的可用连接的数量采用类成员 initialConnections 中设置的值
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws SQLException 
     */
    public synchronized void createPool() throws SQLException, 
                                        InstantiationException, 
                                        IllegalAccessException, 
                                        ClassNotFoundException {

        // 确保连接池没有创建
        // 如果连接池己经创建了，保存连接的向量 connections 不会为空
        if (connections != null) {
            return; // 如果己经创建，则返回
        }
        
        // 实例化 JDBC Driver 中指定的驱动类实例
        DriverManager.registerDriver((Driver) Class.forName(this.jdbcDriver).newInstance());
        
        // 创建保存连接的向量 , 初始时有 10 个元素
        connections = new Vector<PooledConnection>();
        // 根据 initialConnections 中设置的值，创建连接。
        createConnections(this.initialConnections);
        Log.i(TAG,"Created the DB connection pool Successfully.");
    }

    /**
     * 
     * 创建由 numConnections 指定数目的数据库连接 , 并把这些连接 放入 connections 向量中
     * 
     * @param numConnections
     *            要创建的数据库连接的数目
     */
    private void createConnections(int numConnections) throws SQLException {
        // 循环创建指定数目的数据库连接
        for (int x = 0; x < numConnections; x++) {
            // 是否连接池中的数据库连接的数量己经达到最大？最大值由类成员 maxConnections
            // 指出，如果 maxConnections 为 0 或负数，表示连接数量没有限制。
            // 如果连接数己经达到最大，即退出。
            if (this.maxConnections > 0
                    && this.connections.size() >= this.maxConnections) {
                Log.w(TAG," ConnectionPool::createConnections() reached max, can not create new connections.");
                break;
            }

            // add a new PooledConnection object to connections vector
            // 增加一个连接到连接池中（向量 connections 中）
            try {
                connections.addElement(new PooledConnection(newConnection()));
            } catch (SQLException e) {
                Log.e(TAG," ConnectionPool::createConnections()  failed to create DB connectoin." + e.toString());
                throw new SQLException();
            }
            
            Log.i(TAG,"The DB connection is created......" +x);
        }

    }

    /**
     * 
     * 创建一个新的数据库连接并返回它
     * 
     * @return 返回一个新创建的数据库连接
     */

    private Connection newConnection() throws SQLException {

        // 创建一个数据库连接
        Log.i(TAG,"ConnectionPool::newConnection(): " + dbUrl);
        Connection conn = DriverManager.getConnection(dbUrl);

        // 如果这是第一次创建数据库连接，即检查数据库，获得此数据库允许支持的
        // 最大客户连接数目
        // connections.size()==0 表示目前没有连接己被创建

        if (connections.size() == 0) {
            DatabaseMetaData metaData = (DatabaseMetaData) conn.getMetaData();
            int driverMaxConnections = metaData.getMaxConnections();
            
            // 数据库返回的 driverMaxConnections 若为 0 ，表示此数据库没有最大
            // 连接限制，或数据库的最大连接限制不知道
            // driverMaxConnections 为返回的一个整数，表示此数据库允许客户连接的数目
            // 如果连接池中设置的最大连接数量大于数据库允许的连接数目 , 则置连接池的最大
            // 连接数目为数据库允许的最大数目

            if (driverMaxConnections > 0
                    && this.maxConnections > driverMaxConnections) {
                this.maxConnections = driverMaxConnections;
            }

        }
        return conn; // 返回创建的新的数据库连接
    }

    /**
     * 
     * 通过调用 getFreeConnection() 函数返回一个可用的数据库连接 , 如果当前没有可用的数据库连接，并且更多的数据库连接不能创
     * 建（如连接池大小的限制），此函数等待一会再尝试获取。
     * 
     * @return 返回一个可用的数据库连接对象
     */

    public synchronized Connection getConnection() throws SQLException {

        // 确保连接池己被创建
        if (connections == null) {
            Log.e(TAG,"ConnectionPool::getConnection() return null since the DB connections pool is null.");
            return null; // 连接池还没创建，则返回 null
        }

        Connection conn = getFreeConnection(); // 获得一个可用的数据库连接
        // 如果目前没有可以使用的连接，即所有的连接都在使用中
        while (conn == null) {
            // 等一会再试
            Log.w(TAG,"ConnectionPool::getConnection(), no free connections in the pool. wait 250ms until there has free connection.");
            wait(250);
            conn = getFreeConnection(); // 重新再试，直到获得可用的连接，如果
            // getFreeConnection() 返回的为 null
            // 则表明创建一批连接后也不可获得可用连接
        }
        return conn;// 返回获得的可用的连接
    }

    /**
     * 
     * 本函数从连接池向量 connections 中返回一个可用的的数据库连接，如果 当前没有可用的数据库连接，本函数则根据
     * incrementalConnections 设置 的值创建几个数据库连接，并放入连接池中。
     * 
     * 如果创建后，所有的连接仍都在使用中，则返回 null
     * 
     * @return 返回一个可用的数据库连接
     */

    private Connection getFreeConnection() throws SQLException {
        // 从连接池中获得一个可用的数据库连接
        Connection conn = findFreeConnection();
        if (conn == null) {
            // 如果目前连接池中没有可用的连接
            // 创建一些连接
            Log.w(TAG,"ConnectionPool::getFreeConnection() no more free connection, now expand the pool.");
            createConnections(incrementalConnections);

            // 重新从池中查找是否有可用连接
            conn = findFreeConnection();
            if (conn == null) {
                // 如果创建连接后仍获得不到可用的连接，则返回 null
                Log.w(TAG,"ConnectionPool::getFreeConnection(), there is no free connection even after expanded, return null.");
                return null;
            } else {
                Log.i(TAG,"ConnectionPool::getFreeConnection(), got one connection after expaned, return it.");
                return conn;
            }
        }
        Log.i(TAG,"ConnectionPool::getFreeConnection() got one connection and start to use it.");
        return conn;
    }

    /**
     * 
     * 查找连接池中所有的连接，查找一个可用的数据库连接， 如果没有可用的连接，返回 null
     * 
     * @return 返回一个可用的数据库连接
     */

    private Connection findFreeConnection() throws SQLException {
        Connection conn = null;        
        PooledConnection pConn = null;
        
        // 获得连接池向量中所有的对象        
        Enumeration<PooledConnection> enumVec = connections.elements();
        
        // 遍历所有的对象，看是否有可用的连接        
        while (enumVec.hasMoreElements()) {        
            pConn = (PooledConnection) enumVec.nextElement();
        
            if (!pConn.isBusy()) {
                
                // 如果此对象不忙，则获得它的数据库连接并把它设为忙
                conn = pConn.getConnection();
                pConn.setBusy(true);
                // 测试此连接是否可用
                if( conn.isClosed() ||
                    (getIfTestEachConnectionWhenUseIt() && (!testConnection(conn)))) {
                    // 如果此连接不可再用了，则创建一个新的连接，
                    // 并替换此不可用的连接对象，如果创建失败，返回 null
                    Log.w(TAG,"ConnectionPool::findFreeConnection(), the connection can not be used since it failed during testing. create a new connection to replace it.");
                    try{
                        conn = newConnection();
                        if(conn != null) {
                            pConn.setConnection(conn);
                            Log.i(TAG,"ConnectionPool::findFreeConnection(), created a new connection and return it");
                            return conn; // 创建一个新的连接，退出
                        } else {
                            Log.e(TAG,"ConnectionPool::findFreeConnection(), failed to create connection, no free connection can be used, return null.");
                            return null;
                        }
                    }catch(SQLException e){
                        Log.e(TAG,"ConnectionPool::findFreeConnection() failed to create connection, no free connection can be used, return null." + e.toString());
                        return null;
                    }
                } else {//连接可用
                    return conn; // 己经找到一个可用的连接，退出
                }
            } else {
                //该连接已经在忙了，继续寻找下一个不忙的连接
                continue;
            }
        }
        
        return conn;// 返回找到到的可用连接或null
    }

    /**
     * 
     * 测试一个连接是否可用，如果不可用，关掉它并返回 false 
     * 否则可用返回 true
     * 
     * @param conn    需要测试的数据库连接
     * 
     * @return 返回 true 表示此连接可用， false 表示不可用
     */

    private boolean testConnection(Connection conn) {
        Statement stmt = null;
        Log.i(TAG,"ConnectionPool::testConnection(), begin to test connection.");
        try {
            // 判断测试表是否存在
            if (testTable.equals("")) {
              Log.w(TAG,"ConnectionPool::testConnection(), test table is null or empty.");
                // 如果测试表为空，试着使用此连接的 setAutoCommit() 方法
                // 来判断连接否可用（此方法只在部分数据库可用，如果不可用 ,
                // 抛出异常）。注意：使用测试表的方法更可靠
                conn.setAutoCommit(true);
            } else {// 有测试表的时候使用测试表测试
                // check if this connection is valid
                stmt = (Statement) conn.createStatement();
                String testSQL = "select count(*) from " + testTable;
                stmt.execute(testSQL);
                ResultSet rs = stmt.getResultSet();
                rs.close();
                rs = null;
            }
        } catch (SQLException e) {
            // 上面抛出异常，此连接己不可用，关闭它，并返回 false;
            Log.w(TAG,"ConnectionPool::testConnection(), got exception during testing, this connection can not be used this moment, close it."  + e.toString());
            closeConnection(conn);
            return false;
        } finally {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    //ignore
                }
                stmt = null;
            }
        }
        // 连接可用，返回 true
        Log.i(TAG,"ConnectionPool::testConnection(), end the connection testing. return true and this connection can be used.");
        return true;
    }

    /**
     * 
     * 此函数返回一个数据库连接到连接池中，并把此连接置为空闲。      
     * 所有使用连接池获得的数据库连接均应在不使用此连接时返回它。
     * 
     * @param 需返回到连接池中的连接对象
     */
    public void returnConnection(Connection conn) {
        //确保连接池存在，如果连接没有创建（不存在），直接返回
        if (connections == null) {
            Log.w(TAG,"Connection pool is null, can not return this connection back to the pool");
            return;
        }
        PooledConnection pConn = null;
        Enumeration<PooledConnection> enumVec = connections.elements();
        
        //遍历连接池中的所有连接，找到这个要返回的连接对象
        while (enumVec.hasMoreElements()) {
            pConn = (PooledConnection) enumVec.nextElement();
            // 先找到连接池中的要返回的连接对象
            if (conn == pConn.getConnection()) {
                // 找到了 , 设置此连接为空闲状态
                pConn.setBusy(false);
                Log.i(TAG,"ConnectionPool::returnConnection(), return a connection to the pool successfully.");
                break;
            }
        }
    }

    /**
     * 
     * 刷新连接池中所有的连接对象
     * 
     */
    public synchronized void refreshConnections() throws SQLException {

        // 确保连接池己创新存在
        if (connections == null) {
            Log.w(TAG,"Connection Pool is null, can not reflesh. return.");
            return;
        }
        PooledConnection pConn = null;
        Enumeration<PooledConnection> enumVec = connections.elements();
        while (enumVec.hasMoreElements()) {
            // 获得一个连接对象
            pConn = (PooledConnection) enumVec.nextElement();
            // 如果对象忙则等 5 秒 ,5 秒后直接刷新
            if (pConn.isBusy()) {
                wait(5000); // 等 5 秒
            }
            // 关闭此连接，用一个新的连接代替它。
            closeConnection(pConn.getConnection());
            pConn.setConnection(newConnection());
            pConn.setBusy(false);
        }
    }

    /**
     * 
     * 关闭连接池中所有的连接，并清空连接池。
     */
    public synchronized void closeConnectionPool() throws SQLException {
        // 确保连接池存在，如果不存在，返回
        if (connections == null) {
            Log.w(TAG,"Connection pool is null, can not return.");
            return;
        }
        PooledConnection pConn = null;
        Enumeration<PooledConnection> enumVec = connections.elements();
        while (enumVec.hasMoreElements()) {
            pConn = (PooledConnection) enumVec.nextElement();
            // 如果忙，等 5 秒
            if (pConn.isBusy()) {
                wait(5000); // 等 5 秒
            }
            //5 秒后直接关闭它
            pConn.setBusy(false);
            closeConnection(pConn.getConnection());
            
            // 从连接池向量中删除它
            connections.removeElement(pConn);
        }
        // 置连接池为空
        connections.clear();
        connections = null;
    }

    /**
     * 
     * 关闭一个数据库连接
     * 
     * @param 需要关闭的数据库连接
     */
    private void closeConnection(Connection conn) {
        try {
            conn.close();
            conn = null;
        } catch (SQLException e) {
          Log.w(TAG, "Got exception when closeConnection： " + e.toString());
          conn = null;
        }
    }

    /**
     * 
     * 使程序等待给定的毫秒数
     * 
     * @param 给定的毫秒数
     */
    private void wait(int mSeconds) {
        try {
            Thread.sleep(mSeconds);
        } catch (InterruptedException e) {
            Log.w(TAG, "Got exception during wait:" + e.toString());
        }
    }

    /**
     * 
     * 内部使用的用于保存连接池中连接对象的类
     *  
     * 此类中有两个成员，一个是数据库的连接，另一个是指示此连接是否
     * 正在使用的标志。
     */
    class PooledConnection {
        Connection connection = null;// 数据库连接
        boolean busy = false; // 此连接是否正在使用的标志，默认没有正在使用
        
        // 构造函数，根据一个 Connection 构告一个 PooledConnection 对象
        public PooledConnection(Connection connection) {
            this.connection = connection;
        }
        // 返回此对象中的连接
        public Connection getConnection() {
            return connection;
        }
        // 设置此对象的，连接
        public void setConnection(Connection connection) {
            this.connection = connection;
        }
        // 获得对象连接是否忙
        public boolean isBusy() {
            return busy;
        }
        // 设置对象的连接正在忙
        public void setBusy(boolean busy) {
            this.busy = busy;
        }
    } 

}
