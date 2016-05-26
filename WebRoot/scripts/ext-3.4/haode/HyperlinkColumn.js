/**
 * @class HAODE.HyperlinkColumn
 * @cfg items {Array} the items config array, one of it can be like this:
 * {
 * 		text: String/Function the hyperlink text, if a function the arguments is all the same with renderer function,
 * 		hidden: hide this item, the value can be true, false or a function return true or fasle
 * 				the function with two arguments: record, column
 * 		handler: the function to call when click the link, the function has these arguments:
 * 				record, grid, rowIndex, colIndex, item, e
 * 		scope: the scope the function execute width
 * }
 */
HAODE.HyperlinkColumn = Ext.extend(Ext.grid.Column, {
	header: '&#160;',

    actionIdRe: /x-action-col-(\d+)/,
    
	constructor: function(config){
		var items = this.items = config.items || [];
		//this.scope = this;
		HAODE.HyperlinkColumn.superclass.constructor.call(this, config);
		
		this.renderer = function(v, meta, r){
			var tpl = [];
			for (var i = 0; i < items.length; i++) {
				var item = items[i];
				var h = item.hidden;
				var hidden = (h === true || (Ext.isFunction(h) && h.call(item, r, this) === true));
				if (hidden) {
					continue;
				}
				
				var text = item.text;
				if (Ext.isFunction(text)) {
					text = text.apply(this, arguments);
				}
				
				tpl.push('<a href="#" class="x-action-col-'+(i+1)+'" style="color:blue;text-decoration:underline;">'+text+'</a>');
				if (i < items.length - 1) {
					tpl.push('&nbsp;|&nbsp;');
				}
			}
			return tpl.join('');
		}
	},
	
	processEvent : function(name, e, grid, rowIndex, colIndex){
        var m = e.getTarget().className.match(this.actionIdRe),
            item, fn;
        if (m && (item = this.items[parseInt(m[1], 10)-1])) {
            if (name == 'click') {
            	var r = grid.getStore().getAt(rowIndex);
                (fn = item.handler || this.handler) && fn.call(item.scope||this.scope||this, r, grid, rowIndex, colIndex, item, e);
            } else if ((name == 'mousedown') && (item.stopSelection !== false)) {
                return false;
            }
        }
        return HAODE.HyperlinkColumn.superclass.processEvent.apply(this, arguments);
    }
});

Ext.grid.Column.types.hyperlinkcolumn = HAODE.HyperlinkColumn;
