Ext.define("Pms.modules.bookingButton.view.Viewport", {
    extend: "Pms.abstract.Viewport",
    alias: 'widget.bookingButtonViewport',
    requires: [
        'Pms.modules.bookingButton.view.BookingButtonGrid'
    ],
    border: false,

    buildItems: function () {
        return [
            {
                region: 'center',
                border: false,
                viewConfig: {
                    forceFit: true
                },
                height: '100%',
                autoHeight: true,
                layout: 'fit',
                xtype: 'panel',
                items: [
                    {
                        xtype: 'bookingButtonGrid',
                        layout: 'fit',
                        autoHeight: true,
                        height: '100%',
                        viewConfig: {
                            forceFit: true
                        }
                    }
                ]
            }
        ];
    },

    buildTopButtons: function () {
        var me = this;
        return [
            {
                xtype: "buttongroup",
                defaults: {
                    scale: 'small',
                    iconAlign: "left",
                    width: '60'
                },
                items: [
                    {
                        text: l('add.btn'),
                        action: "new",
                        iconCls: "pms-add-icon-16",
                        width: "60"
                    },
                    {
                        text: l('delete.btn'),
                        action: "delete",
                        disabled: false,
                        iconCls: "pms-delete-icon-16",
                        width: "60"
                    }
                ]
            }
        ];
    }
});