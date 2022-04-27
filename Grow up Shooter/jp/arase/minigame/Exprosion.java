/*
 * 作成日: 2005/01/25
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package jp.arase.minigame;

import java.awt.image.BufferedImage;

/**
 * 爆発生成クラス
 * @author yuuta
 *
 */
public class Exprosion extends GameChara {
	/**
	 * 演算用タイマー
	 */
	public int waittime;
	/**
	 * 爆発種類
	 */
	public static int ptn=0;
	
	/**
	 * 
	 * @param x x座標
	 * @param y　ｙ座標
	 * @param img　画像データ
	 */
	public Exprosion(int x, int y, BufferedImage img) {
		super(x, y, 0, 0, img, 32*ptn, 0, 32, 32);
		waittime = 10;
	}
	
	/**
	 * 描画をやめる
	 */
	public boolean isFrameOut(){
		if (waittime <0){
			ptn=0;
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 移動処理メソッド
	 */
	public void move() {
		waittime = waittime-1;
		if(waittime%2 == 0) ++ptn;
	}
}
