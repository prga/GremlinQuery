package util

import java.io.File;

class RecursiveFileList {
	
	ArrayList<String> filenames
	
	
	
	RecursiveFileList(){
		
		
	}
	
	def removeFiles(File dir){
		
		File[] files = dir.listFiles()
		
		for(int i = 0; i < files.length; i++){
			
			if(files[i].isFile()){
				
				String filePath = files[i].getAbsolutePath()
				
				if(!(filePath.endsWith(".java"))){
					
					if(files[i].delete()){
    			//println(files[i].getName() + " is deleted!");
    		}else{
    			println(files[i].getName() + " delete operation has failed.");
    		}
				}
				
			} else if (files[i].isDirectory()){
				
			this.removeFiles(files[i])
		}
		
	}
	}
	
	
	
	
	public static void main (String[] args){
		
		File file = new File("/Users/paolaaccioly/Desktop/Teste/rev_6b959_8ceae/rev_right_8ceae")
		RecursiveFileList rec = new RecursiveFileList()
	
		rec.removeFiles(file)
		
	}

}
