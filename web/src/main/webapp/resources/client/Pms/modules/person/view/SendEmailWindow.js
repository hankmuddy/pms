Ext.define('Pms.modules.person.view.SendEmailWindow', {
    extend: 'Pms.abstract.Window',
    requires: ['Ext.form.Panel'],
    alias: 'widget.sendEmailWindow',
    title: l('person.sendMail'),
    width: 380,
    height: 300,
    email: '',
    name: '',

    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=send]').fireHandler()
                },
                scope: this
            });
        }
    },

    initComponent: function () {
        var me = this;

        me.items = [
            {
                region: 'center',
                xtype: 'panel',
                border: false,
                items: [
                    {
                        xtype: 'propertygrid',
                        hideHeaders: true,
                        editable: false,
                        source: {
                            name: me.name
                        },
                        sourceConfig: {
                            name: {displayName: l('person.firstName') + ' ' +  l('person.lastName')},
                        },
                        listeners: {
                            beforeedit: function () {
                                return false
                            }
                        }
                    },
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
                                    labelWidth: 100
                                },
                                items: [
                                    {
                                        fieldLabel: l('email') + Pms.requiredStatus,
                                        name: 'email',
                                        allowBlank: true,
                                        value: me.email
                                    },
                                    {
                                        fieldLabel: l('email.theme') + Pms.requiredStatus,
                                        name: 'theme',
                                        allowBlank: true
                                    },
                                    {
                                        fieldLabel: l('email.text') + Pms.requiredStatus,
                                        xtype: 'textarea',
                                        name: 'text',
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

        me.buttons = [
            {
                text: l('send.btn'),
                action: 'send',
                requestDisable: true
            },
            {
                text: l('cancel.btn'),
                scope: me,
                handler: me.close
            }
        ];

        me.callParent();
    }
});