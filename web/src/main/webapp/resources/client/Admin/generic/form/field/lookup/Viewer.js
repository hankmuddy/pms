Ext.define('Admin.generic.form.field.lookup.Viewer', {
    extend: 'Ext.form.field.Display',
    alias: 'widget.lookupviewer',
    constructor: function (config) {
        var me = this;
        var lookupType = config.lookupType;
        if (!lookupType)throw Error('lookupviewer \'' + config.name + '\': can not found lookupType')
        Ext.apply(me, config)
        me.callParent()
    },
    renderer: function (id) {
        var value = Admin.Lookup.getLookupValue(this.lookupType, id);
        if (value)return value;
        else return id;
    }
});
