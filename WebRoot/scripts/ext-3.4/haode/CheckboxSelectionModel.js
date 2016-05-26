/**
 * @class Ext.grid.CheckboxSelectionModel
 * @extends Ext.grid.RowSelectionModel
 * A custom selection model that renders a column of checkboxes that can be toggled to select or deselect rows.
 * @constructor
 * @param {Object} config The configuration options
 */
HAODE.CheckboxSelectionModel = Ext.extend(Ext.grid.CheckboxSelectionModel, {
	
	constructor : function(){
		HAODE.CheckboxSelectionModel.superclass.constructor.apply(this, arguments);
        
        this.on('beforerowselect', function(sm, rowIndex, keepExisting, record){
        	if (record.get('disabled')) {
        		return false;
        	}
        });
    },
	
    // private
    onMouseDown : function(e, t){
        if(e.button === 0 && t.className.indexOf('x-grid3-row-checker') > -1){ // Only fire if left-click
            e.stopEvent();
            var row = e.getTarget('.x-grid3-row');
            if(row){
                var index = row.rowIndex;
                if(this.isSelected(index)){
                    this.deselectRow(index);
                }else{
                    this.selectRow(index, true);
                    this.grid.getView().focusRow(index);
                }
            }
        }
    },

    // private
    onHdMouseDown : function(e, t) {
        if(t.className == 'x-grid3-hd-checker'){
            e.stopEvent();
            var hd = Ext.fly(t.parentNode);
            var isChecked = hd.hasClass('x-grid3-hd-checker-on');
            if(isChecked){
                hd.removeClass('x-grid3-hd-checker-on');
                this.clearSelections();
            }else{
                hd.addClass('x-grid3-hd-checker-on');
                this.selectAll();
            }
        }
    },

    // private
    renderer : function(v, p, record){
    	console.log(record);
    	console.log(record.get('disabled'));
    	if (record.get('disabled')) {
    		//return '<div class="x-grid3-row-checker x-grid3-row-checker-disabled">&#160;</div>';
    		return '';
    	}
        return '<div class="x-grid3-row-checker">&#160;</div>';
    }
    
});