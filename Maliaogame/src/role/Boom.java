package role;
import java.awt.Rectangle;

/**
 * 子弹类
 */
public class Boom {
	// 子弹的坐标，大小，速度
	public int x, y;
	public int width;
	public int speed; // Speed is set externally based on direction

	public Boom(int x, int y, int width) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.speed = 0; // Default speed, set externally
	}

	public Rectangle getBounds() {
		return new Rectangle(this.x, this.y, this.width, 1);
	}
}
