Ext.define('Admin.modules.user.view.List', {
    extend: 'Admin.generic.grid.List',
//    addLabel: l('user.add'),
    //selModel: Ext.create('Ext.selection.CheckboxModel'),
    actions: ['view'],
    columns: Ext.create('Admin.modules.user.view.ColumnList').toArray('dataIndex'),
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    text: l('user.add'),
                    icon: 'themes/default/images/icons/16/add.png',
                    handler: function () {
                        var grid = this.up('grid');
                        grid.fireEvent('_add_')
                    }
                },
                {
                    text: l('broadcast.alert'),
                    icon: 'themes/default/images/icons/upload/exclamation.png',
                    handler: function () {
                        var grid = this.up('grid');
                        grid.fireEvent('broadcastAlert')
                    }
                }
            ]
        }
    ]
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
