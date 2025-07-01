package role;
import java.awt.Image;
import java.awt.Rectangle;
//障碍物的抽象父类
public abstract class Enemy {
	public int x,y;
	public int width,height;
	public Image img;
	public Enemy(int x, int y, int width, int height, Image img) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.img=img;
	}
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	//修改图片，用于马里奥碰撞后的变化
	public void setImg(Image img) {
		this.img=img;
	}
	public Image getImg() {
		return img;
	}
}