import java.awt.geom.Path2D.Iterator;




class GremlinQueryApp {

	def static run(){
		//testing

		Read r = new Read("projects.csv")
		def projects = r.getProjects()
		println('Reader Finished!')

		projects.each {
			GremlinQuery gq = new GremlinQuery(it.graph)

			GremlinPrinter p = new GremlinPrinter()
			p.writeCSV(gq.getMergeCommitsList())
			println('Printer Finished!')
			println("----------------------")

			/*it.setMergeCommits(gq.getMergeCommitsList())
			 Extractor e = new Extractor(it)
			 e.extractCommits()
			 println('Extractor Finished!\n')*/
			projects.each {
				r.setCsvCommitsFile("commits.csv")
				r.readCommitsCSV()
				def ls = r.getMergeCommitsList()
	
				it.setMergeCommits(ls)
	
				Extractor e = new Extractor(it, '/Users/paolaaccioly/Desktop/')
				e.extractCommits()
				println('Extractor Finished!\n')
			}
		}
		System.exit(0)
	}

	def static runWithCommitCsv(){
		// running directly with the commit list in a CSV file

		Read r = new Read("projects.csv")
		def projects = r.getProjects()
		println('Reader Finished!')

		projects.each {
			r.setCsvCommitsFile("commits.csv")
			r.readCommitsCSV()
			def ls = r.getMergeCommitsList()

			it.setMergeCommits(ls)

			Extractor e = new Extractor(it,'/Users/paolaaccioly/Desktop' )
			e.extractCommits()
			println('Extractor Finished!\n')
		}
	}

	def static choose25MergeScenarios(){

		Read r = new Read("projects.csv")
		def project = r.getProjects().get(0)
		println('Reader Finished!')


		GremlinQuery gq = new GremlinQuery(project.graph)

		//ArrayList<MergeCommit> mergeScenarios = removeAnalyzedScenarios(analyzedScenarios, gq.getMergeCommitsList())
		ArrayList<MergeCommit> mergeScenarios = gq.getMergeCommitsList()
		double temp = mergeScenarios.size/5
		int partitionsSize = temp.round()
		ArrayList<ArrayList<MergeCommit>> partitionsTemp = mergeScenarios.collate(partitionsSize)
		ArrayList<MergeCommit> partitions = new ArrayList<MergeCommit>();

		(0..4).each{

			partitions.add(partitionsTemp.get(it))
		}



		ArrayList<MergeCommit> chosenMergeScenarios = new ArrayList<MergeCommit>();



		for(ArrayList<MergeCommit> partition : partitions){

			Collections.shuffle(partition)
			
			(0..4).each{
				
				chosenMergeScenarios.add(partition.get(it))
			}
			

		}

		GremlinPrinter p = new GremlinPrinter()
		p.writeCSV(chosenMergeScenarios)
		println('Printer Finished!')
		println("----------------------")



	}

	def static ArrayList<MergeCommit> removeAnalyzedScenarios(String revisionFile, ArrayList<MergeCommit> allMergeScenarios){

		ArrayList<MergeCommit> notAnalyzedScenarios = new ArrayList<MergeCommit>()

		def analyzedScenarios = new File(revisionFile)
		
		for(MergeCommit commit : allMergeScenarios){
			
			boolean found = false
			analyzedScenarios.eachLine {
				
				if(!it.empty){
					String sha = it.toString().substring(0, 40)
					
					if(commit.getSha().equals(sha)){
						found = true
					}
					
				}
				
				
			}
			
			if(!found){
				
				notAnalyzedScenarios.add(commit)
			}
			
			
		}

		return notAnalyzedScenarios
	}
	
	
	public String run(String projectName, String projectRepo, String graphBase, String downloadPath){
		this.createProjectDir(projectName)
		GremlinProject project = new GremlinProject(projectName, projectRepo, graphBase)
		GremlinQuery gq = new GremlinQuery(project.graph)
		project.listMergeCommit = gq.mergeCommitsList
		gq.shutdownExistingGraph()
		Extractor e = new Extractor(project, downloadPath)
		e.extractCommits()
		println('All merges were downloaded from GitHub!\n')
		return e.getRevisionFile()

	}
	
	private void createProjectDir(String projectName){
		File projectDir = new File( "ResultData" + File.separator + projectName)
		if(!projectDir.exists()){
			projectDir.mkdir()
		}
		
	}
	
	public static void main (String[] args){
		//runWithCommitCsv()
		
		run()

		//choose25MergeScenarios()
		

	}
}
