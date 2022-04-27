package jp.arase.minigame;

import java.awt.Color;
import java.awt.Graphics;

/**
 * ゲージ描画クラス 
 * @author yuuta
 *
 */
public class Gauge {

	/**
	 * x座標
	 */
    private int x;
    /**
	 * y座標
	 */
	private int y;
	/**
	 * 幅
	 */
	private int width;
	/**
	 * 高さ
	 */
    private int height;

	//見た目変更用
    /**
     * ゲージの線の長さ
     */
	private int borderWidth;
	/**
	 * 割合
	 */
	private int margin;
	/**
	 * 
	 */
	private int a;

	/**
	 * 速度
	 */
	@SuppressWarnings("unused")
	private int sleepTime;
	
	/**
	 * ゲージ最大値
	 */
	private int max;
	/**
	 * ゲージ現在値
	 */
	private int now;
	/**
	 * ゲージ変更値
	 */
	private int next;
	
	//計算用
	private double step;

	//文字表示の有無
	private boolean messageMode;

	
    /**
     * 
     * @param x 配置x座標
     * @param y　配置y座標
     * @param width　幅
     * @param height　高さ
     * @param max　ゲージ最大値
     * @param now　ゲージ現在値
     */
    public Gauge(int x, int y, int width, int height, int max, int now) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

		setStyle(1, 2, 5);
		
		this.now = now;
		this.next = now;
		setMax(max);
		
		messageMode = true;
    }
	
    /**
     * 描画処理
     */
 	public void draw(Graphics g) {
		
		//枠になる角丸四角形を描画
		g.setColor(Color.WHITE);
	    g.fillRoundRect(x, y, width, height, a, a);

		//背景の黒を描画
		g.setColor(Color.BLACK);
	    g.fillRoundRect(x + borderWidth, y + borderWidth,
		 width - borderWidth * 2, height - borderWidth * 2, a, a);

		//現在の値に応じてゲージの幅を計算
		int nowWidth = (int)(step * now);
		int nextWidth = (int)(step * next);
		
		//maxの時に誤差で赤が見えないように
		if(now == max) {
			//正確な最大の幅を設定
			nowWidth = width - borderWidth * 2 - margin * 2;
		} else {
			//減少済みの表示のため赤を描画
			g.setColor(Color.RED);
	    	g.fillRoundRect(x + borderWidth + margin, y + borderWidth + margin,
			 width - borderWidth * 2 - margin * 2, height - borderWidth * 2 - margin * 2, a, a);
		}
		
		//減少か上昇かによってゲージの色や幅を変える
		if(now > next) {
			
			//減少中のゲージ（黄色を現在値の幅で）
			g.setColor(Color.YELLOW);
    		g.fillRoundRect(x + borderWidth + margin, y + borderWidth + margin,
	 		nowWidth, height - borderWidth * 2 - margin * 2, a, a);
			
		} else if(now < next) {
			
			//上昇中のゲージ（青を目標値の幅で）
			g.setColor(Color.BLUE);
    		g.fillRoundRect(x + borderWidth + margin, y + borderWidth + margin,
	 		nextWidth, height - borderWidth * 2 - margin * 2, a, a);
		}

		//実際の現在値を表すゲージ
		if(next != 0) {
			
			g.setColor(Color.GREEN);
			
			//減少中か上昇中によって幅を変える
			if(now > next) {
				
				//減少中の場合（目標値の幅で）
    			g.fillRoundRect(x + borderWidth + margin, y + borderWidth + margin,
	 			nextWidth, height - borderWidth * 2 - margin * 2, a, a);
				
			} else {
				
				//上昇中の場合（現在値の幅で）
    			g.fillRoundRect(x + borderWidth + margin, y + borderWidth + margin,
	 			nowWidth, height - borderWidth * 2 - margin * 2, a, a);
				
			}
		}
		
		//文字を描画するか
		if(messageMode) {
			
			g.setColor(Color.WHITE);
			g.drawString(now + "/" + max, x + width / 2, y + height * 2 + borderWidth + margin);
		}		
    }

    /**
     * 位置の変更
     */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

    /**
     * 最大値の変更
     */
	public void setMax(int max) {
		
		this.max = max;
		
		if(now > max) {
			now = max;
		}
		if(next > max) {
			next = max;
		}

		step = ((double)width - (double)borderWidth * 2.0 - (double)margin * 2.0) / (double)max;
	}

    /**
     * スタイルの変更
     */
	public void setStyle(int borderWidth, int margin, int a) {

		this.borderWidth = borderWidth;
		this.margin = margin;
		this.a = a;
	}
	
    /**
     * ゲージを指定の値まで徐々に減らす
     */
	public void down(int value) {
		
		next -= value;
		
		if(next < 0) {
			next = 0;
		} else if(next > max) {
			next = max;
		}
	}
	
    /**
     * modeがtrueならゲージを指定の値まで一気に減らす
     */
	public void down(int value, boolean mode) {
		down(value);
		if(mode) {
			now = next;
		}
	}
	
    /**
     * ゲージを指定の値まで徐々に増やす
     */
	public void up(int value) {
		down(-value);
	}
	
    /**
     * modeがtrueならゲージを指定の値まで一気に増やす
     */
	public void up(int value, boolean mode) {
		down(-value, mode);
	}
	
    /**
     * ゲージを最大値まで徐々に増やす
     */
	public void toMax() {
		up(max);
	}
	
    /**
     * modeがtrueならゲージを最大値まで一気に増やす
     */
	public void toMax(boolean mode) {
		up(max, mode);
	}
	
    /**
     * ゲージを0まで徐々に増やす
     */
	public void toZero() {
		down(next);
	}
	
    /**
     * modeがtrueならゲージを0まで一気に増やす
     */
	public void toZero(boolean mode) {
		down(next, mode);
	}
	
    /**
     * 文字を表示するか指定
     */
	public void setMessage(boolean mode) {
		this.messageMode = mode;
	}
	
    /**
     * 文字表示の有無を切替
     */
	public void toggleMessage() {
		messageMode = !messageMode;
	}
	
    /**
     * 状態を更新（スレッド非使用時は外部から呼び出す必要がある）
     */
	public void updateState() {
		
		//nowをnextの値に近づける
		if(now > next) {
			now--;
		} else if(now < next) {
			now++;
		}
	}
}
