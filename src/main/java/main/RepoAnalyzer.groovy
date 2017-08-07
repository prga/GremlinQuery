package main

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.RenameBranchCommand
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

//clone repos and check different properties such as
//do they use travis ci?

class RepoAnalyzer {

	String projectsFiles

	String downloadPath


	public RepoAnalyzer(String projectF){
		this.downloadPath = projectF
		this.projectsFiles = projectF + File.separator + 'projectList'
	}

	public void analyzeProjectsUsingTravis(){
		File projects = new File(this.projectsFiles)

		projects.eachLine {

			analyzerepo(it)

		}
	}

	private analyzerepo(String project) {
		
		//clone repo
		Git git = openRepository(project)

		if(git !=null){
			
			//search for travis config file
			String repoDir = this.getRepoDir(project)
			File dir = new File(repoDir)
			boolean hasTravisConfigFile = this.hasTravisConfigFile(dir)
			if(hasTravisConfigFile){
				//update report
				this.printReport(project)
			}


		}
	}

	private void printReport(String project){
		String fileName = this.downloadPath + File.separator + 'results.csv'
		File results = new File(fileName)
		String line = project + '\n'
		println 'project ' + project + ' uses Travis CI'
		results.append(line)
	}

	public Git openRepository(String project) {
		String repositoryDir = getRepoDir(project)
		try {
			File gitWorkDir = new File(repositoryDir);
			Git git = Git.open(gitWorkDir);
			Repository repository = git.getRepository()
			this.renameMainBranchIfNeeded(repository)
			return git
		} catch(org.eclipse.jgit.errors.RepositoryNotFoundException e){
			this.cloneRepository(project)
			this.openRepository(project)
		}
	}

	private String getRepoDir(String project) {
		String [] tokens = project.split(Pattern.quote(File.separator))
		String projectName = tokens[1]
		String repositoryDir = this.downloadPath + File.separator + projectName

		return repositoryDir
	}

	public void cloneRepository(String project){
		String repositoryDir = getRepoDir(project)

		// prepare a new folder for the cloned repository
		File gitWorkDir = new File(repositoryDir)
		gitWorkDir.mkdirs()

		// then clone
		String url = 'https://github.com/' + project + '.git'
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

	private renameMainBranchIfNeeded(Repository repository){
		def branchName = repository.getBranch();
		if(branchName != "master"){
			RenameBranchCommand renameCommand = new RenameBranchCommand(repository);
			renameCommand.setNewName("master")
			renameCommand.call()
		}
	}


	private boolean hasTravisConfigFile(File dir){
		File[] files = dir.listFiles()
		boolean hasTravisConfigFile = false
		int i = 0

		while(!hasTravisConfigFile && i < files.length){
			
			File file = files[i]
			String path = file.getAbsolutePath()
			//println path
			
			if(file.isDirectory() && !path.endsWith('.git')){
				
				hasTravisConfigFile = this.hasTravisConfigFile(file)
			}else{

				if(path.endsWith('.travis.yml')){
					hasTravisConfigFile = true
				}
			}
			
			i++
		}


		return hasTravisConfigFile

	}


	public static void main (String [] args){
		RepoAnalyzer ra = new RepoAnalyzer('/Users/paolaaccioly/Documents/repoanalyzer')
		ra.analyzeProjectsUsingTravis()
	}

}
