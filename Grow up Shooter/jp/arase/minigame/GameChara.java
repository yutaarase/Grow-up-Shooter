/*
 * 作成日: 2005/01/19
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package jp.arase.minigame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * キャラ作成用基底クラス
 * @author yuuta
 *
 */
public abstract class GameChara {
	public static final int AREA_X1 = -48;
	public static final int AREA_Y1 = -48;
	public static final int AREA_X2 = GameMain.SCREEN_W+48;
	public static final int AREA_Y2 = GameMain.SCREEN_H+48;
		
	/**
	 * ｘ座標
	 */
	public int chara_x;
	/**
	 * ｙ座標
	 */
	public int chara_y;
	/**
	 * 画像データ切り取り始端ｘ座標
	 */
	private int image_x;
	/**
	 * 画像データ切り取り始端ｙ座標
	 */
	private int image_y;
	/**
	 * 画像データ切り取り幅
	 */
	private int image_w;
	/**
	 * 画像データ切り取り高さ
	 */
	private int image_h;
	/**
	 * 当たり判定幅
	 */
	private int atari_w;
	/**
	 * 当たり判定高さ
	 */
	private int  atari_h;
	BufferedImage image1;
	
	/**
	 * 
	 * @param x x座標
	 * @param y　ｙ座標
	 * @param aw　当たり判定幅
	 * @param ah　当たり判定高さ
	 * @param img　画僧データ
	 * @param ix　画像データ切り取り始端ｘ座標
	 * @param iy　画像データ切り取り始端ｙ座標
	 * @param iw　画像データ切り取り幅
	 * @param ih　画像データ切り取り高さ
	 */
	GameChara(int x,int y, int aw,int ah, BufferedImage img,
			int ix,int iy, int iw,int ih){
		chara_x=x;	chara_y=y;
		atari_w=aw;	atari_h=ah;
		image1=img;	
		image_x=ix;	image_y=iy;	image_w=iw;	image_h=ih;
	}
	
	/**
	 * 描画
	 * @param g　描画場所
	 * @param io　ImageObserver　
	 */
	public void draw(Graphics g,ImageObserver io){
		g.drawImage(image1, chara_x, chara_y, chara_x+image_w, chara_y+image_h,
				image_x, image_y, image_x+image_w, image_y+image_h, io);
		move();
	}

	/**
	 * 移動処理メソッド
	 */
	public abstract void move();
	
	/**
	 * 当たり判定左x座標取得
	 * @return　左x座標
	 */
	public int getx1(){
		return chara_x+(image_w-atari_w)/2;
	}
	/**
	 * 当たり判定上ｙ座標取得
	 * @return　上ｙ座標
	 */
	public int gety1(){
		return chara_y+(image_h-atari_h)/2;
	}
	/**
	 * 当たり判定右x座標取得
	 * @return　右x座標
	 */
	public int getx2(){
		return chara_x+(image_w+atari_w)/2;
	}
	/**
	 * 当たり判定下ｙ座標取得
	 * @return　下ｙ座標
	 */
	public int gety2(){
		return chara_y+(image_h+atari_h)/2;
	}
	
	/**
	 * 当たり判定
	 * @param object 衝突先
	 * @return true or false
	 */
	public boolean isCollision(GameChara object){
		if( object.getx2()>getx1() && getx2()>object.getx1() &&
				object.gety2()>gety1() && gety2()>object.gety1() ){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 画面外に出たか判定する
	 * @return　true or false
	 */
	public boolean isFrameOut(){
		if ( chara_x<AREA_X1 || chara_y<AREA_Y1 ||
				chara_x+image_w>AREA_X2 || 	chara_y+image_w>AREA_Y2 ){
			return true;
		} else {
			return false;
		}
	}
}
