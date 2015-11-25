package main
import java.util.ArrayList;


class GremlinPrinter {
	
	public static void writeCSV(ArrayList<MergeCommit> listMC, String projectName){	
		String filepath = 'ResultData' + File.separator + projectName + File.separator  + 'mergeCommits.csv'
		def out = new File(filepath)
		
		// deleting old files if it exists
		out.delete()
		
		out = new File(filepath)
		
		def firstRow = ["Merge commit", "Parent 1", "Parent 2"]
		out.append firstRow.join(',')
		out.append '\n'
		
		listMC.each {
			def row = [it.sha, it.parent1, it.parent2]
			out.append row.join(',')
			out.append '\n'
		}
		
	}
	
	public void writeMissingUnknow(ArrayList<String> listMU){
		
		def out = new File('missingUnknown.csv')
		
		// deleting old files if it exists
		out.delete()
		
		out = new File('missingUnknown.csv')
		
		listMU.each {
			def row = [it]
			out.append row.join(',')
			out.append '\n'
		}
		
	}

}
