package main

import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.RenameBranchCommand

class CommitterAnalyzer {

	private String downloadPath

	private ArrayList<GremlinProject> projects

	private String repoDir

	public CommitterAnalyzer(String downloadpath, String resultsPath){
		this.downloadPath = downloadpath
		this.repoDir = downloadPath + File.separator
		this.initializeProjectsList(downloadpath, resultsPath)
	}

	public void initializeProjectsList(String projectsFile, String resultsPath){
		this.projects = new ArrayList<GremlinProject>()
		File projects = new File(projectsFile + File.separator + 'projectList')

		projects.eachLine {
			String projectName = it.split('/')[1]
			GremlinProject gProject = new GremlinProject(projectName, it.trim(), '')
			ArrayList<MergeCommit> mcList = this.initializeMergeCommits(projectName, resultsPath)
			if(!mcList.empty){
				gProject.listMergeCommit = mcList
				this.projects.add(gProject)
			}

		}

	}


	private  ArrayList<MergeCommit>initializeMergeCommits(String projectName, String resultsPath){

		ArrayList<MergeCommit> result = new ArrayList<MergeCommit>()
		String mergeCommitFilePath = resultsPath + File.separator + projectName + File.separator +
				'mergeCommits.csv'

		File mergeCommitFile = new File(mergeCommitFilePath)

		if(mergeCommitFile.exists()){
			boolean header = true

			mergeCommitFile.eachLine{
				if(!header){
					String[] line = it.split(',')
					MergeCommit mc = new MergeCommit()
					mc.sha = line[0].trim()
					mc.parent1 = line[1].trim()
					mc.parent2 = line[2].trim()
					result.add(mc)
				}else{
					header = false
				}
			}
		}

		return result
	}

	public void analyzeCommitters(){
		for(GremlinProject gProject : this.projects){
			//clone repo
			Git git = openRepository(gProject)
			Repository repo = git.getRepository()
			println 'analyzing merge commit parents'
			for(MergeCommit mc : gProject.listMergeCommit ){
				
				mc.parentsAreDifferent = this.setParentsAreDifferent(repo, mc)

			}
			println 'finished merge commit parents anlysis'
			this.printResults(gProject)
		}
	}

	private boolean setParentsAreDifferent(Repository repo, MergeCommit mc){
		boolean parentsAreDifferent = true

		//get parent1
		String parent1 = this.getParentId(repo, mc.parent1)
		//get parent2
		String parent2 = this.getParentId(repo, mc.parent2)
		//compare them
		
		
		if(parent1.equals(parent2)){
			parentsAreDifferent = false
		}

		return parentsAreDifferent
	}

	public static String getParentId(Repository repo, String commit){

		String email = ''
		
		try{
			RevWalk walk = new RevWalk(repo)
			ObjectId id = repo.resolve(commit)
			RevCommit parent = walk.parseCommit(id)
			PersonIdent person = parent.getCommitterIdent()
			email = person.getEmailAddress()
		} catch(Exception e){
			println 'did not find commit: ' + commit
		}
		
		

		return email

	}

	public static Git openRepository(String clonePath, String repo) {
		Git git = null
		String repositoryDir = clonePath
		try {
			File gitWorkDir = new File(repositoryDir);
			git = Git.open(gitWorkDir);
			Repository repository = git.getRepository()
			this.renameMainBranchIfNeeded(repository)
			return git
		} catch(org.eclipse.jgit.errors.RepositoryNotFoundException e){
			this.cloneRepository(clonePath, repo)
			this.openRepository(clonePath, repo)
		}
		return git
	}

	public static void cloneRepository(String clonePath, String repo){
		String repositoryDir = clonePath
		String url = 'https://github.com/' +  repo
		// prepare a new folder for the cloned repository
		File gitWorkDir = new File(repositoryDir)
		gitWorkDir.mkdirs()

		// then clone
		println "Cloning from " + url  + " to " + gitWorkDir + "..."
		Git.cloneRepository()
				.setURI(url)
				.setDirectory(gitWorkDir)
				.call();

		// now open the created repository
		FileRepositoryBuilder builder = new FileRepositoryBuilder()
		Repository repository = builder.setGitDir(gitWorkDir)
				.readEnvironment() // scan environment GIT_* variables
				.findGitDir() // scan up the file system tree
				.build();

		println "Having repository: " + repository.getDirectory()
		repository.close()

	}

	 public static void renameMainBranchIfNeeded(Repository repository){
		def branchName = repository.getBranch();
		if(branchName != "master"){
			RenameBranchCommand renameCommand = new RenameBranchCommand(repository);
			renameCommand.setNewName("master")
			renameCommand.call()
		}
	}

	public void printResults(){
		String fileName = this.downloadPath + File.separator + 'results.csv'
		File results = new File(fileName)
		results.delete()
		results = new File(fileName)

		results.append('Project, DiffParentsPercentage\n')
		int totalMerges = 0
		int mergesDiffParents = 0
		for(GremlinProject project : this.projects){
			double p = project.diffParentsPercentages
			results.append(project.name + ', ' + p + '\n')
			totalMerges = totalMerges + project.listMergeCommit.size()
			mergesDiffParents = mergesDiffParents + project.diffParents
		}
		double totalPercentage = (mergesDiffParents/totalMerges) * 100

		results.append('TOTAL, ' + totalPercentage)
	}

	public void printResults(GremlinProject gp){
		String fileName = this.downloadPath + File.separator + 'results.csv'
		File results = new File(fileName)
		if(!results.exists()){
			results.append('Project, DiffParentsPercentage\n')
		}

		gp.computeDiffParentsPercentage()
		double p = gp.diffParentsPercentages
		results.append(gp.name + ', ' + p + '\n')

	}

	public static void main (String [] args){
		CommitterAnalyzer ca = new CommitterAnalyzer(
				'/Users/paolaaccioly/Documents/testeConflictsAnalyzer/committerAnalyzer',
				'/Users/paolaaccioly/Documents/testeConflictsAnalyzer/conflictsAnalyzer/ResultData')
		ca.analyzeCommitters()
		ca.printResults()
	}
}
