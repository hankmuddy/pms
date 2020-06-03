Ext.define('Admin.modules.hotel.view.Update', {
    extend: 'Admin.generic.form.UpdateForm',
    items: [
        {
            name: 'maxRooms',
            required: true,
            xtype: 'numericfield',
            minValue: 0,
            fieldLabel: l('hotel.maxRooms')
        }
    ]
});
