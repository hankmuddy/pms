Ext.define('Pms.abstract.field.lookup.Picker', {
    extend: 'Ext.form.field.ComboBox',
    alias: ['widget.lookupCombobox'],
    displayField: 'value',
    editable: false,
    valueField: 'id',
    constructor: function (config) {
        var me = this;
        var lookupType = config.lookupType;
        if (!lookupType)throw Error('lookuppicker \'' + config.name + '\': can not found lookupType')
        config.store = Ext.create('Ext.data.Store', {
            fields: ['id', 'value'],
            data: Pms.Lookup.getByType(lookupType)
        });
        Ext.apply(me, config);
        me.callParent()
    }
});
