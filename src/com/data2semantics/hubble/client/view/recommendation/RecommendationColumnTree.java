package com.data2semantics.hubble.client.view.recommendation;

import java.util.ArrayList;

import com.data2semantics.hubble.client.view.View;
import com.data2semantics.hubble.client.view.patientinfo.PatientInfo;
import com.data2semantics.hubble.client.view.patientlisting.PatientListing;
import com.data2semantics.hubble.shared.models.Recommendation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ColumnTree;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.NodeSelectedHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * Recommendations are shown as Miller Column tree, with recommendations, evidence summary and supporting evidence as the columns.
 * The RecommendationTreeNode are arranged following this structure, recommendations as top levels having evidence summary as children and 
 * each evidence in turns as children of evidence summary
 * @author wibisono
 *
 */
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
					getView().onLoadingFinish();
					loadResult(result);
				}

				});
			
		} catch (Exception e) {
			getView().onError("Failed retrieving patient details:<br/>" + e.getMessage());
		}
		
	}

	
	private void loadResult(ArrayList<Recommendation> result) {
		TreeNode rootNode = 	new RecommendationTreeNode(result);
		
		Tree recommendationTree = new Tree();
		recommendationTree.setModelType(TreeModelType.PARENT);
		recommendationTree.setNameProperty(RecommendationTreeNode.HEADER_PROPERTY);
		recommendationTree.setChildrenProperty(RecommendationTreeNode.CHILDREN_PROPERTY);
		recommendationTree.setRoot(rootNode);
	
		setData(recommendationTree);

		redraw();
	}
	
	private void initializeGrid() {
		setHoverWidth(300);
		setHeight(500);
		setMargin(20);
		setWidth(PatientInfo.RHS_WIDTH+PatientListing.WIDTH+40);
		setShowMultipleColumns(true);
		setFirstColumnTitle("Recommendations");
		setNodeIcon("icons/fugue/information-white.png");
		setFolderIcon("icons/fugue/information-white.png");
		setShowOpenIcons(false);  
	    setShowDropIcons(false);  
	    setClosedIconSuffix("");
	    setTitle("Reccommendations");
	    setShowHeaders(true);
	    ListGrid template = new ListGrid();
		template.setFixedRecordHeights(false);
		template.setWrapCells(true);
		setColumnProperties(template);
		
	}

	@Override
    public ListGrid getCustomColumnProperties(TreeNode node, int colNum) {
		ListGridField blankField = new ListGridField("uri","");
		blankField.setWidth(1);
		
		ListGridField uriField = new ListGridField("uri","");
		uriField.setType(ListGridFieldType.LINK);
		uriField.setLinkText("<img src='images/icons/glyphicons_222_share.png' width=20px>");
		uriField.setWidth(30);
        uriField.setTitle(" ");
        uriField.setAttribute("showHeader", "false");
        
		String bodyColumnTitle = "";
		
		if(colNum==0) bodyColumnTitle = "Recommendations";
		else
		if(colNum == 1) bodyColumnTitle = "Evidence Summary";
		else
		if(colNum == 2) bodyColumnTitle = "Supporting Evidence";
		
		ListGridField bodyField = new ListGridField("body",bodyColumnTitle);
        
        ListGrid customColumnPropertyTemplate = new ListGrid();
        customColumnPropertyTemplate.setShowHeader(true);
		customColumnPropertyTemplate.setFixedRecordHeights(false);
		customColumnPropertyTemplate.setWrapCells(true);
		
		customColumnPropertyTemplate.setFields(new ListGridField[]{blankField, uriField, bodyField});
		customColumnPropertyTemplate.setCanResizeFields(false);
		
		return customColumnPropertyTemplate;
    }

	public View getView(){
		return view;
	}
}
