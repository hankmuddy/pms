Ext.define('Pms.modules.groupRoomUse.view.ccViewWindow', {
    extend: 'Pms.abstract.Window',
    alias: 'widget.ccViewWindow',
    title: l('cardInfo'),
    autoShow: false,
    width: 340,
    height: 167,
    resizable: false,
    data: {},

    initComponent: function () {
        this.items = [
            {
                xtype: 'container',
                items: [
                    {
                        xtype: 'propertygrid',
                        hideHeaders: true,
                        editable: false,
                        source: {
                            cc_expiring: this.data.cc_expiring,
                            cc_type: this.data.cc_type,
                            cc_code: this.data.cc_code,
                            cc_number: this.data.cc_number,
                            cc_cvv: this.data.cc_cvv
                        },
                        sourceConfig: {
                            cc_expiring: {displayName: l('cc.expiring')},
                            cc_type: {displayName: l('cc.type')},
                            cc_code: {displayName: l('cc.code')},
                            cc_number: {displayName: l('cc.number')},
                            cc_cvv: {displayName: l('cc.ccv')}
                        },
                        listeners: {
                            beforeedit: function () {
                                return false;
                            }
                        }
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: l('close.btn'),
                scope: this,
                handler: this.close
            }
        ];

        this.callParent(arguments);
    }
});