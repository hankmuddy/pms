Ext.define('Admin.generic.grid.column.Lookup', {
    extend: 'Ext.grid.column.Column',
    alias: ['widget.lookupcolumn'],
    constructor: function(config){
        var me = this;
        var lookupType = config.lookupType;
        if(!lookupType)throw Error('lookupviewer \''+config.name+'\': can not found lookupType')
        Ext.apply(me,config)
        me.callParent()
    },
    renderer: function (id,cell) {
        var value = Admin.Lookup.getLookupValue(cell.column.lookupType,id);
        if(value)return value;
        else return id;
    }
//    renderer: function (id) {
//        console.log(Admin.Lookup.getLookupValue(this.lookupType,id));
//        return Admin.Lookup.getLookupValue(id)
//    }
});
