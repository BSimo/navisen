$.urlParam = function(name){
  var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
  if (results == null) return null;
  return results[1] || 0;
}

var host = $.urlParam('host');
if(!host) host = document.location.hostname;
var port = $.urlParam('port');
if(!port) port = 8888;

var wsUrl = "ws://" + host + ":" + port;
var ws;

function WS_init() {
  ws = new WebSocket(wsUrl);
  ws.onopen = function(evt) { WS_onOpen(evt); };
  ws.onclose = function(evt) { WS_onClose(evt); };
  ws.onmessage = function(evt) { WS_onMessage(evt); };
  ws.onerror = function(evt) { WS_onError(evt); };
}

function WS_onOpen(evt) {
  console.log("[WS] Connected on " + wsUrl);
}

function WS_onClose(evt) {
  console.log("[WS] Disconnected from " + wsUrl);
}

function WS_onMessage(evt) {
  console.log("[WS] RECEIVE: " + evt.data);
  var data = JSON.parse(evt.data);
  if(!data.success) return ;

  resetPath();
  for(var i = 0; i < data.path.length - 1; i++) {
      drawLine(data.path[i], data.path[i+1]);
  }
}

function WS_onError(evt) {
  console.log("[WS] ERROR: " + evt.data);
}

function WS_isReady() {
  return ws.readyState == 1;
}

function WS_sendData(data) {
  if(WS_isReady()) {
    console.log("[WS] SEND: " + JSON.stringify(data));
    ws.send(JSON.stringify(data));
  }
}

WS_init();
