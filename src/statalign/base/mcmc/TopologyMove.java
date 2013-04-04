package statalign.base.mcmc;

import statalign.base.Tree;
import statalign.base.Utils;
import statalign.base.Vertex;
import statalign.mcmc.McmcModule;
import statalign.mcmc.McmcMove;

public class TopologyMove extends McmcMove {

	Tree tree = null;
	int vnum;
	Vertex nephew;
	Vertex uncle;
	
	public TopologyMove (McmcModule m, String n) {
		owner = m;
		name = n;
		autoTune = false;
	}

	@Override
	public void copyState(Object externalState) {
		if (externalState instanceof Tree) {
			if (tree == null) {
				tree = (Tree) externalState;
			}
		}
		else {
			throw new IllegalArgumentException("TopologyMove.copyState must take an argument of type Tree.");
		}
		vnum = tree.vertex.length;
		if (vnum <= 3) { // then we can't do the nephew-uncle swap
			return;
		}
		
		int rnd = Utils.generator.nextInt(vnum);
		int vertId = tree.getTopVertexId(rnd);
		// Need to choose a valid nephew, i.e. not the root or either of its children
		while (vertId >= 0) { // while nephew is invalid
			rnd = Utils.generator.nextInt(vnum); // select a new nephew
			vertId = tree.getTopVertexId(rnd);
		}
		

		nephew = tree.vertex[rnd];
		uncle = nephew.parent.brother();
		((CoreMcmcModule) owner).getModelExtMan().beforeTreeChange(tree, nephew);		
		// Should also do a beforeAlignChange here, but not obvious what to pass
		// as the selectedRoot argument.
		// The rest of the state copying is handled inside the Vertex
	}
	@Override
	public double proposal(Object externalState) {
		if(Utils.DEBUG){
			System.out.println("Before:");
			System.out.println("Likelihood:");
			System.out.println(owner.curLogLike);
			double[] params = tree.getState().indelParams;
			for(int i = 0; i < params.length; i++)
				System.out.println(params[i]);
			String[] fullAlign = tree.getState().getFullAlign();
			for(int i = 0; i < fullAlign.length; i++)
				System.out.println(fullAlign[i]);
			String printTree = tree.printedTree();
			printChildren(tree.root);
			printEdges(tree.root);
			System.out.println(printTree);
			System.out.println(tree.root.indelLogLike);
			System.out.println(tree.root.orphanLogLike);
			System.out.println(Utils.calcEmProb(tree.root.first.seq, tree.substitutionModel.e));
			System.out.println(Utils.calcEmProb(tree.root.first.next.seq, tree.substitutionModel.e));
		}
		double logProposalRatio = nephew.fastSwapWithUncle();
		if(Utils.DEBUG){
			System.out.println("Proposed:");
			String[] fullAlign = tree.getState().getFullAlign();
			for(int i = 0; i < fullAlign.length; i++)
				System.out.println(fullAlign[i]);
			String printTree = tree.printedTree();
			System.out.println(printTree);
			printChildren(tree.root);
			printEdges(tree.root);
			System.out.println("logProposalRatio:");
			System.out.println(logProposalRatio);
		}
		// Below is another version, slow and slightly better mixing
		// double logProposalRatio = nephew.swapWithUncleAlignToParent();
		return logProposalRatio;
	}
	
	public void printChildren(Vertex v){
		if(v.left != null){
			System.out.println(v.index + " " + v.left.index);
			System.out.println(v.index + " " + v.right.index);
			printChildren(v.left);
			printChildren(v.right);
		}
	}

	public void printEdges(Vertex v){
		System.out.println(v.index + " " + v.edgeLength);
		if(v.left != null){
			printEdges(v.left);
			printEdges(v.right);
		}
	}
	
	
	@Override
	public double logPriorDensity(Object externalState) {
		return 0.0;
	}
	@Override
	public void updateLikelihood(Object externalState) {
		owner.setLogLike(((CoreMcmcModule) owner).getModelExtMan().logLikeTreeChange(tree, nephew));
		if(Utils.DEBUG){
			System.out.println("Proposed Likelihood:");
			System.out.println(owner.curLogLike);
		}
	}
	@Override
	public void restoreState(Object externalState) {
		uncle.fastSwapBackUncle();
		// If using the alternative move:
        // uncle.swapBackUncleAlignToParent();
	}
	
	@Override
	public void afterMove(Object externalState) {
		((CoreMcmcModule) owner).getModelExtMan().afterTreeChange(tree,lastMoveAccepted ? uncle : nephew,lastMoveAccepted);
		// Should also do an afterAlignChange here, but not obvious what to pass
		// as the selectedRoot argument.
		
		if (Utils.DEBUG) {
			tree.checkPointers();
		}
	}
	
	 
	
}
