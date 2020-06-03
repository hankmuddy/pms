Ext.define('Admin.generic.data.proxy.Ajax', {
    extend: 'Ext.data.proxy.Ajax',
    // requires: ['Admin.generic.data.reader.Json'],
    reader: Ext.create('Admin.generic.data.reader.Json'),
    headers: {'Content-type': 'application/json;charset=UTF-8'}
    // buildUrl: function(request) {
    //     var sortParams = request.params.sort;
    //     if(sortParams){
    //         sortParams = JSON.parse(sortParams);
    //         sortParams.forEach(function(item){
    //             request.params['sort']=item.property;
    //             request.params[item.property +'.dir'] = item.direction.toLowerCase();
    //         });
    // 
    //     }
    //     return this.callParent(arguments);
    // }
});
