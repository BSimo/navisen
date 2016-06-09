function sendAjax(uri, data, callback) {
	$.ajax({
		type: 'POST',
		url: '/ajax/' + uri,
		data: 'data=' + JSON.stringify(data),
		success: function(data, status) {
			if(typeof callback === "function")
				callback(data);
		}
	});
}

function getAjax(uri, callback) {
	$.ajax({
		type: 'GET',
		url: '/ajax/' + uri,
		success: function(data, status) {
			if(typeof callback === "function")
				callback(data);
		}
	});
}
