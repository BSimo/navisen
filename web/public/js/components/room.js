function updateRoomModal(roomIndex, e){
	if (added_mark[roomIndex]) {
		var roomModal = $('#roomModal');
		$('#roomModal h4')[0].textContent = added_mark[roomIndex].title;
		$('#roomModal h4')[0].setAttribute('markID', added_mark[roomIndex].id);
		$('#roomModal p')[0].textContent = added_mark[roomIndex].description;
	}
	if (is_admin) {
		$('#roomModal .mark_admin').show();
		$('#roomModal')[0].style.height = '100vh';
		$('#roomModal')[0].style.maxHeight = '100vh';
		for (mark in added_mark) {
			if (roomIndex != mark) {
				var option = document.createElement('option');
				var option_text = document.createTextNode(mark+'');
				option.setAttribute('value', mark);
				option.appendChild(option_text);
				$('#mark_form select').html('');
				$('#mark_form select')[0].appendChild(option);
				$('#mark_form select').material_select();
			}
		}
		$('#qrcode')[0].style.display='flex';
		var qrcode = new QRCode(document.getElementById("qrcode"), {
			width : 100,
			height : 100
		});
		qrcode.makeCode(added_mark[roomIndex].id);
	}
}

$('#exit_point_config_panel').on('click', function(){
	$('#roomModal').closeModal();
});

$('#submit_point_config_panel').on('click', function(){
	ajaxSend(data);
	$('#roomModal').closeModal();
});

$('#textarea1').val('New Text');
$('#textarea1').trigger('autoresize');
$('#mark_desc')[0].value='';
$('')

function redraw(tag){
	tag.style.display='none';
	tag.offsetHeight;
	tag.style.display='block';
}

var qrcode = new QRCode(document.getElementById("qrcode"), {
	width : 200,
	height : 200
});
function createQR(markID){
	added_mark[markID].qr = qrcode.makeCode(added_mark[markID].id);
}
$(document).ready(function() {
	$('select').material_select();
});
function goThere(){
	$('#roomModal').closeModal();
	var data = {};
	data.from = curr_location;
	data.to = $('#roomModal h4')[0].getAttribute('markID');
	WS_sendData(data);
}
$('#go_btn').on('click', goThere);
