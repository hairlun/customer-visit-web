Ext.namespace("Ext.haode");

Ext.haode.Control = function(args){
	Ext.apply(this, args);
	this.init();
};

Ext.haode.Control.prototype = {
		
	viewport : null,
	
	qrcode : '',
	
	timest : '',
	
	init : function() {
		this.viewport = this.getViewport();
	},

    getViewport : function(){
    	var viewport;
    	if (this.viewport) {
    		viewport = this.viewport;
    	} else {
    		var centerPnl = this.getCenterPnl();
    		var items = [centerPnl];
    		viewport =  new Ext.Viewport({
                layout: 'border',
                items: items
            });
    	}
    	return viewport;
    },
    
    getCenterPnl : function(){
    	var pnl;
    	if (this.viewport) {
    		pnl = this.getViewport().items.get(0);
    	} else {
    		pnl = new Ext.form.FormPanel({
//    			border: false,
    			region : 'center',
    			autoScroll: false,
    			labelWidth : 65,
    			labelAlign : 'right',
    			height : 300,
    			bodyStyle : 'padding : 100px; 130px;',
    			defaults : {
    				width : 200
    			},
    			items : [{
    				fieldLabel : '二维码开关',
    				id : 'qrcode',
    				name : 'qrcode',
    				xtype : 'combo',
    				mode : 'local',
    				triggerAction : 'all',
    				value : this.qrcode,
    				editable : false,
    				valueField : 'data',
    				displayField : 'text',
    				store : new Ext.data.SimpleStore({
    					fields : ['data', 'text'],
    					data : [["0", '关'], ["1", '开']]
    				})
    			}, {
    				fieldLabel : '时间戳开关',
    				id : 'timest',
    				name : 'timest',
    				xtype : 'combo',
    				mode : 'local',
    				triggerAction : 'all',
    				value : this.timest,
    				editable : false,
    				valueField : 'data',
    				displayField : 'text',
    				store : new Ext.data.SimpleStore({
    					fields : ['data', 'text'],
    					data : [["0", '关'], ["1", '开']]
    				})
    			}, {
    				xtype : 'panel',
    				height : 30,
    				border : false
    			}, {
    				xtype : 'panel',
    				layout : 'column',
    				border:false,
    				width : 273,
    				items : [{
    					xtype : 'panel',
    					border : false,
    					columnWidth : .6,
    					html : '<br>'
    				}, {
    					columnWidth : .4,
    					xtype : 'button',
        				text : '修改',
        				align : 'right',
        				width : 120,
        				handler : this.update,
        				scope : this
    				}]
    			}]
//    			buttons : [{
//    				text : '修改',
//    				handler : this.update,
//    				scope : this
//    			}]
    		});
    	}
    	return pnl;
    },
    
    update : function() {
    	var qrcode = Ext.getCmp("qrcode").getValue();
    	var timest = Ext.getCmp("timest").getValue();
    
		Ext.haode.ajax({
			url : 'switch.do?action=configSwitch',
			params : {
				qrcode : qrcode,
				timest : timest
			},
			success : function(json, opts) {
				alert(json.myHashMap.msg);
			},
			scope : this
		});
    }
    
};
