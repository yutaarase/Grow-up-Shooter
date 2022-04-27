/*
 * 作成日: 2005/01/26
 *
 * TODO この生成されたファイルのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
package jp.arase.minigame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * マップ描画クラス
 * @author yuuta
 *
 */
public class MapReader {
	//地形パーツ1個のサイズ
	/**
	 * 地形パーツ幅
	 */
	static final int CHIP_W = 32;
	/**
	 * 地形パーツ高さ
	 */
	static final int CHIP_H = 32;
	
	
	//1画面に表示できるマップ幅・高さ
	/**
	 * 描画できるマップ幅
	 */
	static final int VIEW_W = GameMain.SCREEN_W/CHIP_W;
	/**
	 * 描画できるマップ高さ
	 */
	static final int VIEW_H = GameMain.SCREEN_H/CHIP_H;	
									
	/**
	 * マップデータ幅
	 */
	int mapwidth;
	/**
	 * マップデータ高さ
	 */
	int mapheight;
	/**
	 * 現在描画開始ｘ座標（マップデータ）
	 */
	int worldx;
	/**
	 * 現在描画開始ｙ座標（マップデータ）
	 */
	int worldy;
	
	/**
	 * ステージ番号
	 */
	int stagenum;
	
	/**
	 * 敵移動パターン格納配列
	 */
	String[] enemypattern = new String[10];
	
	/**
	 * マップデータ格納配列
	 */
	public String[] mapdata;
	
	//マップチップ
	BufferedImage terrainimage;
	
	
	/**
	 * 
	 * @param img 画像データ（マップチップ）
	 * @param mapname mapファイル名(マップデータ)
	 * @param enemypatname　patファイル名
	 * @param num　ステージ番号
	 */
	@SuppressWarnings("unused")
	MapReader(BufferedImage img, String mapname, String enemypatname, int num) {
		// TODO Auto-generated constructor stub
		stagenum = num;
		worldy = 0;
		String mapread;
		terrainimage=img;
		worldx=0;
		boolean readend=false;
		
		
		//mapサイズ読み込み
		
		InputStream is = getClass().getResourceAsStream(mapname);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		try{
			int i=0;
			while(true) {
				mapread = br.readLine();
				if (mapread==null) {
					mapheight = i;
					break;
				}
				++i;
			}
			br.close();
			is.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		this.mapdata = new String[mapheight];
		
		
		//mapString配列格納
		is = getClass().getResourceAsStream(mapname);
		br = new BufferedReader(new InputStreamReader(is));
		try{
			for(int i=mapheight-1;i>=0; --i){
				mapdata[i]= br.readLine();
				if (mapdata[i]==null) {
				    System.err.println("invalid mapdata");
				    System.exit(0);
				}
			}
			br.close();
			is.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		
		mapwidth=mapdata[0].length();
		
		//敵動きパターン読み込み
				is = getClass().getResourceAsStream(enemypatname);
				br = new BufferedReader(new InputStreamReader(is));
				try{
					for(int i=0; i<enemypattern.length; i=i+1){
						enemypattern[i] = br.readLine();
						if (enemypattern[i]==null) break;
					}
					br.close();
					is.close();
				} catch(Exception e){
					e.printStackTrace();
				}
	}
	
	/**
	 * マップデータ取得
	 * @param x　描画x座標
	 * @param y　描画ｙ座標
	 * @return　マップデータ
	 */
	int getMapData(int x, int y){
		return Character.getNumericValue(mapdata[y].charAt(x));
	}
	
	/**
	 * マップが終端判定
	 * @return　true or false
	 */
	public boolean isMapEnd(){
		if (worldy>=(mapheight-VIEW_H)*CHIP_H) {
			worldy = 0;
			return true;
		}
		else return false;
	}
	
	
	/**
	 * 当たり判定
	 * @param jx　自機ｘ座標
	 * @param jy　自機ｙ座標
	 * @return
	 */
	public boolean isAtari(int jx,int jy){
		int cx = (worldx+jx)/CHIP_W;
		int cy = jy/CHIP_H;
		
		if (getMapData(cx,cy)>0 && getMapData(cx,cy)<10) return true;
		else return false;
	}
	
	/**
	 * ArryList新規追加メソッド
	 * @param img　画像データ
	 * @return　ArrayList
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getNewEnemy(BufferedImage img){
		ArrayList newenemys = new ArrayList();
		int cy = worldy / CHIP_H;
		if (worldy % CHIP_H>0) return newenemys;	//空のArrayListを返す
		
		for(int i=0; i<mapwidth; i=i+1){
			int csnum=getMapData(i, cy+VIEW_H-1);
			if(csnum>9) {
				newenemys.add((Object)new Enemy(i*CHIP_W, 
						0, img, csnum-10, enemypattern[csnum-10],stagenum-1));
			}
		}
		return newenemys;
	}
	
	
	/**
	 * mapと敵描画
	 * @param g 描画場所
	 * @param io　ImageObserver
	 */
	public void draw(Graphics g,ImageObserver io){
		int cy = worldy / CHIP_H;
		int shy = worldy % CHIP_H;
		
		for(int i=0; i<VIEW_H+2; ++i){
			for(int j=0; j<mapwidth; ++j){
				if (cy+i>mapheight-1) break;

				int csnum = getMapData(j, cy+i);
				if (csnum>=0 && csnum<10){
					g.drawImage(terrainimage, j*CHIP_W, (VIEW_H-i)*CHIP_H+shy, 
							j*CHIP_W+CHIP_W, (VIEW_H-i)*CHIP_H+shy-CHIP_H,
							csnum*CHIP_W, (stagenum-1)*CHIP_H, 
							(csnum+1)*CHIP_W, stagenum*CHIP_H, io);
				}
				if (csnum>9 && csnum<20){
					g.drawImage(terrainimage, j*CHIP_W, (VIEW_H-i)*CHIP_H+shy, 
							j*CHIP_W+CHIP_W, (VIEW_H-i)*CHIP_H+shy-CHIP_H,
							0, (stagenum-1)*CHIP_H, 
							(1)*CHIP_W, stagenum*CHIP_H, io);
				}
			}
		}
		//スクロール
		if (worldy<(mapheight-VIEW_H)*CHIP_H) worldy = worldy+2;
		
	}

}
