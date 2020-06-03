Ext.define('Pms.desktop.SupportWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.supportWindow',

    title: l('supportWin.title'),
    layout: 'border',
    autoShow: false,
    width: 384,
    height: 550,
    modal: true,

    initComponent: function () {
        var me = this;
        this.items = [
            {
                region: 'north',
                border: false,
                xtype: 'panel',
                height: 260,
                html: '<div style="text-align: center; padding-top: 30px;"><img align="center" src="themes/default/images/logo.svg"></div><p style="padding-left: 70px; line-height: 1.4em;">' + l('support.phones') + ':<br/> 8 323 723-3343 ' + l('support.LosAngeles') + '<br/>+7 812 425 24 17 ' + l('support.stPetersburg') + '<br/>+7 495 204 23 17 ' + l('support.Moscow') + '<br/>+38 044 392 72 08 ' + l('support.Kiev') + '</p><p style="padding-left: 70px; padding-bottom: 15px;">Email:<br/>support@pmscloud.com</p>',
            },
            {
                region: 'center',
                xtype: 'panel',
                border: false,
                items: [
                    {
                        xtype: 'form',
                        border: false,
                        autoscroll: true,
                        fileupload: false,
                        items: [
                            {
                                xtype: "fieldset",
                                border: false,
                                padding: 10,
                                height: '100%',
                                defaults: {
                                    xtype: "textfield",
                                    anchor: '100%',
                                    labelWidth: 55
                                },
                                items: [
                                    {
                                        fieldLabel: l('firstName'),
                                        name: 'name',
                                        allowBlank: true
                                    },
                                    {
                                        fieldLabel: l('email'),
                                        name: 'email',
                                        allowBlank: true
                                    },
                                    {
                                        fieldLabel: l('question'),
                                        xtype: 'textarea',
                                        name: 'question',
                                        allowBlank: true,
                                        grow: false,
                                        width: 400,
                                        height: 150
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ];

        this.buttons = [
            {
                text: l('send.btn'),
                scope: this,
                handler: function (btn) {
                    var values = btn.up('window').down('form').getValues()
                    Pms.Ajax.request({
                        url: 'rest/mail',
                        method: 'POST',
                        jsonData: {
                            to: 'yaroslav@planetofhotels.com',
                            subject: 'Обратная связь PMS Cloud',
                            content: 'Имя отправителя: ' + values.name + '<br /> Email отправителя: ' + values.email + '<br /> Вопрос: ' + values.question
                        },
                        success: function(resp){
                            Pms.App.showNotification({
                                message: l('feedback.sent'),
                                icon: Pms.notificationOk
                            });
                            me.close();
                        }
                    })
                },
                action: 'update'
            },
            {
                text: l('cancel.btn'),
                scope: this,
                handler: this.close
            }
        ];
        this.callParent(arguments);
    }
});