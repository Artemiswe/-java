package role;

import ui.GameFrame;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Turtle extends Enemy {
    public int speed = 2; // 乌龟的移动速度
    public boolean moveRight = true; // 初始向右移动
    public boolean isDead = false; // 判断乌龟是否被击败

    // 乌龟的图片
    private Image rightImage;
    private Image leftImage;

    public Turtle(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
        // 初始化乌龟的图片
        this.rightImage = new ImageIcon("image/Ltortoise2right.png").getImage();
        this.leftImage = new ImageIcon("image/Ltortoise2.png").getImage();
        this.img = rightImage; // 默认向右移动的图片
    }

    // 乌龟的移动逻辑
    public void move() {
        if (moveRight) {
            this.x += speed;
        } else {
            this.x -= speed;
        }
    }

    // 检测是否碰到障碍物或边界，反向移动
    public void checkCollision(GameFrame gf) {
        for (Enemy e : gf.eneryList) {
            if (e != this && this.getBounds().intersects(e.getBounds())) {
                moveRight = !moveRight; // 碰撞后反向移动
                updateImage(); // 更新图片方向
                break;
            }
        }

        // 边界检测
        if (this.x <= 0 || this.x + this.width >= gf.getWidth()) {
            moveRight = !moveRight;
            updateImage(); // 更新图片方向
        }
    }

    // 更新乌龟的图片方向
    private void updateImage() {
        if (moveRight) {
            this.img = rightImage; // 向右移动时使用右向图片
        } else {
            this.img = leftImage; // 向左移动时使用左向图片
        }
    }

    // 检测是否被子弹击中
    public boolean isHitByBullet(ArrayList<Boom> boomList) {
        for (Boom boom : boomList) {
            if (this.getBounds().intersects(boom.getBounds())) {
                isDead = true;
                return true; // 被子弹击中
            }
        }
        return false;
    }

    // 获取乌龟的边界
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }
}
