/*
 * 作成日: 2005/01/24
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package jp.arase.minigame;

import java.awt.image.BufferedImage;

/**
 * 敵弾生成クラス
 * @author yuuta
 *
 */
public class EnemyBullet extends GameChara {
	/**
	 * x座標移動演算用変数
	 */
	private int dx;
	/**
	 * y座標移動演算用変数
	 */
	private int dy;
	/**
	 * 自機のx座標保存用変数
	 */
	private int px;
	/**
	 * 自機のy座標保存用変数
	 */
	private int py;
	/**
	 * 移動演算の種類
	 */
	private int type;
	/**
	 *　円軌道演算用変数
	 */
	private int ang;
	/**
	 * 螺旋演算用変数
	 */
	private int cnt;
	
	/**
	 * 
	 * @param x　x座標
	 * @param y　y座標
	 * @param jx　自機x座標, 移動量x座標
	 * @param jy　自機ｙ座標, 移動量y座標
	 * @param img　画像イメージ
	 * @param i　弾の種類
	 * @param typ　移動演算の種類
	 */
	public EnemyBullet(int x, int y, int jx, int jy, BufferedImage img, int i,int typ) {
		super(x, y, 8, 8, img, i*8, 0, 8, 8);
		type = typ;
		
		
		
		switch(type) {
		case 1:
			double b = Math.sqrt((jx-x)*(jx-x) + (jy-y)*(jy-y));
			if (b!=0){
				dx = (int)Math.round((jx-x)/b * 6.0);
				dy = (int)Math.round((jy-y)/b * 6.0);
			}
			break;
		case 2:
			px = jx; py = jy;
		case 5:
			ang= jx*10;
			cnt = 100;
			break;
		case 6:
			dx = jx; dy = jy;
			break;
		}
		
	}

	/**
	 * 移動処理メソッド
	 */
	public void move() {
		switch(type) {
		case 1:
			chara_x = chara_x + dx;
			chara_y = chara_y + dy;
			break;
		case 2:
			double d = Math.sqrt((px-chara_x)*(px-chara_x) + (py-chara_y)*(py-chara_y));
			if (d!=0){
				dx = (int)Math.round((px-chara_x)/d * 6.0)+1;
				dy = (int)Math.round((py-chara_x)/d * 6.0)+1;			
				chara_x = chara_x + dx;
				chara_y = chara_y + dy;
			}
			break;
		case 3:
			cnt++;
	        double a = Math.toRadians(cnt) * 6;
	        
	        chara_x += Math.cos(a) + a * Math.sin(a);
	        chara_y += Math.sin(a) - a * Math.cos(a);
	        break;
		case 4:
			cnt++;
	        double b = Math.toRadians(60+cnt) * 20;
	        
	        chara_x += Math.cos(b) + b * Math.sin(b);
	        chara_y += Math.sin(b) - b * Math.cos(b);
	        break;
		case 5:
			cnt++;
	        double c = Math.toRadians(cnt + ang) * 4;
	        
	        chara_x += Math.cos(c) - c * Math.sin(c);
	        chara_y += Math.sin(c) + c * Math.cos(c);
	        break;
		case 6:
			chara_x += dx;
			chara_y += dy;
			break;
		}
	}
}
