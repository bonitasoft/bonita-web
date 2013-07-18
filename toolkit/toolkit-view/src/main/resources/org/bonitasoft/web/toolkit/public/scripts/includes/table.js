/**
 * Copyright (C) 2011 BonitaSoft S.A. BonitaSoft, 32 rue Gustave Eiffel - 38000
 * Grenoble This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 2.0 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

$(function() {

	$.tableKnownColumns = {
	    'checkbox' : [ 'checkboxes', 'checkbox' ],
	    'title' : [ 'title', 'name', 'firstname', 'firstName', 'first_name', 'lastName', 'last_name', 'displayname', 'displayName', 'display_name' ],
	    'description' : [ 'description', 'details' ],
	    'image' : [ 'image', 'thumbnail', 'icon', 'avatar' ],
	    'action' : [ 'action', 'actions' ]
	};

	// Plugin
	$.fn.prepareTable = function() {


		this.each(function() {
			var datatable = $(this);
			var table = $('.table', datatable);


			// Add select all
			// $('.thead .th:first', table).before('<div class="th
			// th_checkbox"><input type="checkbox" /></div>');

			// get semantics
			var semantics = new Array();
			$('.thead .th', table).each(function() {
				var sem = $(this).attr('class').split(' ');
				for ( var i = 0; i < sem.length; i++) {
					if (sem[i].length > 3 && sem[i].substring(0, 3) == 'th_') {
						semantics.push(sem[i].substring(3));
						return;
					}
				}
				semantics.push(undefined);
			});

			// Line counters
			var cnt_lines = 1, cnt_cols = 0;
			$('.thead,.tbody,.tfoot', table).each(function() {
				cnt_lines = 1;
				$('.tr', $(this)).each(function() {
					var line = $(this);
					line.addClass(cnt_lines % 2 == 0 ? 'even' : 'odd').addClass('tr_' + (cnt_lines++));

					cnt_cols = 0;
					$('.td, .th', $(this)).each(function() {
						var e = $(this);
						e.addClass(cnt_cols % 2 == 0 ? 'even' : 'odd').addClass((e.is('.td') ? 'td' : 'th') + '_' + semantics[cnt_cols++]).addClass((e.is('.td') ? 'td' : 'th') + '_' + (cnt_cols));

						// Col types
						for ( var cat in $.tableKnownColumns) {
							for ( var i = 0; i < $.tableKnownColumns[cat].length; i++) {
								if (e.is('.td_' + $.tableKnownColumns[cat][i])) {
									e.addClass('td_type_' + cat);
									break;
								} else if (e.is('.th_' + $.tableKnownColumns[cat][i])) {
									e.addClass('th_type_' + cat);
									break;
								}
							}
						}

					});
					$('.td:last', line).addClass('td_last');
					$('.th:last', line).addClass('th_last');
				});
				$('.tr:last', table).addClass('tr_last');
			});

			// Columns types
			$('.td, .th').each(function() {
				var $currentCell = $(this);

			})
		});

		return this;
	};


	// Register maker
	$.uiManager.addMaker(function(c) {

		$('.datatable', c).prepareTable();

		if (c.is('.datatable')) {
			c.prepareTable();
		}
	}, 1)


	/**
	 * JQuery+ maker : hides 2 nodata
	 */
	$.uiManager.addMaker(function(context) {
		if (context.is(".subtasks") || context.is(".tasks") ) {
			if (context.is(".empty")) {
				context.show();
				if (context.parent().find(".subtasks:visible").length > 1) {
					context.hide();
				}
				if (context.parent().find(".tasks:visible").length > 1) {
					context.hide();
				}
			} else {
				context.parent().find(".subtasks.empty").hide();
				context.parent().find(".tasks.empty").hide();
			}
		}
	});


});

function SortableItemTable() {}
SortableItemTable.getRow = function (ui){ return ui.item.get(0); }
SortableItemTable.getNextRow = function(elt){ return $(elt).next(".tr"); }

function DOMUtil() {}
DOMUtil.getApiid= function(elt){
	var apiidPrefix = "APPID";
	classList = elt.className.split(/\s+/);
	var apiid = null;
	for (var i = 0; i < classList.length && apiid==null; i++) {
		if (classList[i].substring(0, apiidPrefix.length) === apiidPrefix) {
			apiid = classList[i].substring(apiidPrefix.length + 1);
		}
	}
	return apiid;
}
