Ext.define('Admin.modules.bookingButton.Store', {
    extend: 'Admin.generic.data.Store',
    model: 'Admin.modules.bookingButton.Model',
    proxy: Ext.create('Admin.generic.data.proxy.Ajax', {
        url: 'admin/bbs',
        reader: Ext.create('Admin.generic.data.reader.Json')
    })
});
