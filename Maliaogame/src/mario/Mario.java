package mario;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import role.*;
import ui.GameFrame;

/**
 * 这个类代表了游戏中的马里奥角色。
 * 它处理马里奥的移动、碰撞以及与游戏环境的交互。
 */
public class Mario extends Thread {
	public GameFrame gf; // 游戏窗口的引用
	public boolean jumpFlag = true; // 标识马里奥是否可以跳跃
	public int blood = 3; // 马里奥的生命值
	public int score = 0; // 马里奥的得分
	public int x = 0, y = 358; // 马里奥在屏幕上的位置
	public int xspeed = 4, yspeed = 1; // 马里奥在x和y方向上的速度
	public boolean speedUp = false; // 加速标识
	public int speedUpDuration = 2000; // 加速持续时间（毫秒）
	public int width = 30, height = 32; // 马里奥的尺寸
	public Image img = new ImageIcon("image/mari1.png").getImage(); // 马里奥的图像
	public boolean left = false, right = false, down = false, up = false; // 方向标识
	public String Dir_Up = "Up", Dir_Left = "Left", Dir_Right = "Right", Dir_Down = "Down"; // 方向字符串

	// 枚举类型用于追踪马里奥的最后移动方向
	public enum Direction { LEFT, RIGHT };
	public Direction lastDirection = Direction.RIGHT; // 默认初始方向

	// 标识是否受到重力影响
	public boolean isGravity = false;

	/**
	 * 构造器，初始化马里奥对象并接收游戏窗口的引用。
	 */
	public Mario(GameFrame gf) {
		this.gf = gf;
		this.Gravity(); // 启动重力效果
	}

	/**
	 * 马里奥线程的主循环，处理移动和游戏逻辑。
	 */
	public void run() {
		while (true) {
			if (left) {
				lastDirection = Direction.LEFT; // 更新最后方向
				if (!speedUp) {
					this.xspeed = 5; // 正常速度
				}
				if (hit(Dir_Left)) {
					this.xspeed = 0; // 如果碰撞则停止
				}
				if (this.x >= 0) {
					this.x -= this.xspeed; // 向左移动
					this.img = new ImageIcon("image/mari_left.gif").getImage(); // 更新图像
				}
			}

			if (right) {
				lastDirection = Direction.RIGHT; // 更新最后方向
				if (hit(Dir_Right)) {
					this.xspeed = 0; // 如果碰撞则停止
				}
				if (this.x < 600) {
					this.x += this.xspeed; // 向右移动
					this.img = new ImageIcon("image/mari_right.gif").getImage(); // 更新图像
				}
				if (this.x >= 600) {
					gf.bg.x -= this.xspeed; // 移动背景
					if (gf.bg.x == -6500) {
						gf.bg.endflag = true; // 到达关卡末尾
					}
					for (int i = 0; i < gf.eneryList.size(); i++) {
						Enemy enery = gf.eneryList.get(i);
						enery.x -= this.xspeed; // 移动所有敌人
					}
					gf.mario2.x2 -= this.xspeed; // 移动第一个马里奥
					this.img = new ImageIcon("image/mari_right.gif").getImage(); // 更新图像
				}
				this.xspeed = 5; // 重置速度
			}

			if (up) {
				if (jumpFlag && !isGravity) {
					jumpFlag = false;
					new Thread(() -> {
                        jump(); // 执行跳跃动作
                        jumpFlag = true;
                    }).start();
				}
			}

			try {
				this.sleep(20); // 控制循环速度
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 执行马里奥的跳跃动作。
	 */
	public void jump() {
		int jumpHeigh = 0;
		for (int i = 0; i < 150; i++) {
			gf.mario.y -= this.yspeed; // 向上移动
			jumpHeigh++;
			if (hit(Dir_Up)) {
				break; // 如果碰到东西则停止跳跃
			}
			try {
				Thread.sleep(2); // 控制跳跃速度
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < jumpHeigh; i++) {
			gf.mario.y += this.yspeed; // 向下移动
			if (hit(Dir_Down)) {
				this.yspeed = 0; // 如果碰到地面则停止
			}
			try {
				Thread.sleep(2); // 控制下降速度
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.yspeed = 1; // 重置y速度
	}

	/**
	 * 设置马里奥的暂停状态。
	 */
	public void setPaused(boolean isPaused) {
		if (isPaused) {
			this.xspeed = 0; // 如果暂停则停止移动
		} else {
			this.xspeed = 5; // 继续移动
		}
	}

	/**
	 * 检查马里奥在指定方向上是否发生碰撞。
	 * 处理与金币、敌人和障碍物的碰撞。
	 */
	public boolean hit(String dir) {
		Rectangle myrect = new Rectangle(this.x, this.y, this.width, this.height); // 马里奥的边界框
		Rectangle rect = null;

		for (int i = 0; i < gf.eneryList.size(); i++) {
			Enemy enery = gf.eneryList.get(i);

			// 根据方向定义碰撞检测的边界框
			if (dir.equals("Left")) {
				rect = new Rectangle(enery.x + 5, enery.y, enery.width, enery.height);
			} else if (dir.equals("Right")) {
				rect = new Rectangle(enery.x - 5, enery.y, enery.width, enery.height);
			} else if (dir.equals("Up")) {
				rect = new Rectangle(enery.x, enery.y + 2, enery.width, enery.height);
			} else if (dir.equals("Down")) {
				rect = new Rectangle(enery.x, enery.y - 2, enery.width, enery.height);
			}

			// 处理与金币的碰撞
			if (myrect.intersects(rect) && dir.equals("Up")) {
				if (enery instanceof Coin) {
					Coin coin = (Coin) enery;
					if (!coin.isCollected()) {
						coin.setCollected(true); // 设置为已收集状态
						coin.img = new ImageIcon("image/coin_brick_null.png").getImage(); // 修改图片
						score=gf.score1;
						score++; // 增加得分
						gf.score1 = score;
						System.out.println("得分增加，当前分数：" + score);
						// 显示太阳图片
						coin.setShowSun(true);
						Timer timer = new Timer(200, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								coin.setShowSun(false); // 隐藏太阳图片
								gf.repaint(); // 刷新界面以消失太阳图像
							}
						});
						timer.setRepeats(false);
						timer.start();
					}
					return true;
				}
			} else if (myrect.intersects(rect) && dir.equals("Down")) {
				if (enery.getClass() == Wave.class) {
					gf.hitflag1 = true; // 标记被波浪击中
				}
				return true;
			}

			// 处理与蘑菇的碰撞
			if (myrect.intersects(rect)) {
				if (enery.getClass() == Mogu.class) {
					score += 10; // 增加蘑菇得分
					gf.score1 = score;
					gf.eneryList.remove(i); // 移除蘑菇
					System.out.println("吃到蘑菇，加10分！");
				}
				return true;
			}
		}
		return false; // 没有碰撞
	}

	/**
	 * 处理马里奥的重力。
	 * 当马里奥不在跳跃或在地面上时，持续应用重力。
	 */
	public void Gravity() {
		new Thread(() -> {
            while (true) {
                try {
                    sleep(5); // 控制循环速度
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (true) {
                    if (!jumpFlag) {
                        break; // 如果马里奥正在跳跃则退出循环
                    }

                    if (hit(Dir_Down)) {
                        break; // 如果马里奥碰到地面则退出循环
                    }

                    if (y >= 358) {
                        isGravity = false; // 如果在地面上则停止重力效果
                    } else {
                        isGravity = true; // 应用重力
                        y += yspeed; // 向下移动马里奥
                    }

                    try {
                        sleep(2); // 控制重力效果速度
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
	}
}