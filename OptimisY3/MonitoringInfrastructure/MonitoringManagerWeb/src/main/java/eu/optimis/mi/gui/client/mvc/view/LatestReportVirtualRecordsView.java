/**

Copyright 2012 University of Stuttgart

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at 

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

**/

package eu.optimis.mi.gui.client.mvc.view;


import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import eu.optimis.mi.gui.client.MainEvents;
import eu.optimis.mi.gui.client.mvc.MainView;
import eu.optimis.mi.gui.client.userwidget.grid.LatestVirtualReportItemsPanel;


public class LatestReportVirtualRecordsView extends View {

	private LayoutContainer container;
	private LatestVirtualReportItemsPanel itemsGridPanel;

	public LatestReportVirtualRecordsView(Controller controller) {
		super(controller);
	}

	@Override
	protected void handleEvent(AppEvent event) {
		if (event.getType() == MainEvents.ReportVirtualIerms) {
			LayoutContainer wrapper = (LayoutContainer) Registry
					.get(MainView.CENTER_PANEL);
			wrapper.removeAll();
			wrapper.add(container);
			wrapper.layout();
			return;
		}
	}

	@Override
	protected void initialize() {
		container = new LayoutContainer();
		BorderLayout layout = new BorderLayout();
		layout.setEnableState(false);
		container.setLayout(layout);
		itemsGridPanel = new LatestVirtualReportItemsPanel();
		container
				.add(itemsGridPanel, new BorderLayoutData(LayoutRegion.CENTER));
	}

}
