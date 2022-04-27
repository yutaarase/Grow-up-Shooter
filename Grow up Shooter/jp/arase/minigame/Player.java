/*
 * 作成日: 2005/01/19
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package jp.arase.minigame;

import java.awt.image.BufferedImage;

/**
 * 自機処理クラス
 * @author yuuta
 *
 */
public class Player extends GameChara {
	/**
	 * 自機のHP
	 */
	public int HP=100;
	/**
	 * 自機の攻撃力
	 */
	public int Attack=10;
	/**
	 * 自機の移動速度(移動量)
	 */
	public int Speed= 5;
	/**
	 * 自機の防御力
	 */
	public int Defense=5;
	/**
	 * 自機のレベル
	 */
	public int Level=1;
	/**
	 * 所持金
	 */
	public int mony=0;
	
	/**
	 * スコア限界値
	 */
	int scorelimit;
	
	/**
	 * 
	 * @param x ｘ座標
	 * @param y　ｙ座標
	 * @param img　画像データ
	 * @param stagenum　ステージ番号
	 */
	public Player(int x, int y, BufferedImage img, int stagenum) {
		super(x, y, 30, 30, img, 0, 0, 32, 32);
	}

	/**
	 * 移動処理用メソッド
	 */
	public void move() {
	}

	/**
	 * 移動処理用メソッド
	 * @param up　上矢印キー判定
	 * @param down　下矢印キー判定
	 * @param left　左矢印キー判定
	 * @param right　右矢印キー判定
	 */
	public void move(boolean up,boolean down,boolean left,boolean right){
		if (up==true)		chara_y=chara_y-Speed;
		if (down==true)		chara_y=chara_y+Speed;
		if (left==true)		chara_x=chara_x-Speed;
		if (right==true)	chara_x=chara_x+Speed;
		
		if (chara_y<0)	chara_y=0;
		if (chara_x<0)	chara_x=0;
		if (chara_y>GameMain.SCREEN_H-48)	chara_y=GameMain.SCREEN_H-48;
		if (chara_x>GameMain.SCREEN_W-48)	chara_x=GameMain.SCREEN_W-48;
	}
	
	/**
	 * レベルアップ
	 * @param score　スコア
	 */
	public void levelup(int score) {
		if(scorelimit <= score) {
			++Level;
			scorelimit = Level * scorelimit;
			
		}
	}
	
	/**
	 * ステータス更新
	 * @param Alevel　攻撃力レベル
	 * @param Slevel　移動速度レベル
	 * @param Dlevel　防御力レベル
	 */
	public void update(int Alevel, int Slevel, int Dlevel) {
		Attack = 10 + Alevel*2;
		Speed = 5 + Slevel;
		Defense = 5 + Dlevel;
	}
	
}
