package com.data2semantics.hubble.client.view.recommendation;

import java.util.ArrayList;

import com.data2semantics.hubble.client.view.View;
import com.data2semantics.hubble.client.view.patientinfo.PatientInfo;
import com.data2semantics.hubble.client.view.patientlisting.PatientListing;
import com.data2semantics.hubble.shared.models.Recommendation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ColumnTree;
import com.smartgwt.client.widgets.tree.Tree;

public class RecommendationColumnTree extends ColumnTree {
	private View view;
	private String patientId;
	

	public RecommendationColumnTree(View view, String patientId) {
		this.view= view;
		this.patientId = patientId;
		initializeGrid();
		loadData();
	}


	private void loadData() {
		getView().onLoadingStart();
		try {
			getView().getServerSideApi().getRelevantRecommendations(patientId, new AsyncCallback< ArrayList<Recommendation>>() {
				@Override
				public void onFailure(Throwable caught) {
					
				}
				@Override
				public void onSuccess(ArrayList<Recommendation> result) {
					loadResult(result);
				}

				});
			
		} catch (Exception e) {
			getView().onError("Failed retrieving patient details:<br/>" + e.getMessage());
		}
		
	}

	private void loadResult(ArrayList<Recommendation> result) {
		Tree recommendationTree = new Tree();
		recommendationTree.setModelType(TreeModelType.CHILDREN);
		recommendationTree.setNameProperty(RecommendationTreeNode.NAME_PROPERTY);
		recommendationTree.setChildrenProperty(RecommendationTreeNode.CHILDREN_PROPERTY);
		recommendationTree.setRoot(new RecommendationTreeNode(result));
	
		setData(recommendationTree);

		redraw();
	}
	
	private void initializeGrid() {
		setHoverWidth(300);
		setHeight(500);
		setMargin(20);
		setWidth(PatientInfo.RHS_WIDTH+PatientListing.WIDTH+40);
		setShowHeaders(true);
		
		
	}
	
	public View getView(){
		return view;
	}
}
