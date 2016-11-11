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
				header : '客户经理名称',
				width : 200,
				dataIndex : 'name',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '用户名',
				width : 230,
				dataIndex : 'username',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '部门',
				width : 230,
				dataIndex : 'department',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}, {
				header : '片区',
				width : 230,
				dataIndex : 'area',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}]);
			this.store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : 'customerManager.do?action=queryAll'
				}),
				reader : new Ext.data.JsonReader({
					root : 'rows',
					totalProperty : 'total',
					id : 'id',
					fields : ['id', 'name', 'username', 'password', 'department', 'area']
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
					handler : this.newCustomerManager,
					scope : this
				}, '-', {
					text : '编辑',
					iconCls : 'edit',
					id : 'edit',
					width : 45,
					xtype : 'button',
					handler : this.editCustomerManager,
					scope : this
				}, '-', {
					text : '删除',
					iconCls : 'del',
					width : 45,
					xtype : 'button',
					handler : this.delCustomerManager,
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
					limit : 20
				}
			});
		}
		return panel;
	},
	
	newCustomerManager : function() {
		var win = new Ext.Window({
			title : '新增',
			id : 'addWin',
			layout : 'fit',
			border : false,
			modal : true,
			width : 400,
			height : 250,
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
					fieldLabel : '客户经理名称',
					xtype : 'textfield',
					id : 'name',
					allowBlank : false,
					blankText : '客户经理名称不能为空'
				}, {
					fieldLabel : '客户经理用户名',
					allowBlank : false,
					xtype : 'textfield',
					id : 'username',
					blankText : '客户经理用户名不能为空'
				}, {
					fieldLabel : '密码',
					xtype : 'textfield',
					inputType : 'password',
					id : 'password',
					allowBlank : false,
					blankText : '密码不能为空'
				}, {
					fieldLabel : '确认密码',
					xtype : 'textfield',
					inputType : 'password',
					id : 'cpassword',
					allowBlank : false,
					blankText : '确认密码不能为空'
				}, {
					fieldLabel : '部门',
					allowBlank : false,
					xtype : 'textfield',
					id : 'department',
					blankText : '部门不能为空'
				}, {
					fieldLabel : '片区',
					allowBlank : true,
					xtype : 'textfield',
					id : 'area'
				}],
				buttons : [{
					text : '确定',
					handler : function() {
						if (!Ext.getCmp('addForm').getForm().isValid()) {
							alert('请输入合法的参数');
							return;
						}
						if (Ext.getCmp('username').getValue().trim().length < 1) {
							alert('用户名不能为空');
							return;
						}
						if (Ext.getCmp('password').getValue() != Ext.getCmp('cpassword').getValue()) {
							alert('前后密码输入不一致，请重新输入！');
							return;
						}
						if (Ext.getCmp('department').getValue().trim().length < 1) {
							alert('部门不能为空');
							return;
						}
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.haode.ajax({
							url : 'customerManager.do?action=addManager',
							params : {
								name : Ext.getCmp('name').getValue(),
								username : Ext.getCmp('username').getValue(),
								password : hex_md5(Ext.getCmp('password').getValue()),
								department : Ext.getCmp('department').getValue(),
								area : Ext.getCmp('area').getValue()
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
						Ext.getCmp('addWin').close();
					}
				}]
			})]
		});
		
		win.show(Ext.getBody());
	},
	
	editCustomerManager : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length != 1) {
			alert('请选择一个要修改的客户经理');
			return;
		}
		
		var win = new Ext.Window({
			title : '编辑',
			id : 'editWin',
			layout : 'fit',
			border : false,
			modal : true,
			width : 400,
			height : 250,
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
					fieldLabel : '客户经理名称',
					xtype : 'textfield',
					id : 'ename',
					allowBlank : false,
					value : records[0].get('name'),
					blankText : '客户经理名称不能为空'
				}, {
					fieldLabel : '客户经理用户名',
					allowBlank : false,
					xtype : 'textfield',
					id : 'eusername',
					value : records[0].get('username'),
					blankText : '编号不能为空'
				}, {
					fieldLabel : '密码',
					xtype : 'textfield',
					inputType : 'password',
					id : 'epassword',
					value : records[0].get('password'),
					allowBlank : false,
					blankText : '密码不能为空'
				}, {
					fieldLabel : '确认密码',
					xtype : 'textfield',
					inputType : 'password',
					id : 'ecpassword',
					value : records[0].get('password'),
					allowBlank : false,
					blankText : '确认密码不能为空'
				}, {
					fieldLabel : '部门',
					allowBlank : false,
					xtype : 'textfield',
					id : 'edepartment',
					value : records[0].get('department'),
					blankText : '部门不能为空'
				}, {
					fieldLabel : '片区',
					allowBlank : true,
					xtype : 'textfield',
					id : 'earea',
					value : records[0].get('area')
				}],
				buttons : [{
					text : '确定',
					handler : function() {
						if (!Ext.getCmp('editForm').getForm().isValid()) {
							alert('请输入合法的参数');
							return;
						}
						if (Ext.getCmp('eusername').getValue().trim().length < 1) {
							alert('用户名不能为空');
							return;
						}
						if (Ext.getCmp('epassword').getValue() != Ext.getCmp('ecpassword').getValue()) {
							alert('前后密码输入不一致，请重新输入！');
							return;
						}
						var password = Ext.getCmp('epassword').getValue();
						if (password != records[0].get('password')) {
							password = hex_md5(password);
						}
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.haode.ajax({
							url : 'customerManager.do?action=editManager',
							params : {
								id : records[0].get('id'),
								name : Ext.getCmp('ename').getValue(),
								username : Ext.getCmp('eusername').getValue(),
								ousername : records[0].get('username'),
								password : password,
								department : Ext.getCmp('edepartment').getValue(),
								area : Ext.getCmp('earea').getValue()
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
	
	delCustomerManager : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length < 1) {
			alert('请选择要删除的客户经理');
			return;
		}
		var ids = "";
		for (var i = 0; i < records.length; i++) {
			if (records[i].get('id') == 1) {
				alert('admin为系统管理员，不能删除！');
				continue;
			}
			ids += ',' + records[i].get('id');
		}
		if (ids.length < 2) {
			return;
		}
		if (!window.confirm("确认删除？")) {
			return;
		}
		Ext.haode.ajax({
			url : 'customerManager.do?action=delManagers',
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
	}
	
};
