var localeRange = [{
				text : "今日",
				dateStart : "today",
				dateEnd : "today"
			}, {
				text : "直近７日間",
				dateStart : "today-7days",
				dateEnd : "today"
			}, {
				text : "直近１ヶ月間",
				dateStart : function() {
					return Date.parse("today").moveToFirstDayOfMonth()
				},
				dateEnd : "today"
			}, {
				text : "今年",
				dateStart : function() {
					var w = Date.parse("today");
					w.setMonth(0);
					w.setDate(1);
					return w
				},
				dateEnd : "today"
			}, {
				text : "前月",
				dateStart : function() {
					return Date.parse("1 month ago").moveToFirstDayOfMonth()
				},
				dateEnd : function() {
					return Date.parse("1 month ago").moveToLastDayOfMonth()
				}
			} ];

var localePresets = {
	specificDate : "特定日",
	allDatesBefore : "指定日以前",
	allDatesAfter : "指定日以降",
	dateRange : "期間"
};
