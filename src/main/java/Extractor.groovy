

import org.eclipse.jgit.api.CheckoutCommand
import org.eclipse.jgit.api.CleanCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.MergeCommand
import org.eclipse.jgit.api.MergeResult
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.api.ResetCommand.ResetType
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder

class Extractor {

	// the url of the repository
	def static final String REMOTE_URL = "https://github.com/prga/TGM.git"

	// the directory to clone to
	def static final String REPOSITORY_DIR = "C:/Users/Guilherme/Desktop/Itens Recentes/tgm"

	// the commit to checkout
	def static final String SHA1_1 = "448259185594ed4f0b9ea2c6be9197ca3f5573db"
	def static final String SHA1_2 = "c0c6d0e7b0f175b800925472be4c550e5a39567d"


	def cloneRepository(){
		// prepare a new folder for the cloned repository
		File gitWorkDir = new File(REPOSITORY_DIR)
		gitWorkDir.mkdirs()

		// then clone
		println "Cloning from " + REMOTE_URL + " to " + gitWorkDir
		Git.cloneRepository()
				.setURI(REMOTE_URL)
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

	def Git openRepository() {
		File gitWorkDir = new File(REPOSITORY_DIR);
		Git git = Git.open(gitWorkDir);
		Repository repository = git.getRepository()
		return git
	}

	def listAllBranches() {
		// opening the working directory
		Git git = openRepository();

		List<Ref> call = git.branchList().call();
		for (Ref ref : call) {
			println "Branch-Before: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName();
		}
	}

	def checkoutMasterBranch() {
		// opening the working directory
		Git git = openRepository();

		CheckoutCommand checkoutCommand = git.checkout()
		checkoutCommand.setName("refs/heads/master")
		Ref checkoutResult = checkoutCommand.call()
		println "Checked out branch sucessfully: " + checkoutResult.getName()
	}

	def Ref checkoutAndCreateBranch(String branchName, String commit){
		// opening the working directory
		Git git = openRepository();

		CheckoutCommand checkoutCommand = git.checkout()
		checkoutCommand.setName(branchName)
		checkoutCommand.setStartPoint(commit)
		checkoutCommand.setCreateBranch(true)
		Ref checkoutResult = checkoutCommand.call()
		println "Checked out and created branch sucessfully: " + checkoutResult.getName()

		return checkoutResult
	}

	def deleteBranch(String branchName) {
		// opening the working directory
		Git git = openRepository();

		git.branchDelete()
				.setBranchNames(branchName)
				.call()
	}

	def reset() {
		// opening the working directory
		Git git = openRepository();

		//git reset --hard SHA1_1
		ResetCommand resetCommand = git.reset()
		resetCommand.setMode(ResetType.HARD)
		resetCommand.setRef(SHA1_1)
		Ref resetResult = resetCommand.call()
		println "Reseted sucessfully to: " + resetResult.getName()

		// git clean -f
		CleanCommand cleanCommandgit = git.clean()
		cleanCommandgit.call()

		// git checkout -b new SHA1_2
		def refNew = checkoutAndCreateBranch("new", SHA1_2)

		// git checkout master
		checkoutMasterBranch()

		MergeCommand mergeCommand = git.merge()
		mergeCommand.include(refNew)
		MergeResult res = mergeCommand.call()

		if (res.getMergeStatus().equals(MergeResult.MergeStatus.CONFLICTING)){
			println "Revision Base: " + res.getBase().toString()
			println "Conflitcts: " + res.getConflicts().toString()

			println ""
			
			Map allConflicts = res.getConflicts();
			for (String path : allConflicts.keySet()) {
				int[][] c = allConflicts.get(path);
				println "Conflicts in file " + path
				for (int i = 0; i < c.length; ++i) {
					println "  Conflict #" + i
					for (int j = 0; j < (c[i].length) - 1; ++j) {
						if (c[i][j] >= 0)
							println"    Chunk for " + res.getMergedCommits()[j] + " starts on line #" + c[i][j];
					}
				}
			}
		}
	}

	static void main(args) {
		def extrc = new Extractor()
		extrc.reset()
	}
}
