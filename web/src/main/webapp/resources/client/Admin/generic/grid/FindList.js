Ext.define('Admin.generic.grid.FindList', {
    extend: 'Admin.generic.grid.Panel',
    destinationPage: null,
    destinationCmp: null,
    constructor: function (conf) {
        var me = this;
        me.store = conf.store;
        me.dockedItems = [
            {
                xtype: 'toolbar',
                dock: 'top',
                items: [
                    {
                        text: 'Select',
                        tooltip: 'Select and Go Back',
                        handler: function () {
                            var grid = this.up('grid');
                            var selModel = grid.getSelectionModel();
                            var record = selModel.getSelection()[0];
                            if (record) {
                                grid.getBackCmp().setValue(conf.onSelect(record));
                            }
                            grid.up('wrapper').getLayout().setActiveItem(grid.getBackPage())
                            selModel.deselectAll();
                        }
                    },
                    {
                        text: 'Select none',
                        tooltip: 'Reset selection',
                        handler: function () {
                            var grid = this.up('grid');
                            var selModel = grid.getSelectionModel();
                            grid.getBackCmp().setValue(null);
                            grid.up('wrapper').getLayout().setActiveItem(grid.getBackPage())
                            selModel.deselectAll();
                        }
                    },

                ]
            },
            {
                xtype: 'pagingtoolbar',
                store: conf.store,
                dock: 'bottom',
                displayInfo: true
            }
        ];
        me.callParent();
    },
    setBack: function (cmp, page) {
        this.destinationPage = page;
        this.destinationCmp = cmp;
    },
    getBackPage: function () {
        return this.destinationPage;
    },
    getBackCmp: function () {
        return this.destinationCmp;
    },
})
