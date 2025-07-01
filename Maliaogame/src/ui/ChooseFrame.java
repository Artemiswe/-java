package ui;

import util.KeyListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;

/**
 * ChooseFrame类是一个用于选择游戏关卡的窗口。
 * 它扩展了JFrame，并实现了ActionListener接口来处理按钮点击事件。
 */
public class ChooseFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L; // 序列化ID
    private JButton[] buttons; // 按钮数组用于存储关卡按钮
    private int userid; // 用户ID
    private String username; // 用户名

    /**
     * 带参数的构造函数，用于初始化选择窗口。
     *
     * @param username 用户名
     * @param userid 用户ID
     * @throws Exception 如果出现异常
     */
    public ChooseFrame(String username, int userid) throws Exception {
        this.userid = userid; // 初始化用户ID
        this.username = username; // 初始化用户名

        // 初始化窗体相关属性
        this.setSize(800, 450); // 设置窗口大小
        this.setTitle("超级玛丽"); // 设置窗口标题
        this.setResizable(false); // 禁止调整窗口大小
        this.getContentPane().setLayout(null); // 使用绝对布局

        // 设置背景图片，不影响按钮组件
        JPanel myPanel = (JPanel) this.getContentPane();
        myPanel.setOpaque(false); // 使面板透明

        ImageIcon icon = new ImageIcon("image/mario1.jpg"); // 获取背景图片
        JLabel label = new JLabel(icon); // 使用JLabel显示图片
        label.setBounds(0, 0, this.getWidth(), this.getHeight()); // 设置图片大小与窗口匹配
        icon.setImage(icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH)); // 平滑缩放图片
        this.getLayeredPane().add(label, Integer.valueOf(Integer.MIN_VALUE)); // 添加背景图片到层次面板

        // 定义按钮的背景颜色
        Color mycolor = new Color(190, 141, 41);

        // 初始化按钮数组
        buttons = new JButton[5];
        Font buttonFont = new Font("方正舒体", Font.PLAIN, 30); // 按钮字体

        // 使用循环创建并设置多个按钮
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(String.valueOf(i + 1)); // 按钮文本为关卡编号
            buttons[i].setFont(buttonFont); // 设置按钮字体
            buttons[i].setForeground(Color.yellow); // 设置按钮前景色
            buttons[i].setBackground(mycolor); // 设置按钮背景色
            buttons[i].setBounds(10 + 101 * i, 95, 65, 65); // 设置按钮位置和大小
            buttons[i].setHorizontalAlignment(JButton.CENTER); // 设置按钮文本居中
            buttons[i].addActionListener(this); // 为按钮添加动作监听
            this.getContentPane().add(buttons[i]); // 将按钮添加到内容面板
        }

        // 设置窗口居中显示
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * 默认构造函数。
     */
    public ChooseFrame() {
    }

    /**
     * 覆写的动作事件处理方法。
     * 根据按钮的动作命令执行不同的操作。
     *
     * @param e 动作事件对象
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        int level = Integer.parseInt(command); // 将按钮的动作命令转换为整数以表示关卡

        dispose(); // 关闭当前窗口
        try {
            // 创建一个新的游戏窗口，并为其添加键盘监听器
            GameFrame g = new GameFrame(level, this.userid, this.username);
            KeyListener kl = new KeyListener(g);
            g.addKeyListener(kl);
        } catch (Exception e1) {
            e1.printStackTrace(); // 打印异常信息
        }
    }
}
