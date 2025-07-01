package util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import mario.Mario;
import mario.Mario2;
import ui.*;
import role.*;
import javax.swing.*;

/**
 * 键盘按下监听类，用于处理游戏界面的按键事件。
 */
public class KeyListener extends KeyAdapter {

	// 游戏界面的引用，用于访问和修改游戏状态
	public GameFrame gf;

	/**
	 * 构造函数，初始化游戏界面引用。
	 *
	 * @param gf 游戏界面对象
	 */
	public KeyListener(GameFrame gf) {
		this.gf = gf;
	}

	/**
	 * 键盘按下事件监听，用于处理不同按键的按下操作。
	 *
	 * @param e 键盘事件
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			// 向右走
			case 39: // Key code for the right arrow key
				gf.mario.right = true; // 设置信号位为真，表示向右移动
				break;
			// 向左走
			case 37: // Key code for the left arrow key
				gf.mario.left = true; // 设置信号位为真，表示向左移动
				break;
			// 发射子弹
			case 32: // Key code for the space bar
				addBoom(); // 调用发射子弹方法
				break;
			// 向上跳跃
			case 38: // Key code for the up arrow key
				gf.mario.up = true; // 设置信号位为真，表示跳跃
				break;
			// 第二个角色（mario2）向右走
			case 68: // Key code for the 'D' key
				gf.mario2.right2 = true; // 设置信号位为真，表示向右移动
				break;
			// 第二个角色（mario2）向左走
			case 65: // Key code for the 'A' key
				gf.mario2.left2 = true; // 设置信号位为真，表示向左移动
				break;
			// 暂停或继续游戏
			case 80: // Key code for the 'P' key
				gf.setPaused(!gf.isPaused()); // 切换游戏暂停状态
				if (gf.isPaused()) {
					JOptionPane.showMessageDialog(null, "游戏已暂停，按 P 键继续", "暂停", JOptionPane.INFORMATION_MESSAGE);
				}
				break;
			// 第二个角色（mario2）发射子弹
			case 83: // Key code for the 'S' key
				addBoom2(); // 调用发射子弹方法
				break;
			// 第二个角色（mario2）向上跳跃
			case 87: // Key code for the 'W' key
				gf.mario2.up2 = true; // 设置信号位为真，表示跳跃
				break;
		}
	}

	/**
	 * 添加主角的子弹。
	 */
	public void addBoom() {
		Boom b = new Boom(gf.mario.x, gf.mario.y + 5, 10);
		if (gf.mario.left) {
			b.speed = -6; // 左向速度
		} else if (gf.mario.right) {
			b.speed = 6; // 右向速度
		} else {
			// 如果角色没有移动，判断角色最后一次的朝向
			b.speed = gf.mario.lastDirection == Mario.Direction.LEFT ? -6 : 6;
		}
		gf.boomList.add(b);
	}

	/**
	 * 添加第二个角色的子弹。
	 */
	public void addBoom2() {
		Boom b2 = new Boom(gf.mario2.x2, gf.mario2.y2 + 5, 10);
		if (gf.mario2.left2) {
			b2.speed = -6; // 左向速度
		} else if (gf.mario2.right2) {
			b2.speed = 6; // 右向速度
		} else {
			// 如果角色没有移动，判断角色最后一次的朝向
			b2.speed = gf.mario2.lastDirection2 == Mario2.Direction.LEFT ? -6 : 6;
		}
		gf.boomList2.add(b2);
	}

	/**
	 * 键盘释放事件监听，用于处理不同按键的释放操作。
	 *
	 * @param e 键盘事件
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == 39) { // 右箭头键释放
			gf.mario.right = false; // 设置信号位为假，停止向右移动
			gf.mario.img = new ImageIcon("image/mari1.png").getImage(); // 恢复角色的默认图像
		}
		if (code == 37) { // 左箭头键释放
			gf.mario.left = false; // 设置信号位为假，停止向左移动
			gf.mario.img = new ImageIcon("image/mari_left1.png").getImage(); // 恢复角色的默认图像
		}
		if (code == 38) { // 上箭头键释放
			gf.mario.up = false; // 设置信号位为假，停止跳跃
		}
		// 第二个角色（mario2）键释放处理
		if (code == 68) { // 'D' 键释放
			gf.mario2.right2 = false; // 设置信号位为假，停止向右移动
			gf.mario2.img2 = new ImageIcon("image/mario31.png").getImage(); // 恢复角色的默认图像
		}
		if (code == 65) { // 'A' 键释放
			gf.mario2.left2 = false; // 设置信号位为假，停止向左移动
			gf.mario2.img2 = new ImageIcon("image/mario2_left1.png").getImage(); // 恢复角色的默认图像
		}
		if (code == 87) { // 'W' 键释放
			gf.mario2.up2 = false; // 设置信号位为假，停止跳跃
		}
	}
}
