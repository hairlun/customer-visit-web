/**
 * 带多态复选框的树节点
 * 你需要再TreePanel中配置stateConfig属性，例如：
 *  var tree = new Ext.tree.TreePanel({
        title: config.title,
        loader: loader,
		stateConfig: [{
			state: true,
			iconCls: 'x-tree-node-tristate-yes'	
		}, {
			state: false,
			iconCls: 'x-tree-node-tristate-no'
		}, {
			state: null,
			iconCls: 'x-tree-node-tristate-nvl'
		}]
    });
 */
Ext.namespace("HAODE");
HAODE.MultistateCheckTreeNodeUI = Ext.extend(Ext.tree.TreeNodeUI, {
	
    renderElements : function(n, a, targetNode, bulkRender){
        // add some indent caching, this helps performance when rendering a large tree
        this.indentMarkup = n.parentNode ? n.parentNode.ui.getChildIndent() : '';

        var state = a.state,
			stateObj = this.getState(state),
            nel,
            href = this.getHref(a.href);
		
		var checkboxMarkup = '';
		if (Ext.isDefined(state)) {
			checkboxMarkup = ['<img src="', this.emptyIcon, '" class="x-tree-node-multistate ', stateObj.iconCls, '" />'].join('');
		}
		
        var buf = ['<li class="x-tree-node"><div ext:tree-node-id="',n.id,'" class="x-tree-node-el x-tree-node-leaf x-unselectable ', a.cls,'" unselectable="on">',
            '<span class="x-tree-node-indent">',this.indentMarkup,"</span>",
            '<img alt="" src="', this.emptyIcon, '" class="x-tree-ec-icon x-tree-elbow" />',
            checkboxMarkup,
            '<img alt="" src="', a.icon || this.emptyIcon, '" class="x-tree-node-icon',(a.icon ? " x-tree-node-inline-icon" : ""),(a.iconCls ? " "+a.iconCls : ""),'" unselectable="on" />',
            '<a hidefocus="on" class="x-tree-node-anchor" href="',href,'" tabIndex="1" ',
             a.hrefTarget ? ' target="'+a.hrefTarget+'"' : "", '><span unselectable="on">',n.text,"</span></a></div>",
            '<ul class="x-tree-node-ct" style="display:none;"></ul>',
            "</li>"].join('');

        if(bulkRender !== true && n.nextSibling && (nel = n.nextSibling.ui.getEl())){
            this.wrap = Ext.DomHelper.insertHtml("beforeBegin", nel, buf);
        }else{
            this.wrap = Ext.DomHelper.insertHtml("beforeEnd", targetNode, buf);
        }
        this.elNode = this.wrap.childNodes[0];
        this.ctNode = this.wrap.childNodes[1];
        var cs = this.elNode.childNodes;
        this.indentNode = cs[0];
        this.ecNode = cs[1];
		
		var index = 2; 
		if (Ext.isDefined(state)) {
			this.checkbox = cs[index];
			this.checkbox.state = a.state;
			this.checkboxEl = Ext.get(this.checkbox);
			this.checkboxEl.on('click', function(e, target){
				
				this.toggleState();
			}, this);
	        // fix for IE6
	        //this.checkbox.defaultChecked = this.checkbox.checked;
			index++;
		}
		
        this.iconNode = cs[index];
        this.anchor = cs[index+1];
        this.textNode = cs[index+1].firstChild;
    },
	
	getStateConfg: function(){
		if (!Ext.isDefined(this.stateConfig)) {
			this.stateConfig = this.node.ownerTree.stateConfig;
		}
		return this.stateConfig;
	},
	
	getState : function(state){
		var sc = this.getStateConfg();
		for (var i = 0; i < sc.length; i++) {
			if (sc[i].state === state) {
				return sc[i];
			}
		}
		return {};
	},
	
	getNextState: function(){
		var sc = this.getStateConfg();
		for (var i = 0; i < sc.length; i++) {
			if (sc[i].state === this.node.attributes.state) {
				return sc[(i == (sc.length - 1)) ? 0 : ++i];
			}
		}
		return {};
	},
	
	/**
	 * 设置选中状态
	 * @param tree.stateConfig.item
	 */
	toggleState : function(state){
		var stateObj;
		if (!Ext.isDefined(state)) {
			stateObj = this.getNextState();
		} else  {
			stateObj = this.getState(state);
		}
		
		if (this.checkbox) {
			var oldStateObj = this.getState(this.checkbox.state);
			this.checkbox.state = stateObj.state;
			this.checkboxEl.replaceClass(oldStateObj.iconCls, stateObj.iconCls);
	    	this.onCheckChange();
		} else {
			if (this.node && Ext.isDefined(this.node.attributes)) {
				this.node.attributes.state = stateObj.state;		
        		this.fireEvent('checkchange', this.node, stateObj.state);	
			}
		}
    },
	
	// private
    onCheckChange : function(){
        var state = this.checkbox.state;
        // fix for IE6
        //this.checkbox.defaultChecked = state;
        this.node.attributes.state = state;
        this.fireEvent('checkchange', this.node, state);
    },
	
	getCheckedState: function(){
		if (this.checkbox) {
			return this.checkbox.state;
		}
		return this.node.attributes.state;
	}
	
	
});