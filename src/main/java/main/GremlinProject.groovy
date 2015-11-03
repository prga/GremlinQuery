package main
import java.util.ArrayList;


class GremlinProject {

	String name
	String url
	String graph
	ArrayList<MergeCommit> listMergeCommit
	int diffParents
	double diffParentsPercentages

	public GremlinProject(){}

	public GremlinProject(String projectName, String projectRepo, String graphBase){
		this.name = projectName
		this.url = 'https://github.com/' + projectRepo + '.git'
		this.graph = graphBase
		this.listMergeCommit = new ArrayList<MergeCommit>()
		this.diffParents = 0
	}

	public void computeDiffParentsPercentage(){
		this.diffParentsPercentages = 0

		int total = this.listMergeCommit.size()
		for(MergeCommit mc : this.listMergeCommit){
			if(mc.parentsAreDifferent){
				this.diffParents++
			}
		}

		if(total!=0){
			this.diffParentsPercentages = (diffParents/total) * 100
		}
	}


	public void setMergeCommits(mergeCommits){
		this.listMergeCommit = mergeCommits
	}

}
