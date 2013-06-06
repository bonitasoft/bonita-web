var cookie = document.cookie;
var bosCookieValue = getCookie("bos_cookie");
var bosCookieObj = jQuery.parseJSON(bosCookieValue);

getDatePickerLocale(bosCookieObj);

function getDatePickerLocale(bosCookie) {
	var head= document.getElementsByTagName('head')[0]
	var oScript =  document.createElement("script");
	oScript.src = "scripts/includes/daterangepicker."+bosCookie._l+".js";
	oScript.type = "text/javascript";
	head.appendChild(oScript);
}

function getProfileId(){
	paramsMap = {}; 
	location.hash.replace(/([^&=]+)=?([^&]*)(?:&+|$)/g, function(match, key, value) {
		(paramsMap[key] = paramsMap[key] || []).push(value);
	});
	var profileId = paramsMap["_pf"];
	if(profileId) return profileId;
	else return null;
}
function removeReportStyle(){
	$("table, div, tr", $(".report")).each(function(){
		var elt = $(this); 
		if(elt.attr("style")){
			if(elt.attr("style").indexOf("background")!=-1
			){
				return;
			}
			$(this).removeAttr("style");
		}
	})
	$("#report-form-ctn").parent().removeAttr("style");
}
function reportDateRangePicker(localeDateFormat, prefix){
	$('#'+prefix+'from, #'+prefix+'to').daterangepicker({
		presetRanges: localeRange,
		presets: localePresets,
		dateFormat: localeDateFormat, 
		constrainDates: true, 
		onClose: function() {
			 setTimeout(function(){$('#report-form').submit();},500);
		}
	}).addClass("dateRangePickers");
	$("#report-form select").attr("onchange", "refreshReport(this.form, \""+localeDateFormat+"\", \""+prefix+"\")");
}

function hookReportFormSubmition(localeDateFormat, prefix){
	$(document).delegate("#report-form","submit",function(event){
		event.stopPropagation();
		refreshReport(this, localeDateFormat, prefix);
		return false;
	});
	
	
	$(document).delegate(".report .bonita_report_hyperlink a, .report area","click", function(event){
		event.stopPropagation();
		var urlToRefresh = this.href+"&locale="+bosCookieObj._l+"&_pf="+getProfileId();
		$.ajax({
			url: urlToRefresh,
			cache: false,
			async: true,
			success: function(refreshResponse){
				refreshResponse = forceImgRefresh(refreshResponse.substring(refreshResponse.indexOf("<div class=\"report\">"), refreshResponse.lastIndexOf("</div>")));
				$("div.report").html(refreshResponse);
				removeReportStyle();
				reportDateRangePicker(localeDateFormat, prefix);
				retrieveFieldsValues(urlToRefresh.split('?')[1], localeDateFormat);
			}
		});
		return false;
	});
	
}

function refreshReport(e, localeDateFormat, prefix){
	var reportForm = $(e);
	
	var urlRefresh = reportForm.attr("action");
	
	var toDate = $('#'+prefix+'to', reportForm);
	var fromDate = $('#'+prefix+'from', reportForm);
	
	var toDateVal = toDate.val();
	var fromDateVal = fromDate.val();
	
	var fromDateObj = Date.parse(fromDateVal, localeDateFormat);
	var toDateObj =  Date.parse(toDateVal, localeDateFormat);
	
	fromDate.val(fromDateObj.getTime());// 00:00:00
	toDate.val(toDateObj.getTime()+(24*3600*1000)-1000);// 23:59:59
	
	var params = reportForm.serialize();
	
	toDate.val(toDateVal);
	fromDate.val(fromDateVal);
	
	$.ajax({
		beforeSend: function() {
			$("div.report").html("<div id=\"initloader\">" +
		            "<div class=\"loader\">" +
		            "<img src=\"images/loader.gif\" />" +
		            "</div>" +
		            "</div>");
		 },
		url: urlRefresh,
		data: params+"&locale="+bosCookieObj._l+"&_pf="+getProfileId(),
		cache: false,
		async: true,
		success: function(refreshResponse){
			refreshResponse = forceImgRefresh(refreshResponse.substring(refreshResponse.indexOf("<div class=\"report\">"), refreshResponse.lastIndexOf("</div>")));
			$("div.report").html(refreshResponse);
			removeReportStyle();
			reportDateRangePicker(localeDateFormat, prefix);
			retrieveFieldsValues(params, localeDateFormat);
		}
	});
}

function retrieveFieldsValues(params, localeDateFormat){
	map = {};
	params.replace(/([^&=]+)=?([^&]*)(?:&+|$)/g, function(match, key, value) {
		(map[key] = map[key] || []).push(value);
	});
	$("div.report #report-form :input").each(function(){
		var field = $(this);
		var uriValue = decodeURIComponent(map[field.attr("name")]);
		if(uriValue){
			if(field.is("input, textarea") ){
				if(field.is(".dateRangePickers")){
					if(uriValue %1 == 0){
						uriValue =  $.datepicker.formatDate(localeDateFormat, new Date(parseInt(uriValue)));
					}else{
						uriValue = Date.parseExact(uriValue, localeDateFormat); // if the date is returned in localized format instead timestamp
					}
					field.val(uriValue);
				}else{
					field.val(uriValue);
				}
			}else if(field.is("select")){
				field.prop("selectedIndex", $("option[value=\""+uriValue+"\"]", field).prop("index"));
			}
		}
	});
}

function getCookie(c_name){
	var c_value = document.cookie;
	var c_start = c_value.indexOf(" " + c_name + "=");
	if (c_start == -1){
		c_start = c_value.indexOf(c_name + "=");
	}
	if (c_start == -1){
		c_value = null;
	}else{
		c_start = c_value.indexOf("=", c_start) + 1;
		var c_end = c_value.indexOf(";", c_start);
		if (c_end == -1){
			c_end = c_value.length;
		}
		c_value = unescape(c_value.substring(c_start,c_end));
	}
	return c_value;
}
function forceImgRefresh(ajaxResponse){
	var r = Math.random();
	var reg= /<img(.*)src=\"([^\s]*)\"(.*)([>|/>])/gi;
	return ajaxResponse.replace(reg, '<img$1src="$2&r='+r+'"$3$4');
}