package role;

import java.awt.Image;
import java.awt.Rectangle;
import ui.GameFrame;

public class Mogu extends Enemy {
    private boolean isActive; // 蘑菇是否还在场景中
    public int speed = 2; // 蘑菇的移动速度
    public boolean moveRight = true; // 初始向右移动

    public Mogu(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
        this.isActive = true;
    }

    // 判断是否被吃掉
    public boolean isActive() {
        return isActive;
    }

    // 蘑菇被吃掉
    public void consume() {
        isActive = false;
    }

    // 蘑菇的移动逻辑
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
                break;
            }
        }

        // 边界检测
        if (this.x <= 0 || this.x + this.width >= gf.getWidth()) {
            moveRight = !moveRight;
        }
    }

    // 获取蘑菇的边界
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }
}
