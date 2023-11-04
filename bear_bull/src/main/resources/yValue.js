if (o.getString('Report_Type') == 'DAILY') {
	if (o.getBoolean('Is_Dashboard')) {
		return DateUtils.formatDateTime(o.getDate('Date')); 
	} else {
		return DateUtils.formatDate(o.getDate('Date'));
	}

} else if (o.getString('Report_Type') == 'WEEKLY') {
	if (o.getBoolean('Is_Dashboard')) {
		return DateUtils.formatDate(DateUtils.getWeek(o.getDate('Date'))) + ' - ' + DateUtils.formatDateTime(o.getDate('Date')); 
	} else {
		return DateUtils.formatDate(o.getDate('Date'))  + ' - ' + DateUtils.formatDate(DateUtils.getEndOfTheWeek(o.getDate('Date')));
	} 
} else {
	return DateUtils.formatDate(o.getDate('Date'));
}