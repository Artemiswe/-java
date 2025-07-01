package role;

import java.sql.Timestamp;

/**
 * GameRecord类用于表示游戏记录。
 * 该类包含与数据库表列对应的字段，以及相关的构造函数和访问器方法（getter和setter）。
 */
public class GameRecord {

    // 与数据库表中的列对应的字段
    private int id; // 游戏记录的唯一标识符
    private String username; // 玩家用户名
    private int score; // 玩家得分
    private int blood; // 玩家剩余生命值或血量
    private int level; // 游戏关卡
    private Timestamp createdAt; // 游戏记录的创建时间

    /**
     * 带参构造函数，用于初始化GameRecord对象的所有字段。
     *
     * @param username 玩家用户名
     * @param score 玩家得分
     * @param blood 玩家血量
     * @param createdAt 游戏记录创建时间
     * @param level 游戏关卡
     */
    public GameRecord(String username, int score, int blood, Timestamp createdAt, int level) {
        this.username = username;  // 初始化用户名
        this.score = score;        // 初始化得分
        this.blood = blood;        // 初始化血量
        this.createdAt = createdAt; // 初始化创建时间
        this.level = level;         // 初始化关卡
    }

    /**
     * 获取游戏关卡。
     *
     * @return 当前的游戏关卡
     */
    public int getLevel() {
        return level;
    }

    /**
     * 设置游戏关卡。
     *
     * @param level 新的游戏关卡
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 默认构造函数。
     * 用于创建一个空的GameRecord对象。
     */
    public GameRecord() {}

    /**
     * 带部分字段的构造函数，用于初始化GameRecord对象。
     *
     * @param username 玩家用户名
     * @param level 游戏关卡
     * @param createdAt 游戏记录创建时间
     */
    public GameRecord(String username, int level, Timestamp createdAt) {
        this.username = username;
        this.level = level;
        this.createdAt = createdAt;
    }

    /**
     * 获取游戏记录的ID。
     *
     * @return 游戏记录的ID
     */
    public int getId() {
        return id;
    }

    /**
     * 设置游戏记录的ID。
     *
     * @param id 新的游戏记录ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取玩家用户名。
     *
     * @return 玩家用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置玩家用户名。
     *
     * @param username 新的玩家用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取玩家得分。
     *
     * @return 玩家得分
     */
    public int getScore() {
        return score;
    }

    /**
     * 设置玩家得分。
     *
     * @param score 新的玩家得分
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * 获取玩家血量。
     *
     * @return 玩家血量
     */
    public int getBlood() {
        return blood;
    }

    /**
     * 设置玩家血量。
     *
     * @param blood 新的玩家血量
     */
    public void setBlood(int blood) {
        this.blood = blood;
    }

    /**
     * 获取游戏记录的创建时间。
     *
     * @return 游戏记录的创建时间
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置游戏记录的创建时间。
     *
     * @param createdAt 新的游戏记录创建时间
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 返回对象的字符串表示形式。
     *
     * @return 包含对象所有字段的字符串
     */
    @Override
    public String toString() {
        return "GameRecord{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", score=" + score +
                ", blood=" + blood +
                ", createdAt=" + createdAt +
                '}';
    }
}
