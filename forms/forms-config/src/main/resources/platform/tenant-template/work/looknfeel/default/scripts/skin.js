function overrideBrowserNativeInputs() {
	$('input').customInput();
	$(".bonita_form_field.list_container select").each(function(){
		resizeSelect($(this).parent());
	});
}

function forceResizeSelect(e){
	$(e).removeClass("resized");
	resizeSelect(e);
}

function resizeSelect(f){
	var listContainer = $(f);
	if(listContainer.hasClass("resized")){
		return;
	}
	var arrowBgWidth = 35;
	var paddingRight = 10;
	var containerWidth = listContainer.outerWidth();
	var list = listContainer.find('select:first');
	var selectedOption = listContainer.find('select:first').prop("selectedIndex"); // backup the selected index

	//get the label
	var label = listContainer.siblings(".bonita_form_label:first");
	if(label.length == 0) listContainer.siblings(".label:first");
	var maxAvailable = 0;
	//if label position is top or bottom the max width of the select container will be the label width (100% of available space in the form width)
	if(label.hasClass("bonita_form_label_top") || label.hasClass("bonita_form_label_bottom")){
		maxAvailable = label.outerWidth();
	}
	//else if label position is left or right the max width of the select container will be 66% of available space in the form width because the label takes 33%
	else{
		maxAvailable = label.outerWidth() * 2;
	}
	
	// remove css width to calculate max size
	list.css("width","100%");
		
	// init vars to search the longer string in options
	var lengths = Array();
	var maxI = 0;
	var max = 0;
	
	// retreive max characters and index in options value
	list.find('option').each(function(index, element){
		if(index==0 || $(this).text().length > max){
			maxI = index;
			max = $(this).text().length;
		}
	});
	// select the option with the highest number of character
	list.prop("selectedIndex",maxI);
	
	//get the size of the highest option
	var listWidth = list.outerWidth();
	
	// reselect the initial option
	list.prop("selectedIndex",selectedOption);
	
	//set the with of the select to the max select width including the arrow width	
	list.outerWidth(listWidth+ arrowBgWidth*2);
	
	//if the options has value
	if(listWidth>0){
		// set the container width to list width including arrow width & padding if the max available width is not reached
		if(maxAvailable>listWidth+arrowBgWidth+paddingRight){
		listContainer.css("width", listWidth+arrowBgWidth+paddingRight);
		}else{
		//if the width is higher set to max available width
		listContainer.css("width", maxAvailable);
		}
	}else{
		// if there is no option or options has no value, set only the arrow width
		listContainer.css("width", arrowBgWidth);
	}
	listContainer.addClass("resized");
}