Ext.define('Admin.generic.grid.List', {
    extend: 'Admin.generic.grid.Panel',
    constructor: function (conf) {
        var me = this;
        var actionColumn = this.buildActionColumn(conf.actions || me.actions);
        var dockedItems = this.buildDockedItems(me, conf);
        var _conf = Ext.apply({
            columns: actionColumn.concat(me.columns),
            dockedItems: dockedItems
        }, conf);
        Ext.apply(me, _conf);
        me.addEvents('_add_', 'edit', 'view', 'delete', 'groupAction', 'message', 'code');
        me.callParent(arguments);
    },
    buildDockedItems: function (me, conf) {
        var dockedItems = [];
        if (me.dockedItems) dockedItems = Ext.clone(me.dockedItems);
        var firstTopToolbarItems = [];

        if (me.addLabel) {
            firstTopToolbarItems.push({
                text: me.addLabel,
                icon: 'themes/default/images/icons/16/add.png',
                handler: function () {
                    var grid = this.up('grid');
                    grid.fireEvent('_add_')
                },
            });
        }
        if (me.groupOperation) {
            firstTopToolbarItems.push({
                xtype: 'combobox',
                emptyText: l('common.choose_an_action'),
                editable: false,
                displayField: 'label',
                valueField: 'id',
                store: {
                    fields: ['id', 'label'],
                    data: [
                        {
                            id: 'delete',
                            label: l('common.delete')
                        },
                        {
                            id: 'another',
                            label: l('common.another_operation')
                        }
                    ]
                },
                listeners: {
                    select: function (combo, records) {
                        var me = this.up('grid');
                        var action = {
                            operation: records[0].getId(),
                            target: combo
                        };
                        me.fireEvent('groupAction', action);
                    }
                }
            })
        }
        var firstTopToolbar = {
            xtype: 'toolbar',
            dock: 'top',
            items: firstTopToolbarItems
        };
        if (firstTopToolbarItems.length) dockedItems.push(firstTopToolbar);

        if (me.paginator === false);
        else {
            dockedItems.push({
                xtype: 'pagingtoolbar',
                store: conf.store,
                dock: 'bottom',
                displayInfo: true,
                plugins: [Ext.create('Admin.generic.grid.pageSize.PageSize')]
            });
        }
        return dockedItems;
    },
    buildActionColumn: function (actions) {
        if (actions && !actions.length) return [];
        return [
            {
                xtype: 'actioncolumn',
                header: l('common.actions'),
//                width: 80,
                align: 'center',
                iconCls: 'app-action-col-icon',
                items: (function (actions) {
//                    actions = actions || ['delete','edit','view'];
                    actions = actions || ['view', 'edit', 'delete'];
                    var actionColumns = [];
                    actions.forEach(function (item) {
                        switch (item) {
                            case 'delete':
                                actionColumns.push({
                                    icon: 'themes/default/images/icons/gray/icons/remove.png',
                                    width: 20,
                                    tooltip: l('common.delete'),
                                    handler: function (grid, rowIndex, colIndex, item, e, record, row) {
                                        var me = this.up('grid');
                                        me.fireEvent('delete', record, grid);
                                    }
                                });
                                break;
                            case 'edit':
                                actionColumns.push({
                                    icon: 'themes/default/images/icons/gray/icons/edit.png',
                                    tooltip: l('common.edit'),
                                    width: 20,
                                    handler: function (grid, rowIndex, colIndex, item, e, record, row) {
                                        var grid = this.up('grid');
                                        grid.fireEvent('edit', record)
                                    }
                                });
                                break;
                            case 'view':
                                actionColumns.push({
                                    icon: 'themes/default/images/icons/gray/icons/view.png',
                                    tooltip: l('common.view'),
                                    width: 20,
                                    handler: function (grid, rowIndex, colIndex, item, e, record, row) {
                                        var grid = this.up('grid');
                                        grid.fireEvent('view', record)
                                    }
                                });
                                break;
                            case 'message':
                                actionColumns.push(({
                                    icon: 'resources/images/post_go.gif',
                                    tooltip: l('common.message'),
                                    width: 20,
                                    handler: function (grid, rowIndex, colIndex, item, e, record, row) {
                                        var grid = this.up('grid');
                                        grid.fireEvent('message', record)
                                    }
                                }))
                                break;
                            case 'block':
                                actionColumns.push(({
                                    icon: 'themes/default/images/icons/gray/icons/key.png',
                                    tooltip: l('common.block'),
                                    width: 20,
                                    handler: function (grid, rowIndex, colIndex, item, e, record, row) {
                                        var grid = this.up('grid');
                                        grid.fireEvent('block', record)
                                    }
                                }))
                                break;
                            case 'paidUntil':
                                actionColumns.push(({
                                    icon: 'themes/default/images/icons/gray/icons/calendar/calendar_view_day.png',
                                    tooltip: l('common.paidUntil'),
                                    width: 20,
                                    handler: function (grid, rowIndex, colIndex, item, e, record, row) {
                                        var grid = this.up('grid');
                                        grid.fireEvent('paidUntil', record)
                                    }
                                }))
                                break;
                            case 'pms':
                                actionColumns.push(({
                                    icon: 'themes/default/images/favicon.ico',
                                    tooltip: l('common.pms'),
                                    width: 20,
                                    handler: function (grid, rowIndex, colIndex, item, e, record, row) {
                                        var grid = this.up('grid');
                                        grid.fireEvent('pms', record)
                                    }
                                }))
                                break;
                            case 'bbCode':
                                actionColumns.push(({
                                    icon: 'themes/default/images/icons/gray/icons/calendar/calendar_view_day.png',
                                    tooltip: l('common.bbCode'),
                                    width: 20,
                                    handler: function (grid, rowIndex, colIndex, item, e, record, row) {
                                        var grid = this.up('grid');
                                        grid.fireEvent('bbCode', record)
                                    }
                                }))
                        }
                    });
                    return actionColumns
                })(actions)
            }
        ]
    }
})

