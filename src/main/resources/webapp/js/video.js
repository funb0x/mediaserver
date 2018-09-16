var canvas = document.createElement("canvas");
document.body.appendChild(canvas);
// Create h264 player
var loc = window.location, uri;
if (loc.protocol === "https:") {
	uri = "wss:";
} else {
	uri = "ws:";
}
uri += "//" + loc.host;
uri += loc.pathname + "socket/";
console.log(uri);
var wsavc = new WSAvcPlayer(canvas, "webgl", 1, 35);
wsavc.connect(uri);
//expose instance for button callbacks
window.wsavc = wsavc;