 $(document).ready(function(){
    // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
    $('#locateModaleBtn').leanModal();
  });

 $('#reader').html5_qrcode(function(data){
		$('#read')[0].textContent = data;
		curr_location = +data;
	},
 	function(error){
	},
    function(videoError){
		$('#read')[0].textContent = 'videoError';
	}
);
function locateMe(){
	added_mark[curr_location].LeafMark.setIcon(redIcon);
	temp_floor = ($('#btn_slide')[0].children.length - 1 - added_mark[curr_location].floor);
	$('#btn_slide')[0].children[temp_floor].click();
}
