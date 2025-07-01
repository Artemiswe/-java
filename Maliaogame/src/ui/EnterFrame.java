package ui;

import java.awt.*;
import javax.swing.*;

public class EnterFrame extends JFrame {
    public JButton jb1, loginButton, registerButton;
    public JLabel userInfoLabel;
    private boolean isLoggedIn = false; // 标记用户是否成功登录
    private String username = ""; // 存储成功登录的用户名
    private String gameRecord = ""; // 存储游戏记录信息
    private int userid = 0; // 存储用户ID

    // 构造函数初始化背景
    public EnterFrame() {
        // 设置窗口大小、标题、不可变大小
        this.setSize(800, 450);
        this.setTitle("超级玛丽");
        this.setResizable(false);
        this.getContentPane().setLayout(null);

        // 设置背景图片
        JPanel myPanel = (JPanel) this.getContentPane();
        myPanel.setOpaque(false);
        ImageIcon icon = new ImageIcon("image/homePage.jpeg");
        JLabel label = new JLabel(icon);
        label.setBounds(0, 0, this.getWidth(), this.getHeight());
        icon.setImage(icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT));
        this.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE)); // 将背景添加到层面板中

        // "START GAME" 按钮，初始不可见，登录后可见
        jb1 = new JButton("开始游戏");
        jb1.setFont(new Font("微软雅黑", Font.PLAIN, 30));
        jb1.setForeground(Color.black);
        jb1.setBounds(260, 310, 290, 40);
        jb1.setHorizontalAlignment(JButton.CENTER);
        jb1.addActionListener(e -> startGame());
        jb1.setVisible(false); // 初始不可见
        this.getContentPane().add(jb1);

        // 登录按钮
        loginButton = new JButton("登录");
        loginButton.setFont(new Font("方正舒体", Font.PLAIN, 20));
        loginButton.setBounds(200, 200, 150, 40);
        loginButton.addActionListener(e -> new LoginFrame(this)); // 打开登录窗口并传递自身
        this.getContentPane().add(loginButton);

        // 注册按钮
        registerButton = new JButton("注册");
        registerButton.setFont(new Font("方正舒体", Font.PLAIN, 20));
        registerButton.setBounds(450, 200, 150, 40);
        registerButton.addActionListener(e -> {
            try {
                new RegisterFrame(); // 打开注册窗口
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        this.getContentPane().add(registerButton);

        // 用户信息标签，用于显示登录后的游戏记录或者欢迎信息
        userInfoLabel = new JLabel("", SwingConstants.CENTER);
        userInfoLabel.setFont(new Font("方正舒体", Font.PLAIN, 23));
        userInfoLabel.setForeground(Color.ORANGE);
        userInfoLabel.setBounds(100, 100, 600, 50);
        this.getContentPane().add(userInfoLabel);

        // 窗口居中显示
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    // 更新登录状态和游戏记录信息
    public void updateLoginState(boolean isLoggedIn, String username, int userid, String gameRecord) {
        this.isLoggedIn = isLoggedIn;
        this.username = username;
        this.userid = userid;
        this.gameRecord = gameRecord;
        refreshUI();
    }

    // 刷新界面
    private void refreshUI() {
        // 隐藏登录和注册按钮
        loginButton.setVisible(false);
        registerButton.setVisible(false);
        // 显示用户游戏记录和 "START GAME" 按钮
        userInfoLabel.setText(gameRecord);
        jb1.setVisible(true);
    }

    // 开始游戏
    private void startGame() {
        // 关闭当前窗口并打开游戏选择窗口
        dispose();
        try {
            new ChooseFrame(this.username, this.userid); // 打开选择框
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
