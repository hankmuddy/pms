Ext.define("Pms.modules.catalog.view.CatalogGrid", {
    extend: "Pms.abstract.Grid",
    alias: 'widget.catalogGrid',
    store: 'Pms.modules.catalog.store.Catalog',
    requires: ['Pms.abstract.field.MoneyColumn'],
    storeParams: {
        serviceSelect: true,
        toggled: ['product']
    },

    border: false,
    editable: false,
    paging: true,
    autoScroll: true,
    forceFit: true,
    multiSelect: true,

    initComponent: function () {
        var me = this;

        me.columns = [
            {
                header: l('catalog.title'),
                dataIndex: 'title',
                flex: 4,
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
            }
        ];
        me.callParent();
    },
});