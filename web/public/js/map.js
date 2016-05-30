var can = $('#map');
var map_scale = 0.2;

function resizeCanvas() {
  can[0].width = window.innerWidth;
  can[0].height = window.innerHeight;
  can.drawLayers();
}
resizeCanvas();

window.addEventListener('resize', resizeCanvas, false);

can.drawImage({
  layer: true,
  name: 'map',
  draggable: true,
  source: 'img/map/isen/floor_1.png',
  x: can[0].width / 2, y: can[0].height / 2,
  scale: map_scale
});

// The ToolBox
can.drawRect({
  layer: true,
  groups: 'toolbox',
  fillStyle: "#000",
  opacity: 0.1,
  x: 24, y: 64,
  width: 48,
  height: 126
});
can.drawRect({
  layer: true,
  groups: 'toolbox',
  strokeStyle: "#000",
  x: 24, y: 64,
  width: 46,
  height: 126
});

can.drawImage({
  layer: true,
  name: 'zoom_in',
  groups: 'toolbox',
  source: 'img/icons/zoom_in.png',
  x: 24, y: 24,
  width: 32,
  height: 32,
  click: function(layer) {
    map_scale += 0.1;
    if(map_scale > 1) map_scale = 1;
    $(this).setLayer('map', {
      scale: map_scale
    }).drawLayers();
  }
});

can.drawImage({
  layer: true,
  groups: 'toolbox',
  source: 'img/icons/zoom_out.png',
  x: 24, y: 56,
  width: 32,
  height: 32,
  click: function(layer) {
    map_scale -= 0.1;
    if(map_scale < 0.1) map_scale = 0.1;
    $(this).setLayer('map', {
      scale: map_scale
    }).drawLayers();
  }
});

can.drawArc({
  layer: true,
  groups: 'toolbox',
  fillStyle: "green",
  x: 24, y: 92,
  radius: 12
});

can.drawArc({
  draggable: true,
  fillStyle: "green",
  x: 100, y: 100,
  radius: 10
});
