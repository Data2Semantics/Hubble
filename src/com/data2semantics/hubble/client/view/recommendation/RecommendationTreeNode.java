package com.data2semantics.hubble.client.view.recommendation;

import java.util.ArrayList;

import com.data2semantics.hubble.shared.models.Evidence;
import com.data2semantics.hubble.shared.models.EvidenceSummary;
import com.data2semantics.hubble.shared.models.Recommendation;
import com.smartgwt.client.widgets.tree.TreeNode;

public class RecommendationTreeNode extends TreeNode {

	static final String CHILDREN_PROPERTY = "children";
	static final String HEADER_PROPERTY = "header";

	public RecommendationTreeNode(Evidence rec) {
		setAttribute("uri", rec.getUri());
		setAttribute("body", rec.getBody());
		setAttribute("header","Evidence");
		
	}

	public RecommendationTreeNode(EvidenceSummary rec) {
		setAttribute("uri", rec.getUri());
		setAttribute("body", rec.getBody());
		setAttribute("header","Evidence Summary");
	}

	public RecommendationTreeNode(Recommendation rec) {
		setAttribute("uri", rec.getUri());
		setAttribute("body",rec.getBody());
		setChildren(getRecommendationTreeNodeFromEvidenceSummaries(rec
				.getEvidenceSummaries()));
		
		setAttribute("header",rec.getBody());
		setAttribute("wrapCells",true);
	}

	public RecommendationTreeNode(ArrayList<Recommendation> result) {
		setAttribute("uri", "RootNode");
		setAttribute("body", "Recommendations");
		setAttribute("header","Recommendations");
		
		RecommendationTreeNode[] mainChildren = new RecommendationTreeNode[result
				.size()];
		for (int i = 0; i < result.size(); i++) {
			mainChildren[i] = new RecommendationTreeNode(result.get(i));
		}
		setChildren(mainChildren);
	}

	RecommendationTreeNode[] getRecommendationTreeNodeFromEvidenceSummaries(
			ArrayList<EvidenceSummary> es) {
		RecommendationTreeNode[] results = new RecommendationTreeNode[es.size()];

		for (int i = 0; i < es.size(); i++) {
			results[i] = new RecommendationTreeNode(es.get(i));
			results[i].setChildren(getRecommendationTreeNodeFromEvidences(es
					.get(i).getSuportingEvidences()));

		}

		return results;
	}

	private RecommendationTreeNode[] getRecommendationTreeNodeFromEvidences(
			ArrayList<Evidence> se) {

		RecommendationTreeNode[] results = new RecommendationTreeNode[se.size()];

		for (int i = 0; i < se.size(); i++) {
			results[i] = new RecommendationTreeNode(se.get(i));

		}

		return results;
	}

	public void setChildren(RecommendationTreeNode[] children) {
		setAttribute(CHILDREN_PROPERTY, children);
	}

	public RecommendationTreeNode[] getChildren() {

		return (RecommendationTreeNode[]) getAttributeAsObject(CHILDREN_PROPERTY);
	}
}
