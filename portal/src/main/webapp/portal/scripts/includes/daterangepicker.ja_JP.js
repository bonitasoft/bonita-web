var localeRange = [{
				text : "今日",
				dateStart : "today",
				dateEnd : "today"
			}, {
				text : "最後の7日間",
				dateStart : "today-7days",
				dateEnd : "today"
			}, {
				text : "過去1カ月間",
				dateStart : function() {
					return Date.parse("today").moveToFirstDayOfMonth()
				},
				dateEnd : "today"
			}, {
				text : "本年度",
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
	specificDate : "具体的な期日",
	allDatesBefore : "All Dates Before",
	allDatesAfter : "All Dates After",
	dateRange : "日付範囲"
};