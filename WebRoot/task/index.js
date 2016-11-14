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
			var n = new Ext.Button({
				id : 'tsb',
				text : '发放任务',
				align : 'right',
				width : 80,
				menu : [{
					text : '常规任务',
					handler : function() {
						if (!Ext.getCmp('form').getForm().isValid()) {
							alert('请正确填写表单');
							return;
						}
						Ext.getCmp('form').getForm().submit({
							 waitTitle : '提示',
						     waitMsg : '正在提交数据请稍后...',
						     timeout : 1000000,
						     url : 'task.do?action=normal',  
						     method : 'post',
						     success : function(form, action) {
						    	 alert(action.result.myHashMap.msg);
						     },
						     failure : function(form, action) {
						    	 alert(action.result.myHashMap.msg);
						     }
						});
					}
//				}, {
//					text : '个别任务',
//					handler : function() {
//						if (!Ext.getCmp('form').getForm().isValid()) {
//							alert('请正确填写表单');
//							return;
//						}
//						
//						var sm =  new Ext.grid.CheckboxSelectionModel();
//						var store1 = new Ext.data.Store({
//							proxy : new Ext.data.HttpProxy({
//								url : 'customerManager.do?action=queryAll'
//							}),
//							reader : new Ext.data.JsonReader({
//								root : 'rows',
//								totalProperty : 'total',
//								id : 'id',
//								fields : ['id', 'name', 'username']
//							})
//						});
//
//						var paging = new Ext.PagingToolbar({
//							pageSize : 20,
//							store : store1,
//							displayInfo : true,
//							displayMsg : '当前显示数据 {0} - {1} of {2}',
//							emptyMsg : '没有数据'
//						});
//						
//						var win = new Ext.Window({
//							title : '客户经理',
//							id : 'bind',
//							layout : 'fit',
//							border : false,
//							modal : true,
//							width : 500,
//							height : 400,
//							items : [new Ext.grid.GridPanel({
//								id : 'grid1',
//								loadMask : true,
////								tbar : [{
////									xtype : 'textfield',
////									id : 'searchName',
////									emptyText : '请输入客户经理名称...',
////									width : 150
////								}, {
////									text : '搜索',
////									width : 45,
////									xtype : 'button',
////									handler : function() {
////										
////									}
////								}],
//								store : store1,
//								sm : sm,
//								cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
//									header : '客户经理名称',
//									width : 200,
//									dataIndex : 'name',
//									align : 'center'
//								}, {
//									header : '客户经理用户名',
//									width : 230,
//									dataIndex : 'username',
//									align : 'center'
//								}]),
//								bbar : paging
//							})],
//							buttons : [{
//								text : '确定',
//								handler : function() {
//									var mrecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
//									if (mrecords.length < 1) {
//										alert('请选择要做任务的客户经理!');
//										return;
//									}
//									var mids = '';
//									for (var j = 0; j < mrecords.length; j++) {
//										mids += ',' + mrecords[j].get('id');
//									}
//									
//									Ext.getCmp('bind').close();
//									Ext.getCmp('form').getForm().submit({
//										 waitTitle : '提示',
//									     waitMsg : '正在提交数据请稍后...',
//									     url : 'task.do?action=indevi',
//									     params : {
//									    	 mids : mids 
//									     },
//									     method : 'post',
//									     success : function(form, action) {
//									    	 alert(action.result.myHashMap.msg);
//									     },
//									     failure : function(form, action) {
//									    	 alert(action.result.myHashMap.msg);
//									     }
//									});
//									
//								}
//							}, {
//								text : '取消',
//								handler : function() {
//									Ext.getCmp('bind').close();
//								}
//							}]
//						});
//						win.show(Ext.getBody());
//						store1.load({
//							params : {
//								start : 0,
//								limit : 20
//							}
//						});
//						
//					}
				}, {
					text : '分组任务',
					handler : function() {
						if (!Ext.getCmp('form').getForm().isValid()) {
							alert('请正确填写表单');
							return;
						}
						
						var sm =  new Ext.grid.CheckboxSelectionModel();
						var store1 = new Ext.data.Store({
							proxy : new Ext.data.HttpProxy({
								url : 'customerGroup.do?action=queryAll'
							}),
							reader : new Ext.data.JsonReader({
								root : 'rows',
								totalProperty : 'total',
								id : 'id',
								fields : ['id', 'name']
							})
						});

						var paging = new Ext.PagingToolbar({
							pageSize : 20,
							store : store1,
							displayInfo : true,
							displayMsg : '当前显示数据 {0} - {1} of {2}',
							emptyMsg : '没有数据'
						});
						
						var win = new Ext.Window({
							title : '客户分组',
							id : 'bind',
							layout : 'fit',
							border : false,
							modal : true,
							width : 500,
							height : 400,
							items : [new Ext.grid.GridPanel({
								id : 'grid1',
								loadMask : true,
								store : store1,
								sm : sm,
								cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
									header : '客户分组名称',
									width : 200,
									dataIndex : 'name',
									align : 'center'
								}]),
								bbar : paging
							})],
							buttons : [{
								text : '确定',
								handler : function() {
									var grecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
									if (grecords.length < 1) {
										alert('请选择客户分组!');
										return;
									}
									var gids = '';
									for (var j = 0; j < grecords.length; j++) {
										gids += ',' + grecords[j].get('id');
									}
									
									Ext.getCmp('bind').close();
									Ext.getCmp('form').getForm().submit({
										 waitTitle : '提示',
									     waitMsg : '正在提交数据请稍后...',
									     url : 'task.do?action=group',
									     params : {
									    	 gids : gids 
									     },
									     method : 'post',
									     success : function(form, action) {
									    	 alert(action.result.myHashMap.msg);
									     },
									     failure : function(form, action) {
									    	 alert(action.result.myHashMap.msg);
									     }
									});
									
								}
							}, {
								text : '取消',
								handler : function() {
									Ext.getCmp('bind').close();
								}
							}]
						});
						win.show(Ext.getBody());
						store1.load({
							params : {
								start : 0,
								limit : 20,
								all : 0
							}
						});
						
					}
				}, {
					text : '客户任务',
					handler : function() {
						if (!Ext.getCmp('form').getForm().isValid()) {
							alert('请正确填写表单');
							return;
						}
						
						var store1 = new Ext.data.Store({
							proxy : new Ext.data.HttpProxy({
								url : 'customer.do?action=queryAll'
							}),
							reader : new Ext.data.JsonReader({
								root : 'rows',
								totalProperty : 'total',
								id : 'id',
								fields : ['id', 'name', 'number', 'sell_number', 'store_name', 'level', 'phone_number', 'manager', 
								          'backup_number', 'address', 'order_type', 'gps', 'last_visit_time']
							})
						});

						var paging = new Ext.PagingToolbar({
							pageSize : 20,
							store : store1,
							displayInfo : true,
							displayMsg : '当前显示数据 {0} - {1} of {2}',
							emptyMsg : '没有数据'
						});
						var sm =  new Ext.grid.CheckboxSelectionModel();
						var win1 = new Ext.Window({
							title : '选择客户',
							id : 'chooseCustomer',
							layout : 'fit',
							border : false,
							modal : true,
							width : 800,
							height : 600,
							items : [new Ext.grid.GridPanel({
								id : 'grid1',
								loadMask : true,
								store : store1,
								sm : sm,
								cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
									header : '客户名称',
									width : 100,
									dataIndex : 'name',
									sortable : true,
									remoteSort : true,
									align : 'center'
								}, {
									header : '客户编号',
									width : 130,
									dataIndex : 'number',
									sortable : true,
									remoteSort : true,
									align : 'center'
								}, {
									header : '专卖证号',
									width : 130,
									dataIndex : 'sell_number',
									sortable : true,
									remoteSort : true,
									align : 'center'
								}, {
									header : '店铺名称',
									width : 200,
									dataIndex : 'store_name',
									sortable : true,
									remoteSort : true,
									align : 'left'
								}, {
									header : '客户级别',
									width : 90,
									dataIndex : 'level',
									sortable : true,
									remoteSort : true,
									align : 'center'
								}, {
									header : '电话号码',
									width : 100,
									dataIndex : 'phone_number',
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
									header : '备用号码',
									width : 100,
									dataIndex : 'backup_number',
									sortable : true,
									remoteSort : true,
									align : 'center'
								}, {
									header : '经营地址',
									width : 240,
									dataIndex : 'address',
									sortable : true,
									remoteSort : true,
									align : 'left',
									renderer : function(value, meta) {
										meta.attr = 'title="' + value + '"';
										return value;
									}
								}, {
									header : '订货类型',
									width : 60,
									dataIndex : 'order_type',
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
									header : '最近一次拜访时间',
									width : 180,
									dataIndex : 'last_visit_time',
									sortable : true,
									remoteSort : true,
									align : 'center'
								}]),
								bbar : paging
							})],
							buttons : [{
								text : '确定',
								handler : function() {
									var crecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
									if (crecords.length < 1) {
										alert('请选择要拜访的客户!');
										return;
									}
									var size = crecords.length;
									var cids = "";
									for (var i = 0; i < size; i++) {
										cids += ',' + crecords[i].get('id');
									}
									
									Ext.getCmp('form').getForm().submit({
										 waitTitle : '提示',
									     waitMsg : '正在提交数据请稍后...',
									     url : 'task.do?action=customerTask',
									     params : {
									    	 cids : cids
									     },
									     method : 'post',
									     success : function(form, action) {
									    	 alert(action.result.myHashMap.msg);
									     },
									     failure : function(form, action) {
									    	 alert(action.result.myHashMap.msg);
									     }
									});
									
									Ext.getCmp('chooseCustomer').close();
								}
							}, {
								text : '取消',
								handler : function() {
									Ext.getCmp('chooseCustomer').close();
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
				}, {
					text : '自定义任务',
					handler : function() {
						if (!Ext.getCmp('form').getForm().isValid()) {
							alert('请正确填写表单');
							return;
						}
						
						var cids = '';
						var mid = '';
						
						var win = new Ext.Window({
							title : '自定义任务',
							id : 'editWin',
							layout : 'fit',
							border : false,
							modal : true,
							width : 500,
							height : 250,
							items : [new Ext.form.FormPanel({
								id : 'editForm',
								frame : true,
								bodyStyle : 'padding : 30px; 20px;',
								defaults : {
									msgTarget : 'under'
								},
								height : 'auto',
								labelWidth : 80,
								labelAlign : 'right',
								items : [{
									xtype : 'compositefield',
									width : 500,
									items : [{
										fieldLabel : '客户名称',
										xtype : 'textfield',
										id : 'customer',
										allowBlank : false,
										width : 300
									}, {
										text : '浏览…',
										xtype : 'button',
										handler : function() {
											// 选择客户
											var store1 = new Ext.data.Store({
												proxy : new Ext.data.HttpProxy({
													url : 'customer.do?action=queryAll'
												}),
												reader : new Ext.data.JsonReader({
													root : 'rows',
													totalProperty : 'total',
													id : 'id',
													fields : ['id', 'name', 'number', 'sell_number', 'store_name', 'level', 'phone_number', 'manager', 
													          'backup_number', 'address', 'order_type', 'gps', 'last_visit_time']
												})
											});

											var paging = new Ext.PagingToolbar({
												pageSize : 20,
												store : store1,
												displayInfo : true,
												displayMsg : '当前显示数据 {0} - {1} of {2}',
												emptyMsg : '没有数据'
											});
											var sm =  new Ext.grid.CheckboxSelectionModel();
											var win1 = new Ext.Window({
												title : '选择客户',
												id : 'chooseCustomer',
												layout : 'fit',
												border : false,
												modal : true,
												width : 800,
												height : 600,
												items : [new Ext.grid.GridPanel({
													id : 'grid1',
													loadMask : true,
													store : store1,
													sm : sm,
													cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
														header : '客户名称',
														width : 100,
														dataIndex : 'name',
														sortable : true,
														remoteSort : true,
														align : 'center'
													}, {
														header : '客户编号',
														width : 130,
														dataIndex : 'number',
														sortable : true,
														remoteSort : true,
														align : 'center'
													}, {
														header : '专卖证号',
														width : 130,
														dataIndex : 'sell_number',
														sortable : true,
														remoteSort : true,
														align : 'center'
													}, {
														header : '店铺名称',
														width : 200,
														dataIndex : 'store_name',
														sortable : true,
														remoteSort : true,
														align : 'left'
													}, {
														header : '客户级别',
														width : 90,
														dataIndex : 'level',
														sortable : true,
														remoteSort : true,
														align : 'center'
													}, {
														header : '电话号码',
														width : 100,
														dataIndex : 'phone_number',
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
														header : '备用号码',
														width : 100,
														dataIndex : 'backup_number',
														sortable : true,
														remoteSort : true,
														align : 'center'
													}, {
														header : '经营地址',
														width : 240,
														dataIndex : 'address',
														sortable : true,
														remoteSort : true,
														align : 'left',
														renderer : function(value, meta) {
															meta.attr = 'title="' + value + '"';
															return value;
														}
													}, {
														header : '订货类型',
														width : 60,
														dataIndex : 'order_type',
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
														header : '最近一次拜访时间',
														width : 180,
														dataIndex : 'last_visit_time',
														sortable : true,
														remoteSort : true,
														align : 'center'
													}]),
													bbar : paging
												})],
												buttons : [{
													text : '确定',
													handler : function() {
														var crecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
														if (crecords.length < 1) {
															alert('请选择要拜访的客户!');
															return;
														}
														var size = crecords.length;
														var cnames = '';
														for (var i = 0; i < size; i++) {
															cids += ',' + crecords[i].get('id');
															cnames += ',' + crecords[i].get('name');
														}
														Ext.getCmp('customer').setValue(cnames.substring(1));
														
														Ext.getCmp('chooseCustomer').close();
													}
												}, {
													text : '取消',
													handler : function() {
														Ext.getCmp('chooseCustomer').close();
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
									xtype : 'compositefield',
									width : 500,
									items : [{
										fieldLabel : '客户经理',
										xtype : 'textfield',
										id : 'manager',
										allowBlank : false,
										width : 300
									}, {
										text : '浏览…',
										xtype : 'button',
										handler : function() {
											// 选择客户经理
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
															alert('请选择客户经理!');
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
								}],
								buttons : [{
									text : '确定',
									handler : function() {
										Ext.getCmp('editWin').close();
										Ext.getCmp('form').getForm().submit({
											 waitTitle : '提示',
										     waitMsg : '正在提交数据请稍后...',
										     url : 'task.do?action=newCustomerTask',
										     params : {
										    	 mid : mid,
										    	 cids : cids,
										     },
										     method : 'post',
										     success : function(form, action) {
										    	 alert(action.result.myHashMap.msg);
										     },
										     failure : function(form, action) {
										    	 alert(action.result.myHashMap.msg);
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

					}
				}]
			
			});

			panel = new Ext.form.FormPanel({
				id : 'form',
				defaults : {
					width : 250,
					msgTarget : 'under'
				},
				bodyStyle : 'padding : 50px; 150px;',
				labelWidth : 80,
				labelAlign : 'right',
				tbar : [{
					xtype : 'button',
					id : 'ad',
					iconCls : 'add',
					text : '增加内容',
					align : 'right',
					width : 80,
					handler : function() {
						this.cn = this.cn + 1;
						var f = Ext.getCmp('form');
						var a = Ext.getCmp('ad');
						var t = Ext.getCmp('tsb');
						var c = new Ext.form.TextField({
							fieldLabel : '任务内容' + this.cn,
							allowBlank : false,
							name : 'content' + this.cn,
							id : 'content' + this.cn,
							xtype : 'textfield'
						});
						f.remove(t);
						f.add(c);
						f.add(n);
						f.doLayout();
					}, 
					scope : this
				}],
				items : [{
					fieldLabel : '任务起始时间',
					allowBlank : false,
					editable : false,
					name : 'start',
					id : 'start',
					xtype : 'datefield'
				}, {
					fieldLabel : '任务完成时间',
					allowBlank : false,
					editable : false,
					name : 'end',
					id : 'end',
					xtype : 'datefield'
				}, {
					fieldLabel : '任务标题',
					allowBlank : false,
					name : 'content',
					id : 'content',
					xtype : 'textfield'
				}, {
					fieldLabel : '任务内容' + this.cn,
					allowBlank : false,
					name : 'content' + this.cn,
					id : 'content' + this.cn,
					xtype : 'textfield'
				}, {
					xtype : 'button',
					id : 'tsb',
					text : '发放任务',
					align : 'right',
					width : 80,
					menu : [{
						text : '常规任务',
						handler : function() {
							if (!Ext.getCmp('form').getForm().isValid()) {
								alert('请正确填写表单');
								return;
							}
							Ext.getCmp('form').getForm().submit({
								 waitTitle : '提示',
							     waitMsg : '正在提交数据请稍后...',  
							     url : 'task.do?action=normal',  
							     method : 'post',
							     success : function(form, action) {
							    	 alert(action.result.myHashMap.msg);
							     },
							     failure : function(form, action) {
							    	 alert(action.result.myHashMap.msg);
							     }
							});
						}
//					}, {
//						text : '个别任务',
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
					}, {
						text : '分组任务',
						handler : function() {
							if (!Ext.getCmp('form').getForm().isValid()) {
								alert('请正确填写表单');
								return;
							}
							
							var sm =  new Ext.grid.CheckboxSelectionModel();
							var store1 = new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : 'customerGroup.do?action=queryAll'
								}),
								reader : new Ext.data.JsonReader({
									root : 'rows',
									totalProperty : 'total',
									id : 'id',
									fields : ['id', 'name']
								})
							});

							var paging = new Ext.PagingToolbar({
								pageSize : 20,
								store : store1,
								displayInfo : true,
								displayMsg : '当前显示数据 {0} - {1} of {2}',
								emptyMsg : '没有数据'
							});
							
							var win = new Ext.Window({
								title : '客户分组',
								id : 'bind',
								layout : 'fit',
								border : false,
								modal : true,
								width : 500,
								height : 400,
								items : [new Ext.grid.GridPanel({
									id : 'grid1',
									loadMask : true,
									store : store1,
									sm : sm,
									cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
										header : '客户分组名称',
										width : 200,
										dataIndex : 'name',
										align : 'center'
									}]),
									bbar : paging
								})],
								buttons : [{
									text : '确定',
									handler : function() {
										var grecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
										if (grecords.length < 1) {
											alert('请选择客户分组!');
											return;
										}
										var gids = '';
										for (var j = 0; j < grecords.length; j++) {
											gids += ',' + grecords[j].get('id');
										}
										
										Ext.getCmp('bind').close();
										Ext.getCmp('form').getForm().submit({
											 waitTitle : '提示',
										     waitMsg : '正在提交数据请稍后...',
										     url : 'task.do?action=group',
										     params : {
										    	 gids : gids 
										     },
										     method : 'post',
										     success : function(form, action) {
										    	 alert(action.result.myHashMap.msg);
										     },
										     failure : function(form, action) {
										    	 alert(action.result.myHashMap.msg);
										     }
										});
										
									}
								}, {
									text : '取消',
									handler : function() {
										Ext.getCmp('bind').close();
									}
								}]
							});
							win.show(Ext.getBody());
							store1.load({
								params : {
									start : 0,
									limit : 20,
									all : 0
								}
							});
							
						}
					}, {
						text : '客户任务',
						handler : function() {
							if (!Ext.getCmp('form').getForm().isValid()) {
								alert('请正确填写表单');
								return;
							}
							
							var store1 = new Ext.data.Store({
								proxy : new Ext.data.HttpProxy({
									url : 'customer.do?action=queryAll'
								}),
								reader : new Ext.data.JsonReader({
									root : 'rows',
									totalProperty : 'total',
									id : 'id',
									fields : ['id', 'name', 'number', 'sell_number', 'store_name', 'level', 'phone_number', 'manager', 
									          'backup_number', 'address', 'order_type', 'gps', 'last_visit_time']
								})
							});

							var paging = new Ext.PagingToolbar({
								pageSize : 20,
								store : store1,
								displayInfo : true,
								displayMsg : '当前显示数据 {0} - {1} of {2}',
								emptyMsg : '没有数据'
							});
							var sm =  new Ext.grid.CheckboxSelectionModel();
							var win1 = new Ext.Window({
								title : '选择客户',
								id : 'chooseCustomer',
								layout : 'fit',
								border : false,
								modal : true,
								width : 800,
								height : 600,
								items : [new Ext.grid.GridPanel({
									id : 'grid1',
									loadMask : true,
									store : store1,
									sm : sm,
									cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
										header : '客户名称',
										width : 100,
										dataIndex : 'name',
										sortable : true,
										remoteSort : true,
										align : 'center'
									}, {
										header : '客户编号',
										width : 130,
										dataIndex : 'number',
										sortable : true,
										remoteSort : true,
										align : 'center'
									}, {
										header : '专卖证号',
										width : 130,
										dataIndex : 'sell_number',
										sortable : true,
										remoteSort : true,
										align : 'center'
									}, {
										header : '店铺名称',
										width : 200,
										dataIndex : 'store_name',
										sortable : true,
										remoteSort : true,
										align : 'left'
									}, {
										header : '客户级别',
										width : 90,
										dataIndex : 'level',
										sortable : true,
										remoteSort : true,
										align : 'center'
									}, {
										header : '电话号码',
										width : 100,
										dataIndex : 'phone_number',
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
										header : '备用号码',
										width : 100,
										dataIndex : 'backup_number',
										sortable : true,
										remoteSort : true,
										align : 'center'
									}, {
										header : '经营地址',
										width : 240,
										dataIndex : 'address',
										sortable : true,
										remoteSort : true,
										align : 'left',
										renderer : function(value, meta) {
											meta.attr = 'title="' + value + '"';
											return value;
										}
									}, {
										header : '订货类型',
										width : 60,
										dataIndex : 'order_type',
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
										header : '最近一次拜访时间',
										width : 180,
										dataIndex : 'last_visit_time',
										sortable : true,
										remoteSort : true,
										align : 'center'
									}]),
									bbar : paging
								})],
								buttons : [{
									text : '确定',
									handler : function() {
										var crecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
										if (crecords.length < 1) {
											alert('请选择要拜访的客户!');
											return;
										}
										var size = crecords.length;
										var cids = "";
										for (var i = 0; i < size; i++) {
											cids += ',' + crecords[i].get('id');
										}
										
										Ext.getCmp('form').getForm().submit({
											 waitTitle : '提示',
										     waitMsg : '正在提交数据请稍后...',
										     url : 'task.do?action=customerTask',
										     params : {
										    	 cids : cids
										     },
										     method : 'post',
										     success : function(form, action) {
										    	 alert(action.result.myHashMap.msg);
										     },
										     failure : function(form, action) {
										    	 alert(action.result.myHashMap.msg);
										     }
										});
										
										Ext.getCmp('chooseCustomer').close();
									}
								}, {
									text : '取消',
									handler : function() {
										Ext.getCmp('chooseCustomer').close();
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
					}, {
						text : '自定义任务',
						handler : function() {
							if (!Ext.getCmp('form').getForm().isValid()) {
								alert('请正确填写表单');
								return;
							}
							
							var cids = '';
							var mid = '';
							
							var win = new Ext.Window({
								title : '自定义任务',
								id : 'editWin',
								layout : 'fit',
								border : false,
								modal : true,
								width : 500,
								height : 250,
								items : [new Ext.form.FormPanel({
									id : 'editForm',
									frame : true,
									bodyStyle : 'padding : 30px; 20px;',
									defaults : {
										msgTarget : 'under'
									},
									height : 'auto',
									labelWidth : 80,
									labelAlign : 'right',
									items : [{
										xtype : 'compositefield',
										width : 500,
										items : [{
											fieldLabel : '客户名称',
											xtype : 'textfield',
											id : 'customer',
											allowBlank : false,
											width : 300
										}, {
											text : '浏览…',
											xtype : 'button',
											handler : function() {
												// 选择客户
												var store1 = new Ext.data.Store({
													proxy : new Ext.data.HttpProxy({
														url : 'customer.do?action=queryAll'
													}),
													reader : new Ext.data.JsonReader({
														root : 'rows',
														totalProperty : 'total',
														id : 'id',
														fields : ['id', 'name', 'number', 'sell_number', 'store_name', 'level', 'phone_number', 'manager', 
														          'backup_number', 'address', 'order_type', 'gps', 'last_visit_time']
													})
												});

												var paging = new Ext.PagingToolbar({
													pageSize : 20,
													store : store1,
													displayInfo : true,
													displayMsg : '当前显示数据 {0} - {1} of {2}',
													emptyMsg : '没有数据'
												});
												var sm =  new Ext.grid.CheckboxSelectionModel();
												var win1 = new Ext.Window({
													title : '选择客户',
													id : 'chooseCustomer',
													layout : 'fit',
													border : false,
													modal : true,
													width : 800,
													height : 600,
													items : [new Ext.grid.GridPanel({
														id : 'grid1',
														loadMask : true,
														store : store1,
														sm : sm,
														cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), sm, {
															header : '客户名称',
															width : 100,
															dataIndex : 'name',
															sortable : true,
															remoteSort : true,
															align : 'center'
														}, {
															header : '客户编号',
															width : 130,
															dataIndex : 'number',
															sortable : true,
															remoteSort : true,
															align : 'center'
														}, {
															header : '专卖证号',
															width : 130,
															dataIndex : 'sell_number',
															sortable : true,
															remoteSort : true,
															align : 'center'
														}, {
															header : '店铺名称',
															width : 200,
															dataIndex : 'store_name',
															sortable : true,
															remoteSort : true,
															align : 'left'
														}, {
															header : '客户级别',
															width : 90,
															dataIndex : 'level',
															sortable : true,
															remoteSort : true,
															align : 'center'
														}, {
															header : '电话号码',
															width : 100,
															dataIndex : 'phone_number',
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
															header : '备用号码',
															width : 100,
															dataIndex : 'backup_number',
															sortable : true,
															remoteSort : true,
															align : 'center'
														}, {
															header : '经营地址',
															width : 240,
															dataIndex : 'address',
															sortable : true,
															remoteSort : true,
															align : 'left',
															renderer : function(value, meta) {
																meta.attr = 'title="' + value + '"';
																return value;
															}
														}, {
															header : '订货类型',
															width : 60,
															dataIndex : 'order_type',
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
															header : '最近一次拜访时间',
															width : 180,
															dataIndex : 'last_visit_time',
															sortable : true,
															remoteSort : true,
															align : 'center'
														}]),
														bbar : paging
													})],
													buttons : [{
														text : '确定',
														handler : function() {
															var crecords = Ext.getCmp('grid1').getSelectionModel().getSelections();
															if (crecords.length < 1) {
																alert('请选择要拜访的客户!');
																return;
															}
															var size = crecords.length;
															var cnames = '';
															for (var i = 0; i < size; i++) {
																cids += ',' + crecords[i].get('id');
																cnames += ',' + crecords[i].get('name');
															}
															Ext.getCmp('customer').setValue(cnames.substring(1));
															
															Ext.getCmp('chooseCustomer').close();
														}
													}, {
														text : '取消',
														handler : function() {
															Ext.getCmp('chooseCustomer').close();
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
										xtype : 'compositefield',
										width : 500,
										items : [{
											fieldLabel : '客户经理',
											xtype : 'textfield',
											id : 'manager',
											allowBlank : false,
											width : 300
										}, {
											text : '浏览…',
											xtype : 'button',
											handler : function() {
												// 选择客户经理
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
																alert('请选择客户经理!');
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
									}],
									buttons : [{
										text : '确定',
										handler : function() {
											Ext.getCmp('editWin').close();
											Ext.getCmp('form').getForm().submit({
												 waitTitle : '提示',
											     waitMsg : '正在提交数据请稍后...',
											     url : 'task.do?action=newCustomerTask',
											     params : {
											    	 mid : mid,
											    	 cids : cids,
											     },
											     method : 'post',
											     success : function(form, action) {
											    	 alert(action.result.myHashMap.msg);
											     },
											     failure : function(form, action) {
											    	 alert(action.result.myHashMap.msg);
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

						}
					}]
				}]
			});
		}
		return panel;
	}
	

	
};
