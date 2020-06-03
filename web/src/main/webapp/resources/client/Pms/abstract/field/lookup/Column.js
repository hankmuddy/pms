Ext.define('Pms.abstract.field.lookup.Column', {
    extend: 'Ext.grid.column.Column',
    alias: 'widget.lookupcolumn',
    constructor: function(config) {
        var me = this;
        var lookupType = config.lookupType;
        if(!lookupType)throw Error('lookupviewer \'' + config.name + '\': can not found lookupType')
        Ext.apply(me, config);
        me.callParent()
    },
    renderer: function(id, obj) {
        var lookupType = obj.column.lookupType;
        var value = Pms.Lookup.getLookupValue(lookupType, id);
        if(value)return value;
        else return id;
    },
});
