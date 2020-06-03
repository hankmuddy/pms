Ext.define('Admin.generic.data.reader.Json', {
    extend: 'Ext.data.reader.Json',
    root: 'content',
    totalProperty: 'page.totalCount'
});
