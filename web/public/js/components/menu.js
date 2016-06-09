$('.button-collapse-left').sideNav({
    menuWidth: 300,
    edge: 'left',
    closeOnClick: true
  }
);

$('#btn_menu').on('click', function (){
	  $('.button-collapse-left').sideNav('show');
});

$(function(){
  $('.modal-trigger').leanModal();
});
