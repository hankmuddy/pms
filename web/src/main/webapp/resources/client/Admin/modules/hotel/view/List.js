Ext.define('Admin.modules.hotel.view.List', {
    extend: 'Admin.generic.grid.List',
    addLabel: l('hotel.add'),
    //selModel: Ext.create('Ext.selection.CheckboxModel'),
    actions: ['view', 'edit', 'block','paidUntil','pms'],
    columns: Ext.create('Admin.modules.hotel.view.ColumnList').toArray('dataIndex'),
    initComponent: function () {
        var me = this;
        me.plugins = [Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 2,
            listeners: {
                edit: function (editor, context, eOpts) {
                    var grid = context.grid,
                        hotelData = context.record.data;
                    Ext.Msg.confirm(l('hotel.changeManager'), l('hotel.changeManager.confirm'), function (btn) {
                        if (btn == 'yes') {
                            Ext.Ajax.request({
                                url: 'admin/hotel/' + hotelData.id + '/' + context.column.dataIndex + '/' + context.value,
                                method: 'PUT',
                                success: function () {
                                    grid.getStore().reload();
                                }
                            })
                        }
                        else {
                            grid.getStore().reload();
                        }
                    })
                },
                beforeedit: function (editor, context, eOpts) {
                    if (!context.value) return;
                    var userType = context.column.dataIndex;
                    if (userType == 'manager') {
                        context.record.data.manager = context.value.id;
                    }
                    else if (userType == 'supervisor') {
                        context.record.data.supervisor = context.value.id;
                    }
                }
            }
        })];

        me.callParent(arguments);
        me.getView().getRowClass = function (record, rowIndex, rowParams, store) {
            if (record.get('blocked')) {
                return 'unavailable-grid-row';
            }
        };
    }
});
