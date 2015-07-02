package main
import java.util.ArrayList;


class GremlinPrinter {
	
	public void writeCSV(ArrayList<MergeCommit> listMC){
		
		def out = new File('commits.csv')
		
		// deleting old files if it exists
		out.delete()
		
		out = new File('commits.csv')
		
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
