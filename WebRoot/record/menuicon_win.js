//图标窗口
Ext.haode.MenuIconWin = Ext.extend(Ext.Window, {
	
	id : 'iconsWin',
	
	width : 575,
	
	height : 390,
	
	modal : true,
	
	layout : 'fit',
	
	bodyStyle : 'background-color:white;overflow-y:visible;',
	
	rid : '',
	
	title : '图片',
	
	initComponent : function() {
		
		var buttons = [{
//			xtype : 'button',
//			id : 'sub',
//			text : '确定',
//			tooltip : '双击选择图标',
//			handler : this.subFn,
//			scope : this
//		}, {
			id : 'cancel',
			text : '取消',
			handler : function() {
				this.close();
			},
			scope : this
		}];
		
//		var menu = new Ext.menu.Menu({
//			width : 80,
//			items : [{
//				text : '16 x 16',
//				handler : function() {
//					this.showBigCategory('16x16');
//				},
//				scope : this
//			}, {
//				text : '32 x 32',
//				handler : function() {
//					this.showBigCategory('32x32');
//				},
//				scope : this
//			}, {
//				text : '48 x 48',
//				handler : function() {
//					this.showBigCategory('48x48');
//				},
//				scope : this
//			}, {
//				text : '64 x 64',
//				handler : function() {
//					this.showBigCategory('64x64');
//				},
//				scope : this
//			}, {
//				text : '全&nbsp&nbsp部',
//				handler : function() {
//					this.showBigCategory('');
//				},
//				scope : this
//			}]
//		});
		
//		if (this.winAction == 'edit') {
//			this.title = '设置图标';
//			menu = null;
//		} else if (this.winAction == 'icons') {
//			this.title = '图标库';
//			buttons = null;
//		}
		
//		this.tbar = [{
//			id : 'menuIconType',
//			text : '图标类型',
//			menu : menu
//		},'-' , {
//			text : '上传图标',
//			iconCls : 'btn_page_add',
//			handler : this.upload,
//			scope : this
//		},'-' , {
//			text : '删除',
//			iconCls: 'btn_page_delete',
//			handler : this.deleteIcon,
//			scope : this
//		}];
		
		this.buttons = buttons;
		
		var store = new Ext.data.JsonStore({
			autoLoad: true,
		    url : 'record.do?action=showImages',
		    baseParams : {
		    	recordId : this.rid
		    },
		    root : 'rows',
		    fields : [
		        'iconId', 'name', 'url', 'category'
		    ]
		});

		var tpl = new Ext.XTemplate(
		    '<tpl for=".">',
		        '<div class="thumb-wrap">',
			        '<div class="thumb"><img src="{url}" title="{name}"></div>',
		        '</div>',
		    '</tpl>',
		    '<div class="x-clear"></div>'
		);
		
		this.items = new Ext.DataView({
			id : 'dataview',
	        store: store,
	        layout: 'fit',
	        tpl: tpl,
	        bodyBorder: false,
	        autoHeight:true,
	        multiSelect: true,
	        overClass:'x-view-over',
			itemSelector:'div.thumb-wrap',
			emptyText: ''
	    });

//		this.listeners = {
//			'afterrender' : function() {
//				if (this.winAction == 'edit') {
//					Ext.getCmp('menuIconType').hide();
//				}
//			}	
//		};
		
//		if (this.winAction == 'edit') {
//			Ext.getCmp('dataview').on('dblclick', function() {
//				this.subFn();
//			}, this);
//		}
//		
		Ext.haode.MenuIconWin.superclass.initComponent.apply(this, arguments);
		
	}
	
//	subFn : function() {
//		var records = Ext.getCmp('dataview').getSelectedRecords();
//		var record = records[0];
//		if (!record) {
//			alert('请选中一个图标!');
//			return;
//		} 
//		this.fireEvent('save', {iconId: record.get("iconId"), name:record.get("name")});
//		this.close();
//	},
	
//	deleteIcon : function() {
//		var tmp = Ext.getCmp('dataview').getSelectedRecords();
//		if (tmp == '' || tmp == null) {
//			alert('请选择一个图标!');
//			return;
//		}
//		var cf = window.confirm('确定删除此图标吗?');
//		if (!cf) {
//			return;
//		}
//		var iconIds = '';
//		for (var i = 0; i < tmp.length; i++) {
//			iconIds += (',' + tmp[i].get('iconId'));
//		}
//         Ext.haode.ajax({
//        	 url : contextPath + '/menu_mgr.do?action=deleteIcon',
//        	 params : {
//        		 iconIds : iconIds
//        	 },
//        	 success : function(json, opts) {
//        		 this.refresh();
//        	 },
//        	 scope : this
//         });
//	},
	
//	showBigCategory : function(s) {
//		var store = Ext.getCmp('dataview').getStore();
//		store.baseParams.category = s;
//		this.iconType = s;
//		this.refresh();
//	},

//	upload : function() {
//		var alertMsg = this.alertMsg;
//		var win = new Ext.Window({
//			id : 'win',
//			title : '上传图标',
//			layout : 'fit',
//			modal : true,
//			width : 650,
//			height : 300,
//			closeAction : 'close',
//			items : [{
//				xtype : 'uploadPanel',
//				border : false,
//				fileSize : 65,
//				alertMsg : alertMsg,
//				uploadUrl : contextPath + '/menu_mgr.do?action=uploadIcon',
//				flashUrl : contextPath + '/file/uploader/swfupload.swf',
//				filePostName : 'file',
//				fileTypes : '*.png;*.jpg;*.jpeg;*.gif',
//				postParams : {
//					iconsType : Ext.getCmp('iconsWin').iconType
//				}
//			}],
//			listeners : {
//				close : this.refresh,
//				scope : this
//			}
//		});
//		win.show();
//	},
//	
//	refresh : function() {
//		Ext.getCmp('dataview').getStore().reload();
//	}
	
});