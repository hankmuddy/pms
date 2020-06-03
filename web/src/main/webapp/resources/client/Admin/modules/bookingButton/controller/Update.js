Ext.define('Admin.modules.bookingButton.controller.Update', {
    extend: 'Admin.generic.app.controller.Update',
    viewClass: 'Admin.modules.bookingButton.view.Update',
    modelClass: 'Admin.modules.bookingButton.Model',

    onUpdateSubmit: function (record) {
        var keyValues = [];
        for (var i in record.data.key) {
            if (record.data.key[i] != "" && record.data.value[i] != "") {
                keyValues.push({
                    key: record.data.key[i],
                    value: '#' + record.data.value[i]
                });
            }
        }
        delete record.data.keyValues;

        record.data.keyValues = keyValues;

//        this.callParent(record);
        if (!record) return;
        record.save({
            scope: this,
            success: function () {
                Admin.getApplication().navigateTo(this.links['list']);
            },
            failure: function () {
                var errorpanel = this.view.down('errorpanel');

                errorpanel.setErrorTitle(l('error.incorrect_form_data'));
            }
        });
    },
    convertBeforeRender: function (record) {

        var settingsPanel = Ext.getCmp('settingsForm');
        var formPanel = Ext.getCmp('myForm');
        var keyValues = [];

        for (var i in record.data.keyValues) {
            keyValues.push({
                key: record.data.keyValues[i].key,
                value: record.data.keyValues[i].value.split('#')[1]
            });
        }

        delete record.data.keyValues;
        record.data.keyValues = keyValues;

        var keyField = Ext.create('Ext.form.field.Text', {
            xtype: 'hidden',
            hidden: true,
            name: 'key',
            fieldLabel: 'Key',
            readOnly: true,
            value: ''
        });

        var valueField = Ext.create('Ext.form.field.Text', {
            xtype: 'hidden',
            hidden: true,
            fieldLabel: '',
            name: 'value',
            value: ''
        });

        settingsPanel.add(keyField);
        settingsPanel.add(valueField);

        for (var k in keyValues) {
            var keyField = Ext.create('Ext.form.field.Text', {
                name: 'key',
                hidden: true,
                fieldLabel: 'Key',
                readOnly: true,
                value: keyValues[k].key
            });

            var valueField = Ext.create('Ext.ux.ColorField', {
                fieldLabel: keyValues[k].key,
                name: 'value',
                value: keyValues[k].value
            });

            settingsPanel.add(keyField);
            settingsPanel.add(valueField);
        }

        record.data.hotel = record.get('hotel').hotelId;

        return record;
    }
});
