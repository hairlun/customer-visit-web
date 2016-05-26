Ext.namespace("HAODE");
/**
 * The HAODE.WebSocket class which wrap a Window.WebSocket Object. To create it,
 * the url property is required. You can use 'send' method to send message to
 * server. If you want close the socket can use close method
 * 
 * method: send(String), close() events: onMessage, onOpen, onClose
 * 
 * @param config
 * @returns {HAODE.WebSocket}
 */
HAODE.WebSocket = function(config) {
	Ext.apply(this, config);
	this.init();
};

HAODE.WebSocket.prototype = {

	// required
	url : null,

	websocket : null,

	init : function() {
		this.getWebSocket();
	},

	getWebSocket : function() {
		if (!this.url) {
			alert('Error: Cant\'t create WebSocket without url!');
			return;
		}
		if ('WebSocket' in window) {
			this.websocket = new WebSocket(this.url);
		} else if ('MozWebSocket' in window) {
			this.websocket = new MozWebSocket(this.url);
		} else {
			alert('Error: WebSocket is not supported by this browser.');
			return;
		}

		var t = this;
		this.websocket.onopen = function() {
			t._onOpen.apply(t, arguments);
		};
		this.websocket.onclose = function() {
			t._onClose.apply(t, arguments);
		}, this.websocket.onmessage = function() {
			t._onMessage.apply(t, arguments);
		};
	},

	// public
	send : function(message) {
		if (message) {
			this.websocket.send(message);
		}
	},

	// public
	close : function() {
		this.websocket.close();
	},

	// private
	_onMessage : function(evt) {
		if (this.onMessage) {
			this.onMessage.call(this.scope || this, evt);
		}
	},

	// private
	_onOpen : function() {
		if (this.onOpen) {
			this.onOpen.call(this.scope || this);
		}
	},

	// private
	_onClose : function() {
		if (this.onClose) {
			this.onClose.call(this.scope || this);
		}
	}

};
