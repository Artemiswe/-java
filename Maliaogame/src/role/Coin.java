package role;
import java.awt.Image;

//金币类
public class Coin extends Enemy {
	private boolean isCollected = false; // 新增属性，标识金币是否已被收集
	private boolean showSun = false; // 新增属性，标识是否显示太阳
	public Coin(int x, int y, int width, int height, Image img) {
		super(x, y, width, height, img);
	}
	public boolean isCollected() {
		return isCollected;
	}

	public void setCollected(boolean collected) {
		isCollected = collected;
	}
	public boolean isShowSun() {
		return showSun;
	}

	public void setShowSun(boolean showSun) {
		this.showSun = showSun;
	}
}

