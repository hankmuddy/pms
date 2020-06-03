Ext.define('Pms.desktop.PasswordWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.passwordWindow',
    requires: ['Pms.modules.user.view.UserForm'],

    title: l('changePassword'),
    layout: 'border',
    autoShow: false,
    width: 345,
    height: 180,
    resizable: false,
    modal: true,
    withToolbar: true,

    initComponent: function () {
        this.items = [
            {
                xtype: 'userForm',
                buildItems: function () {
                    return [
                        {
                            xtype: 'fieldset',
                            padding: 10,
                            defaults: {
                                xtype: 'textfield',
                                anchor: '100%'
                            },
                            items: [
                                {
                                    fieldLabel: l('oldPassword') + Pms.requiredStatus,
                                    inputType: 'password',
                                    name: 'oldPassword',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: l('newPassword') + Pms.requiredStatus,
                                    inputType: 'password',
                                    name: 'newPassword',
                                    allowBlank: false
                                },
                                {
                                    fieldLabel: l('newPasswordAgain') + Pms.requiredStatus,
                                    inputType: 'password',
                                    allowBlank: false,
                                    submitValue: false,
                                    validator: function (value) {
                                        return value == this.previousNode().getValue() ? true : l('password.doNotMatch');
                                    },
                                    validateOnChange: false
                                }
                            ]
                        }
                    ]
                }
            }
        ];
        this.bbar = ['->',
            {
                iconCls: 'save-action-icon',
                text: l('save.btn'),
                handler: function (btn) {
                    var form = this.up('window').down('form').getForm(),
                        values = form.getValues();
                    Pms.Ajax.request({
                        url: 'rest/profile/password',
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
        ],

            this.callParent(arguments);
    }
});