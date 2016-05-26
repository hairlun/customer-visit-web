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
