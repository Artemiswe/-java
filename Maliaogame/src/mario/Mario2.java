package mario;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import role.*;
import ui.GameFrame;

public class Mario2 extends Thread {
	public GameFrame gf;
	public boolean jumpFlag2 = true;
	public int blood2 = 3;
	public int score2 = 0;
	public int x2 = 50, y2 = 358;
	public int xspeed2 = 4, yspeed2 = 1;
	public boolean speedUp2 = false; // 加速标识
	public int width2 = 30, height2 = 32;
	public Image img2 = new ImageIcon("image/mario31.png").getImage();
	public boolean left2 = false, right2 = false, down2 = false, up2 = false;
	public String Dir_Up2 = "Up2", Dir_Left2 = "Left2", Dir_Right2 = "Right2", Dir_Down2 = "Down2";

	// 添加一个字段来记录最后的移动方向
	public enum Direction { LEFT, RIGHT };
	public Direction lastDirection2 = Direction.RIGHT; // 初始方向

	public Mario2(GameFrame gf) {
		this.gf = gf;
		this.Gravity();
	}

	public void run() {
		while (true) {
			if (left2) {
				lastDirection2= Mario2.Direction.LEFT; // 更新最后方向
				if (!speedUp2) {
					this.xspeed2 = 5; // 正常速度
				}
				if (hit(Dir_Left2)) {
					this.xspeed2 = 0; // 如果碰撞则停止
				}
				if (this.x2 >= 0) {
					this.x2 -= this.xspeed2; // 向左移动
					this.img2 = new ImageIcon("image/mario2_left.gif").getImage(); // 更新图像
				}
			}

			if (right2) {
				lastDirection2 = Mario2.Direction.RIGHT; // 更新最后方向
				if (hit(Dir_Right2)) {
					this.xspeed2 = 0; // 如果碰撞则停止
				}
				if (this.x2 < 600) {
					this.x2 += this.xspeed2; // 向右移动
					this.img2 = new ImageIcon("image/mario2_right.gif").getImage(); // 更新图像
				}
				if (this.x2 >= 600) {
					gf.bg.x -= this.xspeed2; // 移动背景
					if (gf.bg.x == -6500) {
						gf.bg.endflag = true; // 到达关卡末尾
					}
					for (int i = 0; i < gf.eneryList.size(); i++) {
						Enemy enery = gf.eneryList.get(i);
						enery.x -= this.xspeed2; // 移动所有敌人
					}
					gf.mario.x -= this.xspeed2; // 移动第二个马里奥
					this.img2 = new ImageIcon("image/mario2_right.gif").getImage(); // 更新图像
				}
				this.xspeed2 = 5; // 重置速度
			}

			if (up2) {
				if (jumpFlag2 && !isGravity2) {
					jumpFlag2 = false;
					new Thread(() -> {
						jump(); // 执行跳跃动作
						jumpFlag2 = true;
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

	public void jump() {
		int jumpHeigh2 = 0;
		for (int i = 0; i < 150; i++) {
			gf.mario2.y2 -= this.yspeed2;
			jumpHeigh2++;
			if (hit(Dir_Up2)) {
				break;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < jumpHeigh2; i++) {
			gf.mario2.y2 += this.yspeed2;
			if (hit(Dir_Down2)) {
				this.yspeed2 = 0;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.yspeed2 = 1;
	}

	public void setPaused(boolean isPaused) {
		if (isPaused) {
			this.xspeed2 = 0;
		} else {
			this.xspeed2 = 5;
		}
	}

	public boolean hit(String dir) {
		Rectangle myrect = new Rectangle(this.x2, this.y2, this.width2, this.height2);
		Rectangle rect = null;

		for (int i = 0; i < gf.eneryList.size(); i++) {
			Enemy enery = gf.eneryList.get(i);

			if (dir.equals("Left2")) {
				rect = new Rectangle(enery.x + 5, enery.y, enery.width, enery.height);
			} else if (dir.equals("Right2")) {
				rect = new Rectangle(enery.x - 5, enery.y, enery.width, enery.height);
			} else if (dir.equals("Up2")) {
				rect = new Rectangle(enery.x, enery.y + 2, enery.width, enery.height);
			} else if (dir.equals("Down2")) {
				rect = new Rectangle(enery.x, enery.y - 2, enery.width, enery.height);
			}

			if (myrect.intersects(rect) && dir.equals("Up2")) {
				if (enery instanceof Coin) {
					Coin coin = (Coin) enery;
					if (!coin.isCollected()) {
						coin.setCollected(true); // 设置为已收集状态
						coin.img = new ImageIcon("image/coin_brick_null.png").getImage(); // 修改图片
						score2=gf.score2;
						score2++; // 增加得分
						gf.score2 = score2;
						System.out.println("得分增加，当前分数：" + score2);
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
			} else if (myrect.intersects(rect) && dir.equals("Down2")) {
				if (enery.getClass() == Wave.class) {
					gf.hitflag2 = true;
				}
				return true;
			}

			if (myrect.intersects(rect)) {
				if (enery.getClass() == Mogu.class) {
					score2 += 10;
					gf.score2 = score2;
					gf.eneryList.remove(i);
					System.out.println("吃到蘑菇，加10分");
				}
				return true;
			}
		}

		return false;
	}

	public boolean isGravity2 = false;

	public void Gravity() {
		new Thread(() -> {
            while (true) {
                try {
                    sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (true) {
                    if (!jumpFlag2) {
                        break;
                    }

                    if (hit(Dir_Down2)) {
                        break;
                    }

                    if (y2 >= 358) {
                        isGravity2 = false;
                    } else {
                        isGravity2 = true;
                        y2 += yspeed2;
                    }

                    try {
                        sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
	}
}
