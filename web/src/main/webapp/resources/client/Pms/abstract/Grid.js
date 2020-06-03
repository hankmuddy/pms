Ext.define('Pms.abstract.Grid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.abstractGrid',

    paging: true,
    border: false,
    full: true,
    editable: false,
    cellEditing: false,
    rowEditing: false,
    forceFit: true,
    multiSelect: true,
    plugins: [],

    autoLoading: true,
    withToolbar: false,
    withActions: false,
    refreshBtn: false,
//    storeId: null,
    storeParams: {},
    loadParams: {},
    filterParams: {},

    initComponent: function () {
        var me = this;
        me.storeParams.paging = me.paging;
        me.storeParams.loadParams = me.loadParams;
        me.storeParams.filterParams = me.filterParams;

        me.on("itemcontextmenu", function (cell, record, item, index, e, eOpts) {
            e.preventDefault();
        });

//        console.log(Ext.StoreManager);
//        console.log(me, me.gridStoreId ? Ext.StoreManager.lookup(me.gridStoreId) : 'no store');

        // storeId will be added to store params when store is created:
        if(me.gridStoreId) me.storeParams.storeId = me.gridStoreId;

        // prevents double ids for store if storeId was set and store has been already created, otherwise creates store with provided params:
        me.store = me.gridStoreId && Ext.StoreManager.lookup(me.gridStoreId) ? Ext.StoreManager.lookup(me.gridStoreId) : Ext.create(me.store, me.storeParams);

        if (me.autoLoading) me.store.load();

        me.filterRows();

        if (me.withToolbar) me.tbar = me.buildToolbar();

        if (me.withActions && !Ext.isEmpty(me.columns)) me.columns.push(me.buildActions());

        if (me.cellEditing) {
            me.plugins.push(Ext.create('Ext.grid.plugin.CellEditing', {clicksToEdit: 1}));
        }
        else if (me.rowEditing) {
            me.plugins.push(Ext.create('Ext.grid.plugin.RowEditing', {
                saveBtnText: l('save.btn'),
                cancelBtnText: l('cancel.btn')
            }));
        }

        if (me.paging) {
            me.dockedItems = [
                {
                    xtype: 'pagingtoolbar',
                    store: me.store,
                    dock: 'bottom',
                    displayInfo: true,
                    doRefresh: function () {
                        var _this = this;
                        var current = _this.store.currentPage;

                        me.getStore().reload();
//                        if (_this.fireEvent('beforechange', _this, current) !== false) {
//                            _this.store.refresh = true;
//                            _this.store.reload();
//                            me.getSelectionModel().clearSelections();
//                        }
                    },
                    items: ['->', {
                        xtype: 'combobox',
                        labelWidth: 130,
                        fieldLabel: l('pagination.pageSizeLabel'),
                        store: Ext.create('Ext.data.ArrayStore', {
                            fields: ['label', 'value'],
                            data: [
                                ['50', 50],
                                ['100', 100],
                                ['200', 200],
                                ['500', 500]
                            ]
                        }),
                        displayField: 'label',
                        valueField: 'value',
                        triggerAction: 'all',
                        queryMode: 'remote',
                        queryParam: '',
                        selectOnFocus: true,
                        indent: true,
                        value: me.store.pageSize,
                        listeners: {
                            change: function (combo, newVal, prevVal, e) {
                                me.store.pageSize = newVal;
                                me.store.currentPage = 1;
                                me.store.load();
                            }
                        }
                    }]
                }
            ];
        } else if (me.refreshBtn === true) {
            me.dockedItems = [
                {
                    xtype: 'toolbar',
                    store: me.store,
                    dock: 'bottom',
                    items: [{
//                        text: l('refresh'),
                        iconCls: 'x-tbar-loading',
                        handler: function (btn) {
                            me.getStore().reload();
                        }
                    }]
                }
            ];
        }

        me.callParent();
    },

    applyFilter: function (params, filter, connective) {
        var me = this;
        me.store.currentPage = 1;
        me.store.filterParams = filter;
        me.store.load();
    },

    resetFilter: function () {
        var me = this;
        me.store.filterParams = [];
        me.store.load();
    },

    filterRows: function () {
        var me = this;
        if(me.rowFilter != null) {
            me.viewConfig = {
                getRowClass: function(record, rowIndex, rowParams, store) {
                    if (me.rowFilter(record) == false) return 'display-none';
                }
            }
        }
    },

    buildToolbar: function () {
        return [
            {
                xtype: 'toolbar',
                defaults: {
                    scale: 'small',
                    iconAlign: 'left',
                    width: '60'
                },
                items: [
                    {
                        text: l('add.btn'),
                        action: 'new',
                        iconCls: 'pms-add-icon-16'
                    },
                    {
                        text: l('edit.btn'),
                        action: 'edit',
                        iconCls: 'app-icon-edit2',
                        gridAction: true
                    },
                    {
                        text: l('commit.btn'),
                        action: 'commit',
                        iconCls: 'pms-success-icon-16',
                        gridAction: true
                    },
                    {
                        text: l('delete.btn'),
                        action: 'delete',
                        iconCls: 'pms-delete-icon-16',
                        gridAction: true
                    },
                    {
                        xtype: 'button',
                        flex: 1,
                        text: l('exportToExcel'),
                        iconCls: 'fa fa-file-excel-o',
                        handler: function(b, e) {
                            b.up('grid').downloadExcelXml();
                        }
                    }
                ]
            }
        ]
    },

    buildActions: function () {
        return {
            header: l('actions.title'),
            xtype: 'actioncolumn',
            width: 100,
            items: [
                {
                    iconCls: 'app-icon-edit2',
                    tooltip: l('tooltip.edit'),
                    handler: function (grid, rowIndex, colIndex) {
                        this.up('grid').fireEvent('editbuttonclick', grid, rowIndex, colIndex);
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, rec) {
                        return rec.data.approved ? true : false;
                    }
                },
                {
                    iconCls: 'app-icon-commit',
                    tooltip: l('tooltip.commit'),
                    handler: function (grid, rowIndex, colIndex) {
                        this.up('grid').fireEvent('commitbuttonclick', grid, rowIndex, colIndex);
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, rec) {
                        return rec.data.approved ? true : false;
                    }
                },
                {
                    iconCls: 'app-icon-remove',
                    tooltip: l('tooltip.delete'),
                    handler: function (grid, rowIndex, colIndex) {
                        this.up('grid').fireEvent('deletebuttonclick', grid, rowIndex, colIndex);
                    },
                    isDisabled: function (view, rowIndex, colIndex, item, rec) {
                        return rec.data.approved ? true : false;
                    }
                }
            ]
        };
    }
});