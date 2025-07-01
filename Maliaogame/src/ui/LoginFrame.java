package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DatabaseUtil;
import util.PasswordUtil;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private EnterFrame enterFrame; // 引用EnterFrame

    public LoginFrame(EnterFrame enterFrame) {
        this.enterFrame = enterFrame; // 保存传入的EnterFrame实例

        this.setSize(400, 300);
        this.setTitle("登录");
        this.setResizable(false);
        this.getContentPane().setLayout(null);

        // 设置背景颜色
        this.getContentPane().setBackground(new Color(60, 63, 65));

        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(50, 50, 80, 30);
        this.getContentPane().add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameField.setBounds(140, 50, 200, 30);
        this.getContentPane().add(usernameField);

        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 100, 80, 30);
        this.getContentPane().add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField.setBounds(140, 100, 200, 30);
        this.getContentPane().add(passwordField);

        loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        loginButton.setBackground(new Color(0, 122, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBounds(140, 150, 100, 30);
        this.getContentPane().add(loginButton);
        loginButton.addActionListener(new LoginAction());

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "用户名或密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 登录逻辑
            try (Connection conn = DatabaseUtil.getConnection()) {
                String sql = "SELECT id, password_hash, salt FROM users WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int userid = rs.getInt("id");
                    String storedHash = rs.getString("password_hash");
                    String salt = rs.getString("salt"); // 从数据库中获取盐值

                    // 验证密码
                    if (PasswordUtil.checkPassword(password, storedHash, salt)) {
                        // 加载用户的游戏记录
                        String gameRecord = loadGameRecord(conn, username);
                        JOptionPane.showMessageDialog(null, "登录成功", "信息", JOptionPane.INFORMATION_MESSAGE);

                        // 更新EnterFrame的状态
                        enterFrame.updateLoginState(true, username, userid, gameRecord);

                        dispose(); // 关闭登录窗口
                    } else {
                        JOptionPane.showMessageDialog(null, "密码错误", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "用户名不存在", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "登录失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }

        private String loadGameRecord(Connection conn, String username) throws SQLException {
            String gameRecord;
            String sql = "SELECT * FROM game_records WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            // 检查用户是否有游戏记录
            if (rs.next()) {
                int level = rs.getInt("level");
                int blood = rs.getInt("blood");
                gameRecord = "用户: " + username + " | 记录:第 " + level + "关" + " | 血量: " + blood;
            } else {
                gameRecord = "欢迎, " + username + "! 暂无游戏记录，请开始新游戏！";
            }
            return gameRecord;
        }
    }
}
