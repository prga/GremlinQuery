package main
import java.util.ArrayList;


class GremlinProject {
	
	String name
	String url
	String graph
	ArrayList<MergeCommit> listMergeCommit
	
	public GremlinProject(){}
	
	public GremlinProject(String projectName, String projectRepo, String graphBase){
		this.name = projectName
		this.url = 'https://github.com/' + projectRepo + '.git'
		this.graph = graphBase
		this.listMergeCommit = new ArrayList<MergeCommit>()
	}
	
	
	
	public void setMergeCommits(mergeCommits){
		this.listMergeCommit = mergeCommits
	}

}
