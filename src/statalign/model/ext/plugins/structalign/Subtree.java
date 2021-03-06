package statalign.model.ext.plugins.structalign;

import java.util.ArrayList;

import statalign.base.Utils;
import statalign.base.Vertex;
import statalign.base.Tree;
import statalign.model.ext.plugins.structalign.Funcs;

public class Subtree {
	
	public static ArrayList<Integer> getSubtreeLeaves(Tree tree, Vertex subtreeRoot, int nLeaves) {
		ArrayList<Integer> subtreeLeaves = Funcs.collectLeaves(subtreeRoot);
		if(subtreeLeaves.contains(0)){	// check if subtree contains reference protein
			ArrayList<Integer> complement = new ArrayList<Integer>(0);
			for(int i = 0; i < nLeaves; i++)
				complement.add(i);
			for(int i = 0; i < subtreeLeaves.size(); i++) {
				complement.remove(subtreeLeaves.get(i));
			}
			subtreeLeaves = complement;
		}
//		if (Utils.DEBUG) {
//			System.out.print(" {");
//			for(int i = 0; i < subtreeLeaves.size(); i++){
//				int j = subtreeLeaves.get(i);
//				System.out.print(j);
//				if (i < (subtreeLeaves.size() - 1)) {
//					System.out.print(",");
//				}
//			}
//			System.out.println("} ");
//		}
		return subtreeLeaves;
	}
}
