Ext.define("Pms.modules.child.view.ChildForm", {
    extend: "Pms.abstract.Form",
    alias: 'widget.childForm',

    layout: 'hbox',
    autoScroll: false,
    data: null,
    initComponent: function () {
        var me = this;
        me.items = [
            {
                xtype: 'container',
                margin: 5,
                items: [
                    {
                        xtype: 'fieldset',
                        title: l('common'),
                        defaultType: 'textfield',
                        items: [
                            {
                                xtype: "hidden",
                                name: "id"
                            },
                            {
                                fieldLabel: l('lastName') + Pms.requiredStatus,
                                name: "lastName",
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('firstName') + Pms.requiredStatus,
                                name: "firstName",
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('patronymic'),
                                name: "patronymic"
                            },
                            {
                                fieldLabel: l('dateOfBirth'),
                                name: 'dob',
                                xtype: 'pmsdatefield',
                                format: 'd/m/y',
                                submitFormat: 'U'
                            }
                        ]
                    }
                ]
            },
            {
                xtype: 'fieldset',
                width: 330,
                height: 340,
                margin: 5,
                padding: 5,
                title: l('birthCertificate'),
                overflowY: 'auto',
                overflowX: false,
                items: [
                    {
                        xtype: 'form',
                        border: false,
                        url: 'rest/document/upload',
                        items: [
                            {
                                fieldLabel: l('scannedDocuments'),
                                name: 'file',
                                xtype: 'filefield',
                                labelWidth: 120
                            }
                        ]
                    },
                    {
                        xtype: 'button',
                        text: l('tooltip.delete'),
                        iconCls: 'app-icon-remove',
                        requestDisable: true,
                        disabled: !me.data || Ext.isEmpty(me.data.identity),
                        handler: function (btn, eOpts) {
                            Pms.Ajax.request({
                                url: 'rest/document/doc/' + me.data.identity,
                                method: 'DELETE',
                                success: function (response) {
                                    me.data.identity = null;
                                    btn.up('fieldset').down('image').setSrc(Pms.emptyImgSrc);
                                    Pms.App.showNotification({
                                        message: l('photo.deleteSuccess'),
                                        icon: Pms.notificationOk
                                    });
                                },
                                failure: function () {
                                    Pms.App.showNotification({message: l('photo.deleteError')});
                                }
                            });
                        }
                    },
                    {
                        xtype: 'container',
                        width: '100%',
                        items: [
                            {
                                xtype: 'image',
                                border: 5,
                                style: {
                                    borderColor: '#eee',
                                    borderStyle: 'solid',
                                    margin: '15px 0 0 0',
                                    maxWidth: '300px',
                                    maxHeight: '300px'
                                },
                                src: (me.data && me.data.identity != '') ? _('imagesUrlPrefix') + me.data.identity : Pms.emptyImgSrc
                            }
                        ]
                    }
                ]
            }
        ];
        this.callParent(arguments);
    },

    bbar: ['->', {
        iconCls: 'save-action-icon',
        text: l('save.btn'),
        action: 'save',
        requestDisable: true
    }, {
        iconCls: 'app-icon-remove',
        text: l('cancel.btn'),
        handler: function (btn) {
            btn.up('window').close();
        }
    }]
});