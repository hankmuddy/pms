Ext.define('Pms.desktop.LoginWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.loginWindow',

    width: 300,
    height: 150,
    border: 0,
    title: l('entry'),
    layout: 'fit',
    closeAction: 'destroy',
    modal: true,

    initComponent: function () {
        var me = this;
        me.items = {
            xtype: 'panel',
            region: 'center',
            height: '100%',
            items: [
                {
                    xtype: "fieldset",
                    padding: 10,
                    defaults: {
                        xtype: "textfield",
                        anchor: '100%'
                    },
                    items: [
                        {
                            fieldLabel: l('login.hotel'),
                            name: 'hotel',
                            allowBlank: true
                        },
                        {
                            fieldLabel: l('login.login'),
                            name: 'login',
                            allowBlank: true
                        },
                        {
                            fieldLabel: l('login.password'),
                            name: 'password',
                            allowBlank: false
                        }
                    ]
                }
            ]
        };

        me.buttons = [
            {
                text: l('entry'),
                action: 'save',
                handler: function () {

                }
            },
            {
                text: l('cancel.btn'),
                action: 'close',
                scope: this,
                handler: function () {
                    document.location.href = '/app';
                }
            }
        ];
        me.callParent();
    },

    close: function () {
        document.location.href = '/login';
    }
});