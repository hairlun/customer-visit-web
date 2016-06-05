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
			var centerPanel = this.getCenterPanel();
			viewport = new Ext.Viewport({
			    layout: 'fit',
			    items: [centerPanel]
			});
		}
		return viewport;
	},
	
	getCenterPanel : function() {
		var panel;
		if (this.viewport) {
			panel = this.getViewport().items[0];
		} else {
			var sm =  new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
				header : '客户分组名称',
				width : 200,
				dataIndex : 'name',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}]);
			this.store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : 'customerGroup.do?action=queryAll'
				}),
				reader : new Ext.data.JsonReader({
					root : 'rows',
					totalProperty : 'total',
					id : 'id',
					fields : ['id', 'name']
				}),
				remoteSort : true
			});
			var paging = new Ext.PagingToolbar({
				pageSize : 20,
				store : this.store,
				displayInfo : true,
				displayMsg : '当前显示数据 {0} - {1} of {2}',
				emptyMsg : '没有数据'
			});
			panel = new Ext.grid.GridPanel({
				id : 'grid',
//				viewConfig : {
//					forcefit : true
//				},
				loadMask : true,
				tbar : [{
					text : '新增',
					iconCls : 'user',
					id : 'add',
					width : 45,
					xtype : 'button',
					handler : this.newCustomerGroup,
					scope : this
				}, '-', {
					text : '编辑',
					iconCls : 'edit',
					id : 'edit',
					width : 45,
					xtype : 'button',
					handler : this.editCustomerGroup,
					scope : this
				}, '-', {
					text : '删除',
					iconCls : 'del',
					width : 45,
					xtype : 'button',
					handler : this.delCustomerGroup,
					scope : this
				}],
				store : this.store,
				sm : sm,
				cm : cm,
				bbar : paging
			});
			this.store.load({
				params : {
					start : 0,
					limit : 20,
					all : 0
				}
			});
		}
		return panel;
	},
	
	newCustomerGroup : function() {
//		var cids = "";
		var win = new Ext.Window({
			title : '新增',
			id : 'addWin',
			layout : 'fit',
			border : false,
			modal : true,
			width : 400,
			height : 150,
			items : [new Ext.form.FormPanel({
				id : 'addForm',
				defaultType : 'textfield',
				bodyStyle : 'padding : 30px; 20px;',
				defaults : {
					width : 200,
					msgTarget : 'under'
				},
				labelWidth : 100,
				labelAlign : 'right',
				items : [{
					fieldLabel : '客户分组名称',
					xtype : 'textfield',
					id : 'name',
					allowBlank : false,
					blankText : '客户分组名称不能为空'
//				}, {
//					xtype : 'compositefield',
//					width : 500,
//					items : [{
//						fieldLabel : '客户名称',
//						xtype : 'textfield',
//						id : 'customer',
//						allowBlank : true,
//						width : 300
//					}, {
//						text : '浏览…',
//						xtype : 'button',
//						handler : function() {
//							// 选择客户
//							var store1 = new Ext.data.Store({
//								proxy : new Ext.data.HttpProxy({
//									url : 'customer.do?action=queryAll'
//								}),
//								reader : new Ext.data.JsonReader({
//									root : 'rows',
//									totalProperty : 'total',
//									id : 'id',
//									fields : ['id', 'name', 'number', 'sell_number', 'store_name', 'level', 'phone_number', 'mname', 
//									          'backup_number', 'address', 'order_type', 'gps', 'last_visit_time']
//								})
//							});
//
//							var paging = new Ext.PagingToolbar({
//								pageSize : 20,
//								store : store1,
//								displayInfo : true,
//								displayMsg : '当前显示数据 {0} - {1} of {2}',
//								emptyMsg : '没有数据'
//							});
//							var sm =  new Ext.grid.CheckboxSelectionModel();
//							var win1 = new Ext.Window({
//								title : '选择客户',
//								id : 'chooseCustomer',
//								layout : 'fit',
//								border : false,
//								modal : true,
//								width : 800,
//								height : 600,
//								items : [new Ext.grid.GridPanel({
//									id : 'grid1',
//									loadMask : true,
//									store : store1,
//									sm : sm,
//									cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
//										header : '客户名称',
//										width : 100,
//										dataIndex : 'name',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '客户编号',
//										width : 130,
//										dataIndex : 'number',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '专卖证号',
//										width : 130,
//										dataIndex : 'sell_number',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '店铺名称',
//										width : 200,
//										dataIndex : 'store_name',
//										sortable : true,
//										remoteSort : true,
//										align : 'left'
//									}, {
//										header : '客户级别',
//										width : 90,
//										dataIndex : 'level',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '电话号码',
//										width : 100,
//										dataIndex : 'phone_number',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '客户经理',
//										width : 120,
//										dataIndex : 'mname',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '备用号码',
//										width : 100,
//										dataIndex : 'backup_number',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '经营地址',
//										width : 240,
//										dataIndex : 'address',
//										sortable : true,
//										remoteSort : true,
//										align : 'left',
//										renderer : function(value, meta) {
//											meta.attr = 'title="' + value + '"';
//											return value;
//										}
//									}, {
//										header : '订货类型',
//										width : 60,
//										dataIndex : 'order_type',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : 'GPS(经度,纬度)',
//										width : 150,
//										dataIndex : 'gps',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '最近一次拜访时间',
//										width : 180,
//										dataIndex : 'last_visit_time',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}]),
//									bbar : paging
//								})],
//								buttons : [{
//									text : '确定',
//									handler : function() {
//										var crecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
//										if (crecords.length < 1) {
//											alert('请选择客户!');
//											return;
//										}
//										var size = crecords.length;
//										var cnames = '';
//										for (var i = 0; i < size; i++) {
//											cids += ',' + crecords[i].get('id');
//											cnames += ',' + crecords[i].get('name');
//										}
//										Ext.getCmp('customer').setValue(cnames.substring(1));
//										
//										Ext.getCmp('chooseCustomer').close();
//									}
//								}, {
//									text : '取消',
//									handler : function() {
//										Ext.getCmp('chooseCustomer').close();
//									}
//								}]
//							});
//							win1.show(Ext.getBody());
//							store1.load({
//								params : {
//									start : 0,
//									limit : 20
//								}
//							});
//						}
//					}]
				}],
				buttons : [{
					text : '确定',
					handler : function() {
						if (!Ext.getCmp('addForm').getForm().isValid()) {
							alert('请输入合法的参数');
							return;
						}
						if (Ext.getCmp('name').getValue().trim().length < 1) {
							alert('客户分组名称不能为空');
							return;
						}
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.haode.ajax({
							url : 'customerGroup.do?action=addGroup',
							params : {
//								cids : cids,
								name : Ext.getCmp('name').getValue()
							},
							callback : function() {
								Ext.MessageBox.hide();
							},
							success : function(json, opts) {
								Ext.MessageBox.hide();
								alert(json.myHashMap.msg);
								if (json.myHashMap.success) {
									Ext.getCmp('addWin').close();
									Ext.getCmp('grid').getStore().load({
										params : {
											start : 0,
											limit : 20,
											all : 0
										}
									});
								}
							}
						});
					}
				}, {
					text : '取消',
					handler : function() {
						Ext.getCmp('addWin').close();
					}
				}]
			})]
		});
		
		win.show(Ext.getBody());
	},
	
	editCustomerGroup : function() {
//		var cids = "";
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length != 1) {
			alert('请选择一个要修改的客户分组');
			return;
		}
		
		var win = new Ext.Window({
			title : '编辑',
			id : 'editWin',
			layout : 'fit',
			border : false,
			modal : true,
			width : 400,
			height : 150,
			items : [new Ext.form.FormPanel({
				id : 'editForm',
				defaultType : 'textfield',
				bodyStyle : 'padding : 30px; 20px;',
				defaults : {
					width : 200,
					msgTarget : 'under'
				},
				labelWidth : 100,
				labelAlign : 'right',
				items : [{
					fieldLabel : '客户分组名称',
					xtype : 'textfield',
					id : 'ename',
					allowBlank : false,
					value : records[0].get('name'),
					blankText : '客户分组名称不能为空'
//				}, {
//					xtype : 'compositefield',
//					width : 500,
//					items : [{
//						fieldLabel : '客户名称',
//						xtype : 'textfield',
//						id : 'customer',
//						allowBlank : true,
//						width : 300
//					}, {
//						text : '浏览…',
//						xtype : 'button',
//						handler : function() {
//							// 选择客户
//							var store1 = new Ext.data.Store({
//								proxy : new Ext.data.HttpProxy({
//									url : 'customer.do?action=queryAll'
//								}),
//								reader : new Ext.data.JsonReader({
//									root : 'rows',
//									totalProperty : 'total',
//									id : 'id',
//									fields : ['id', 'name', 'number', 'sell_number', 'store_name', 'level', 'phone_number', 'mname', 
//									          'backup_number', 'address', 'order_type', 'gps', 'last_visit_time']
//								})
//							});
//
//							var paging = new Ext.PagingToolbar({
//								pageSize : 20,
//								store : store1,
//								displayInfo : true,
//								displayMsg : '当前显示数据 {0} - {1} of {2}',
//								emptyMsg : '没有数据'
//							});
//							var sm =  new Ext.grid.CheckboxSelectionModel();
//							var win1 = new Ext.Window({
//								title : '选择客户',
//								id : 'chooseCustomer',
//								layout : 'fit',
//								border : false,
//								modal : true,
//								width : 800,
//								height : 600,
//								items : [new Ext.grid.GridPanel({
//									id : 'grid1',
//									loadMask : true,
//									store : store1,
//									sm : sm,
//									cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
//										header : '客户名称',
//										width : 100,
//										dataIndex : 'name',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '客户编号',
//										width : 130,
//										dataIndex : 'number',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '专卖证号',
//										width : 130,
//										dataIndex : 'sell_number',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '店铺名称',
//										width : 200,
//										dataIndex : 'store_name',
//										sortable : true,
//										remoteSort : true,
//										align : 'left'
//									}, {
//										header : '客户级别',
//										width : 90,
//										dataIndex : 'level',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '电话号码',
//										width : 100,
//										dataIndex : 'phone_number',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '客户经理',
//										width : 120,
//										dataIndex : 'mname',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '备用号码',
//										width : 100,
//										dataIndex : 'backup_number',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '经营地址',
//										width : 240,
//										dataIndex : 'address',
//										sortable : true,
//										remoteSort : true,
//										align : 'left',
//										renderer : function(value, meta) {
//											meta.attr = 'title="' + value + '"';
//											return value;
//										}
//									}, {
//										header : '订货类型',
//										width : 60,
//										dataIndex : 'order_type',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : 'GPS(经度,纬度)',
//										width : 150,
//										dataIndex : 'gps',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}, {
//										header : '最近一次拜访时间',
//										width : 180,
//										dataIndex : 'last_visit_time',
//										sortable : true,
//										remoteSort : true,
//										align : 'center'
//									}]),
//									bbar : paging
//								})],
//								buttons : [{
//									text : '确定',
//									handler : function() {
//										var crecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
//										if (crecords.length < 1) {
//											alert('请选择客户!');
//											return;
//										}
//										var size = crecords.length;
//										var cnames = '';
//										for (var i = 0; i < size; i++) {
//											cids += ',' + crecords[i].get('id');
//											cnames += ',' + crecords[i].get('name');
//										}
//										Ext.getCmp('customer').setValue(cnames.substring(1));
//										
//										Ext.getCmp('chooseCustomer').close();
//									}
//								}, {
//									text : '取消',
//									handler : function() {
//										Ext.getCmp('chooseCustomer').close();
//									}
//								}]
//							});
//							win1.show(Ext.getBody());
//							store1.load({
//								params : {
//									start : 0,
//									limit : 20
//								}
//							});
//						}
//					}]
				}],
				buttons : [{
					text : '确定',
					handler : function() {
						if (!Ext.getCmp('editForm').getForm().isValid()) {
							alert('请输入合法的参数');
							return;
						}
						if (Ext.getCmp('ename').getValue().trim().length < 1) {
							alert('客户分组名称不能为空');
							return;
						}
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.haode.ajax({
							url : 'customerGroup.do?action=editGroup',
							params : {
								id : records[0].get('id'),
								name : Ext.getCmp('ename').getValue(),
								oname : records[0].get('name')
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
											limit : 20,
											all : 0
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
//		
//		var store2 = new Ext.data.Store({
//			proxy : new Ext.data.HttpProxy({
//				url : 'customer.do?action=queryAll'
//			}),
//			reader : new Ext.data.JsonReader({
//				root : 'rows',
//				totalProperty : 'total',
//				id : 'id',
//				fields : ['id', 'name', 'number', 'sell_number', 'store_name', 'level', 'phone_number', 'mname', 
//				          'backup_number', 'address', 'order_type', 'gps', 'last_visit_time']
//			})
//		});
//		store2.load({
//			params : {
//				start : 0,
//				limit : 20
//			}
//		});
	},
	
	delCustomerGroup : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length < 1) {
			alert('请选择要删除的客户分组');
			return;
		}
		var ids = "";
		for (var i = 0; i < records.length; i++) {
			ids += ',' + records[i].get('id');
		}
		if (ids.length < 2) {
			return;
		}
		if (!window.confirm("确认删除？")) {
			return;
		}
		Ext.haode.ajax({
			url : 'customerGroup.do?action=delGroups',
			params : {
				ids : ids
			},
			success : function(json, opts) {
				alert(json.myHashMap.msg);
				if (json.myHashMap.success) {
					Ext.getCmp('grid').getStore().load({
						params : {
							start : 0,
							limit : 20,
							all : 0
						}
					});
				}
			}
		});
	}
	
};
