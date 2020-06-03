Ext.define('Admin.generic.data.reader.JsonOne',{
    extend: 'Ext.data.reader.Json',
    readRecords: function(data){
        if(data.content) data = data.content;
        return this.callParent(arguments);
    }
})
