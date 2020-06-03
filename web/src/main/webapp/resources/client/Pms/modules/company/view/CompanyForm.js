Ext.define('Pms.modules.company.view.CompanyForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.companyForm',
    autoScroll: false,
    fileupload: true,
    listeners: {
        afterRender: function (thisForm, options) {
            this.keyNav = Ext.create('Ext.util.KeyNav', this.el, {
                enter: function () {
                    if (!thisForm.down('combobox[isExpanded=true]'))
                        this.down('button[action=save]').fireHandler()
                },
                scope: this
            });
        }
    },

    data: {
        documents: []
    },
    documentsData: [],

    initComponent: function () {
        var me = this;

        me.items = [
            {
                xtype: 'container',
                layout: 'hbox',
                defaults: {
                    xtype: 'fieldset',
                    defaultType: 'textfield',
                    margin: 5
                },
                items: [
                    {
                        title: l('common'),
                        height: 180,
                        items: [
                            {
                                xtype: 'hidden',
                                name: 'id'
                            },
                            {
                                fieldLabel: l('title') + Pms.requiredStatus,
                                name: 'name',
                                allowBlank: false
                            },
                            {
                                fieldLabel: l('inn'),
                                name: 'inn'
                            },
                            {
                                fieldLabel: l('registrationAddress'),
                                name: 'registrationAddress'
                            }
                        ]
                    },
                    {
                        title: l('contacts'),
                        items: [
                            {
                                fieldLabel: l('country'),
                                name: 'country',
                                xtype: 'combobox',
                                store: Ext.create('Pms.modules.person.store.Country'),
                                valueField: 'abbr',
                                displayField: 'country',
                                queryMode: 'local'
                            },
                            {
                                fieldLabel: l('city'),
                                name: 'city'
                            },
                            {
                                fieldLabel: l('company.legalAddress'),
                                name: 'legalAddress'
                            },
                            {
                                fieldLabel: l('company.postAddress'),
                                name: 'postAddress'
                            },
                            {
                                fieldLabel: l('email'),
                                name: 'email'
                            },
                            {
                                fieldLabel: l('phone'),
                                name: 'phone'
                            }
                        ]
                    },
                    {
                        title: l('bankDetails'),
                        height: 180,
                        items: [
                            {
                                fieldLabel: l('bankName'),
                                name: 'bankName'
                            },
                            {
                                fieldLabel: l('company.accountNumber'),
                                name: 'accountNumber'
                            },
                            {
                                fieldLabel: l('company.bankMfo'),
                                name: 'bankMfo'
                            }
                        ]
                    }
                ]
            },
            {
                xtype: 'container',
                items: [
                    {
                        xtype: 'fieldset',
                        title: l('scannedDocuments'),
                        layout: 'fit',
                        margin: 5,
                        items: [
                            {
                                xtype: 'form',
                                border: false,
                                url: 'rest/document/upload',
                                items: [{
                                    fieldLabel: l('loading'),
                                    name: 'file',
                                    xtype: 'filefield'
                                }]
                            },
                            Ext.create('Ext.view.View', {
                                store: Ext.create('Ext.data.ArrayStore', {fields: ['file'], data: me.documentsData}),
                                autoScroll: true,
                                tpl: [
                                    '<tpl for=".">',
                                        '<div class="thumb-wrap" style="float:left;margin:5px;width:170px;height:180px;">',
                                            '<div class="x-editable"><a href="' + _('imagesUrlPrefix') + '{file}" target="_blank">' + l('openFile') + '</a></div>',
                                            '<div class="thumb"><img src="' + _('imagesUrlPrefix') + '{file}" style="max-width:150px;border:5px solid #ddd;margin: 5;"></div>',
                                        '</div>',
                                    '</tpl>',
                                    '<div class="x-clear"></div>'
                                ],
                                multiSelect: true,
                                trackOver: true,
                                overItemCls: 'x-item-over',
                                itemSelector: 'div.thumb-wrap',
                                emptyText: l('scannedDocumentsNotFound'),
                                listeners: {
                                    itemcontextmenu: function (dataview, rec, item, index, e, eOpts) {
                                        var me = this;

                                        e.stopEvent();
                                        dataview.ctx = new Ext.menu.Menu({
                                            items: [{
                                                text: l('tooltip.delete'),
                                                iconCls: 'app-icon-remove',
                                                handler: function (btn, e) {
                                                    dataview.up('companyForm').fireEvent('deletefile', dataview, rec);
                                                }
                                            }],
                                            listeners: {
                                                hide: function () {
                                                    dataview.ctx.destroy();
                                                }
                                            }
                                        });
                                        dataview.ctx.rec = rec;
                                        dataview.ctx.showAt(e.getXY());
                                    }
                                }
                            })
                        ]
                    }
                ]
            }
        ];

        me.callParent(arguments);
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