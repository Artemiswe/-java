package util;

import role.GameRecord;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * GameRecordDAO 类用于对游戏记录进行数据库操作，包括保存、更新和查询。
 */
public class GameRecordDAO {

    /**
     * 保存游戏记录到数据库的方法。
     *
     * @param gameRecord 要保存的游戏记录对象。
     */
    public void saveGameRecord(GameRecord gameRecord) {
        // SQL 语句，用于插入一条游戏记录
        String sql = "INSERT INTO game_records (username, score, blood, created_at, level) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置 SQL 语句中的参数值
            pstmt.setString(1, gameRecord.getUsername());
            pstmt.setInt(2, gameRecord.getScore());
            pstmt.setInt(3, gameRecord.getBlood());
            pstmt.setTimestamp(4, gameRecord.getCreatedAt());
            pstmt.setInt(5, gameRecord.getLevel());

            // 执行插入操作
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈跟踪信息
        }
    }

    public void updateGameRecord(GameRecord gameRecord) {
        String sql = "UPDATE game_records SET score = ?, blood = ?, created_at = ?, level = ? WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameRecord.getScore());
            pstmt.setInt(2, gameRecord.getBlood());
            pstmt.setTimestamp(3, gameRecord.getCreatedAt());
            pstmt.setInt(4, gameRecord.getLevel());
            pstmt.setString(5, gameRecord.getUsername());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Game record updated successfully for username: " + gameRecord.getUsername());
            } else {
                System.out.println("No record found for username: " + gameRecord.getUsername());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 根据用户名获取游戏记录的方法。
     *
     * @param username 要查询的用户名。
     * @return 返回一个包含该用户所有游戏记录的列表。
     */
    public List<GameRecord> getGameRecordsByUsername(String username) {
        List<GameRecord> gameRecords = new ArrayList<>();
        // SQL 语句，用于查询指定用户名的游戏记录
        String sql = "SELECT id, username, score, blood, created_at, level FROM game_records WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置 SQL 语句中的参数值
            pstmt.setString(1, username);

            // 执行查询操作并处理结果集
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String user = rs.getString("username");
                int score = rs.getInt("score");
                int blood = rs.getInt("blood");
                Timestamp createdAt = rs.getTimestamp("created_at");
                int level = rs.getInt("level");

                // 创建 GameRecord 对象并设置属性
                GameRecord gameRecord = new GameRecord(user, score, blood, createdAt, level);
                gameRecord.setId(id); // 设置从数据库获取的ID
                gameRecords.add(gameRecord);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return gameRecords; // 返回游戏记录列表
    }
}
