package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * DatabaseUtil 类用于管理数据库连接和资源的关闭。
 */
public class DatabaseUtil {
    // 数据库连接的URL，其中包括了数据库的地址、端口和数据库名称
    private static final String URL = "jdbc:mysql://localhost:3306/mario?useSSL=false&serverTimezone=UTC";
    // 数据库用户名
    private static final String USER = "hang";
    // 数据库密码
    private static final String PASSWORD = "8892297";

    /**
     * 获取数据库连接的方法。
     *
     * @return 返回一个 Connection 对象，用于与数据库进行交互。
     * @throws Exception 如果加载驱动或建立连接失败，则抛出异常。
     */
    public static Connection getConnection() throws Exception {
        // 加载数据库驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        // 建立并返回数据库连接
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 关闭数据库资源的方法，包括连接、预编译语句和结果集。
     *
     * @param conn  要关闭的数据库连接
     * @param pstmt 要关闭的预编译语句
     * @param rs    要关闭的结果集
     */
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            // 关闭结果集
            if (rs != null) rs.close();
            // 关闭预编译语句
            if (pstmt != null) pstmt.close();
            // 关闭数据库连接
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈跟踪信息
        }
    }

    /**
     * 关闭数据库资源的方法，仅包括连接和预编译语句。
     *
     * @param conn  要关闭的数据库连接
     * @param pstmt 要关闭的预编译语句
     */
    public static void close(Connection conn, PreparedStatement pstmt) {
        // 调用重载的 close 方法，传递 null 作为结果集参数
        close(conn, pstmt, null);
    }
}
