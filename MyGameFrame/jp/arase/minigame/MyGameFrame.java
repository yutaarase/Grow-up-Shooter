package jp.arase.minigame;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

public abstract class MyGameFrame {
	/**
	 * ゲームステータス：HOME
	 */
	public static final int GS_HOME = 0;
	/**
	 * ゲームステータス：STAGESELECT
	 */
	public static final int GS_STAGESELECT = 1;
	/**
	 * ゲームステータス：SHOP
	 */
	public static final int GS_SHOP = 2;
	/**
	 * ゲームステータス：START
	 */
	public static final int GS_START = 3;
	/**
	 * ゲームステータス：GAMESTAGE
	 */
	public static final int GS_GAMESTAGE = 4;
	/**
	 * ゲームステータス：GAMECLEAR
	 */
	public static final int GS_GAMECLEAR = 5;
	/**
	 * ゲームステータス：GAMEOVER
	 */
	public static final int GS_GAMEOVER = 6;
	/**
	 * ゲームステータス：CREDIT
	 */
	public static final int GS_CREDIT = 7;
	
	/**
	 * ゲームステータス
	 */
	private int gamestate;
	
	/**
	 * 演算用タイマー
	 */
	private int waittimer;
	
	public JFrame frame1;
	
	public BufferStrategy bstrategy;
	
	public Sequencer midiseq = null;
	
	public Timer t;
	public boolean shopopen = false;
	
	Sequence[] seq = new Sequence[10];
	
	/**
	 * 
	 * @param w 幅
	 * @param h　高さ
	 * @param title タイトル
	 */
	public MyGameFrame(int w,int h, String title){
		frame1 = new JFrame(title);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setBackground(Color.WHITE);
		frame1.setResizable(false);
		frame1.setBounds(0,0,w,h);
		frame1.setLayout(new FlowLayout());
		frame1.setVisible(true);
		Insets insets = frame1.getInsets();
		frame1.setSize(w + insets.left + insets.right,
				h + insets.top + insets.bottom);
		frame1.setLocationRelativeTo(null);
		
		frame1.createBufferStrategy(2);
		bstrategy = frame1.getBufferStrategy();
		frame1.setIgnoreRepaint(true);

		frame1.addKeyListener(new MyKeyEvent());
		
		TimerStart();
	}
	
	/**
	 * ショップを開く
	 */
	public void ShopOpen() {
		shopopen = true;
	}
	
	/**
	 * タイマーを止める
	 */
	public void TimerStop() {
		t.cancel();
		t = null;
	}
	
	/**
	 * タイマーを動かす
	 */
	public void TimerStart() {
		if (t == null) {
			t = new Timer();
		}
		t.schedule(new MyTimerTask(), 10, 48);
	}
	
	/**
	 * ゲームステータスをHOMEにする
	 */
	public void goHomeWindow(){
		gamestate = GS_HOME;
				
	}
	
	/**
	 * ゲームステータスをSTAGESELECTにする
	 */
	public void goStageSelectWindow() {
		gamestate = GS_STAGESELECT;
	}
	
	/**
	 * ゲームステータスをSHOPにする
	 */
	public void goShopWindow() {
		gamestate = GS_SHOP;
	}
	
	/**
	 * ゲームステータスをSTARTにする
	 */
	public void goStartWindow(){
		initStartWindow();
		waittimer = 90;
		ShopOpen();
		gamestate = GS_START;
	}
	
	/**
	 * ゲームステータスをGAMESTAGEにする
	 */
	public void goStageWindow(){
		gamestate = GS_GAMESTAGE;
	}
	
	/**
	 * ゲームステータスをCLEARにする
	 */
	public void goGameClearWindow(){
		initGameClearWindow();
		gamestate = GS_GAMECLEAR;
		
	}
	
	/**
	 * ゲームステータスをGAMEOVERにする
	 */
	public void goGameOverWindow(){
		initGameOverWindow();
		gamestate = GS_GAMEOVER;
	}
	
	/**
	 * ゲームステータスをCREDITにする
	 */
	public void goCreditWindow() {
		gamestate = GS_CREDIT;
	}
	
	/**
	 * ゲームステータスがSTARTのとき一度だけ呼び出されるメソッド
	 */
	public abstract void initStartWindow();
	/**
	 * ゲームステータスがGAMECLEARのとき一度だけ呼び出されるメソッド
	 */
	public abstract void initGameClearWindow();
	/**
	 * ゲームステータスがGAMEOVERのとき一度だけ呼び出されるメソッド
	 */
	public abstract void initGameOverWindow();
	/**
	 * Keyが押されたときのメソッド
	 * @param keycode キーコード
	 */
	public abstract void keyPressedSub(int keycode);
	/**
	 * Keyが離れたときのメソッド
	 * @param keycode キーコード
	 */
	public abstract void keyReleasedSub(int keycode);
	
	/**
	 * 中央揃えの文字列を表示
	 * @param str 文字列
	 * @param y　高さ
	 * @param g　描画場所
	 */
	public void drawStringCenter(String str, int y, Graphics g) {
		int fw = frame1.getWidth() / 2;
		FontMetrics fm = g.getFontMetrics();
		int strw = fm.stringWidth(str) /2 ;
		g.drawString(str,fw-strw,y);
	}
	
	/**
	 * 音声ファイルを読み込みClipに加工
	 * @param fname　音声ファイル名
	 * @return clip クリップを返す
	 */
	public Clip soundReader(String fname) {
		Clip clip = null;
		
		try {
			AudioInputStream aistream = AudioSystem.
				getAudioInputStream(getClass().getResource(fname));
			DataLine.Info info = new DataLine.Info(Clip.class, aistream.getFormat());
			clip = (Clip)AudioSystem.getLine(info);
			clip.open(aistream);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		return clip;
	}
	
	/**
	 * midiファイルを読み込みSequenceに格納
	 * @param fname midi, midファイル名
	 * @param i　格納番地,Sequenceの要素数
	 */
	public void midiReader(String fname, int i) {
		
		if(midiseq == null) {
			try {
				midiseq = MidiSystem.getSequencer();
				midiseq.setLoopCount(10);
				midiseq.open();
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
			
		}
		try {
			seq[i] = MidiSystem.getSequence(getClass().getResource(fname));	
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * SequencerにSequenceをセットする
	 * @param i 格納番地,Sequenceの要素数
	 */
	public void seqset(int i) {
		try {
			midiseq.setSequence(seq[i]);
		} catch (InvalidMidiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Homeの処理メソッド
	 * @param g 描画場所
	 */
	public abstract void runHomeWindow(Graphics g);
	/**
	 * StageSelectの処理メソッド
	 * @param g 描画場所
	 */
	public abstract void runStageSelectWindw(Graphics g);
	/**
	 * Shopの処理メソッド
	 * @param g 描画場所
	 */
	public abstract void runShopWindw(Graphics g);
	/**
	 * Startの処理メソッド
	 * @param g 描画場所
	 */
	public abstract void runStartWindow(Graphics g);
	/**
	 * Stageの処理メソッド
	 * @param g 描画場所
	 */
	public abstract void runStageWindow(Graphics g);
	/**
	 * GameClearの処理メソッド
	 * @param g 描画場所
	 */
	public abstract void runGameClearWindow(Graphics g);
	/**
	 * GameOverの処理メソッド
	 * @param g 描画場所
	 */
	public abstract void runGameOverWindow(Graphics g);
	/**
	 * Creditの処理メソッド
	 * @param g 描画場所
	 */
	public abstract void runCreditWindow(Graphics g);
	
	
	/**
	 * Key動作処置用クラス
	 * @author yuuta
	 * 
	 */
	@SuppressWarnings("serial")
	private class MyKeyEvent extends JFrame implements KeyListener {
		/**
		 * Keyが押されたら呼び出されるメソッド
		 * @param　KeyEvent ev キーコード
		 */
		public void keyPressed(KeyEvent ev) {
			int keycode = ev.getKeyCode();
			switch(gamestate) {
			case GS_HOME:
				keyPressedSub(keycode);
				break;
			case GS_STAGESELECT:
				keyPressedSub(keycode);
				break;
			case GS_SHOP:
				keyPressedSub(keycode);
				break;
			case GS_GAMESTAGE:
				keyPressedSub(keycode);
				break;
			case GS_GAMEOVER:
				keyPressedSub(keycode);
				break;
			case GS_GAMECLEAR:
				keyPressedSub(keycode);
				break;
			case GS_CREDIT:
				keyPressedSub(keycode);
				break;
			}
			
		}
		
		/**
		 * Keyが話されたら呼び出されるメソッド
		 * @param　KeyEvent ev キーコード
		 */
		public void keyReleased(KeyEvent ev) {
			int keycode = ev.getKeyCode();
			switch(gamestate){
				case GS_HOME:
					keyReleasedSub(keycode);
					
					if (keycode == KeyEvent.VK_P) {
						
						goStageSelectWindow();
					}
					if (keycode == KeyEvent.VK_S && shopopen == true) {
						TimerStart();
						goShopWindow();
					}
					break;
				case GS_STAGESELECT:
					keyReleasedSub(keycode);
					break;
				case GS_SHOP:
					keyReleasedSub(keycode);
					if (keycode == KeyEvent.VK_H) {
						
						goHomeWindow();
					}
					break;
				case GS_GAMESTAGE:
					keyReleasedSub(keycode);
					break;
				case GS_GAMECLEAR:
					keyReleasedSub(keycode);
					if (keycode == KeyEvent.VK_R) {
						TimerStart();
						goStartWindow();
					}
					if (keycode == KeyEvent.VK_H) {
						TimerStart();
						goHomeWindow();
					}
					break;
				case GS_GAMEOVER:
					keyReleasedSub(keycode);
					if (keycode == KeyEvent.VK_R) {
						TimerStart();
						goStartWindow();
					}
					if (keycode == KeyEvent.VK_H) {
						TimerStart();
						goHomeWindow();
					}
				case GS_CREDIT:
					keyReleasedSub(keycode);
					if (keycode == KeyEvent.VK_H) {
						goHomeWindow();
					}
					break;
			}
		}
		
		/**
		 * Keyを複数押したとき呼び出されるメソッド
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * タイマータスク設定した周期で処理を行うクラス
	 * @author yuuta
	 *
	 */
	private class MyTimerTask extends TimerTask {
		/**
		 * 処理のメインメソッド
		 */
		public void run() {
			Graphics g = bstrategy.getDrawGraphics();
			if (bstrategy.contentsLost()==false){
				Insets insets = frame1.getInsets();
				g.translate(insets.left, insets.top);
	
				switch(gamestate){
					case GS_HOME:
						runHomeWindow(g);
						
						break;
					case GS_STAGESELECT:
						runStageSelectWindw(g);
						
						break;
					case GS_SHOP:
						runShopWindw(g);
						
						break;
					case GS_START:
						runStartWindow(g);
						waittimer = waittimer-1;
						if (waittimer < 0)	{
							goStageWindow();
							
						}
						break;
					case GS_GAMESTAGE:
						runStageWindow(g);
						break;
					case GS_GAMECLEAR:
						runGameClearWindow(g);
						TimerStop();
						break;
					case GS_GAMEOVER:
						runGameOverWindow(g);
						TimerStop();
						break;		
					case GS_CREDIT:
						runCreditWindow(g);
						break;
				}
				
				bstrategy.show();
				g.dispose();
			}
		}
		
		
	}
	
}
