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
	$.uiManager.addMaker(function(context) {
		if (context.is(".datatable")){
			// hack for datatable where there is not a label after the checkbox
			// or radio
			var dataTable = context;
			$("input[type=checkbox],input[type=radio]", context).each(function(i){
				if($("label", $(this).parent()).length>0){
					return;
				}
				var input = $(this);
				var inputId = this.id;
				var forValue = "";
				if(inputId!= ""){
					forvalue = inputId;
				}
				label = $('<label for="'+forvalue+'" >&nbsp;</label>');
				label.click(function(e){
					e.stopPropagation();
				});
				
				input.parent().append(label);
				
			});
			$('input', context).customInput();
			
			$(".formentry.select .input", context).each(function(i,e){
				resizeSelect(e);
			});
			$(".formentry.select .input select", context).each(function(i,e){
				$(e).change(function(e){
					resizeSelect($(e).parent());
				});
			});
			
			//manage all lines checked label style
			$(context).bind("cssChange",function(){
				var checkAllCheckbox = $(".th_checkboxes input", context);
				if(checkAllCheckbox.prop('checked')){
					$("label", checkAllCheckbox.parent()).addClass("checked");
				}else{
					$("label", checkAllCheckbox.parent()).removeClass("checked");
				}
			});
			
			
		}
	});
});

function resizeSelect(f){
	//init vars
	var listContainer = $(f);
	var arrowBgWidth = 35;
	var paddingRight = 10;
	var browserElevatorWidth = 15;
	var containerWidth = listContainer.outerWidth();
	var maxAvailableWidth = 0;
	var listWidth = 0;

	var list = $("select", listContainer);
	list.css("width","");
	listContainer.css("width","");
	//get the label
	var label = listContainer.siblings(".bonita_form_label:first");

	// if the label is not found with the class bonita_form_label, let's try with portal class .label
	if (label.length == 0 && listContainer.siblings(".label").length == 1) {
		label = listContainer.siblings(".label:first");
	}
	
	//if label position is top or bottom the max width of the select container will be the label width (100% of available space in the form width)
	if (label.hasClass("bonita_form_label_top") || label.hasClass("bonita_form_label_bottom")) {
		maxAvailableWidth = label.outerWidth();
	}//else if label position is left or right the max width of the select container will be 66% of available space in the form width because the label takes 33%
	else {
		maxAvailableWidth = label.outerWidth() * 2;
	}

  var listClone = list.clone();
  
  listClone.appendTo(listContainer);
  listClone.css("width","auto");


	// transform unique choice select into a multiple to get the final size
	listClone.prop("multiple", true);

	//get the width
	listWidth = listClone.outerWidth();
	listClone.prop("multiple", true);
	listClone.remove();

	// revert to unique choice the select box
	list.prop("multiple", false);
	
	//if select has options
	if ($("option", list).length > 0) {
		list.outerWidth(listWidth + paddingRight + arrowBgWidth*2 + browserElevatorWidth);// arrow width is added twice because if we remove this width to the parent container, the container will not have enough space to display this arrow without text overlap
		var newListWidth = list.outerWidth();
		// set the container width to list width including arrow width & padding if the max available width is not reached
		if (newListWidth < maxAvailableWidth) {
			listContainer.css("width", newListWidth - arrowBgWidth);
		} else {
			//if the width is higher set to max available width
			listContainer.css("width", maxAvailableWidth);
		}
	} else {
		// if there is no option or options has no value, set only the arrow width
		list.outerWidth(arrowBgWidth * 2);
		listContainer.css("width", arrowBgWidth);
	}
}



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
$(function() {
	$.fn.bonitaSectionsInTabs = function() {
		$(this).each(function(i,e){
			var panelClass = "sections_panel";
			var tabListId = "tab_list";
			var currentClass = "current";
			var panelSelector = "."+panelClass;
			var d = document;
			var section = $(this);
			var tabTitle = $(">.header:first-child > H1",this);
			var parentBlock = section.parent();
			if($(panelSelector).length < 1){
				var panelSectionInTab = $("<div/>");
				panelSectionInTab.addClass(panelClass);
				var tabList = $("<ul/>");
				tabList.attr("id",tabListId);
				panelSectionInTab.append(tabList);
				parentBlock.prepend(panelSectionInTab);
			}
			
			var tabElement = $("<li/>");
			if(i==0){
				tabElement.addClass(currentClass);
				section.addClass(currentClass);
			}
			var tabLink = $("<a>"+tabTitle.text()+"</a>");
			tabElement.append(tabLink);
			tabTitle.parent().css("display","none");
			tabElement.click(function(){
				var currentIndex = $("li", $(panelSelector+" ul")).index(this);
				$(this).siblings().removeClass(currentClass);
				$(".tabSection").removeClass(currentClass);
				$(this).addClass(currentClass);
				$($(".tabSection").get(currentIndex)).addClass(currentClass);
			});
			$(panelSelector+" ul").append(tabElement);
		});
	};
	
	$.uiManager.addMaker(function(c) {
		$(".tabSection", c).bonitaSectionsInTabs();
	});
	
	
});


