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
				dataIndex : 'mname',
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
				header : 'GPS经度',
				width : 80,
				dataIndex : 'lng',
				align : 'center'
			}, {
				header : 'GPS纬度',
				width : 80,
				dataIndex : 'lat',
				align : 'center'
			}, {
				header : '最近一次拜访时间',
				width : 180,
				dataIndex : 'last_visit_time',
				sortable : true,
				remoteSort : true,
				align : 'center'
			}]);
			this.store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : 'customer.do?action=queryAll'
				}),
				reader : new Ext.data.JsonReader({
					root : 'rows',
					totalProperty : 'total',
					id : 'id',
					fields : ['id', 'number', 'name', 'sell_number', 'store_name', 'level', 'phone_number', 
					          'backup_number', 'address', 'order_type', 'lng', 'lat', 'mname', 'last_visit_time']
				}),
				remoteSort : true
//				autoLoad : true
			});
			this.groupStore = new Ext.data.Store({
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
			this.groupId = -1;

			var paging = new Ext.PagingToolbar({
				pageSize : 20,
				store : this.store,
				displayInfo : true,
				displayMsg : '当前显示数据 {0} - {1} of {2}',
				emptyMsg : '没有数据'
			});
			var tbar1 = new Ext.Toolbar([{
				text : '新增',
				iconCls : 'user',
				id : 'add',
				width : 45,
				xtype : 'button',
				handler : this.newCustomer,
				scope : this
			}, '-', {
				text : '编辑',
				iconCls : 'edit',
				width : 45,
				xtype : 'button',
				handler : this.editCustomer,
				scope : this
			}, '-', {
				text : '删除',
				iconCls : 'del',
				width : 45,
				xtype : 'button',
				handler : this.delCustomer,
				scope : this
			}, '-', {
				text : '查看二维码',
				iconCls : 'del',
				width : 45,
				xtype : 'button',
				handler : this.qrcode,
				scope : this
			}, '-', {
				text : '绑定客户经理',
				handler : this.binding,
				iconCls : "bind",
				scope : this
			}, '-', {
				text : '导出(Excel)',
				handler : this.exportAll,
				iconCls : "btn_page_excel",
				scope : this
			}, '-', {
				text : '导入(Excel)',
				iconCls : "btn_page_excel",
				width : 45,
				xtype : 'button',
				handler : this.importAll,
				scope : this
			}]);
			//创建Combobox
			var combobox = new Ext.form.ComboBox({
				id : "combo",
				store : this.groupStore,
				displayField : 'name',
				valueField : 'id',
				triggerAction : 'all',
				allowBlank : false,
				editable : false,
				listeners : {
					'select' : function(combo, record, index) {
						Ext.getCmp('grid').getStore().load({
							params : {
								start : 0,
								limit : 20,
								group : Ext.getCmp('combo').getValue()
							}
						});
					}
				}
			});
			this.groupStore.on('load', function() {
				combobox.setValue(-1);
			})
			var tbar2 = new Ext.Toolbar(['客户分组：', combobox/*, ' ', {
				text : '查询',
				xtype : 'button',
				handler : this.query,
				scope : this
			}*/]);
			panel = new Ext.grid.GridPanel({
				id : 'grid',
//				viewConfig : {
//					forcefit : true
//				},
				loadMask : true,
				tbar : tbar1,
				listeners : {
					'render' : function() {
						tbar2.render(this.tbar);
					}
				},
				store : this.store,
				sm : sm,
				cm : cm,
				bbar : paging
			});
			this.store.load({
				params : {
					start : 0,
					limit : 20,
					group : -1
				}
			});
			this.groupStore.load();
		}
		return panel;
	},
	
	qrcode : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length != 1) {
			alert('请选择一个要查看的客户');
			return;
		}
		
		new Ext.Window({
			title : '二维码',
			layout : 'fit',
			border : false,
			modal : true,
			width : 400,
			height : 400,
			items : [{  
			    xtype: 'box',
			    width: 100, 
			    height: 100,  
			    autoEl: {  
			        tag: 'img',
//			        src : 'theme/liantu.png'
			        src: 'customer.do?action=qrcode&number=' + records[0].get('number')
			    }  
			}]
		}).show(Ext.getBody());
	},

	query : function() {
		Ext.getCmp('grid').getStore().load({
			params : {
				start : 0,
				limit : 20,
				group : Ext.getCmp('combo').getValue()
			}
		});
	},

	newCustomer : function() {
		var win = new Ext.Window({
			title : '新增',
			id : 'addWin',
			layout : 'fit',
			border : false,
			modal : true,
			width : 370,
			height : 480,
			items : [new Ext.form.FormPanel({
				id : 'addForm',
				defaultType : 'textfield',
				bodyStyle : 'padding : 30px; 20px;',
				defaults : {
					width : 200,
					msgTarget : 'under'
				},
				labelWidth : 55,
				labelAlign : 'right',
				items : [{
					fieldLabel : '客户名称',
					xtype : 'textfield',
					id : 'name',
					allowBlank : false,
					blankText : '客户名称不能为空'
				}, {
					fieldLabel : '客户编号',
					allowBlank : false,
					xtype : 'textfield',
					id : 'number',
					blankText : '编号不能为空'
				}, {
					fieldLabel : '电话号码',
					xtype : 'numberfield',
					id : 'phoneNumber',
					allowBlank : false,
					blankText : '电话号码不能为空'
				}, {
					fieldLabel : '专卖证号',
					xtype : 'textfield',
					allowBlank : false,
					blankText : '专卖证号不能为空',
					id : 'sellNumber'
				}, {
					fieldLabel : '店铺名称',
					xtype : 'textfield',
					id : 'storeName'
				}, {
					fieldLabel : '备用号码',
					xtype : 'numberfield',
//					regex : /^1[3|4|5|8][0-9]\d{8}$|^$/,
//					regexText : '请输入正确的手机号',
					id : 'backupNumber'
				}, {
					fieldLabel : 'GPS(经度,纬度)',
					xtype : 'textfield',
					id : 'gps'
				}, {
					fieldLabel : '客户级别',
					xtype : 'textfield',
//					regex : /^[0-9a-zA-Z]{8}$/,
//					regexText : '请输入8个字母或数字组成',
					id : 'level'
				}, {
					fieldLabel : '订货类型',
					xtype : 'textfield',
					id : 'orderType'
//				}, new Ext.ux.form.DateTimeField({
//					fieldLabel : '拜访时间',
//					editable : false,
//					id : 'lastVisitTime'
				}, {
					fieldLabel : '经营地址',
					xtype : 'textarea',
					height : 60,
					id : 'address'
				}],
				buttons : [{
					text : '确定',
					handler : function() {
						if (!Ext.getCmp('addForm').getForm().isValid()) {
							alert('请输入合法的参数');
							return;
						}
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.haode.ajax({
							url : 'customer.do?action=addCustomer',
							params : {
								name : Ext.getCmp('name').getValue(),
								number : Ext.getCmp('number').getValue(),
								phoneNumber : Ext.getCmp('phoneNumber').getValue(),
								sellNumber : Ext.getCmp('sellNumber').getValue(),
								storeName : Ext.getCmp('storeName').getValue(),
								backupNumber : Ext.getCmp('backupNumber').getValue(),
								gps : Ext.getCmp('gps').getValue(),
								level : Ext.getCmp('level').getValue(),
								orderType : Ext.getCmp('orderType').getValue(),
//								lastVisitTime : Ext.getCmp('lastVisitTime').getValue(),
								address : Ext.getCmp('address').getValue()
							},
							callback : function() {
								Ext.MessageBox.hide();
							},
							success : function(json, opts) {
								Ext.MessageBox.hide();
								alert(json.myHashMap.msg);
								if (json.myHashMap.success) {
									Ext.getCmp('addWin').close();
//									if (window.confirm('是否现在为该客户绑定客户经理？')) {
										
//									} else {
										Ext.getCmp('grid').getStore().load({
											params : {
												start : 0,
												limit : 20,
												group : Ext.getCmp('combo').getValue()
											}
										});
//									}
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
	
	editCustomer : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length != 1) {
			alert('请选择一个要修改的客户');
			return;
		}
		var win = new Ext.Window({
			title : '编辑',
			id : 'editWin',
			layout : 'fit',
			border : false,
			modal : true,
			width : 370,
			height : 480,
			items : [new Ext.form.FormPanel({
				id : 'editForm',
				defaultType : 'textfield',
				bodyStyle : 'padding : 30px; 20px;',
				defaults : {
					width : 200,
					msgTarget : 'under'
				},
				labelWidth : 55,
				labelAlign : 'right',
				items : [{
					fieldLabel : '客户名称',
					xtype : 'textfield',
					id : 'ename',
					value : records[0].get('name'),
					allowBlank : false,
					blankText : '客户名称不能为空'
				}, {
					fieldLabel : '客户编号',
					allowBlank : false,
					xtype : 'textfield',
					id : 'enumber',
					value : records[0].get('number'),
					blankText : '编号不能为空'
				}, {
					fieldLabel : '电话号码',
					xtype : 'numberfield',
					id : 'ephoneNumber',
					allowBlank : false,
					value : records[0].get('phone_number'),
					blankText : '电话号码不能为空'
				}, {
					fieldLabel : '专卖证号',
					xtype : 'textfield',
					allowBlank : false,
					blankText : '专卖证号不能为空',
					id : 'esellNumber',
					value : records[0].get('sell_number')
				}, {
					fieldLabel : '店铺名称',
					xtype : 'textfield',
					id : 'estoreName',
					value : records[0].get('store_name')
				}, {
					fieldLabel : '备用号码',
					xtype : 'numberfield',
					id : 'ebackupNumber',
					value : records[0].get('backup_number')
				}, {
					fieldLabel : 'GPS(经度,纬度)',
					xtype : 'textfield',
					id : 'egps',
					value : records[0].get('gps')
				}, {
					fieldLabel : '客户级别',
					xtype : 'textfield',
//					regex : /^[0-9a-zA-Z]{8}$/,
//					regexText : '请输入8个字母或数字组成',
					id : 'elevel',
					value : records[0].get('level')
				}, {
					fieldLabel : '订单类型',
					xtype : 'textfield',
					id : 'eorderType',
					value : records[0].get('order_type')
				}, {
					fieldLabel : '经营地址',
					xtype : 'textarea',
					height : 60,
					id : 'eaddress',
					value : records[0].get('address')
				}],
				buttons : [{
					text : '确定',
					handler : function() {
						if (!Ext.getCmp('editForm').getForm().isValid()) {
							alert('请输入合法的参数');
							return;
						}
						Ext.MessageBox.wait('正在操作','请稍后...');
						Ext.haode.ajax({
							url : 'customer.do?action=editCustomer',
							params : {
								id : records[0].get('id'),
								name : Ext.getCmp('ename').getValue(),
								number : Ext.getCmp('enumber').getValue(),
								onumber : records[0].get('number'),
								phoneNumber : Ext.getCmp('ephoneNumber').getValue(),
								sellNumber : Ext.getCmp('esellNumber').getValue(),
								storeName : Ext.getCmp('estoreName').getValue(),
								backupNumber : Ext.getCmp('ebackupNumber').getValue(),
								gps : Ext.getCmp('egps').getValue(),
								level : Ext.getCmp('elevel').getValue(),
								orderType : Ext.getCmp('eorderType').getValue(),
								address : Ext.getCmp('eaddress').getValue()
							},
							callback : function() {
								Ext.MessageBox.hide();
							},
							success : function(json, opts) {
								Ext.MessageBox.hide();
								alert(json.myHashMap.msg);
								if (json.myHashMap.success) {
									Ext.getCmp('editWin').close();
//									if (window.confirm('是否现在为该客户绑定客户经理？')) {
										
//									} else {
										Ext.getCmp('grid').getStore().load({
											params : {
												start : 0,
												limit : 20,
												group : Ext.getCmp('combo').getValue()
											}
										});
//									}
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
	
	delCustomer : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length < 1) {
			alert('请选择要删除的客户');
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
			url : 'customer.do?action=delCustomers',
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
							group : Ext.getCmp('combo').getValue()
						}
					});
				}
			}
		});
	},

	binding : function() {
		var sm = Ext.getCmp('grid').getSelectionModel();
		var records = sm.getSelections();
		if (records.length < 1) {
			alert('请选择要绑定的客户');
			return;
		}
		
		var store1 = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
				url : 'customerManager.do?action=queryAll'
			}),
			reader : new Ext.data.JsonReader({
				root : 'rows',
				totalProperty : 'total',
				id : 'id',
				fields : ['id', 'name', 'username']
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
			title : '绑定',
			id : 'bind',
			layout : 'fit',
			border : false,
			modal : true,
			width : 500,
			height : 400,
			items : [new Ext.grid.GridPanel({
				id : 'grid1',
				loadMask : true,
//				tbar : [{
//					xtype : 'textfield',
//					id : 'searchName',
//					emptyText : '请输入客户经理名称...',
//					width : 150
//				}, {
//					text : '搜索',
//					width : 45,
//					xtype : 'button',
//					handler : function() {
//						
//					}
//				}],
				store : store1,
//				sm : ,
				cm : new Ext.grid.ColumnModel([new Ext.grid.RowNumberer({width:38}), {
					header : '客户经理名称',
					width : 200,
					dataIndex : 'name',
					align : 'center'
				}, {
					header : '客户经理用户名',
					width : 230,
					dataIndex : 'username',
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
					var cids = '';
					for (var j = 0; j < records.length; j++) {
						cids += ',' + records[j].get('id');
					}
					
					Ext.haode.ajax({
						url : 'customer.do?action=bindCustomer',
						params : {
							cids : cids,
							mid : mrecords[0].get('id')
						},
						success : function(json, opts) {
							alert(json.myHashMap.msg);
							if (json.myHashMap.success) {
								Ext.getCmp('bind').close();
								Ext.getCmp('grid').getStore().load({
									params : {
										start : 0,
										limit : 20,
										group : Ext.getCmp('combo').getValue()
									}
								});
							}
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
				limit : 20
			}
		});
	},
	
	exportAll : function() {
		Ext.MessageBox.wait('正在导出','请稍后...');
		Ext.haode.ajax({
			url : 'customer.do?action=export',
			method : 'post',
			success : function(json, opts) {
				Ext.MessageBox.hide();
//				alert(json.myHashMap.msg);
				new Ext.Window({
					width : 250,
					height : 80,
					title : '导出文件下载',
					html : "<a href='customer.do?action=downloadExport&res=" + json.myHashMap.msg + "'>请点击此处下载...</a>"
				}).show();
			},
			failure : function(json, opt) {
				Ext.MessageBox.hide();
				alert(json.myHashMap.msg);
			}
		});
	},
	
	importAll : function() {
		var win = new Ext.Window({
			layout : 'fit',
			modal : true,
			title : '导入',
			id : 'importWin',
			layout : 'fit',
			border : false,
			modal : true,
			width : 380,
			height : 170,
			items : [new Ext.form.FormPanel({
				id : 'importForm',
				defaultType : 'textfield',
				bodyStyle : 'padding : 30px; 20px;',
				defaults : {
					width : 200,
					msgTarget : 'under'
				},
				fileUpload : true,
				labelWidth : 100,
				labelAlign : 'right',
				items : [{
					inputType : 'file',
					id : 'file',
					name : 'file',
					fieldLabel : '选择Excel文件'
				}],
				buttons : [{
					text : '确定',
					handler : function() {
						var fileValue = Ext.getCmp('file').getValue();
						if (fileValue == null || fileValue.trim() == '') {
							alert('请选择要导入的Excel文件');
						}
						if (!fileValue.endWith('.xls') && !fileValue.endWith('.xlsx') && !fileValue.endWith('.xlsm') && !fileValue.endWith('.xltx')
								 && !fileValue.endWith('.xltm') && !fileValue.endWith('.xlsb') && !fileValue.endWith('.xlam')) {
							alert('请选择正确格式的Excel文件');
						}
						Ext.getCmp('importForm').getForm().submit({
							url : 'customer.do?action=import',
							method : 'post',
							waitTitle : '提示',
						    waitMsg : '正在提交数据请稍后...',
							success : function(form, action) {
								var text = action.response.responseText;
								if (text.indexOf('成功') > 0) {
									alert("导入成功！");
								} else {
									alert("导入失败！请检查导入文件格式！文件格式应与导出的文件列顺序相同！");
								}
								Ext.getCmp('importWin').close();
								Ext.getCmp('grid').getStore().load({
									params : {
										start : 0,
										limit : 20,
										group : Ext.getCmp('combo').getValue()
									}
								});
							},
							failure : function(form, action) {
								var text = action.response.responseText;
								if (text.indexOf('成功') > 0) {
									alert("导入成功！");
								} else {
									alert("导入失败！请检查导入文件格式！文件格式应与导出的文件列顺序相同！");
								}
								Ext.getCmp('importWin').close();
								Ext.getCmp('grid').getStore().load({
									params : {
										start : 0,
										limit : 20,
										group : Ext.getCmp('combo').getValue()
									}
								});
							}
						});
					}
				}, {
					text : '取消',
					handler : function() {
						Ext.getCmp('importWin').close();
					}
				}]
			})]
		});
		win.show(Ext.getBody());
	}
	
};
