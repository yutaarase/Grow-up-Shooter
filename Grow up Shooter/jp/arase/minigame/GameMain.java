package jp.arase.minigame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;


/**
 * ゲームメインクラス
 * @author yuuta
 *
 */
public class GameMain extends MyGameFrame {
	
	/**
	 * 画面の幅
	 */
	public static final int SCREEN_W = 480;
	/**
	 * 画面の高さ
	 */
	public static final int SCREEN_H = 640;

	/**
	 * 上矢印キー判定用変数
	 */
	private boolean upkey;
	/**
	 * 下矢印キー判定用変数
	 */
	private boolean downkey;
	/**
	 * 右矢印キー判定用変数
	 */
	private boolean rightkey;
	/**
	 * 左矢印キー判定用変数
	 */
	private boolean leftkey;
	/**
	 * SHIFTキー判定用変数
	 */
	@SuppressWarnings("unused")
	private boolean shiftkey;
	/**
	 * ENTERキー判定用変数
	 */
	private boolean enterkey;
	
	/**
	 * 自機弾発射間隔
	 */
	private int P_bulletrate=0;
	/**
	 * 敵弾発射間隔
	 */
	private int E_bulletrate = 100;
	/**
	 * boss弾発射間隔
	 */
	private int B_bulletrate = 10;
	/**
	 * スコア
	 */
	private int score;
	/**
	 * 最大スコア
	 */
	private int highscore;
	/**
	 * ステージ番号
	 */
	private int stagenum=1;
	/**
	 * クリアステージ番号
	 */
	private int clearnum;
	
	/**
	 * ホーム画面選択番号
	 */
	private int homenum = 1;
	/**
	 * ショップ画面選択番号
	 */
	private int shopnum = 1;
	
	/**
	 * ステージクリア回数
	 */
	private int clearcnt [] = new int[3];
	
	/**
	 * ホーム画面待機時間
	 */
	private int hometime;
	/**
	 * ステージ画面待機時間
	 */
	private int stagetime;
	/**
	 * ショップ画面待機時間
	 */
	private int shoptime;
	/**
	 * ステージセレクト画面待機時間
	 */
	private int selecttime;
	
	BufferedImage bulletimage, terrainimage, startimage,backimage;
	BufferedImage effectimage, enemyimage,bossimage, playerimage;
	BufferedImage bosspartsimage,homeimage,subbossimage,selectimage;
	BufferedImage goodsimage;
	
	Player player;
	Boss boss;
	Gauge P_gauge, B_gauge;
	Shop shop;
	MapReader map;
	
	public Timer timer;
	
	@SuppressWarnings("rawtypes")
	ArrayList playerbullets, enemys, enemybullet, exprosions,subboss;
	
	Clip seClip1,seClip2,seClip3,seClip4;
	
	/**
	 * 
	 */
	public GameMain() {
		super(SCREEN_W, SCREEN_H, "Grow up Shooter");
		frame1.setBackground(Color.BLACK);
		shop = new Shop();
		
		try {
			bulletimage = ImageIO.read(getClass().getResource("bullet.png"));
			terrainimage = ImageIO.read(getClass().getResource("terrain.png"));
			startimage = ImageIO.read(getClass().getResource("start.jpg"));
			effectimage = ImageIO.read(getClass().getResource("exprosion.png"));
			enemyimage = ImageIO.read(getClass().getResource("enemy.png"));
			bossimage = ImageIO.read(getClass().getResource("boss.png"));
			backimage = ImageIO.read(getClass().getResource("background.png"));
			playerimage = ImageIO.read(getClass().getResource("ship.png"));
			bosspartsimage = ImageIO.read(getClass().getResource("bossparts01.png"));
			subbossimage = ImageIO.read(getClass().getResource("subboss.png"));
			homeimage = ImageIO.read(getClass().getResource("homewindow.png"));
			goodsimage = ImageIO.read(getClass().getResource("goods.png"));
			selectimage = ImageIO.read(getClass().getResource("select.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		seClip1 = soundReader("exprosion.wav");
		seClip2 = soundReader("fire.wav");
		seClip3 = soundReader("gameover.wav");
		seClip4 = soundReader("clear.wav");
		
		midiReader("boss.mid",0);
		midiReader("stage1.mid", 1);
		midiReader("stage2.mid",2);
		midiReader("stage3.mid",3);
		
		goHomeWindow();
		
	}
	
	
	/**
	 * スタート画面で一度だけ呼び出されるメソッド
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void initStartWindow() {
		// TODO Auto-generated method stub
		
		
		player = new Player(SCREEN_W/2,(int) (SCREEN_H*0.8),playerimage,stagenum-1);
		boss = new Boss(SCREEN_W/4,0,bossimage,stagenum,clearcnt[stagenum-1]);
		playerbullets = new ArrayList();
		enemys = new ArrayList(); 
		enemybullet = new ArrayList();
		exprosions = new ArrayList();
		upkey=false; downkey=false; rightkey=false;
		leftkey=false;
		player.HP = 200;
		P_gauge = new Gauge(0, 630, 480, 10, player.HP, player.HP);
		B_gauge = new Gauge(0, 0, 480, 10, boss.HP, boss.HP);
		P_gauge.toMax(true);
		
		
		score = 0;
		player.Level = 1;
		
		switch(stagenum) {
		case 1:
			map = new MapReader(terrainimage,"001.map","001.pat", stagenum);
			seqset(1);
			stagetime = 2000;
			break;
		case 2:
			map = new MapReader(terrainimage,"002.map","002.pat", stagenum);
			seqset(2);
			stagetime = 2000;
			break;
		case 3:
			map = new MapReader(terrainimage,"003.map","003.pat", stagenum);
			subboss = new ArrayList();
			seqset(3);
			stagetime = 2000;
			break;
		}
		midiseq.setTickPosition(0);
		midiseq.start();
	}
	
	/**
	 * ゲームクリア画面で一度だけ呼び出されるメソッド
	 */
	@Override
	public void initGameClearWindow() {
		// TODO Auto-generated method stub
		if(stagenum >= clearnum) clearnum = stagenum;
		++clearcnt[stagenum-1];
		score = score + stagenum * 100;
		if (score>highscore) highscore = score;
		upkey = false;
		downkey = false;
		enterkey = false;
		midiseq.stop();
		seClip4.setFramePosition(0);
		seClip4.start();
	}

	/**
	 * ゲームオーバー画面で一度だけ呼び出されるメソッド
	 */
	@Override
	public void initGameOverWindow() {
		// TODO Auto-generated method stub
		upkey = false;
		downkey = false;
		enterkey = false;
		midiseq.stop();
		seClip3.setFramePosition(0);
		seClip3.start();
	}

	/**
	 * Keyが押されたときのメソッド
	 */
	public void keyPressedSub(int key) {
		if (key==KeyEvent.VK_UP) 	upkey=true;
		if (key==KeyEvent.VK_DOWN) 	downkey=true;
		if (key==KeyEvent.VK_LEFT) 	leftkey=true;
		if (key==KeyEvent.VK_RIGHT) rightkey=true;
		if (key==KeyEvent.VK_SHIFT) shiftkey=true;
		if (key==KeyEvent.VK_ENTER) enterkey=true;
	}

	/**
	 * Keyが離されたときのメソッド
	 */
	public void keyReleasedSub(int key) {
		// TODO Auto-generated method stub
		if (key==KeyEvent.VK_UP) 	upkey=false;
		if (key==KeyEvent.VK_DOWN) 	downkey=false;
		if (key==KeyEvent.VK_LEFT) 	leftkey=false;
		if (key==KeyEvent.VK_RIGHT) rightkey=false;
		if (key==KeyEvent.VK_SHIFT) shiftkey=false;
		if (key==KeyEvent.VK_ENTER) enterkey=false;
	}

	/**
	 * ホーム画面描画メソッド
	 */
	@Override
	public void runHomeWindow(Graphics g) {
		// TODO Auto-generated method stub
		g.clearRect(0,0,SCREEN_W,SCREEN_H);
		g.drawImage(homeimage, 0, 0, frame1);
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif",Font.BOLD,55));
		
		
		//点滅処理用変数
		int show = hometime % 20;
		
		
		//選択肢ごとに描画
		switch(homenum) {
		case 1:
			
			if(downkey == true && shopopen == true) homenum = 2;
			if(enterkey == true) {
				enterkey = false;
				goStageSelectWindow();
			}
			
			if(show == 0) {
				drawStringCenter("StageSelect", 300, g);
			}
			drawStringCenter("Shop", 400, g);
			
			break;
		case 2:
			if(upkey == true) homenum = 1;
			if(enterkey == true) {
				enterkey = false;
				goShopWindow();
			}
			
			if(show == 0) {
				drawStringCenter("Shop", 400, g);
			}
			drawStringCenter("StageSelect", 300, g);
			
			break;
		}
		
		++hometime;
	}
	
	
	/**
	 * セレクト画面描画メソッド
	 */
	@Override
	public void runStageSelectWindw(Graphics g) {
		// TODO Auto-generated method stub
		g.clearRect(0,0,SCREEN_W,SCREEN_H);
		g.drawImage(backimage, 0, 0, frame1);
		g.setColor(Color.RED);
		g.setFont(new Font("SansSerif",Font.BOLD,80));
		drawStringCenter("StageSelect",70,g);
		
		int show = selecttime % 20;
		
		
		int size_x = 380, size_y = 140;
		switch(stagenum) {
		
		case 1:
			if(upkey == true && clearnum >=2) stagenum = 1;
			if(downkey == true && clearnum >=1) stagenum = 2;
			if(show == 0) {
				g.drawImage(selectimage, 50, 96, 50+size_x, 96+size_y, 380, 0, 760, 140, frame1);
			}
			if(clearnum <1) {
				g.drawImage(selectimage, 50, 267, 50+size_x, 267+size_y, 0, 140, 380, 280, frame1);
				g.drawImage(selectimage, 50, 438, 50+size_x, 438+size_y, 0, 280, 380, 420, frame1);
			}else if(clearnum >=1){
				g.drawImage(selectimage, 50, 267, 50+size_x, 267+size_y, 380, 140, 760, 280, frame1);
				if(clearnum < 2) {
					g.drawImage(selectimage, 50, 438, 50+size_x, 438+size_y, 0, 280, 380, 420, frame1);
				}else {
					g.drawImage(selectimage, 50, 438, 50+size_x, 438+size_y, 380, 280, 760, 420, frame1);
				}
			}
			
			break;
			
		case 2:
			if(upkey == true) stagenum = 1;
			if(downkey == true && clearnum >=2) stagenum = 3;
			g.drawImage(selectimage, 50, 96, 50+size_x, 96+size_y, 380, 0, 760, 140, frame1);
			if(show == 0) {
				g.drawImage(selectimage, 50, 267, 50+size_x, 267+size_y, 380, 140, 760, 280, frame1);
			}
			if(clearnum < 2) {
				g.drawImage(selectimage, 50, 438, 50+size_x, 438+size_y, 0, 280, 380, 420, frame1);
			}else {
				g.drawImage(selectimage, 50, 438, 50+size_x, 438+size_y, 380, 280, 760, 420, frame1);
			}
			
			
			break;
			
		case 3:
			if(upkey == true) stagenum = 2;
			if(downkey == true) stagenum = 1;
			g.drawImage(selectimage, 50, 96, 50+size_x, 96+size_y, 380, 0, 760, 140, frame1);
			g.drawImage(selectimage, 50, 267, 50+size_x, 267+size_y, 380, 140, 760, 280, frame1);
			if(show == 0) {
				g.drawImage(selectimage, 50, 438, 50+size_x, 438+size_y, 380, 280, 760, 420, frame1);
			}
			
			break;
		}
		
		if(enterkey == true) goStartWindow();
		
		++selecttime;
	}
	
	
	/**
	 * ショップ画面描画メソッド
	 */
	@Override
	public void runShopWindw(Graphics g) {
		// TODO Auto-generated method stub
		
		g.clearRect(0,0,SCREEN_W,SCREEN_H);
		g.drawImage(backimage,0,0, frame1);
		g.setColor(Color.RED);
		g.setFont(new Font("SansSerif",Font.BOLD,60));
		drawStringCenter("Shop",70,g);
		
		g.setColor(Color.YELLOW);
		g.setFont(new Font("SansSerif",Font.BOLD,45));
		g.drawString("所持金 : " + player.mony ,120,630);
		
		
		int x=50, y=96, size=128;
		g.setColor(Color.CYAN);
		g.setFont(new Font("SansSerif",Font.BOLD,25));
		
		int show = shoptime % 20;
		
		switch(shopnum) {
		case 1:
			if(upkey == true) shopnum = 3;
			if(downkey == true) shopnum = 2;
			
			
			g.fillRect(x, y, size, size);
			g.fillRect(x, y+size+48, size, size);
			g.fillRect(x, y+size*2+48*2, size, size);
			if(show == 0) {
				g.drawImage(goodsimage,x,y,x+size,y+size,0,0,size,size,frame1);
				
			}
			
			g.setColor(Color.WHITE);
			
			g.drawString("自機弾:攻撃力up", 210, y+40);
			g.drawString("エンジン:移動速度up", 210, y+size+48+40);
			g.drawString("自機装甲:防御力up", 210, y+size*2+48*2+40);
			
			
			g.drawString(shop.level[0]+"レベル", 210, y+80);
			g.drawString(shop.level[1]+"レベル", 210, y+size+48+80);
			g.drawString(shop.level[2]+"レベル", 210, y+size*2+48*2+80);
			
			g.drawString("価格:"+shop.price[0], 210, y+120);
			g.drawString("価格:"+shop.price[1], 210, y+size+48+120);
			g.drawString("価格:"+shop.price[2], 210, y+size*2+48*2+120);
			
			g.drawImage(goodsimage,x,y+size+49,x+size,y+size*2+49,size,0,size*2,size,frame1);
			g.drawImage(goodsimage,x,y+size*2+49*2,x+size,y+size*3+49*2,size*2,0,size*3,size,frame1);
			
			if(enterkey == true) {
				g.clearRect(0,0,SCREEN_W,SCREEN_H);
				g.drawImage(backimage, 0, 0, frame1);
				player.mony  = shop.buy(shopnum, player.mony, g);
				player.update(shop.level[0], shop.level[1], shop.level[2]);
			}
			
			break;
			
		case 2:
			if(upkey == true) shopnum = 1;
			if(downkey == true) shopnum = 3;
			
			

			g.fillRect(x, y, size, size);
			g.fillRect(x, y+size+48, size, size);
			g.fillRect(x, y+size*2+48*2, size, size);
			if(show == 0) {
				g.drawImage(goodsimage,x,y+size+49,x+size,y+size*2+49,size,0,size*2,size,frame1);
				
			}
			
			g.setColor(Color.WHITE);
			
			g.drawString("自機弾:攻撃力up", 210, y+40);
			g.drawString("エンジン:移動速度up", 210, y+size+48+40);
			g.drawString("自機装甲:防御力up", 210, y+size*2+48*2+40);
			
			
			g.drawString(shop.level[0]+"レベル", 210, y+80);
			g.drawString(shop.level[1]+"レベル", 210, y+size+48+80);
			g.drawString(shop.level[2]+"レベル", 210, y+size*2+48*2+80);
			
			g.drawString("価格:"+shop.price[0], 210, y+120);
			g.drawString("価格:"+shop.price[1], 210, y+size+48+120);
			g.drawString("価格:"+shop.price[2], 210, y+size*2+48*2+120);
						
			g.drawImage(goodsimage,x,y,x+size,y+size,0,0,size,size,frame1);
			g.drawImage(goodsimage,x,y+size*2+49*2,x+size,y+size*3+49*2,size*2,0,size*3,size,frame1);
			
			if(enterkey == true) {
				g.clearRect(0,0,SCREEN_W,SCREEN_H);
				g.drawImage(backimage, 0, 0, frame1);
				player.mony  = shop.buy(shopnum, player.mony, g);
				player.update(shop.level[0], shop.level[1], shop.level[2]);
			}
			
			break;
			
		case 3:
			if(upkey == true) shopnum = 2;
			if(downkey == true) shopnum = 1;
			
			
			g.fillRect(x, y, size, size);
			g.fillRect(x, y+size+48, size, size);
			g.fillRect(x, y+size*2+48*2, size, size);
			if(show == 0) {
				g.drawImage(goodsimage,x,y+size*2+49*2,x+size,y+size*3+49*2,size*2,0,size*3,size,frame1);
				
			}
			

			g.setColor(Color.WHITE);
			
			g.drawString("自機弾:攻撃力up", 210, y+40);
			g.drawString("エンジン:移動速度up", 210, y+size+48+40);
			g.drawString("自機装甲:防御力up", 210, y+size*2+48*2+40);
			
			
			g.drawString(shop.level[0]+"レベル", 210, y+80);
			g.drawString(shop.level[1]+"レベル", 210, y+size+48+80);
			g.drawString(shop.level[2]+"レベル", 210, y+size*2+48*2+80);
			
			g.drawString("価格:"+shop.price[0], 210, y+120);
			g.drawString("価格:"+shop.price[1], 210, y+size+48+120);
			g.drawString("価格:"+shop.price[2], 210, y+size*2+48*2+120);
			
			g.drawImage(goodsimage,x,y,x+size,y+size,0,0,size,size,frame1);
			g.drawImage(goodsimage,x,y+size+49,x+size,y+size*2+49,size,0,size*2,size,frame1);
			
			if(enterkey == true) {
				g.clearRect(0,0,SCREEN_W,SCREEN_H);
				g.drawImage(backimage, 0, 0, frame1);
				player.mony  = shop.buy(shopnum, player.mony, g);
				player.update(shop.level[0], shop.level[1], shop.level[2]);
			}
			
			break;
		}
		
		++shoptime;
	}

	
	/**
	 * スタート画面描画メソッド
	 */
	@Override
	public void runStartWindow(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLACK);
		g.fillRect(0,0,SCREEN_W,SCREEN_H);
		g.setColor(Color.CYAN);
		g.setFont(new Font("SansSerif",Font.BOLD,60));
		drawStringCenter("ステージ"+ stagenum,200,g);
		drawStringCenter("スタート",280,g);
		player.Level = 1;
		player.scorelimit = 1000;
	}

	
	/**
	 * ステージ画面描画メソッド
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void runStageWindow(Graphics g) {
		// TODO Auto-generated method stub
		g.clearRect(0,0,SCREEN_W,SCREEN_H);
		
		
		//自機移動
		player.move(upkey,downkey,leftkey,rightkey);
		player.levelup(score);
		
		
		//
		//
		//各ArrayListの実体作成
		//
		//
		//自弾発射
		if (P_bulletrate==0){
			playerbullets.add(new PlayerBullet(player.chara_x+16,
					player.chara_y,bulletimage,1));
			if(player.Level>=2) {
				playerbullets.add(new PlayerBullet(player.chara_x+16,
						player.chara_y,bulletimage,2));
			}
			if(player.Level>=3) {
				playerbullets.add(new PlayerBullet(player.chara_x+16,
						player.chara_y,bulletimage,3));	
			}
			if(player.Level >= 4) {
				playerbullets.add(new PlayerBullet(player.chara_x,
						player.chara_y,bulletimage,1));	
			}
			if(player.Level >= 5) {
				playerbullets.add(new PlayerBullet(player.chara_x+32,
						player.chara_y,bulletimage,1));	
			}
			
			P_bulletrate = 3;
			
		}
		

		//敵出現
		ArrayList al = map.getNewEnemy(enemyimage);
		if (al.size()>0) enemys.addAll(al);

		//敵弾発射
		Iterator it = enemys.iterator();
		while(it.hasNext()==true){
			Enemy tk = (Enemy)it.next();
			if (Math.random()*E_bulletrate<1){
				
				switch(stagenum) {
				case 1:
					if(tk.num < 3) {
						enemybullet.add(new EnemyBullet(tk.chara_x-8, tk.chara_y+24,
								player.chara_x, player.chara_y, bulletimage,2,1));
					}else {
						enemybullet.add(new EnemyBullet(tk.chara_x+8, tk.chara_y+24,
								player.chara_x, player.chara_y, bulletimage,2,1));
						enemybullet.add(new EnemyBullet(tk.chara_x-8, tk.chara_y+24,
								player.chara_x, player.chara_y, bulletimage,2,1));
					}
					break;
				case 2:
				case 3:
					enemybullet.add(new EnemyBullet(tk.chara_x-8, tk.chara_y+24,
							player.chara_x, player.chara_y, bulletimage,2,1));
					break;
					
				}
				
				/*
				enemybullet.add(new EnemyBullet(tk.chara_x-8, tk.chara_y+24,
						player.chara_x, player.chara_y, charaimage,2,2));
				
				enemybullet.add(new EnemyBullet(tk.chara_x-8, tk.chara_y+24,
						player.chara_x, player.chara_y, charaimage,2,3));
				
				enemybullet.add(new EnemyBullet(tk.chara_x-8, tk.chara_y+24,
						player.chara_x, player.chara_y, charaimage,2,4));
				*/
				seClip2.stop();
				seClip2.setFramePosition(0);
				seClip2.start();
			}
		}
		
		//boss弾
		if(boss.app == true){
			if (Math.random()*B_bulletrate<1){
				
				//各ステージ
				switch(stagenum) {
				case 1:
					enemybullet.add(new EnemyBullet(boss.chara_x+120, boss.chara_y+230,
							player.chara_x, player.chara_y, bulletimage,1,1));
					
					enemybullet.add(new EnemyBullet(boss.chara_x+24, boss.chara_y+171,
							player.chara_x, player.chara_y, bulletimage,1,1));
					
					enemybullet.add(new EnemyBullet(boss.chara_x+200, boss.chara_y+171,
							player.chara_x, player.chara_y, bulletimage,1,1));
					
					break;
				case 2:
					enemybullet.add(new EnemyBullet(boss.chara_x+30, boss.chara_y+200,
							player.chara_x, player.chara_y, bulletimage,1,1));
					
					enemybullet.add(new EnemyBullet(boss.chara_x+200, boss.chara_y+200,
							player.chara_x, player.chara_y, bulletimage,1,1));
					
					break;
				case 3:
					enemybullet.add(new EnemyBullet(boss.chara_x+90, boss.chara_y+210,
							player.chara_x, player.chara_y, bulletimage,1,1));
					enemybullet.add(new EnemyBullet(boss.chara_x+170, boss.chara_y+210,
							player.chara_x, player.chara_y, bulletimage,1,1));
					
					enemybullet.add(new EnemyBullet(boss.chara_x+23, boss.chara_y+146,
							player.chara_x, player.chara_y, bulletimage,1,1));
					
					enemybullet.add(new EnemyBullet(boss.chara_x+233, boss.chara_y+146,
							player.chara_x, player.chara_y, bulletimage,1,1));
					
					break;
					
				}
				
				/*
				
				int[] angle = new int[12];

				for ( int i = 0;i < angle.length;i++ ) {
					angle[i] = i * 30;
				}
				int distance = 50;
				
				for (int i = 0; i < angle.length; ++i) {
					++angle[i];
					//角度をラジアンに変換
					double rad = Math.toRadians(angle[i]);
		 
					//その角度のx座標+中心点, y座標+中心点を算出
					int rx = (int)(Math.cos(rad) * distance);
					int ry = (int)(Math.sin(rad) * distance);
					
					enemybullet.add(new EnemyBullet(boss.chara_x+rx, boss.chara_y+ry,
							i, 0, bulletimage,1,5));
					
					enemybullet.add(new EnemyBullet(boss.chara_x, boss.chara_y,
							rx, ry, bulletimage,1,6));
					
				}
				*/
				seClip2.stop();
				seClip2.setFramePosition(0);
				seClip2.start();
			}
		}
		
		//
		//
		//
		//表示・削除
		map.draw(g,frame1);
		player.draw(g,frame1);
		P_gauge.draw(g);
		
		
		//自機弾
		it = playerbullets.iterator();
		while(it.hasNext()==true){
			PlayerBullet jt = (PlayerBullet)it.next();
			jt.draw(g,frame1);
			if(jt.isFrameOut()==true) it.remove();
		}
		
		//敵
		it = enemys.iterator();
		while(it.hasNext()==true){
			Enemy tk = (Enemy)it.next();
			tk.draw(g,frame1);
			
			if(tk.isFrameOut()==true) it.remove();
		}
		
		//敵弾
		it = enemybullet.iterator();
		while(it.hasNext()==true){
			EnemyBullet tm = (EnemyBullet)it.next();
			tm.draw(g,frame1);
			if(tm.isFrameOut()==true) it.remove();
		}
		
		
		//boss
		if(stagetime <= 0) {
			
			B_gauge.draw(g);
			boss.draw(g, frame1);
			
			seqset(0);
			
			//boss特殊仕様
			switch(stagenum) {
			case 1:
				float d = (float) Math.atan2(player.chara_y-(boss.chara_y+194),player.chara_x-(boss.chara_x+44));			
				Graphics2D g2 = (Graphics2D) bstrategy.getDrawGraphics();
				AffineTransform at = g2.getTransform();
				at.setToRotation(d,boss.chara_x+44, boss.chara_y+197);
				g2.setTransform(at);
				g2.drawImage(bosspartsimage,boss.chara_x+21,boss.chara_y+171,frame1);
				d = (float) Math.atan2(player.chara_y-(boss.chara_y+194),player.chara_x-(boss.chara_x+212));
				at.setToRotation(d,boss.chara_x+212, boss.chara_y+194);
				g2.setTransform(at);
				g2.drawImage(bosspartsimage,boss.chara_x+189,boss.chara_y+171,frame1);
				
				break;
			case 2:
				if (Math.random()*300<1){
					for(int i=0; i < 20; ++i) {
						for(int s=0; s < 480; s+=16) {
							enemybullet.add(new EnemyBullet(s,0,
									i, 10, bulletimage,1,6));
						}
					}
				}
				
				if (Math.random()*100<1){
					for(int i=0; i < 500; i+=5) {
						double b = Math.toRadians(i) * 2;
			        
			        	int x = (int) (Math.cos(b) + b * Math.sin(b));
			        	int y = (int) (Math.sin(b) - b * Math.cos(b));
			        
						enemybullet.add(new EnemyBullet(boss.chara_x+128+x,boss.chara_y+128+y,
								x, y, bulletimage,1,6));
					}
				}
				
				if (Math.random()*400<1){
					for(int i=0; i < 20000; i+=5) {
						double b = Math.toRadians(i) * 2;
			        
			        	int x = (int) (Math.cos(b) + b * Math.sin(b));
			        	int y = (int) (Math.sin(b) - b * Math.cos(b));
			        
						enemybullet.add(new EnemyBullet(boss.chara_x+128+x,boss.chara_y+128+y,
								x, y, bulletimage,1,6));
					}
				}
				break;
			case 3:
				d = (float) Math.atan2(player.chara_y-(boss.chara_y+123),player.chara_x-(boss.chara_x+23));			
				g2 = (Graphics2D) bstrategy.getDrawGraphics();
				at = g2.getTransform();
				at.setToRotation(d,boss.chara_x+23, boss.chara_y+123);
				g2.setTransform(at);
				g2.drawImage(bosspartsimage,boss.chara_x,boss.chara_y+100,frame1);
				d = (float) Math.atan2(player.chara_y-(boss.chara_y+123),player.chara_x-(boss.chara_x+233));
				at.setToRotation(d,boss.chara_x+233, boss.chara_y+123);
				g2.setTransform(at);
				g2.drawImage(bosspartsimage,boss.chara_x+210,boss.chara_y+100,frame1);
				
				if (Math.random()*400<1){
				subboss.add(new Boss(boss.chara_x+12, boss.chara_y+27,
						subbossimage,clearcnt[2]));
				subboss.add(new Boss(boss.chara_x+129, boss.chara_y+27,
						subbossimage,clearcnt[2]));
				}
				
				if (Math.random()*100<1){
					for(int i=0; i < 500; i+=5) {
						double b = Math.toRadians(i) * 2;
			        
			        	int x = (int) (Math.cos(b) + b * Math.sin(b));
			        	int y = (int) (Math.sin(b) - b * Math.cos(b));
			        
						enemybullet.add(new EnemyBullet(boss.chara_x+128+x,boss.chara_y+128+y,
								x, y, bulletimage,1,6));
					}
				}
				
				//stage3subboss
				it = subboss.iterator();
				while(it.hasNext()==true){
					Boss subbs = (Boss)it.next();	
					subbs.draw(g,frame1);
					if(subbs.isFrameOut()==true) it.remove();
					
					if (Math.random()*B_bulletrate<1){
						int[] angle = new int[12];

						for ( int i = 0;i < angle.length;i++ ) {
							angle[i] = i * 30;
						}
						int distance = 50;
						
						for (int i = 0; i < angle.length; ++i) {
							++angle[i];
							//角度をラジアンに変換
							double rad = Math.toRadians(angle[i]);
				 
							//その角度のx座標+中心点, y座標+中心点を算出
							int rx = (int)(Math.cos(rad) * distance);
							int ry = (int)(Math.sin(rad) * distance);
							
							enemybullet.add(new EnemyBullet(subbs.chara_x+57+rx,subbs.chara_y+98+ry,
									i, 0, bulletimage,1,5));
						}
					}	
				}
				
				break;
			}
			
			boss.app = true;
		}
		

		//爆発エフェクト
		it = exprosions.iterator();
		while(it.hasNext()==true){
			Exprosion bh = (Exprosion)it.next();	
			bh.draw(g,frame1);
			if(bh.isFrameOut()==true) it.remove();
		}
		
		
		//
		//
		//
		//
		//当たり判定（敵と自機弾）
		it = enemys.iterator();
		while(it.hasNext()==true){
			Enemy tk = (Enemy)it.next();
			Iterator it2 = playerbullets.iterator();
			while(it2.hasNext()==true){
				PlayerBullet jt = (PlayerBullet)it2.next();
				if(tk.isCollision(jt)==true) {
					it2.remove();
					tk.HP -= player.Attack;
					break;
				}	
			}
			if(tk.HP <= 0) {
				exprosions.add(new Exprosion(tk.chara_x, tk.chara_y, effectimage));
				it.remove();
				++player.mony; 
				score = score+10;
				seClip1.stop();
				seClip1.setFramePosition(0);
				seClip1.start();
				break;
			}
		}
		
		
		//当たり判定（bossと自機弾）
		if(boss.app == true && stagetime <= 0) {
			B_gauge.updateState();
			Iterator it2 = playerbullets.iterator();
			while(it2.hasNext()==true){
				PlayerBullet jt = (PlayerBullet)it2.next();
				if(boss.isCollision(jt)==true) {
					it2.remove();
					boss.HP -= player.Attack;
					B_gauge.down(player.Attack,true);
					exprosions.add(new Exprosion(player.chara_x, boss.chara_y+160, effectimage));
					score = score+10;
					seClip1.stop();
					seClip1.setFramePosition(0);
					seClip1.start();
					break;
				}
			}
			
			if(boss.isCollision(player)){
				int damage;
				if(boss.attack <= player.Defense) damage = 1;
				else damage = boss.attack - player.Defense;
				P_gauge.down(damage);
				player.HP -= damage;
				damage = player.Attack *2;
				boss.HP -= damage;
				B_gauge.down(damage,true);
				
			}
		}
		
		//当たり判定（自機と敵、敵弾）
		it = enemys.iterator();
		while(it.hasNext()==true){
			Enemy tk = (Enemy)it.next();
			if(tk.isCollision(player)){
				int damage;
				if(boss.attack <= player.Defense) damage = 1;
				else damage = 10 - player.Defense;
				player.HP -= damage;
				P_gauge.down(damage);
				it.remove();
				break;
			}
		}
		it = enemybullet.iterator();
		while(it.hasNext()==true){
			EnemyBullet tt = (EnemyBullet)it.next();
			if(tt.isCollision(player)){
				it.remove();
				P_gauge.down(1);
				--player.HP;
				break;
			}
		}
		
		//
		//
		//自機HPを更新
		P_gauge.updateState();
		
		//GAMEOVER
		if (player.HP <= 0) goGameOverWindow();
		
		//GAMECLEAE
		if (boss.HP <= 0 && stagetime <= 0) {
			player.mony +=100;
			boss.app = false;
			goGameClearWindow();
		}
		
		//残り時間のチェック
		if(map.isMapEnd()==true) {
			
		}
		
		//スコア表示
		g.setColor(Color.WHITE);
		g.setFont(new Font("SansSerif",Font.PLAIN,20));
		g.drawString("Level:"+player.Level+"   SC:"+score + "  HP:"+ player.HP,2,620);
		if(boss.app == false) {
			g.drawString("ボス出現まで:"+stagetime,190,20);
		}
		
		//
		//
		//タイマー、変数加算減算
		//
		//自機弾発射
		if (P_bulletrate>0) P_bulletrate = P_bulletrate-1;
		
		//ボス出現までの時間
		--stagetime;
	}

	/**
	 * ゲームクリア画面描画メソッド
	 */
	@Override
	public void runGameClearWindow(Graphics g) {
		// TODO Auto-generated method stub
		g.clearRect(0,0,SCREEN_W,SCREEN_H);
		g.setColor(Color.CYAN);
		g.drawImage(backimage,0,0, frame1);
		
		g.setFont(new Font("SansSerif",Font.BOLD,120));
		drawStringCenter("Clear",260,g);
		g.setFont(new Font("SansSerif",Font.BOLD,40));
		drawStringCenter("SCORE:"+score,340,g);
		g.setFont(new Font("SansSerif",Font.PLAIN,24));
		drawStringCenter("PUSH  H  KEY",400,g);
		g.setFont(new Font("SansSerif",Font.PLAIN,24));
		drawStringCenter("PUSH  R  KEY",460,g);
		
	}

	/**
	 * ゲームオーバー画面描画メソッド
	 */
	@Override
	public void runGameOverWindow(Graphics g) {
		// TODO Auto-generated method stub
		g.clearRect(0,0,SCREEN_W,SCREEN_H);
		g.setColor(Color.RED);
		g.setFont(new Font("SansSerif",Font.BOLD,75));
		drawStringCenter("GAMEOVER",260,g);
		g.setFont(new Font("SansSerif",Font.PLAIN,24));
		drawStringCenter("PUSH  H  KEY",400,g);
		g.setFont(new Font("SansSerif",Font.PLAIN,24));
		drawStringCenter("PUSH  R  KEY",460,g);
		
	}
	
	
	//
	//今回使用せず
	//
	/**
	 * クレジット画面描画メソッド
	 */
	public void runCreditWindow(Graphics g) {
		// TODO Auto-generated method stub
		g.clearRect(0,0,SCREEN_W,SCREEN_H);
		
	}
	
	
	//
	//エフェクト用タイマータスク
	//
	/*public class EffectTimerTask extends TimerTask {
		public void run() {
			
		}
		
	}
	
	//
	//Timerを止める
	//
	public void EffectTimerStop() {
		timer.cancel();
		timer = null;
	}
	//
	//Timerを動かす
	//
	public void EffectTimerStart() {
		if (timer == null) {
			timer = new Timer();
		}
		timer.schedule(new EffectTimerTask(), 0, 30);
	}*/
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		Object usm = new GameMain();
	}
}

	
