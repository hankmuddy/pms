Ext.define('Admin.supervisorApp.user.view.List', {
    extend: 'Admin.generic.grid.List',
    addLabel: l('user.add'),
    //selModel: Ext.create('Ext.selection.CheckboxModel'),
    actions: ['view'],
    dockedItems:[],
    columns: Ext.create('Admin.modules.user.view.ColumnList').toArray('dataIndex'),
//    initComponent: function(){
//        var me = this;
//        me.callParent(arguments);
//        me.getView().getRowClass = function(record, rowIndex, rowParams, store) {
//            if (!record.get('available')) {
//                return 'unavailable';
//            }
//        };
//    }
});
