package main

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;

import org.eclipse.jgit.api.CleanCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.MergeCommand
import org.eclipse.jgit.api.MergeResult
import org.eclipse.jgit.api.RenameBranchCommand
import org.eclipse.jgit.api.ResetCommand
import org.eclipse.jgit.api.ResetCommand.ResetType
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.revwalk.filter.RevFilter
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.api.BlameCommand
import org.eclipse.jgit.blame.BlameResult
import org.eclipse.jgit.api.AddCommand
import org.eclipse.jgit.api.CommitCommand
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.lib.ObjectLoader;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.diff.DiffFormatter
import org.eclipse.jgit.diff.DiffEntry
import org.eclipse.jgit.diff.RawTextComparator

import org.apache.commons.io.FileUtils

import java.text.MessageFormat

import org.eclipse.jgit.util.io.DisabledOutputStream

import util.ChkoutCmd
import util.RecursiveFileList

class Blame {

	public String annotateBlame(File left, File base, File right){
		String result = ''

		//Init repo
		Git git = this.openRepository()
		Repository repo = git.getRepository()
		String repoDir = repo.getDirectory().getParent()

		//Create base commit on master
		File movedFile = new File( repoDir + File.separator + 'file' )
		FileUtils.copyFile(base, movedFile);
		def add = git.add().addFilepattern('file').call()
		RevCommit commitBase = git.commit().setMessage("Base commit").call()

		//Creating left commit on left branch
		def ref_left = this.checkoutAndCreateBranch(repo, "left")
		movedFile.delete()
		FileUtils.copyFile(left, movedFile);
		RevCommit commitLeft = git.commit().setAll(true).setMessage("Left commit").call()

		checkoutMasterBranch(repo)

		//Creating right commit on right branch
		def ref_right = this.checkoutAndCreateBranch(repo, "right")
		movedFile.delete()
		FileUtils.copyFile(right, movedFile);
		RevCommit commitRight = git.commit().setAll(true).setMessage("Right commit").call()

		checkoutMasterBranch(repo)

		//Merging left and right on master
		MergeResult res_left = git.merge().include(commitLeft.getId()).setCommit(false).call();
		git.commit().setMessage("Merging left on master").call();
		println res_left.getMergeStatus()

		MergeResult res_right = git.merge().include(commitRight.getId()).setCommit(false).call();
		git.commit().setMessage("Merging right on master").call();
		println res_right.getMergeStatus()

		result = this.executeAndProcessBlame(movedFile, repo, commitLeft, commitBase, commitRight)
		repo.close();
		File dir = new File(repoDir)
		dir.deleteDir()
		
		return result
	}


	private String executeAndProcessBlame(File file, Repository repo, RevCommit left,
			RevCommit base, RevCommit right){

		String result = ''	
		BlameCommand blamer = new BlameCommand(repo);
		ObjectId commitID = repo.resolve("HEAD");
		
		blamer.setStartCommit(commitID);
		blamer.setFilePath('file');
		BlameResult blame = blamer.call();
		int i = 0
		file.eachLine {
			RevCommit commit = blame.getSourceCommit(i);
			String line = ''
			if(commit.equals(left))
			{
				line = '// LEFT //' + it
			}else if(commit.equals(right))
			{
				line = '// RIGHT //' + it 
			}else{
				line = it 
			}
			
			result = result + line + '\n'
			i++
		}
		
		return result
	}



	public Git openRepository() throws IOException {
		String repositoryDir = System.getProperty("user.dir") + File.separator + 'GitBlameRepo'
		File gitWorkDir = new File(repositoryDir);

		//delete repo if it already exists
		if(gitWorkDir.exists()){
			gitWorkDir.deleteDir()
		}
		gitWorkDir.mkdir()

		Git git = Git.init().setDirectory(gitWorkDir).call()
		System.out.println("Having repository: " + git.getRepository().getDirectory());

		return git
	}


	public Ref checkoutAndCreateBranch(Repository repo, String branchName){
		ChkoutCmd chkcmd = new ChkoutCmd(repo)
		chkcmd.setName(branchName)
		chkcmd.setCreateBranch(true)
		chkcmd.setForce(true);
		Ref checkoutResult = chkcmd.call()
		println "Checked out and created branch sucessfully: " + checkoutResult.getName()

		return checkoutResult
	}

	public void checkoutMasterBranch(def repo) {
		ChkoutCmd chkcmd = new ChkoutCmd(repo)
		chkcmd.setName("refs/heads/master")
		chkcmd.setForce(true)
		Ref checkoutResult = chkcmd.call()
		println "Checked out branch sucessfully: " + checkoutResult.getName()
	}
}
