Ext.define('Admin.modules.bookingButton.Model', {
    extend: 'Admin.generic.data.Model',
    proxy: {
        type: 'rest',
        url: 'admin/bbs',
        reader: Ext.create('Admin.generic.data.reader.JsonOne')
    },
    fields: [
        {label: 'ID', name: 'id', type: 'int', useNull: true},
        {label: 'Name', name: 'name', type: 'string'},
        {label: 'Language', name: 'language', type: 'string'},
        {label: 'textColor', name: 'textColor', type: 'string'},
        {label: 'currency', name: 'currency', type: 'string'},
        {label: 'BackgroundColor', name: 'backgroundColor', type: 'string'},
        {label: 'Cancel', name: 'cancel', type: 'boolean', default: true},
        {label: 'Width', name: 'width', type: 'int'},
        {label: 'height', name: 'height', type: 'int'},
        {
            name: 'hotel',
            type: 'auto',
            serialize: function (value) {
                return {
                    hotelId: value
                }
            }
        },
        {
            label: 'keyValues', name: 'keyValues', type: 'auto'
        },
        {
            label: 'Key', name: 'key', type: 'auto', persist: false
        },
        {
            label: 'Value', name: 'value', type: 'auto', persist: false
        }
    ]
});
