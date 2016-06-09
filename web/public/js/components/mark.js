var curr_location = 2;
var Mark = {
	LeafMark: null,
	id:0,
	x:0,
	y:0,
	floor:null,
	title:'',
	description:'',
	init: function(id, x, y, title, description, floor=0){
		this.id = id;
		this.x = x;
		this.y = y;
		this.floor = floor;
		this.title = title;
		this.description = description;
		this.draw(this.x, this.y);
		this.describe();
		this.LeafMark
			.on('drag', function(e) {onMarkDrag(e);})
			.on('click', function(e) {onMarkClick(e);})
			.on('dragend', function(e) {onMarkDragend(e);});
	},
	draw: function(x, y){
		this.LeafMark = L.marker([x, y], {icon: LeafIcon_color, draggable: is_admin, id: this.id}).addTo(map);
		if (this.floor != curr_floor) {this.LeafMark.setOpacity(0);}
	},
	describe: function(){
		console.log('Mark n° : ' + this.id + ', at position : ' + this.x + ':' + this.y);
	},
	updatePos: function(){
		this.x = this.LeafMark._latlng.lat;
		this.y = this.LeafMark._latlng.lng;
	}
};
