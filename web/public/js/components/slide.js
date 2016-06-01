var current_slide = $('#btn_slide span')[0];
current_slide.style.backgroundColor = 'white';

$('#btn_slide span').on('click', function () {
	current_slide.style.backgroundColor = 'red';
	current_slide = this;
	current_slide.style.backgroundColor = 'white';

	var parent = current_slide.parentNode;
	var index = Array.prototype.indexOf.call(parent.children, current_slide);
	//fadeInOut((index+1));
	//upNDown((index+1));

	Materialize.toast('Etage n°'+(index+1), 4000, 'rounded');
});

/*
function fadeInOut(index) {
	var img = $('#my_slider')[0].firstElementChild;
	img.style.opacity = '0.0';
	setTimeout(function(){
		img.src = 'img/slide/bg-' + index + '.jpg';
		img.style.opacity = '1.0';
	}, 500);
}

function upNDown(index) {
	var img = $('#my_slider')[0].firstChild;
	img.style.transform = 'translateY(-100%)';
	setTimeout(function(){
		img.src = 'img/slide/bg-' + index + '.jpg';
		img.style.transform = 'translateY(0%)';
	}, 500);
} */
