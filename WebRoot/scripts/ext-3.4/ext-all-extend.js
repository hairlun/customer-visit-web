Ext.ns("Ext.haode");

Ext.QuickTips.init();

Ext.BLANK_IMAGE_URL  = "scripts/ext-3.4/resources/images/default/s.gif";

//Ext.util.CSS.swapStyleSheet('theme', Ext.haode.contextPath + "/scripts/ext-3.4/resources/css/xtheme-access.css");

/**
 * 数组去重
 */
Array.prototype.distinct = function() {
	var self = this, arr = self.concat().sort(); // 创建一个新数组并排序

	arr.sort(function(a, b) {
		if (a === b) {
			var n = self.indexOf(a); // 获取索引值
			self.splice(n, 1);
		}
	});
	return self;
};


Ext.haode.getCookie = function(name){
	var nameEQ = "yk-" + name + "=";
	var ca = document.cookie.split(";");
	for (var i = 0;i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' '){
			c = c.substring(1, c.length);
		}
		if (c.indexOf(nameEQ) == 0){
			return c.substring(nameEQ.length, c.length);
		}
	}
	return null;
};

Ext.haode.setCookie = function(name, value, days){
	var expires = "";
	if (days) {
	    var date = new Date();
	    date.setTime(date.getTime()+(days*24*60*60*1000));
	    expires = "; expires=" + date.toGMTString();
	}
    document.cookie = "yk-" + name + "=" + value + expires + "; path=/";
}

//获得一个ajax信息的简写
Ext.haode.getMsgStore = function(url){
	return new Ext.data.Store({
        proxy: new Ext.data.HttpProxy(new Ext.data.Connection({
			url: url,
       	 	timeout: 1800000
        })),
        reader: new Ext.data.JsonReader({
                root: 'response', 
			    totalProperty: 'totalCount', 
			    id: 'action' 
           }, [
               {name: 'action', mapping : "action"},
               {name:'msg', mapping : "msg"},
               {name:'status', mapping : "status"},
               {name:'content',mapping:'content'}
           ])
    });
};

/**
 * ajax请求方法，包装了Ext.Ajax.request
 * @param config 配置对象
 * 		和Ext.Ajax.request的config对象一样，只是success函数的参数做了处理
 * 		success: function(json, options)
 * 第一个参数为json对象，第二个参数options和原来一样
 * json为后台发过来的数据，已解析为对象, json中有两个固定的属性为：
 * {
 * 		success Boolean 是否成功（业务逻辑）
 * 		msg String 返回的消息
 * }
 * 可能还有其他的属性，具体看后台传递什么
 */
Ext.haode.ajax = function(config){
	var c = config, s = config.success;
	c.success = function(response, options){
		var json = Ext.decode(response.responseText);
		if (json.success === false) {
			alert('出错了： ' + json.msg + '\t');
			return;
		}
		s.call(config.scope || this, json, options);
	};
	Ext.Ajax.request(c);
};

Ext.haode.showSubFuncs = function(menuId, toolbar){
	var ds = new Ext.data.Store({
		proxy: new Ext.data.HttpProxy({url: "menu.do?action=getSubFuncs&menuId=" + menuId}),
		reader: new Ext.data.JsonReader({
			root: "rows",
			totalProperty: 'totalcount'
			}, [
			{name: "menuId"},
			{name: "name"}
		])
	});
	ds.on("load", function(){
		var totalcount = ds.getCount();
		for (var i = 0; i < totalcount; i++) {
			var menuId = ds.getAt(i).data.menuId;
			var sArray = menuId.split(".");
			var funcId = sArray[1];
			if (toolbar.items.get(funcId)) {
				toolbar.items.get(funcId).show();
			}
		}
	}, this);
	ds.load();
};

/*
 * author lijz
 * 
 * 重写Ext.data.JsonReader
 * 失去session时跳转页面
 */
Ext.override(Ext.data.JsonReader, {
	read : function(response){
        var json = response.responseText;
        if(json.indexOf("<html>") == 2){
//        	window.top.location = Ext.haode.contextPath + "/index.jsp";
        	return;
        }
        var o = eval("(" + json + ")");
        if (!o) {
            throw {message: "JsonReader.read: Json object not found"};
        }
        if (o.metaData) {
            delete this.ef;
            this.meta = o.metaData;
            this.recordType = Ext.data.Record.create(o.metaData.fields);
            this.onMetaChange(this.meta, this.recordType, o);
        }
        return this.readRecords(o);
    }
})


// 改变表格单元格选择的样式
Ext.override(Ext.grid.GridView, {
	onCellSelect : function(row, col){
	    var cell = this.getCell(row, col);
	    if(cell){
	        this.fly(cell).addClass("x-grid3-cell-selected");
	    }
	    
	    if(row){
	    	this.addRowClass(row, "x-grid3-row-selected");
	    }
	},
	onCellDeselect : function(row, col){
        var cell = this.getCell(row, col);
        if(cell){
            this.fly(cell).removeClass("x-grid3-cell-selected");
        }
        if(row){
        	 this.removeRowClass(row, "x-grid3-row-selected");
        }
    },
    
    handleHdDown : function(e, target) {
        if (Ext.fly(target).hasClass('x-grid3-hd-btn')) {
            e.stopEvent();
            
            var colModel  = this.cm,
                header    = this.findHeaderCell(target),
                index     = this.getCellIndex(header),
                sortable  = colModel.isSortable(index),
                menu      = this.hmenu,
                menuItems = menu.items,
                menuCls   = this.headerMenuOpenCls;
            
            this.hdCtxIndex = index;
            
            Ext.fly(header).addClass(menuCls);
            menuItems.get('asc').setDisabled(!sortable);
            menuItems.get('desc').setDisabled(!sortable);
            
            menuItems.get('asc').setVisible(sortable);
            menuItems.get('desc').setVisible(sortable);
            
            menu.on('hide', function() {
                Ext.fly(header).removeClass(menuCls);
            }, this, {single:true});
            
            menu.show(target, 'tl-bl?');
        }
    }
	
});

Ext.override(Ext.form.TextArea, {
	fireKey : function(e){
	    //if(e.isSpecialKey() && (this.enterIsSpecial || (e.hasModifier()))){
	        this.fireEvent("specialkey", this, e);
	    //}
	}
});


Ext.override(Ext.grid.ColumnModel, {
	
	//获得列是否可编辑
	getEditable : function(col){
		return this.config[col].editable;
	}

});


//改变表格单元格选择的样式
Ext.override(Ext.grid.CellSelectionModel, {
	
	onEditorKey: function(field, e){
	    if(e.getKey() == e.TAB){
	        this.handleKeyDown(e);
	        return;
	    } else if (e.getKey() == e.ENTER) {	//回车跳到下个单元格
	    	var s = this.selection
		    if (s) {
		    	var f = "right"
		    	if (this.grid.keyModel == 'access') {
		    		f = "right";
		    	} else if (this.grid.keyModel == 'excel') {
		    		f = "down";
		    	}
		    	
		    	var cell = s.cell;  // currently selected cell
			    var r = cell[0];    // current row
			    var c = cell[1];
		    	this.foucsNextCell(r, c, f);
		    }
	    }
	},
	
	//跳转下个单元格
	foucsNextCell : function(r, c, f){
		var newR = r;
		var newC = c;
		var newF = 1;
		
		if (f == "right") {
			newC = c + 1;
		} else if (f == "left") {
			newC = c - 1;
			newF = -1;
		} else if (f == "up"){
			newR = r - 1;
			newF = -1;
		} else if (f == "down") {
			
			var cm  = this.grid.colModel,
	        clen = cm.getColumnCount(),
	        ds = this.grid.store,
	        rlen = ds.getCount();
			
			newR = r + 1;
			if (newR >= rlen) {
				newR = 0;
				newC = c + 1;
			}
		}
		
	    var newCell = this.grid.walkCells(
	    	newR,
	    	newC,
	    	newF,
            this.grid.isEditor && this.grid.editing ? this.acceptsNav : this.isSelectable, // *** handle tabbing while editorgrid is in edit mode
            this
        );
	    if(newCell){
	        // *** reassign r & c variables to newly-selected cell's row and column
	        r = newCell[0];
	        c = newCell[1];
	
	        var g = this.grid;
	        if (g.getColumnModel().isCellEditable(c, r)) {
	        	window.setTimeout(function(){
		            g.startEditing(r, c);
				}, 100);
	        } else {
	        	this.foucsNextCell(r, c, f);
	        } 
	    }
	}
});

/**
 * 使GridPanel单元格中文字可以复制
 */
/*
{
	if  (!Ext.grid.GridView.prototype.templates) {    
	    Ext.grid.GridView.prototype.templates = {};    
	}    
	Ext.grid.GridView.prototype.templates.cell =  new Ext.Template(    
	    ' <td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,    
	    ' <div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value} </div>' ,    
	    ' </td>'    
	);
}
*/

//Ext.override(Ext.form.DateField, {
//	
//	fullParseDate : function(value) {
//		var v = "";
//		if (value != "") {
//			v = value.replaceAll("-", "");
//	    	v = v.replaceAll("/", "");
//	    	v = v.replaceAll("\\.", "");
//	    	v = v.replaceAll(" ", "");
//	    	v = v.replaceAll(":", "");
//	    	for (var i = v.length + 1; i <= 14; i++) {
//	    		if (i == 6 || i == 8) {
//	    			v += "1";
//	    		} else {
//	    			v += "0";
//	    		}
//	    	}
//		}
//		
//		
//		var sMonth = v.substr(4, 2);
//		var sDay = v.substr(6, 2);
//		if (sMonth > "12") {
//			alert("日期录入非法！");
//			return null;
//		}
//		if (sMonth == '01' || sMonth == '03' || sMonth == '05' || sMonth == '07' || sMonth == '08' || sMonth == '10' || sMonth == '12') {
//			if (sDay > "31") {
//				alert("日期录入非法！");
//				return null;
//			}
//		} else if (sMonth == '04' || sMonth == '06' || sMonth == '09' || sMonth == '11') {
//			if (sDay > "30") {
//				alert("日期录入非法！");
//				return null;
//			}
//		} else if (sMonth == '02') {
//			if (sDay > "28") {
//				alert("日期录入非法！");
//				return null;
//			}
//		}
//		
//		return Date.parseDate(v, 'YmdHis');
//	},
//	
//	beforeBlur : function(){
//	    var d = this.fullParseDate(this.getRawValue());
//	    if (d) {
//	    	var value = d.format('Y-m-d H:i:s');
//	        this.value = value;
//	        this.setValue(value);
//	        var rawValue = d.format(this.format);
//	        this.setRawValue(rawValue);
//	    } else {
//	    	this.value = "";
//			this.setValue("");
//			this.setRawValue("");
//	    }
//	}
//});

String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1,"gm"), s2);
};

/**
 * @author yaolf
 * @date 2011-7-21
 * @param {Object} key  搜索的关键字
 * 
 * 为树添加搜索方法，如果没有配置searchConfig，默认搜索的是树节点的Text属性
 * searchConfig有attributes属性，值为数组，表示要在哪些属性中进行搜索，例如：
 * searchConfig: {
 * 	   attributes: ['text', 'name']
 * }
 */
Ext.override(Ext.tree.TreePanel, {
	search: function(key){
		var se = this.searchEngine;
		if (se && se.key === key) {
			var fn = se.foundNodes;
			for (var i = 0; i < fn.length; i++) {
				if (se.activeNode === fn[i].id) {
					var nn = ((i == fn.length - 1) ? fn[0] : fn[++i]);
					se.activeNode = nn.id;
					break;
				}
			}
			this.searchEngine.activeNode = se.activeNode;
		}
		else {
			se = this.searchEngine = this.createSearchEngine(key);
		}
		var activeNode = this.getNodeById(se.activeNode);
		if (activeNode) {
			try {
				this.expandPath(activeNode.getPath("id"), "id", function(s, o){
					if (s) { o.select(); }
				});
			} catch(err) {
			}
		}
	},
	
	searchMatchNodes: function(key){
		var attrs = this.searchConfig.attributes || ['text'];
		function contains(n, k) {
			var regexp = new RegExp(k, "i");
			for (var i = 0; i < attrs.length; i++) {
				var value = new String(n.attributes[attrs[i]]);
				if (value && value.search(regexp) != -1) {
					return true;
				}
			}
			return false;
		}
		
		var foundNodes = [];
		this.root.cascade(function(n){
			if (contains(n, key)) {
				foundNodes.push(n);
			}
		});
		return foundNodes;
	},
	
	createSearchEngine: function(key){
		var sc = this.searchConfig = this.searchConfig || {},
			se = {};
		se.searchConfig = sc;
		se.key = key;
		var fn = this.searchMatchNodes(key);
		se.foundNodes = fn;
		se.activeNode = fn.length > 0 ? fn[0].id : undefined;
		return se;
	}
});

Ext.Ajax.on('requestexception', function(conn, response, options){
	try {
		var error = Ext.decode(response.responseText);
		if (!error)
			return;
		
		alert('出错了： ' + error.msg);
		
//		var msg = ['噢，出错了！！！\t\n--------------------------------------------------------\n错误信息：',  
//		           error.msg, '\n　控制器：', error.controller, '\n　错误行：', error.line, '\n　动　作：', 
//		           error.action, '\n'].join('');
		
		if (error.type == 'sessiontimeout') {
			window.location.href = window.location.protocol + '//' + window.location.host + contextPath;
		}
	} catch(err) {
	}
});


/**
 * 弹出大文本框
 */
Ext.haode.showTextAreaDlg = function(obj){
	var a = Ext.get(obj).query("span[class=displaytext]");
	if (a.length == 0) {
		return;
	}
	var txtObj = a[0];
	var v = txtObj.innerHTML;
	window.showModalDialog("common/textarea_field/index.jsp", {value:v, readOnly:true}, "dialogWidth=500px;dialogHeight=270px");
};

/**
 * gridpanel 默认有斑马线和列分割线
 */
Ext.override(Ext.grid.GridPanel, {
	columnLines: true,
	stripeRows: true
});

/**
 * 分页条增加每页显示多少条
 */
Ext.override(Ext.PagingToolbar, {
	initComponent : function() {
		// TODO read from cookie
		var defaultPageSize = [50, 100, this.pageSize > 100 ? 100 : this.pageSize], pageSizeStore = [];
		defaultPageSize.distinct();
		
		for (var i = 0; i < defaultPageSize.length; i++) {
			var s = defaultPageSize[i];
			pageSizeStore.push([s, s]);
		}
		pageSizeStore.sort(function(i,j){
			return i[0] - j[0];
		});
		var pagingItems = [ this.first = new Ext.Toolbar.Button({
			tooltip : this.firstText,
			overflowText : this.firstText,
			iconCls : 'x-tbar-page-first',
			disabled : true,
			handler : this.moveFirst,
			scope : this
		}), this.prev = new Ext.Toolbar.Button({
			tooltip : this.prevText,
			overflowText : this.prevText,
			iconCls : 'x-tbar-page-prev',
			disabled : true,
			handler : this.movePrevious,
			scope : this
		}), '-', '每页', new Ext.form.ComboBox({
			store : new Ext.data.ArrayStore({
				fields : [ 'id', 'value' ],
				data : pageSizeStore
			}),
			typeAhead: false,
			triggerAction: 'all',
			mode : 'local',
			valueField : 'id',
			displayField : 'value',
			value: this.pageSize,
			cls : 'x-tbar-page-number',
			selectOnFocus : true,
			submitValue : false,
			enableKeyEvents: true,
			minChars: 4,
			listeners : {
				select : function(t) {
					this.onPageSizeChange(t, t.getValue());
				},
				keydown : function(t, e) {
					if (e.getKey() == e.ENTER) {
						this.onPageSizeChange(t, t.getRawValue(), t.getValue());
					}
				},
				scope : this
			}
		}), '条', this.beforePageText,
				this.inputItem = new Ext.form.NumberField({
					cls : 'x-tbar-page-number',
					allowDecimals : false,
					allowNegative : false,
					enableKeyEvents : true,
					selectOnFocus : true,
					submitValue : false,
					listeners : {
						scope : this,
						keydown : this.onPagingKeyDown,
						blur : this.onPagingBlur
					}
				}), this.afterTextItem = new Ext.Toolbar.TextItem({
					text : String.format(this.afterPageText, 1)
				}), '-', this.next = new Ext.Toolbar.Button({
					tooltip : this.nextText,
					overflowText : this.nextText,
					iconCls : 'x-tbar-page-next',
					disabled : true,
					handler : this.moveNext,
					scope : this
				}), this.last = new Ext.Toolbar.Button({
					tooltip : this.lastText,
					overflowText : this.lastText,
					iconCls : 'x-tbar-page-last',
					disabled : true,
					handler : this.moveLast,
					scope : this
				}), '-', this.refresh = new Ext.Toolbar.Button({
					tooltip : this.refreshText,
					overflowText : this.refreshText,
					iconCls : 'x-tbar-loading',
					handler : this.doRefresh,
					scope : this
				}) ];

		var userItems = this.items || this.buttons || [];
		if (this.prependButtons) {
			this.items = userItems.concat(pagingItems);
		} else {
			this.items = pagingItems.concat(userItems);
		}
		delete this.buttons;
		if (this.displayInfo) {
			this.items.push('->');
			this.items
					.push(this.displayItem = new Ext.Toolbar.TextItem(
							{}));
		}
		Ext.PagingToolbar.superclass.initComponent.call(this);
		this.addEvents('change', 'beforechange');
		this.on('afterlayout', this.onFirstLayout, this, {
			single : true
		});
		this.cursor = 0;
		this.bindStore(this.store, true);
	},
	// how many records in per page
	onPageSizeChange : function(t, nv, ov) {
		if (!/^[0-9]*[1-9][0-9]*$/.test(nv)) {
			alert('请输入正确的数字(正整数)！\t');
			t.setValue(ov);
			t.blur();
			return;
		}
		var v = parseInt(nv, 10);
		if (v > 100) {
			v = 100;
		}
		t.setValue(v);
		this.pageSize = v;
		this.moveFirst();
	},
	// override the doLoad method, use the params in store.lastOptions to load 
	doLoad : function(start){
        var o = this.store.lastOptions.params, pn = this.getParams();
        o[pn.start] = start;
        o[pn.limit] = this.pageSize;
        if(this.fireEvent('beforechange', this, o) !== false){
            this.store.load({params:o});
        }
    }
});

Ext.util.Format.comboRenderer = function(combo){
    return function(value){
        var record = combo.findRecord(combo.valueField, value);
        return record ? record.get(combo.displayField) : combo.valueNotFoundText;
    }
};

Ext.override(Ext.DataView, {
	loadingText: '正在加载，请稍后...'
});

/**
 * @config columnLocked 当Ext.grid.CheckboxSelectionModel用在Ext.ux.grid.LockingColumnModel中时
 * 需要配置该配置项取代locked来锁定该列
 */
Ext.override(Ext.grid.CheckboxSelectionModel, {
   initEvents : function(){
        Ext.grid.CheckboxSelectionModel.superclass.initEvents.call(this);
        this.grid.on('render', function(){
        	if (this.columnLocked === true) {
        		Ext.fly(this.grid.getView().lockedHd).on('mousedown', this.onHdMouseDown, this);
        	} else {
        		Ext.fly(this.grid.getView().innerHd).on('mousedown', this.onHdMouseDown, this);
        	}
        }, this);
    }
});


Ext.override(Ext.form.Field, {
	 alignErrorEl : function(){
		 var ps = this.getErrorCt().getWidth(true);
		 if (ps <= 0) {
			 return;
		 }
	     this.errorEl.setWidth(ps - 20);
	 }
});

/**
 * 表格内容可以复制
 */
//if  (!Ext.grid.GridView.prototype.templates) {    
//    Ext.grid.GridView.prototype.templates = {};    
//}    
//Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(    
//     '<td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,    
//     '<div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value}</div>' ,    
//     '</td>'    
//); 


Ext.haode.log = function(msg){
	if (Ext.haode.log.isDebugEnabled) {
		if (console) {
			console.log(msg);
		}
	}
}
Ext.haode.log.isDebugEnabled = false;
document.onkeydown = function(event){
	if (event.ctrlKey && event.shiftKey && event.keyCode == 76) {
		Ext.haode.log.isDebugEnabled = !Ext.haode.log.isDebugEnabled;
		console.log(Ext.haode.log.isDebugEnabled ? 'log on' : 'log off');
	}
}