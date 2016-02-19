package main

import java.util.ArrayList;

import org.codehaus.groovy.antlr.Main;
import com.tinkerpop.blueprints.impls.tg.*
import com.tinkerpop.blueprints.*
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph
import com.tinkerpop.gremlin.groovy.Gremlin
import com.tinkerpop.gremlin.groovy.GremlinGroovyPipeline
import com.tinkerpop.gremlin.pipes.transform.PropertyMapPipe
import com.tinkerpop.gremlin.*
import com.tinkerpop.pipes.util.StartPipe;

import org.neo4j.management.impl.*

public class NormalizedGremlinQuery {
	
	ArrayList<MergeCommit> allCommitsList
	
	Graph graph
	
	public NormalizedGremlinQuery(String path){
		
		this.allCommitsList = new ArrayList<MergeCommit>()
		
		//initiate gremlin
		Gremlin.load()
		
		/*query all commits' SHAs and their parents and save t
		 * them on local list */
		this.setAllCommits(path)
	}
	
	public void shutdownExistingGraph(){
		this.graph.shutdown()
	}
	
	public void setAllCommits(String path){
		
		//initiates the graph instance
		this.setGraph(path)
		
		//get a list with the project commits' SHAs
		ArrayList<String> allCommits = this.getAllCommitsSHAs()
		
		//set parents shas
		this.setCommitsParents(allCommits)
	}
	
	public void setGraph(String path){
		
		this.graph = new Neo4jGraph(path)
	}
	
	
	public ArrayList<String> getAllCommitsSHAs (){
		ArrayList<String> results = new ArrayList<String>()
		
		def mergeCommits = this.graph.V.map.filter{it._type == "COMMIT" }.sort{it.date}
		
		for(commit in mergeCommits){
			
			String[] result = [GremlinQuery.auxGetSha(commit.toString()), GremlinQuery.getDate(commit.toString())];
			results.add(result)
			
		}
		
		return results
	}
	
	public void setCommitsParents(ArrayList<String> commits){
		
		for(commit in commits){
			
			//intantiates commits
			MergeCommit mc = new MergeCommit()
			String sha = commit[0]
			mc.sha = sha
			
			/*get existing parents and set them accordingly
			 * if it is a normal commit it has only one parent. 
			 * Conversely, if it is a merge commit, it has two 
			 * parents*/
			ArrayList<String> parents = this.getParentsSha(sha)
			
			if(!parents.empty){
				mc.parent1 = parents.getAt(0)
			}else{
				mc.parent1 = ''
			}
			
			if(parents.size > 1){
				mc.parent2 = parents.getAt(1)
			}else{
				mc.parent2 = ''
			}
			
			// set commit date
			if(!commit[1].equals(""))
			{
				mc.date = new Date((long) commit[1].toLong() * 1000)
			}
			
			//add the commit to the project list
			this.allCommitsList.add(mc)
		}
	}
	
	public ArrayList<String> getParentsSha(String shaSon){
		
		def commit = this.graph.idx("commit-idx").get("hash", shaSon).first()
		def parentsTemp = commit.outE('COMMIT_PARENT').sort{it.date}
		ArrayList<String> parentsSha = new ArrayList<String>()
		
		for(parent in parentsTemp){
			
			
			String id = GremlinQuery.auxGetParentsID(parent.toString())
			
			def parentCommit = this.graph.v(id).map
			
			for(p in parentCommit){
				
				parentsSha.add(GremlinQuery.auxGetSha(p.toString()))
			}
			
			
		}
		
		return parentsSha
	}
	
	public static void main (String[] args){
		NormalizedGremlinQuery ngq = new NormalizedGremlinQuery('/Users/paolaaccioly/Documents/Doutorado/workspace_fse/gitminer/TGMgraph.db')
		ngq.shutdownExistingGraph()

	}
}
