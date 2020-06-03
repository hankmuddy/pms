Ext.define('Pms.modules.catalog.view.CatalogOrderGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.catalogOrderGrid',
    store: 'Pms.modules.catalog.store.Catalog',
    requires: ['Pms.abstract.field.MoneyColumn'],


    data: null,

    initComponent: function () {
        var me = this,
            customerGroup = this.data.customerGroup;
        me.startDate = me.data.startDate;
        me.endDate = me.data.endDate;
        me.roomUse = me.data.id;
        me.columns = [
            {
                header: l('catalog.title'),
                dataIndex: 'title',
                width: '50%',
                renderer: function (value, meta, record) {
                    return l(value);
                }
            },
            {
                header: l('catalog.price'),
                dataIndex: 'price',
                xtype: 'moneycolumn',
                flex: 1
            },
            {
                header: l('catalog.measure'),
                dataIndex: 'measure',
                flex: 1,
                renderer: function (value, meta, record) {
                    return l(value);
                }
            },
            {
                xtype: 'actioncolumn',
                sortable: false,
                width: 30,
                items: [
                    {
                        iconCls: 'app-icon-order',
                        tooltip: l('catalog.orderTooltip'),
                        handler: function (grid, rowIndex, colIndex) {
                            var rec = grid.getStore().getAt(rowIndex),
                                curDate = Ext.Date.format(new Date(), 'Y-m-d'),
                                orderDate;

                            if (curDate < Ext.Date.format(me.startDate, 'Y-m-d')) {
                                orderDate = Ext.Date.format(me.startDate, 'Y-m-d');
                            }
                            else if (curDate >= Ext.Date.format(me.startDate, 'Y-m-d') && curDate <= Ext.Date.format(me.toDate, 'Y-m-d')) {
                                orderDate = curDate;
                            }
                            else if (curDate > Ext.Date.format(me.endDate, 'Y-m-d')) {
                                Ext.Msg.alert(l('catalog.orderErrorTitle'), l('catalog.orderError'));
                            }
                            var catalogItem = rec.data,
                                win = grid.up('window'),
                                serviceUseOrderGrid = win.down('serviceUseOrderGrid'),
                                serviceData = {
                                    customerGroup: customerGroup.id,
                                    date: orderDate,
                                    quantity: 1
                                },
                                win = Ext.widget('serviceUseAddWindow', {
                                    measure: catalogItem.measure,
                                    serviceUseTitle: catalogItem.title,
                                    sinceDate: me.startDate,
                                    toDate: me.endDate,
                                    price: me.price,
                                    customerGroup: customerGroup,
                                    service: catalogItem.id,
                                    serviceType: catalogItem.type,
                                    roomUse: me.roomUse
                                });
                            win.down('serviceUseForm').getForm().setValues(serviceData);
                            win.show();
                        },
                        action: 'catalog-item-order'
                    }
                ]
            }
        ];

        me.tbar = [
            {
                xtype: 'combobox',
                multiSelect: true,
                hideLabel: true,
                displayField: 'label',
                valueField: 'id',
                triggerAction: 'all',
                emptyText: l('catalog.serviceType'),
                queryMode: 'remote',
                queryParam: '',
                selectOnFocus: true,
                indent: true,
                width: '100%',
                padding: '2px 5px',
                trigger1Cls: 'x-form-clear-trigger',
                trigger2Cls: 'x-form-arrow-trigger',
                onTrigger1Click: function () {
                    this.clearValue();
                }
            }
        ];

        me.callParent();
    }
});
