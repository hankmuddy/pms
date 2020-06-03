Ext.define('Pms.desktop.UserWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.userWindow',
    requires: ['Pms.modules.user.view.UserForm'],

    title: _('name'),
    layout: 'border',
    autoShow: false,
    width: 345,
    height: 345,
    resizable: false,
    modal: true,

    initComponent: function () {
        this.items = [
            {
                xtype: 'userForm',
                editForm: true,
                withToolbar: true,
                bbar: ['->',
                    {
                        text: l('changePassword'),
                        handler: function (btn) {
                            var pasWin = Ext.create('Pms.desktop.PasswordWindow');
                            pasWin.show();
                        }
                    },
                    {
                        iconCls: 'save-action-icon',
                        text: l('save.btn'),
                        handler: function (btn) {
                            var form = btn.up('form').getForm(),
                                values = form.getValues();
                            values.role = {id: values.role};
                            Pms.Ajax.request({
                                url: 'rest/profile',
                                method: 'PUT',
                                jsonData: values,
                                async: false,
                                success: function (res) {
                                    Pms.App.showNotification({
                                        message: l('saveSuccess.msg'),
                                        icon: Pms.notificationOk
                                    });
                                    location.reload();
                                },
                                failure: function () {
                                    Pms.App.showNotification({
                                        message: l('saveError.msg')
                                    });
                                }
                            });
                        },
                        requestDisable: true
                    },
                    {
                        iconCls: 'app-icon-remove',
                        text: l('cancel.btn'),
                        handler: function (btn) {
                            btn.up('window').close();
                        }
                    }
                ]
            }
        ];
        this.callParent(arguments);
    }
});