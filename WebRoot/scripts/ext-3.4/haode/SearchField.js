Ext.namespace("Ext.haode.SearchField ");

Ext.haode.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
	
	searchHandler: undefined,
	
    validationEvent: false,
    
    validateOnBlur: false,
    
    trigger1Class: 'x-form-clear-trigger',
    
    trigger2Class: 'x-form-search-trigger',
    
    hideTrigger1: true,
    
    width: 180,
    
    hasSearch: false,
    
    paramName: 'query',
    
    emptyText: '请输入关键字',
    
    scope : this,
	
    initComponent : function(){
        Ext.haode.SearchField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },

    onTrigger1Click : function(){
        if(this.hasSearch){
            this.el.dom.value = '';
            this.triggers[0].hide();
            this.hasSearch = false;
            if(this.searchHandler){
            	this.searchHandler.call(this.scope, "");
            }
        }
    },

    onTrigger2Click : function(){
        var v = this.getRawValue();
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
        this.hasSearch = true;
        this.triggers[0].show();
        if(this.searchHandler){
			this.searchHandler.call(this.scope, v);
		}
		this.focus();
    }
});

Ext.reg('searchfield', Ext.haode.SearchField);
