Ext.namespace("Ext.haode");

Ext.haode.UserPanel = function(args){
	Ext.apply(this, args);
	this.init();
};

Ext.haode.UserPanel.prototype = {
	
	init : function() {
		this.viewport = this.getViewport();
	},
	
	getViewport : function() {
		var viewport;
		if (this.viewport) {
			viewport = this.viewport;
		} else {
			var center = this.getCenter();
			viewport = new Ext.Viewport({
				layout : 'fit',
				items : [center]
			});
		}
		return viewport;
	},
	
	getCenter : function() {
		var center;
		if (this.viewport) {
			center = this.viewport.items[0];
		} else {
			var sm =  new Ext.grid.CheckboxSelectionModel();
			var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
				header : '用户名',
				width : 200,
				dataIndex : 'username',
				align : 'center'
			}, {
				header : '全名',
				width : 220,
				dataIndex : 'fullname',
				align : 'center'
			}]);
			this.store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : 'user.do?action=queryAll'
				}),
				reader : new Ext.data.JsonReader({
					root : 'rows',
					totalProperty : 'total',
					id : 'id',
					fields : ['id', 'username', 'fullname']
				})
//				autoLoad : true
			});
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
			center = new Ext.grid.GridPanel({
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
					handler : this.newUser,
					scope : this
				}, '-', {
					text : '删除',
					iconCls : 'del',
					width : 45,
					xtype : 'button',
					handler : this.delUser,
					scope : this
				}],
				store : this.store,
				sm : sm,
				cm : cm,
				bbar : paging
			});
		}
		return center;
	},
	
	newUser : function() {
		var win = new Ext.Window({
			title : '新增',
			id : 'addWin',
			layout : 'fit',
			border : false,
			modal : true,
			width : 370,
			height : 260,
			items : [new Ext.form.FormPanel({
				id : 'addForm',
				defaultType : 'textfield',
				bodyStyle : 'padding : 50px; 20px;',
				defaults : {
					width : 200
				},
				labelWidth : 55,
				labelAlign : 'right',
				items : [{
					fieldLabel : '用户名',
					xtype : 'textfield',
					id : 'username',
					allowBlank : false,
					blankText : '用户名不能为空',
					msgTarget : 'under'
				}, {
					fieldLabel : '密码',
					allowBlank : false,
					xtype : 'textfield',
					id : 'password',
					blankText : '密码不能为空',
					msgTarget : 'under',
					inputType : 'password'
				}, {
					fieldLabel : '确认密码',
					allowBlank : false,
					xtype : 'textfield',
					id : 'cpassword',
					blankText : '确认密码不能为空',
					msgTarget : 'under',
					inputType : 'password'
				}, {
					fieldLabel : '全名',
					xtype : 'textfield',
					id : 'fullname'
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
						if (Ext.getCmp('cpassword').getValue() != Ext.getCmp('password').getValue()) {
							alert('前后密码输入不一致，请重新输入!');
							return;
						}
						Ext.haode.ajax({
							url : 'user.do?action=addUser',
							params : {
								username : Ext.getCmp('username').getValue(),
								password : hex_md5(Ext.getCmp('password').getValue()),
								fullname : Ext.getCmp('fullname').getValue()
							},
							success : function(json, opts) {
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
		
		win.show('add');
	},
	
	delUser : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length < 1) {
			alert('请选择要删除的用户');
			return;
		}
		var ids = "";
		for (var i = 0; i < records.length; i++) {
			if (records[i].get('username') == 'admin') {
				alert('admin用户不能删除！');
			} else {
				ids += ',' + records[i].get('id');
			}
		}
		if (ids.length < 2) {
			return;
		}
		if (!window.confirm("确认删除？")) {
			return;
		}
		Ext.haode.ajax({
			url : 'user.do?action=delUsers',
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
		
}