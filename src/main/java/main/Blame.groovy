package main

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException
import java.io.InputStreamReader;

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

	public static final String LEFT_SEPARATOR = '// LEFT //';
	public static final String RIGHT_SEPARATOR = '// RIGHT //';

	public static final String DIFF3MERGE_SEPARATOR = "<<<<<<<";
	public static final String DIFF3MERGE_END = ">>>>>>>";
	public static final String DIFF3MERGE_MIDDLE = "=======";
	
	public static final String NOT_A_PREDICTOR = "NOT_A_PREDICTOR";
	
	public String annotateBlame(File left, File base, File right){
		String result = ''
		String leftText = left.getText()

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
		CleanCommand cleanCommandgit = git.clean()
		cleanCommandgit.call()
		
		MergeResult res_right = git.merge().include(commitRight.getId()).setCommit(false).call();
		String status = res_right.mergeStatus

		
		if(!status.equalsIgnoreCase('conflicting')){
			git.commit().setMessage("Merging right on master").call();
			println res_right.getMergeStatus()
			cleanCommandgit = git.clean()
			cleanCommandgit.call()
	
			//check for identical lines added by both revisions
			ArrayList<Integer> identicalLines = this.checkIdenticalLinesAddedByBothRevs(left, base, right)
	
			//execute blame routine
			result = this.executeAndProcessBlame(movedFile, repo, commitLeft, commitRight, identicalLines)
		}
		

		//closing git repository and delete temporary dir
		repo.close();
		File dir = new File(repoDir)
		dir.deleteDir()

		return result
	}

	private List<Integer> checkIdenticalLinesAddedByBothRevs(File left, File base, File right){
		ArrayList<Integer> result = new ArrayList<Integer>()
		//execute merge
		String merge = this.executeMerge(left, base, right)
		if(merge.contains(DIFF3MERGE_SEPARATOR) && merge.contains(DIFF3MERGE_END) &&
		merge.contains(DIFF3MERGE_END)){

			result = this.retrieveIdentLines(merge)
		}
		return result
	}

	private List<Integer> retrieveIdentLines(String merge){
		ArrayList<Integer> result = new ArrayList<Integer>()
		int index = 0
		int conflictLines = 0
		String[] lines = merge.split('\n')

		while(index < lines.length){

			if(lines[index].contains(DIFF3MERGE_SEPARATOR)){

				while(!lines[index].contains(DIFF3MERGE_MIDDLE)){
					conflictLines++
					index++
				}
				conflictLines++
				index++
				while(!lines[index].contains(DIFF3MERGE_END)){
					result.add(new Integer(index - conflictLines))
					index++
				}
				conflictLines++
				index++
			}else{
				index++
			}
		}
		println  'Identical lines added by boths revisions ' + result
		return result
	}

	private String executeMerge(File left, File base, File right){

		String mergeCmd = "diff3 --merge " + left.getPath() + " " + base.getPath() + " " + right.getPath()
		Runtime run = Runtime.getRuntime()
		Process pr = run.exec(mergeCmd)

		BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()))
		String line = ""
		String res = ""
		while ((line=buf.readLine())!=null) {
			res += line + "\n"
		}
		pr.getInputStream().close()

		return res
	}


	private String executeAndProcessBlame(File file, Repository repo, RevCommit left,
			RevCommit right, ArrayList<Integer> identicalLines){

		String result = ''
		BlameCommand blamer = new BlameCommand(repo);
		ObjectId commitID = repo.resolve("HEAD");
		
		
		blamer.setStartCommit(commitID);
		blamer.setFilePath('file');
		BlameResult blame = blamer.call();
		ArrayList<Integer> leftIndexes = new ArrayList<Integer>()
		ArrayList<Integer> rightIndexes = new ArrayList<Integer>()
		
		String text = file.getText()
		String[] lines = text.split('\n')
		for(int i = 0 ; i < lines.length; i++){
			RevCommit commit = blame.getSourceCommit(i);
			String line = ''
			if( commit.equals(left) ){
				leftIndexes.add(i)
				if(  ( !this.isIdenticalLine(i, identicalLines) ) ){
					line = this.LEFT_SEPARATOR + lines[i] 
				}else{
					line = lines[i] 
				}

			}else if( commit.equals(right)){
				rightIndexes.add(i)
				if(  ( !this.isIdenticalLine(i, identicalLines) ) ){
					line = this.RIGHT_SEPARATOR + lines[i] 
				}else{
					line = lines[i] 
				}

			}else{

				line = lines[i] 

			}

			result = result + line + '\n'
		}
		
		if((leftIndexes.size() == 0 || rightIndexes.size() == 0) && identicalLines.size() > 0){
			result = Blame.NOT_A_PREDICTOR;
		}
		
		return result
	}

	private boolean isIdenticalLine(int index, List<Integer> identicalLines){
		boolean isIdenticalLine = false
		int i = 0
		while(!isIdenticalLine && i < identicalLines.size() ){
			int r = identicalLines.getAt(i).intValue()
			if(index == r){
				isIdenticalLine = true
			}else{
				i++
			}

		}
		return isIdenticalLine
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

	def resetCommand(git, ref){
		ResetCommand resetCommand = git.reset()
		resetCommand.setMode(ResetType.HARD)
		resetCommand.setRef(ref)
		Ref resetResult = resetCommand.call()
		println "Reseted sucessfully to: " + resetResult.getName()
	}

	public static void main (String [] args){
		File left = new File('/Users/paolaaccioly/Desktop/Teste/jdimeTests/left/Case.java')
		File base = new File('/Users/paolaaccioly/Desktop/Teste/jdimeTests/base/Case.java')
		File right = new File('/Users/paolaaccioly/Desktop/Teste/jdimeTests/right/Case.java')
		Blame blame = new Blame()
		String result = blame.annotateBlame(left, base, right)
		println result

		/*Integer x = new Integer(9)
		 println x.intValue()*/
	}
}
