Ext.define('Admin.generic.form.field.RefField', {
    extend: 'Ext.form.FieldContainer',
    mixins: {
        field: 'Ext.form.field.Field'
    },
    alias: 'widget.reffield',
    layout: 'fit',
    constructor: function (conf) {
        this.data = {id: null, label: ''};
        this.callParent(arguments);
    },
    initComponent: function () {
        this.callParent(arguments);
        this.XXX = Ext.widget({
            xtype: 'form',
            layout: 'hbox',
            items: [
                {
                    xtype: 'textfield',
                    readOnly: true
                },
                {
                    xtype: 'button',
                    text: 'Find',
                    handler: this.initialConfig.handler
                }
            ]
        });
        this.add(this.XXX);
    },
    setValue: function (data) {
        var textfield = this.down('textfield');
        if (!data) {
            textfield.setValue('');
            return;
        }
        textfield.setValue(data.label);
        this.data = data;

    },
    getValue: function () {
        // return this.data.id;
        return this.data;
    },
    reset: function () {
        this.data = {id: null, label: ''};
    }


});
