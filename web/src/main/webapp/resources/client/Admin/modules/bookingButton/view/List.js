Ext.define('Admin.modules.bookingButton.view.List', {
    extend: 'Admin.generic.grid.List',
    actions: ['view', 'delete', 'edit', 'bbCode'],
    columns: Ext.create('Admin.modules.bookingButton.view.ColumnList').toArray('dataIndex'),
    dockedItems: [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    text: l('bb.add'),
                    icon: 'themes/default/images/icons/16/add.png',
                    handler: function () {
                        var grid = this.up('grid');
                        grid.fireEvent('_add_')
                    }
                },
                {
                    text: l('bb.delete'),
                    icon: 'themes/default/images/icons/upload/exclamation.png',
//                    handler: function () {
//                        var grid = this.up('grid');
//                        grid.fireEvent('broadcastAlert')
//                    }
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
