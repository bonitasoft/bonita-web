/* jshint browser:true, jquery:true */
/* global localeRange:false, localePresets:false*/

var bonitasoft = (function(bonitasoft) {
	bonitasoft.locale = getCookie("BOS_Locale") || "en";
	return bonitasoft;
}(bonitasoft || {}));

$.getScript("scripts/includes/daterangepicker." + bonitasoft.locale + ".js");

function getProfileId() {
    var paramsMap = {};
    location.hash.replace(/([^&=]+)=?([^&]*)(?:&+|$)/g, function (match, key, value) {
        (paramsMap[key] = paramsMap[key] || []).push(value);
    });
    var profileId = paramsMap._pf;
    if (profileId) return profileId;
    else return null;
}

function removeReportStyle() {
    $("table, div", $(".report")).each(function () {
        var elt = $(this);
        if (elt.attr("style")) {
            if (elt.attr("style").indexOf("background") != -1) {
                return;
            }
            $(this).removeAttr("style");
        }
    });
    $("#report-form-ctn").parent().removeAttr("style");
}

function reportDateRangePicker(localeDateFormat, prefix) {
    $(".ui-daterangepickercontain").remove();
    try {
        $('#' + prefix + 'from, #' + prefix + 'to').daterangepicker({
            presetRanges: localeRange,
            presets: localePresets,
            dateFormat: localeDateFormat,
            constrainDates: true,
            onClose: function () {
                setTimeout(function () {
                    $('#report-form').submit();
                }, 500);
            }

        }).addClass("dateRangePickers");
    } catch (err) {

    }

    $("#report-form select").attr("onchange", "refreshReport(this.form, \"" + localeDateFormat + "\", \"" + prefix + "\")");
}

function hookReportFormSubmition(localeDateFormat, prefix) {
    $(document).delegate("#report-form", "submit", function (event) {
        event.stopPropagation();
        refreshReport(this, localeDateFormat, prefix);
        return false;
    });


    $(document).delegate(".report .bonita_report_hyperlink a, .report area", "click", function (event) {
        event.stopPropagation();
        event.preventDefault();
        var urlToRefresh = this.href + "&locale=" + bonitasoft.locale + "&_pf=" + getProfileId();
        $.ajax({
            url: urlToRefresh,
            cache: false,
            async: true,
            success: function (refreshResponse) {
                refreshResponse = forceImgRefresh(refreshResponse.substring(refreshResponse.indexOf("<div class=\"report\">"), refreshResponse.lastIndexOf("</div>")));
                $("div.report").html(refreshResponse);
                removeReportStyle();
                reportDateRangePicker(localeDateFormat, prefix);
                retrieveFieldsValues(urlToRefresh.split('?')[1], localeDateFormat);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                $("div.report").html("<p>" + errorThrown + "</p>");
            }
        });
        return false;
    });

}

function refreshReport(e, localeDateFormat, prefix) {
    var reportForm = $(e),
        parseLocale = localeDateFormat.replace("yyyy", "yy").replace("yy", "yyyy").replace("mm", "M"),
        urlRefresh = reportForm.attr("action"),
        toDate, fromDate, toDateVal, fromDateVal, fromDateObj;


    try {
        toDate = $('#' + prefix + 'to', reportForm);
        fromDate = $('#' + prefix + 'from', reportForm);

        toDateVal = toDate.val();
        fromDateVal = fromDate.val();

        var toDateObj = Date.parseExact(toDateVal, parseLocale);
        fromDateObj = Date.parseExact(fromDateVal, parseLocale);

        fromDate.val(fromDateObj.getTime()); // 00:00:00
        toDate.val(toDateObj.getTime() + (24 * 3600 * 1000) - 1000); // 23:59:59
    } catch (err) {
        console.log("An error occured during date parsing");
    }

    var params = reportForm.serialize();

    try {
        toDate.val(toDateVal);
        fromDate.val(fromDateVal);
    } catch (err) {
        console.log("An error occurred while setting date values");
    }

    $.ajax({
        beforeSend: function () {
        	window.showLoader();
        },
        url: urlRefresh,
        data: params + "&locale=" + bonitasoft.locale + "&_pf=" + getProfileId(),
        cache: false,
        async: true,
        success: function (refreshResponse) {
        	window.hideLoader();
            refreshResponse = forceImgRefresh(refreshResponse.substring(refreshResponse.indexOf("<div class=\"report\">"), refreshResponse.lastIndexOf("</div>")));
            $("div.report").html(refreshResponse);
            removeReportStyle();
            reportDateRangePicker(localeDateFormat, prefix);
            retrieveFieldsValues(params, localeDateFormat);
        },
        error: function (jqXHR, textStatus, errorThrown) {
        	window.hideLoader();
            $("div.report").html("<p>" + errorThrown + "</p>");
        }
    });
}

function retrieveFieldsValues(params, localeDateFormat) {
    var map = {};
    params.replace(/([^&=]+)=?([^&]*)(?:&+|$)/g, function (match, key, value) {
        (map[key] = map[key] || []).push(value);
    });
    $("div.report #report-form :input").each(function () {
        var field = $(this);
        if(map[field.attr("name")] === undefined) {
            return;
        }
        var uriValue = decodeURIComponent(map[field.attr("name")]);
        if (uriValue) {
            if (field.is("input, textarea")) {
                if (field.is(".dateRangePickers")) {
                    if (uriValue % 1 === 0) {
                        uriValue = $.datepicker.formatDate(localeDateFormat, new Date(parseInt(uriValue, 10)));
                    } else {
                        uriValue = Date.parseExact(uriValue, localeDateFormat); // if
                        // the
                        // date
                        // is
                        // returned
                        // in
                        // localized
                        // format
                        // instead
                        // timestamp
                    }
                    field.val(uriValue);
                } else {
                    field.val(uriValue);
                }
            } else if (field.is("select")) {
                field.data('uriValue', uriValue);
                field.prop("selectedIndex", $("option[value=\"" + uriValue + "\"]", field).prop("index"));
            }
        }
    });
}

function getCookie(c_name) {
    var c_value = document.cookie;
    var c_start = c_value.indexOf(" " + c_name + "=");
    if (c_start == -1) {
        c_start = c_value.indexOf(c_name + "=");
    }
    if (c_start == -1) {
        c_value = null;
    } else {
        c_start = c_value.indexOf("=", c_start) + 1;
        var c_end = c_value.indexOf(";", c_start);
        if (c_end == -1) {
            c_end = c_value.length;
        }
        c_value = unescape(c_value.substring(c_start, c_end));
    }
    return c_value;
}

function forceImgRefresh(ajaxResponse) {
    var r = Math.random();
    var reg = /<img(.*)src=\"([^\s]*)\"(.*)([>|/>])/gi;
    return ajaxResponse.replace(reg, '<img$1src="$2&r=' + r + '"$3$4');
}
