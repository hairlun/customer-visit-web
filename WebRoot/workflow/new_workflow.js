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
	
	cn : 1,
	
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
			panel = new Ext.form.FormPanel({
				id : 'form',
				defaults : {
					width : 400,
					msgTarget : 'under'
				},
				bodyStyle : 'padding : 50px; 150px;',
				labelWidth : 100,
				labelAlign : 'right',
				items : [{
					fieldLabel : '零售客户名称',
					allowBlank : false,
					name : 'customer',
					id : 'customer',
					xtype : 'textfield'
				}, {
					fieldLabel : '问题发生地点',
					allowBlank : false,
					name : 'address',
					id : 'address',
					xtype : 'textfield'
				}, {
					fieldLabel : '专卖许可证编号',
					allowBlank : false,
					name : 'sellNumber',
					id : 'sellNumber',
					xtype : 'textfield'
				}, {
					fieldLabel : '信息接收时间',
					allowBlank : false,
					name : 'receiveTime',
					id : 'receiveTime',
					xtype : 'textfield'
				}, {
					fieldLabel : '问题发现人',
					allowBlank : false,
					hidden : true,
					name : 'problemFinder',
					id : 'problemFinder',
					xtype : 'textfield'
				}, {
					fieldLabel : '交办时间',
					allowBlank : false,
					hidden : true,
					name : 'handleTime',
					id : 'handleTime',
					xtype : 'textfield'
				}, {
					xtype : 'compositefield',
					width : 500,
					items : [{
						fieldLabel : '交办科室及负责人',
						xtype : 'textfield',
						id : 'manager',
						allowBlank : false,
						width : 400
					}, {
						text : '浏览…',
						xtype : 'button',
						handler : function() {
							// 选择交办负责人
							var store1 = new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : 'customerManager.do?action=queryAll'
								}),
								reader : new Ext.data.JsonReader({
									root : 'rows',
									totalProperty : 'total',
									id : 'id',
									fields : ['id', 'name', 'username', 'department', 'area']
								})
							});

							var paging = new Ext.PagingToolbar({
								pageSize : 20,
								store : store1,
								displayInfo : true,
								displayMsg : '当前显示数据 {0} - {1} of {2}',
								emptyMsg : '没有数据'
							});
							
							var win1 = new Ext.Window({
								title : '选择客户经理',
								id : 'bind',
								layout : 'fit',
								border : false,
								modal : true,
								width : 600,
								height : 400,
								items : [new Ext.grid.GridPanel({
									id : 'grid1',
									loadMask : true,
									store : store1,
									cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), {
										header : '客户经理名称',
										width : 130,
										dataIndex : 'name',
										align : 'center'
									}, {
										header : '用户名',
										width : 130,
										dataIndex : 'username',
										align : 'center'
									}, {
										header : '部门',
										width : 130,
										dataIndex : 'department',
										align : 'center'
									}, {
										header : '片区',
										width : 130,
										dataIndex : 'area',
										align : 'center'
									}]),
									bbar : paging
								})],
								buttons : [{
									text : '确定',
									handler : function() {
										var mrecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
										if (mrecords.length < 1) {
											alert('请选择要绑定的客户经理!');
											return;
										}
										mid = mrecords[0].get('id');
										var manager = mrecords[0].get('name');
										if (mrecords[0].get('department') != "") {
											manager = manager + "-" + mrecords[0].get('department');
										}
										if (mrecords[0].get('area') != "") {
											manager = manager + "-" + mrecords[0].get('area');
										}
										Ext.getCmp('manager').setValue(manager);
										
										Ext.getCmp('bind').close();
									}
								}, {
									text : '取消',
									handler : function() {
										Ext.getCmp('bind').close();
									}
								}]
							});
							win1.show(Ext.getBody());
							store1.load({
								params : {
									start : 0,
									limit : 20
								}
							});
						}
					}]
				}, {
					fieldLabel : '问题情况具体描述',
					allowBlank : false,
					name : 'description',
					id : 'description',
					xtype : 'textarea'
				}, {
					fieldLabel : '备注',
					allowBlank : false,
					name : 'remark',
					id : 'remark',
					xtype : 'textarea'
				}, {
					xtype : 'button',
					id : 'tsb',
					text : '交办',
					align : 'center',
					width : 80,
					handler : function() {
						Ext.getCmp('problemFinder').setValue(loginUser.username);
						Ext.getCmp('handleTime').setValue(new Date());
						if (!Ext.getCmp('form').getForm().isValid()) {
							alert('请正确填写表单');
							return;
						}
						Ext.getCmp('form').getForm().submit({
							 waitTitle : '提示',
						     waitMsg : '正在提交数据请稍后...',  
						     url : 'workflow.do?action=newWorkflowSubmit',  
						     method : 'post',
						     success : function(form, action) {
						    	 alert(action.result.myHashMap.msg);
						     },
						     failure : function(form, action) {
						    	 alert(action.result.myHashMap.msg);
						     }
						});
					}
//						handler : function() {
//							if (!Ext.getCmp('form').getForm().isValid()) {
//								alert('请正确填写表单');
//								return;
//							}
//							
//							
//							var sm =  new Ext.grid.CheckboxSelectionModel();
//							var store1 = new Ext.data.Store({
//								proxy : new Ext.data.HttpProxy({
//									url : 'customerManager.do?action=queryAll'
//								}),
//								reader : new Ext.data.JsonReader({
//									root : 'rows',
//									totalProperty : 'total',
//									id : 'id',
//									fields : ['id', 'name', 'username']
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
//							
//							var win = new Ext.Window({
//								title : '客户经理',
//								id : 'bind',
//								layout : 'fit',
//								border : false,
//								modal : true,
//								width : 500,
//								height : 400,
//								items : [new Ext.grid.GridPanel({
//									id : 'grid1',
//									loadMask : true,
////									tbar : [{
////										xtype : 'textfield',
////										id : 'searchName',
////										emptyText : '请输入客户经理名称...',
////										width : 150
////									}, {
////										text : '搜索',
////										width : 45,
////										xtype : 'button',
////										handler : function() {
////											
////										}
////									}],
//									store : store1,
//									sm : sm,
//									cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
//										header : '客户经理名称',
//										width : 200,
//										dataIndex : 'name',
//										align : 'center'
//									}, {
//										header : '客户经理用户名',
//										width : 230,
//										dataIndex : 'username',
//										align : 'center'
//									}]),
//									bbar : paging
//								})],
//								buttons : [{
//									text : '确定',
//									handler : function() {
//										var mrecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
//										if (mrecords.length < 1) {
//											alert('请选择要做任务的客户经理!');
//											return;
//										}
//										var mids = '';
//										for (var j = 0; j < mrecords.length; j++) {
//											mids += ',' + mrecords[j].get('id');
//										}
//										
//										Ext.getCmp('bind').close();
//										Ext.getCmp('form').getForm().submit({
//											 waitTitle : '提示',
//										     waitMsg : '正在提交数据请稍后...',
//										     url : 'task.do?action=indevi',
//										     params : {
//										    	 mids : mids 
//										     },
//										     method : 'post',
//										     success : function(form, action) {
//										    	 alert(action.result.myHashMap.msg);
//										     },
//										     failure : function(form, action) {
//										    	 alert(action.result.myHashMap.msg);
//										     }
//										});
//										
//									}
//								}, {
//									text : '取消',
//									handler : function() {
//										Ext.getCmp('bind').close();
//									}
//								}]
//							});
//							win.show(Ext.getBody());
//							store1.load({
//								params : {
//									start : 0,
//									limit : 20
//								}
//							});
//							
//						}
				}]
			});
		}
		return panel;
	}
	

	
};
