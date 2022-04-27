/*
 * 作成日: 2005/01/23
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package jp.arase.minigame;

import java.awt.Point;
import java.util.StringTokenizer;

/**
 * patファイル読み込みクラス
 * @author yuuta
 *
 */
public class PatternReader {
	/**
	 * patファイル名
	 */
	String patstr;
	/**
	 * patファイル内容格納変数
	 */
	StringTokenizer tokenizer;
	
	/**
	 * 移動量
	 */
	Point movepoint = new Point();
	
	/**
	 * 実行回数
	 */
	int cnt = 0;
	
	/**
	 * patファイルを読む
	 * @param str　ファイル名
	 */
	PatternReader(String str){
		patstr = str;
		tokenizer = new StringTokenizer(patstr,",");
	}
	
	/**
	 * patファイル内容取り出し
	 * @return　movepoint
	 */
	Point next(){
		if (cnt==0){
			if (tokenizer.hasMoreTokens()==false){
				tokenizer = new StringTokenizer(patstr,",");
			}
			movepoint.x = Integer.parseInt(tokenizer.nextToken());	//X移動量取り出し
			movepoint.y = Integer.parseInt(tokenizer.nextToken());	//Y移動量取り出し
			cnt = Integer.parseInt(tokenizer.nextToken());	//繰り返し回数					
		} else {
			cnt = cnt-1;
		}
		return movepoint;
	}
}
