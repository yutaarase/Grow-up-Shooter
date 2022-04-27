/*
 * 作成日: 2005/01/20
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package jp.arase.minigame;

import java.awt.image.BufferedImage;

/**
 * 自機弾クラス
 * @author yuuta
 *
 */
public class PlayerBullet extends GameChara {
	/**
	 * 移動演算の種類
	 */
	int type;
	
	/**
	 * 
	 * @param x ｘ座標
	 * @param y　ｙ座標
	 * @param img　画像データ
	 * @param i　移動演算の種類
	 */
	public PlayerBullet(int x, int y, BufferedImage img,int i) {
		super(x, y, 8, 8, img, 0, 0, 8, 8);
		type = i;
	}

	/**
	 * 移動処理メソッド
	 */
	public void move() {
		switch(type) {
		case 1:
			chara_y = chara_y - 12;
			break;
		case 2:
			chara_y = chara_y - 12;
			chara_x = chara_x - 3;
			break;
		case 3:
			chara_y = chara_y - 12;
			chara_x = chara_x + 3;
			break;
		}
		
	}

}
