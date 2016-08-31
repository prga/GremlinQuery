package main

class ExtractorResult {
	
	private String revisionFile
	
	private ArrayList<String> nonJavaFilesWithConflict
	
	private ArrayList<String> javaFilesWithConflict
	


	public ExtractorResult(){
		this.revisionFile = ''
		this.nonJavaFilesWithConflict = new ArrayList<String>()
		this.javaFilesWithConflict = new ArrayList<String>()
		
	}
	
	public String getRevisionFile() {
		return revisionFile;
	}
	public void setRevisionFile(String revisionFile) {
		this.revisionFile = revisionFile;
	}
	public ArrayList<String> getNonJavaFilesWithConflict() {
		return nonJavaFilesWithConflict;
	}
	public void setNonJavaFilesWithConflict(ArrayList<String> nonJavaFilesWithConflict) {
		this.nonJavaFilesWithConflict = nonJavaFilesWithConflict;
	}
	
	public ArrayList<String> getJavaFilesWithConflict() {
		return javaFilesWithConflict;
	}

	public void setJavaFilesWithConflict(ArrayList<String> javaFilesWithConflict) {
		this.javaFilesWithConflict = javaFilesWithConflict;
	}
	
}
