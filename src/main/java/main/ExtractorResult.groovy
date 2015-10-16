package main

class ExtractorResult {
	
	private String revisionFile
	
	private ArrayList<String> nonJavaFilesWithConflict
	
	public ExtractorResult(){
		this.revisionFile = ''
		this.nonJavaFilesWithConflict = new ArrayList<String>()
		
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
	
	
}
