Ext.define('Admin.modules.hotel.view.PaidUntil', {
    extend: 'Admin.generic.form.UpdateForm',
    items: [
        {
            name: 'paidUntil',
            required: true,
            xtype: 'datefield',
            submitFormat: 'Y-m-d',
            fieldLabel: l('hotel.paidUntil')
        }
    ]
});
