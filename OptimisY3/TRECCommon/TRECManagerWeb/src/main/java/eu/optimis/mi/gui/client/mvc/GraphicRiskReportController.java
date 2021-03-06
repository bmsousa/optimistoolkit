/**
 *  Copyright 2013 University of Leeds
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package eu.optimis.mi.gui.client.mvc;

import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

import eu.optimis.mi.gui.client.MainEvents;
import eu.optimis.mi.gui.client.mvc.view.GraphicReportRiskDiagramView;
import eu.optimis.mi.gui.client.mvc.view.GraphicReportRiskView;

public class GraphicRiskReportController extends Controller {

	private GraphicReportRiskView reportView;
	private GraphicReportRiskDiagramView grdView;
	
	public GraphicRiskReportController(){
		 registerEventTypes(MainEvents.Init);
		 registerEventTypes(MainEvents.ReportGraphicRiskDiagram);
		 registerEventTypes(MainEvents.ReportGraphicCancel);
	}
	
	@Override
	public void handleEvent(AppEvent event) {
		 EventType type = event.getType();
		    if (type == MainEvents.Init) {
		      forwardToView(reportView, event);
		    }
		    if (type == MainEvents.ReportGraphicRiskDiagram){
		    	forwardToView(grdView, event);
		    }
		    if (type == MainEvents.ReportGraphicCancel){
		    	forwardToView(grdView, event);
		    }
	}
	
	 public void initialize() {
            
		 super.initialize();
		 reportView = new GraphicReportRiskView(this);
		 grdView = new GraphicReportRiskDiagramView(this);
		    
   }
}
