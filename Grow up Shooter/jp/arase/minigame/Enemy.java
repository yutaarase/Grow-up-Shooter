/*
 * 作成日: 2005/01/23
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package jp.arase.minigame;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * 敵生成クラス
 * @author yuuta
 *
 */
public class Enemy extends GameChara {
	PatternReader preader;
	/**
	 * 敵の種類
	 */
	public int num;
	/**
	 * 移動変更用変数
	 */
	private int Inversion;
	/**
	 * 敵の体力
	 */
	public int HP=20;
	/**
	 * 敵の攻撃力
	 */
	public int Attack=10;
	
	/**
	 * コンストラクタ
	 * @param x　x座標
	 * @param y　y座標
	 * @param img　画像イメージ
	 * @param ptnum　敵の種類
	 * @param patstr　patファイル名
	 * @param stagenum　ステージ番号
	 */
	public Enemy(int x, int y, BufferedImage img, int ptnum, String patstr, int stagenum) {
		super(x, y, 30, 30, img, ptnum*32, stagenum*32, 32, 32);
		preader = new PatternReader(patstr);
		
		num = ptnum;
		if (chara_y>GameMain.SCREEN_H/2) {
			Inversion=-1;
		} else {
			Inversion=1;
		}

	}

	/**
	 * 移動処理メソッド
	 */
	public void move() {
		Point movexy = preader.next();
		chara_x = chara_x + movexy.x;
		chara_y = chara_y + movexy.y*Inversion;
	}
}
