Ext.form.TreeField = Ext.extend(Ext.form.TriggerField,  {
	
	minListWidth: 120,
	
	minListHeight: 150,
   
    // private
    defaultAutoCreate : {tag: "input", type: "text", size: "10", autocomplete: "off"},

    initComponent : function(){
        Ext.form.TreeField.superclass.initComponent.call(this);
        this.addEvents(
                'select'
        );
    },

    onTriggerClick : function(){
        if(this.disabled){
            return;
        }
       
        if(this.menu == null){
        	var lw = Math.max(this.wrap.getWidth(), this.minListWidth);
            this.menu = new Ext.menu.Menu({
				plain: false,
				width: lw,
				items: [{
					xtype: 'treepanel',
					border: false,
					width: lw - 5,
					height: this.minListHeight,
					autoScroll: true,
					loader: new Ext.tree.TreeLoader({
						dataUrl: this.dataUrl
					}),
					rootVisible: false,
					root: {
				        nodeType: 'async',
				        id: "_root",
				        text: "根节点",
				        draggable: false,
				        expanded: true
				    },
				    listeners: {
				    	click: function(node, e){
				    		var hiddenValue = node.attributes.hiddenValue ? node.attributes.hiddenValue : node.id;
				    		var dispValue = node.attributes.dispValue ? node.attributes.dispValue : node.text;
				    		var m = {hiddenValue: hiddenValue, dispValue: dispValue};
				    		this.setValue(dispValue);
				    		this.value = hiddenValue;
				    		this.fireEvent('select', this, m, node);
				    		this.menu.hide();
				    	},
				    	scope: this
				    }
				}]
            });
        }
        this.onFocus();
        
        this.menu.show(this.el, "tl-bl?");
    }
});
Ext.reg('treefield', Ext.form.TreeField);
