package jp.arase.minigame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MapGeneration{
	String[] num = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D"};
	int height = 2000, width = 20;
	
	MapGeneration(){
		 try{
		      File file = new File("C:\\Users\\yuuta\\eclipse-workspace\\MYGAME\\Grow up Shooter\\jp\\arase\\minigame\\003.map");

		      if (checkBeforeWritefile(file)){
		        FileWriter filewriter = new FileWriter(file);
		        
		        for(int i = 0; i < height; ++i) {
		        	filewriter.write(edit()+"\r\n");
		        }
		        

		        filewriter.close();
		      }else{
		        System.out.println("ファイルに書き込めません");
		      }
		    }catch(IOException e){
		      System.out.println(e);
		    }
	}
	
	public String edit() { 
		String txt = null;
        Random r = new Random();
        for(int i = 0; i < width; ++i) {
        	if(i == 0) {
        		txt = num[r.nextInt(14)];
        	}else {
        		txt = txt + num[r.nextInt(14)];
        	}
        }
		return txt;
	}
	
	private static boolean checkBeforeWritefile(File file){
		if (file.exists()){
			if (file.isFile() && file.canWrite()){
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		Object tes = new MapGeneration();
		}
}