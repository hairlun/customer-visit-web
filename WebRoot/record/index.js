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
			var north = this.getNorthPanel();
			var centerPanel = this.getCenterPanel();
			viewport = new Ext.Viewport({
			    layout: 'border',
			    items: [north, centerPanel]
			});
		}
		return viewport;
	},
	
	getNorthPanel : function() {
		var panel;
		if (this.viewport) {
			panel = this.getViewport().items[0];
		} else {
			panel = new Ext.form.FormPanel({
				title : '查询',
				region : 'north',
				height : 130,
				frame:true,
                labelWidth: 150,
                labelAlign:'right',
				items : [{
					autoHeight:true,
                    layout:'column',
                    border:false,
                    items : [{
                    	columnWidth:.55,
                        xtype: 'fieldset',
                        layout:'form',
                        border : false,
                        labelWidth : 100,
                        defaults: {anchor: '55%'},
                        style: 'margin-left: 100px;padding-left: 5px;',
                        items:[
                            {xtype: 'textfield',fieldLabel: '客户名称',name: 'customer', id : 'customer'},
                            {xtype: 'datefield',fieldLabel: '开始时间',name: 'startTime', editable : false, id : 'startTime'},
                        ]
                    }, {
                    	columnWidth:.45,
                        xtype: 'fieldset',
                        layout:'form',
                        border : false,
                        labelWidth : 100,
                        defaults: {anchor: '60%'},
                        style: 'margin-left: 5px;padding-left: 5px;',
                        items:[
                            {xtype: 'textfield',fieldLabel: '客户经理名称',name: 'manager', id : 'manager'},
                            {xtype: 'datefield',fieldLabel: '结束时间',name: 'endTime', editable : false, id : 'endTime'}
                        ]
                    }]
				}],
				buttonAlign : 'center',
				buttons : [{
					text : '查询',
					handler : function() {
						Ext.getCmp('grid').getStore().load({
							params : {
								start : 0,
								limit : 20,
								manager : Ext.getCmp('manager').getValue(),
								customer : Ext.getCmp('customer').getValue(),
								startTime : Ext.getCmp('startTime').getValue(),
								endTime : Ext.getCmp('endTime').getValue()
							}
						});
					}
				}, {
					text : '重置',
					handler : function() {
						Ext.getCmp('manager').setValue('');
						Ext.getCmp('customer').setValue('');
						Ext.getCmp('startTime').setValue('');
						Ext.getCmp('endTime').setValue('');
					}
				}]
			});
		}
		return panel;
	},
	
	getCenterPanel : function() {
		var panel;
		if (this.viewport) {
			panel = this.getViewport().items[1];
		} else {
			var sm =  new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
				header : '客户名称',
				width : 120,
				dataIndex : 'cname',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '客户经理',
				width : 120,
				dataIndex : 'manager',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '是否驳回',
				width : 80,
				dataIndex : 'reject',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '状态',
				width : 120,
				dataIndex : 'type',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '签到时间',
				width : 160,
				dataIndex : 'visit_time',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '签退时间',
				width : 160,
				dataIndex : 'leave_time',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '拜访时长(分钟)',
				width : 140,
				dataIndex : 'cost',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '拜访内容',
				width : 280,
				dataIndex : 'content',
				sortable : true,
				remoteSort : true,
				align : 'left',
				renderer : function(value, meta) {
					meta.attr = 'title="' + value + '"';
					return value;
				}
			}, {
				header : '评价',
				width : 100,
				dataIndex : 'result_code',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '城市',
				width : 100,
				dataIndex : 'city',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : 'GPS(经度,纬度)',
				width : 150,
				dataIndex : 'gps',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : 'GPS误差',
				width : 100,
				dataIndex : 'gps_dist',
				align : 'center'
			}]);
			this.store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : 'record.do?action=queryAll'
				}),
				reader : new Ext.data.JsonReader({
					root : 'rows',
					totalProperty : 'total',
					id : 'id',
					fields : ['id', 'cname', 'manager', 'reject', 'type', 'visit_time', 'content', 'city', 'gps', 'gps_dist', 'gps_flag', 'result_code', 'leave_time', 'cost']
				}),
				remoteSort : true
//				autoLoad : true
			});
//			this.store.on('load', function(s, records) {
//				var gridcount = 0;
//				s.each(function(r) {
//					if (r.get('gps_flag') == 1) {
//						cm.getRow(gridcount).style.textColor='#ff0000';
//					}
//				});
//			});
			this.store.load({
				params : {
					start : 0,
					limit : 20
				}
			});
			var paging = new Ext.PagingToolbar({
				pageSize : 20,
				store : this.store,
				displayInfo : true,
				displayMsg : '当前显示数据 {0} - {1} of {2}',
				emptyMsg : '没有数据'
			});
			panel = new Ext.grid.GridPanel({
				region : 'center',
				id : 'grid',
				loadMask : true,
//				autoExpandColumn : 7,
				tbar : [{
					text : '删除',
					iconCls : 'del',
					width : 45,
					xtype : 'button',
					handler : this.delRecords,
					scope : this
				}, {
					text : '查看图片',
					iconCls : 'imge',
					width : 45,
					xtype : 'button',
					handler : this.images,
					scope : this
				}, {
					text : '打包下载图片',
					iconCls : 'imge',
					width : 45,
					xtype : 'button',
					handler : this.zip,
					scope : this
				}, {
					text : '驳回',
					iconCls : 'del',
					width :45,
					xtype : 'button',
					handler : this.reject,
					scope : this
				}],
				store : this.store,
				sm : sm,
				cm : cm,
				bbar : paging,
				viewConfig : {
					getRowClass : function(record, index, p, ds) {
						if (record.data['gps_flag'] == "1") {
							return 'x-grid-record-red';
						}
					}
				}
			});
		}
		return panel;
	},
	
	delRecords : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length < 1) {
			alert('请选择要删除的记录');
			return;
		}
		var ids = "";
		for (var i = 0; i < records.length; i++) {
			ids += "," + records[i].get('id');
		}
		if (ids.length < 2) {
			return;
		}
		if (!window.confirm("确认删除？")) {
			return;
		}
		Ext.haode.ajax({
			url : 'record.do?action=delRecords',
			params : {
				ids : ids
			},
			success : function(json, opts) {
				alert(json.myHashMap.msg);
				if (json.myHashMap.success) {
					Ext.getCmp('grid').getStore().load({
						params : {
							start : 0,
							limit : 20
						}
					});
				}
			}
		});
	},
	
	reject : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length < 1) {
			alert('请选择要驳回的记录');
			return;
		}
		var ids = "";
		for (var i = 0; i < records.length; i++) {
			ids += "," + records[i].get('id');
		}
		if (ids.length < 2) {
			return;
		}
		var win = new Ext.Window({
			title : '驳回',
			id : 'editWin',
			layout : 'fit',
			border : false,
			modal : true,
			width : 500,
			height : 150,
			items : [new Ext.form.FormPanel({
				id : 'editForm',
				defaultType : 'textfield',
				bodyStyle : 'padding : 30px; 20px;',
				defaults : {
					width : 300,
					msgTarget : 'under'
				},
				labelWidth : 100,
				labelAlign : 'right',
				items : [{
					fieldLabel : '驳回原因',
					xtype : 'textfield',
					id : 'reason',
					allowBlank : true
				}],
				buttons : [{
					text : '确定',
					handler : function() {
						if (!window.confirm("确认驳回？")) {
							return;
						}
						if (!Ext.getCmp('editForm').getForm().isValid()) {
							alert('请输入合法的参数');
							return;
						}
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.haode.ajax({
							url : 'record.do?action=rejectRecord',
							params : {
								ids : ids,
								reason : Ext.getCmp('reason').getValue()
							},
							callback : function() {
								Ext.MessageBox.hide();
							},
							success : function(json, opts) {
								Ext.MessageBox.hide();
								alert(json.myHashMap.msg);
								if (json.myHashMap.success) {
									Ext.getCmp('editWin').close();
									Ext.getCmp('grid').getStore().load({
										params : {
											start : 0,
											limit : 20
										}
									});
								}
							}
						});
					}
				}, {
					text : '取消',
					handler : function() {
						Ext.getCmp('editWin').close();
					}
				}]
			})]
		});
		win.show(Ext.getBody());
	},
	
	zip : function() {
		Ext.MessageBox.wait('正在打包','请稍后...');
		Ext.haode.ajax({
			url : 'record.do?action=zip',
			method : 'post',
			success : function(json, opts) {
				Ext.MessageBox.hide();
				new Ext.Window({
					width : 250,
					height : 80,
					title : '打包图片下载',
					html : "<a href='record.do?action=downloadZip&res=" + json.myHashMap.msg + "'>请点击此处下载...</a>"
				}).show();
			},
			failure : function(json, opt) {
				Ext.MessageBox.hide();
				alert(json.myHashMap.msg);
			}
		});
	},
	
	images : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length != 1) {
			alert('请选择一个要查看的记录！');
			return;
		}
		
		var store = new Ext.data.JsonStore({
			autoLoad: true,
		    url : 'record.do?action=getImages',
		    baseParams : {
		    	recordId : records[0].get('id')
		    },
		    root : 'rows',
		    fields : [
		        'title', 'url'
		    ]
		});

		var tpl = new Ext.XTemplate(
		    '<tpl for=".">',
		        '<div class="thumb-wrap">',
			        '<div class="thumb"><a href="{url}" target="_blank"><img src="{url}" title="{title}"></a></div>',
		        '</div>',
		    '</tpl>',
		    '<div class="x-clear"></div>'
		);
		var win = new Ext.Window({
			id : 'iconsWin',
			width : 565,
			height : 390,
			modal : true,
			layout : 'fit',
			bodyStyle : 'background-color:white;overflow-y:visible;',
			title : '图片',
			buttons : [{
				text : '取消',
				handler : function() {
					Ext.getCmp('iconsWin').close();
				}
			}],
			items : [new Ext.DataView({
				id : 'dataview',
		        store: store,
		        tpl: tpl,
		        bodyBorder: false,
		        autoHeight:true,
		        overClass:'x-view-over',
				itemSelector:'div.thumb-wrap',
				emptyText: ''
		    })]
		});
		win.show(Ext.getBody());
	}
	
}