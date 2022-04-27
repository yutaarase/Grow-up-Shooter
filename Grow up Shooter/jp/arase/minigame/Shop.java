package jp.arase.minigame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * ショップクラス
 * @author yuuta
 *
 */
public class Shop {
	
	/**
	 * レベル格納配列
	 */
	public int [] level = {1,1,1};
	/**
	 * 価格格納配列
	 */
	public int [] price = {200,200,200};
	
	/**
	 * 
	 */
	public Shop() {
		// TODO Auto-generated constructor stub
		
	}
	
	/**
	 * 販売
	 * @param num　品の種類
	 * @param mony　所持金
	 * @param g　描画場所
	 * @return　所持金
	 */
	public int buy(int num, int mony, Graphics g) {
		if(mony >= price[num-1]) {
			++level[num-1];
			mony -= price[num-1];
			price[num-1] = 200 * level[num-1];
		}else {
			g.setColor(Color.GREEN);
			g.setFont(new Font("SansSerif",Font.BOLD,45));
			g.drawString("お金が足りません",50,300);
		}
		
		return mony;
	}
	
}
