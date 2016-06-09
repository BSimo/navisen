$('#displayVertex').on('click', function () {
	displayVertex($(this)[0].checked);//In map.js
});	
$('#displayEdges').on('click', function () {
	displayEdges($(this)[0].checked);//In map.js
});	
$('#displayPOI').on('click', function () {
	displayPOI($(this)[0].checked);//In map.js
});	
$('.admin_switch').leanModal({
      dismissible: false,
      opacity: 0,
      ready: function() {$('.lean-overlay')[0].style.display='none';}
  }
);
$('#exit_admin_panel').on('click', function (){
	$('#admin_panel_modal').closeModal();
	setTimeout(function(){	$('#admin_panel_modal')[0].style.display='none';}, 200);
});
$( document ).ready(function() {
	if (!$('#displayVertex').checked) {$('#displayVertex').checked=true;}
});
