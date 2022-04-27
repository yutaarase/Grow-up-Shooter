package jp.arase.minigame;
import java.awt.image.BufferedImage;

/**
 * Boss生成クラス
 * @author yuuta
 *
 */
public class Boss extends GameChara {
	PatternReader preader;
	
	/**
	 * Bossの体力
	 */
	public int HP;
	/**
	 * Bossの攻撃力
	 */
	public int attack;
	/**
	 * ボスを出現判定
	 */
	public boolean app = false;
	/**
	 * サブのBossかどうかの判定
	 */
	private boolean sub;
	
	/**
	 * Bossの生成用コンストラクタ
	 * @param x x座標
	 * @param y ｙ座標
	 * @param img　画像イメージ
	 * @param stagenum　ステージ番号
	 * @param gamelevel　ステージ難易度
	 */
	public Boss(int x, int y, BufferedImage img, int stagenum, int gamelevel) {
		super(x, y, 230, 200, img, (stagenum-1)*256, 0, 256, 256);
		// TODO Auto-generated constructor stub
		
		 
		HP = 3000 * stagenum + 100 * gamelevel;
		attack = 10 + 2*gamelevel;
		
		sub = false;
	}
	
	/**
	 * subBossの生成用コンストラクタ
	 * @param x　初期値x座標
	 * @param y　初期値y座標
	 * @param img　画像イメージ
	 * @param gamelevel　ステージ難易度
	 */
	public Boss(int x, int y, BufferedImage img, int gamelevel) {
		super(x, y, 110, 190, img, 0, 0, 114, 196);
		HP = 1000 + 10 * gamelevel;
		attack = 10 + 2*gamelevel;
		
		sub = true;
	}
		
	/**
	 * 移動処理メソッド
	 */
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
		if(sub == true) {
			chara_y+= 4;
		}else {
			if(chara_y+300 <= GameMain.SCREEN_H /2) ++chara_y;
			else --chara_y;
		}
		
		
	}
	
}
