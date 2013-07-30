$(function() {
	$.uiManager.addMaker(function(context) {
		if (context.is(".datatable")){
			var tablePannel = $(context.parent(".tablePannel"));
			if(tablePannel.length == 1){
				$(".table .tbody", context).sortable({
					items: ".tr:not(.tr_1)",
					cursor: "n-resize",
					update: function(event,ui){
						var row = SortableItemTable.getSortedElement(ui);
						window.updateIndex(DOMUtil.getAPIid(row), SortableItemTable.getProfilePageIndex(row, initialDomPageIndex));
					},
					start: function(event, ui){
						var row = SortableItemTable.getSortedElement(ui);
						initialDomPageIndex = SortableItemTable.getInitialProfilePageDomIndex(row);
					}
					
				});
				
				tablePannel.parent().sortable({
					cursor: "e-resize",
					items: ".tablePannel",
					update: function(event,ui){
						var col = SortableItemTable.getSortedElement(ui);
						window.updateIndex(DOMUtil.getAPIid(col), SortableItemTable.getProfileFolderIndex(col, initialDomFolderIndex))
					},
					
					start: function(event, ui){
						var col = SortableItemTable.getSortedElement(ui);
						initialDomFolderIndex = SortableItemTable.getInitialProfileFolderDomIndex(col);
					}
				});
			}
		}
	});
});
var initialDomFolderIndex;
var initialDomPageIndex;