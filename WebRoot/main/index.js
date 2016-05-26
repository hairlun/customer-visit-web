Ext.namespace("Ext.haode");

Ext.haode.Control = function(args){
	Ext.apply(this, args);
	this.init();
};

Ext.haode.Control.prototype = {
		
	userName : '',
	
	version : '',
	
	app_name : '',
	
	copyright : '',
		
	viewport : null,
	
	init : function() {
		this.viewport = this.getViewport();
	},
	
	getViewport : function() {
		var viewport;
		if (this.viewport) {
			viewport = this.viewport;
		} else {
			var northPnl = this.getNorthPnl();
			var westPnl = this.getWestPnl();
			var centerPnl = this.getCenterPnl();
			viewport = new Ext.Viewport({
			    layout: 'border',
			    items: [northPnl, westPnl, centerPnl]
			});
		}
		return viewport;
	},
	
	getNorthPnl : function(){
    	var pnl;
    	if (this.viewport) {
    		pnl = this.getViewport().items.get(0);
    	} else {
    		pnl = new Ext.Panel({
    			region: 'north',
    			height: 75,
    			split:true,
    	        collapseMode: 'mini',
    	        header: true,
    	        border: false,
    	        margins: '0 0 -4 0',
    			contentEl: 'north-zone'
    		});
    	}
    	return pnl;
    },
    
    getWestPnl : function() {
    	var pnl;
    	if (this.viewport) {
    		pnl = this.getViewport().items.get(1);
    	} else {
    		pnl = new Ext.tree.TreePanel({
				title : '功能菜单',
				region : 'west',
				id : 'tree',
				width : 220,
				split : true,
				collapsible : true,
				enableDD : false,
				margins : '1 -3 0 0',
				cmargins : '1 2 0 0',
				bodyBorder : false,
				containerScroll : true,
				border : true,
				autoScroll : true,
				rootVisible : false,
				animate : true,
				loader : new Ext.tree.TreeLoader({
					dataUrl : 'menu.do'
				}),
				listeners : {
					afterrender : function() {
//						Ext.getCmp('tree').getSelectionModel().select(Ext.getCmp('tree').getRootNode().childNodes[0]);
					}
				}
    		});
			var root = new Ext.tree.AsyncTreeNode({
				text : "功能菜单",
				draggable : false,
				expanded : true,
				id : "_root"
			});
			pnl.setRootNode(root);
			
			var sm = pnl.getSelectionModel();
			sm.on("selectionchange", function(sm, node) {
				this.changeType(node);
			}, this);
    	}
    	return pnl;
    },
	
	getCenterPnl : function(){
    	var pnl;
    	if (this.viewport) {
    		pnl = this.getViewport().items.get(2);
    	} else {
    		var url = 'customer.do?action=forwardIndex';
//    		var url = "";
    		pnl = new Ext.Panel({
    			region: 'center',
    			margins : '1 0 0 0',
    			border: false,
    			width : 500,
    			heigh : 100,
    			html:'<iframe id="iframe" name="iframe" frameborder="0" src="' + url + '"></iframe>'
    		});
    	}
    	return pnl;
    },
    
    changeType : function(node) {
    	if (node == null || !node.isLeaf()) {
    		return;
    	}
    	Ext.getDom('iframe').src = node.attributes.src;
    }
    
};
