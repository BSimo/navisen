var curr_floor = 0;

/********************IMG********************/
var map = L.map('image-map', {
	minZoom: 1,
	maxZoom: 4,
	center: [0, 0],
	zoom: 1,
	crs: L.CRS.Simple
});
var w = 2000,//TO DO adapt size with img
h = 1500,
url = 'img/map/isen/'+curr_floor+'.png';

var southWest = map.unproject([0, h], map.getMaxZoom()-1);
var northEast = map.unproject([w, 0], map.getMaxZoom()-1);
var bounds = new L.LatLngBounds(southWest, northEast);

var LeafImage = L.imageOverlay(url, bounds).addTo(map);
map.setMaxBounds(bounds);

function changeLeafImage() {
	LeafImage.setUrl('img/map/isen/'+curr_floor+'.png');
}

/********************MARK STYLE********************/
var LeafIcon_size_factor = 16;
var LeafIcon = L.Icon.extend({
	options: {
		shadowUrl: 'leaf-shadow.png',
		iconSize:     [LeafIcon_size_factor, LeafIcon_size_factor],
		shadowSize:   [LeafIcon_size_factor, LeafIcon_size_factor],
		iconAnchor:   [(LeafIcon_size_factor/2), (LeafIcon_size_factor/2)],
		shadowAnchor: [(LeafIcon_size_factor/2), (LeafIcon_size_factor/2)],
		popupAnchor:  [-3, -76]
	}
});
var greenIcon = new LeafIcon({iconUrl: 'img/map/green_dot.png',shadowUrl: 'img/map/shadow_dot.png'}),
blueIcon = new LeafIcon({iconUrl: 'img/map/blue_dot.png',shadowUrl: 'img/map/shadow_dot.png'}),
purpleIcon = new LeafIcon({iconUrl: 'img/map/purple_dot.png',shadowUrl: 'img/map/shadow_dot.png'});
redIcon = new LeafIcon({iconUrl: 'img/map/red_dot.png',shadowUrl: 'img/map/shadow_dot.png'});
upIcon = new LeafIcon({iconUrl: 'img/icons/up_dot.png',shadowUrl: 'img/map/shadow_dot.png'});
downIcon = new LeafIcon({iconUrl: 'img/icons/down_dot.png',shadowUrl: 'img/map/shadow_dot.png'});


/********************ADD MARK********************/
var LeafIcon_color = purpleIcon;
var added_mark = [];
var added_path = [];
var poi = [1, 2, 4, 6, 9, 11, 37, 36, 23, 22, 25, 24, 26, 27, 30, 33, 34];


getAjax('retrievePoints', function(data) {
  $.each(data.points, function(index, value) {
    added_mark[value.point_id] = Object.create(Mark);
    added_mark[value.point_id].init(value.point_id, value.point_pos_y, value.point_pos_x, 'Point n°' + value.point_id, value.point_description, value.floor_id - 1);

		/* --- Pour la présenation */
		if($.inArray(+value.point_id, poi) == -1)
			added_mark[value.point_id].LeafMark.setOpacity('0');

	});

	$.each(data.neighbors, function(index, value) {
		if(is_admin) {
			drawLine(value.point_id, value.neighbor_id);
		}
	});
});

/********************MAP EVENT HANDLE********************/
function onMapClick(e) {
	if(is_admin && $('.edit_points')[0].checked) {
    sendAjax('newPoint', {"floor": curr_floor + 1, "y": e.latlng.lat, "x": e.latlng.lng}, function(data) {
      if(data.type == "success") {
        added_mark[data.point_id] = Object.create(Mark);
        added_mark[data.point_id].init(data.point_id, e.latlng.lat, e.latlng.lng, '', '', curr_floor);
				onMarkClick(e);
      }
    });
	}
}
map.on('click', onMapClick);

function onMarkDrag(e){
	var id = e.target.options.id;

	added_mark[id].updatePos();
	drawPath(added_mark[id]);
}

function onMarkDragend(e) {
	console.log('dragend');
	var id = e.target.options.id;

	added_mark[id].updatePos();
	sendAjax("updatePoint/" + id, {"y": added_mark[id].x, "x": added_mark[id].y}, function(data) {
		console.log(data);
	});
}

function onMarkClick(e){
	updateRoomModal(e.target.options.id);
	$('#roomModal').openModal();
}

/********************ADMIN CONTROL********************/
function displayVertex(state){
	for (var id in added_mark) {
		if(added_mark[id].LeafMark && (is_admin || (!is_admin && added_mark[id].title != null)))
			added_mark[id].LeafMark.setOpacity((added_mark[id].floor == curr_floor) ? (+state) : 0);
	}
}

function displayEdges(state){
	for (var id in added_path) {
		if(added_path[id]._path){added_path[id]._path.setAttribute("stroke-opacity", (added_mark[id.split(':')[0]].floor == curr_floor) ? (+state/2) : 0);}
	}
}

/********************ADD PATH********************/
function drawLine(sourceID, destID){
	pathID = sourceID + ':' + destID;
	reverse_pathID = destID + ':' + sourceID;
	if (pathID in added_path){
		map.removeLayer(added_path[pathID]);
		delete added_path[pathID];
	}
	if (reverse_pathID in added_path) {
		map.removeLayer(added_path[reverse_pathID]);
		delete added_path[reverse_pathID];
	}

	if (added_mark[sourceID].floor == added_mark[destID].floor) {
		added_path[pathID] = L.polygon([
			added_mark[sourceID].LeafMark._latlng,
			added_mark[destID].LeafMark._latlng
		]).addTo(map);
	} else if(added_mark[sourceID].floor < added_mark[destID].floor) {
		added_mark[sourceID].LeafMark.setIcon(upIcon);
		added_mark[destID].LeafMark.setIcon(downIcon);

	} else {
		added_mark[sourceID].LeafMark.setIcon(downIcon);
		added_mark[destID].LeafMark.setIcon(upIcon);

	}
}
function drawPath(mark){
	for(path in added_path) {
		pathIDs = path.split(':', 2);
		if(mark.id == pathIDs[0])
			drawLine(mark.id, pathIDs[1]);
		else if(mark.id == pathIDs[1])
			drawLine(mark.id, pathIDs[0]);
	}
}

for (var i = added_mark.length - 1; i >= 0; i--) {
	drawPath(added_mark[i]);
}

function resetPath() {
	for(path in added_path) {
		map.removeLayer(added_path[path]);
	}
}

/********************FLOOR HANDLE********************/
var slide_tag = $('#btn_slide')[0];
var curr_slide = slide_tag.lastElementChild;
curr_slide.style.backgroundColor = 'white';

function changeFloor(){
	curr_slide.style.backgroundColor = '#DD0613';
	curr_slide = this;
	curr_slide.style.backgroundColor = 'white';

	curr_floor = (slide_tag.childElementCount) - (Array.prototype.indexOf.call(slide_tag.children, curr_slide) + 1);
	displayVertex(true);
	displayEdges(true);
	if (curr_floor==0) {
		Materialize.toast('Rez-de-chaussée', 4000, 'rounded');
	}else{
		Materialize.toast('Etage n°'+(curr_floor), 4000, 'rounded');
	}
	changeLeafImage();
}

$('#btn_slide span').on('click', changeFloor);

$('#where_am_i_menu label').on('click', locateMe);
