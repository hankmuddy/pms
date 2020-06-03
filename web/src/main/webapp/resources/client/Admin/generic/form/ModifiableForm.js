Ext.define('Admin.generic.form.ModifiableForm', {
    extend: 'Admin.generic.form.BaseForm',
    requires: [
        'Admin.generic.panel.ErrorPanel',
        'Admin.generic.form.field.lookup.Picker',
        'Admin.generic.form.field.Numeric',
        'Admin.generic.form.field.Money'
    ],
    alias: 'widget.rform',
    defaults: {
        msgTarget: 'under'
    },
    dockedItems: [
        {
            xtype: 'errorpanel',
            dock: 'top'
        }
    ],
    buttonAlign: 'center',
    initComponent: function () {
        this.items.map(function (item) {
            if (item.required) {
                item.afterLabelTextTpl = '<span style="color:red;font-weight:bold" data-qtip="' + l('common.required') + '">*</span>';
                item.allowBlank = false;
            }
            return item
        })
        this.callParent();
    }
});
