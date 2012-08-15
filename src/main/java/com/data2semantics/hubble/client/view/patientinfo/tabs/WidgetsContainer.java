package com.data2semantics.hubble.client.view.patientinfo.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.data2semantics.hubble.client.view.View;
import com.data2semantics.hubble.shared.models.Snippet;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.TileRecord;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class WidgetsContainer extends TileGrid {
	private static class Tile {
		public static String URI = "uri";
		public static String SNIPPET = "snippetField";
	}
	private View view;
	private String patientId;
	private ArrayList<TileRecord> records = new ArrayList<TileRecord>();
	private HashMap<String, Snippet> snippets;
	public WidgetsContainer(View view, String patientId) {
		this.view = view;
		this.patientId = patientId;
		initializeGrid();
		drawRelevantSnippets();
	}
	
	
	public View getView() {
		return view;
	}
	
	private void initializeGrid() {
		setTileWidth(194);  
        setTileHeight(165);
        setHeight100();
        setCanReorderTiles(true);  
        setShowAllRecords(true);
        setShowEdges(false);
        setSelectionType(SelectionStyle.SINGLE);  
        setTileDragAppearance(DragAppearance.OUTLINE); 
        setAnimateTileChange(true);  
        setWidth100();
        DetailViewerField snippetField = new DetailViewerField(Tile.SNIPPET);
        setFields(snippetField);
        setAutoHeight();
        setOverflow(Overflow.VISIBLE);
        setWrapValues(true);

        addRecordClickHandler(new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				TileRecord record = getSelectedRecord();
				Snippet snippet = snippets.get(record.getAttribute(Tile.URI));
				getView().addSouth(new SnippetDetails(getView(), snippet));
			}
		});
		
        
	}
	
	private void drawRelevantSnippets() {
		try {
			getView().getRemoteService().getRelevantSnippets(patientId, getView().getEndpoint(), new AsyncCallback<HashMap<String, Snippet>>() {
				public void onFailure(Throwable e) {
					getView().onError(e.getMessage());
				}

				public void onSuccess(HashMap<String, Snippet> snippetsObject) {
					snippets = snippetsObject;
					for (Map.Entry<String, Snippet> entry : snippets.entrySet()) {
						Snippet snippet = entry.getValue();
						TileRecord record = new TileRecord();
						record.setAttribute(Tile.URI, entry.getKey());
						record.setAttribute(Tile.SNIPPET, snippet.getPrefix() + " <strong>" + snippet.getExact() + "</strong> " + snippet.getPostfix());
						records.add(record);
					}
					TileRecord[] recordsArray = new TileRecord[records.size()];
					records.toArray(recordsArray);
					setData(recordsArray);
				}
			});
		} catch (Exception e) {
			getView().onError(e.getMessage());
		}
	}
}
