package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;

import util.DatabaseUtil;
import util.PasswordUtil;

public class RegisterFrame extends JFrame {
    private JTextField usernameField; // 用户名输入框
    private JPasswordField passwordField; // 密码输入框
    private JButton registerButton; // 注册按钮

    public RegisterFrame() {
        // 设置窗口大小
        this.setSize(400, 300);
        // 设置窗口标题
        this.setTitle("注册");
        // 禁止调整窗口大小
        this.setResizable(false);
        // 使用绝对布局
        this.getContentPane().setLayout(null);

        // 设置背景颜色
        this.getContentPane().setBackground(new Color(60, 63, 65));

        // 创建并添加用户名标签
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        usernameLabel.setForeground(Color.WHITE); // 设置字体颜色为白色
        usernameLabel.setBounds(50, 50, 80, 30); // 设置位置和大小
        this.getContentPane().add(usernameLabel);

        // 创建并添加用户名输入框
        usernameField = new JTextField();
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        usernameField.setBounds(140, 50, 200, 30);
        this.getContentPane().add(usernameField);

        // 创建并添加密码标签
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        passwordLabel.setForeground(Color.WHITE); // 设置字体颜色为白色
        passwordLabel.setBounds(50, 100, 80, 30); // 设置位置和大小
        this.getContentPane().add(passwordLabel);

        // 创建并添加密码输入框
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        passwordField.setBounds(140, 100, 200, 30);
        this.getContentPane().add(passwordField);

        // 创建并添加注册按钮
        registerButton = new JButton("注册");
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 122, 204)); // 设置按钮背景颜色
        registerButton.setForeground(Color.WHITE); // 设置按钮文字颜色
        registerButton.setBounds(140, 150, 100, 30); // 设置位置和大小
        this.getContentPane().add(registerButton);
        registerButton.addActionListener(new RegisterAction()); // 添加按钮点击事件

        // 设置窗口居中显示
        this.setLocationRelativeTo(null);
        // 设置窗口关闭时的操作
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // 设置窗口可见
        this.setVisible(true);
    }

    // 注册操作的具体实现
    private class RegisterAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 获取用户输入的用户名和密码
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // 检查用户名或密码是否为空
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "用户名或密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 生成盐值并加密密码
            String salt = PasswordUtil.generateSalt(); // 生成盐值
            String hashedPassword = PasswordUtil.hashPassword(password, salt); // 使用盐加密密码

            try (Connection conn = DatabaseUtil.getConnection()) {
                // 插入新用户信息到数据库
                String sql = "INSERT INTO users (username, password_hash, salt) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username); // 设置用户名
                pstmt.setString(2, hashedPassword); // 设置加密后的密码
                pstmt.setString(3, salt); // 设置盐值

                int rows = pstmt.executeUpdate(); // 执行SQL语句并返回影响的行数
                if (rows > 0) {
                    // 注册成功提示
                    JOptionPane.showMessageDialog(null, "注册成功", "信息", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // 关闭注册窗口
                }
            } catch (Exception ex) {
                // 注册失败提示
                JOptionPane.showMessageDialog(null, "注册失败", "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // 打印异常信息
            }
        }
    }
}
