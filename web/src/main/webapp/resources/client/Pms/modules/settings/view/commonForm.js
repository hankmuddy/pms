Ext.define('Pms.modules.settings.view.commonForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.commonForm',

    layout: 'border',
    bodyStyle: {
        backgroundColor: '#fff'
    },
    autoScroll: false,

    data: {},
    timeFieldExpand: function () {
        return function (field) {
            var store = field.store,
                Model = store.model,
                nullRec = new Model({
                    disp: '&mdash;', date: null
                });

            if (field.expanded) return;

            field.expanded = true;
            store.insert(0, [nullRec]);
        }
    },

    initComponent: function () {
        var me = this;

        me.defaults = {
            xtype: 'fieldset',
            margin: 3,
            padding: 5,
            defaults: {
                xtype: 'textfield',
                anchor: '100%'
            },
            hint: l('hint.for') + 'fieldset'
        };

        me.items = [
            {
                title: l('settings.CommonInfo'),
                region: 'west',
                items: [
                    {
                        xtype: 'hidden',
                        name: 'id'
                    },
                    {
                        xtype: 'hidden',
                        name: 'mainPhoto'
                    },
                    {
                        name: 'name',
                        fieldLabel: l('settings.hotelName')
                    },
                    {
                        xtype: 'textareafield',
                        name: 'officialAddress',
                        fieldLabel: l('settings.officialAddress')
                    },
                    {
                        xtype: 'textareafield',
                        name: 'postAddress',
                        fieldLabel: l('settings.postAddress')
                    },
                    {
                        name: 'city',
                        fieldLabel: l('city')
                    },
                    {
                        name: 'country',
                        fieldLabel: l('country') + Pms.requiredStatus,
                        allowBlank: false,
                        xtype: 'combobox',
                        valueField: 'abbr',
                        forceSelection: true,
                        displayField: 'country',
                        queryMode: 'local',
                        store: Ext.create('Ext.data.Store', {
                            fields: [
                                'abbr', 'country'
                            ],
                            data: countries
                        })
                    },
                    {
                        name: 'infoPhone',
                        fieldLabel: l('settings.infoPhone')
                    },
                    {
                        name: 'bookPhone',
                        fieldLabel: l('settings.bookPhone')
                    },
                    {
                        name: 'accountPhone',
                        fieldLabel: l('settings.accountPhone')
                    },
                    {
                        xtype: 'numberfield',
                        name: 'vat',
                        fieldLabel: l('settings.vat') + ', %',
                        minValue: 0,
                        maxValue: 100
                    }
                ]
            },
            {
                title: l('settings.options'),
                region: 'center',
                items: [
                    {
                        name: 'email',
                        fieldLabel: l('settings.email'),
                        vtype: 'email'
                    },
                    {
                        name: 'webSite',
                        fieldLabel: l('settings.site')
                    },
                    {
                        xtype: 'numberfield',
                        name: 'multiplier',
                        fieldLabel: l('settings.percentageMarkups') + ', %',
                        step: 1,
                        minValue: 0,
                        maxValue: 100
                    },
                    {
                        name: 'checkIn',
                        fieldLabel: l('hotel.info.checkIn') + Pms.requiredStatus,
                        xtype: 'timefield',
                        format: 'H:i',
                        submitFormat: 'H:i'
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        defaultType: 'timefield',
                        fieldLabel: l('hotel.info.earlyCheckIn'),
                        items: [
                            {
                                name: 'earlyCheckInStart',
                                format: 'H:i',
                                submitFormat: 'H:i',
                                fieldLabel: l('from'),
                                expanded: false,
                                labelWidth: 20,
                                flex: 1,
                                listeners: {
                                    change: function (start, value) {
                                        var end = start.nextNode();
                                        end.setMinValue(value);
                                        end.expanded = false;
                                        end.allowBlank = value ? false : true;
                                        end.validate();
                                    },
                                    expand: me.timeFieldExpand()
                                }
                            },
                            {
                                name: 'earlyCheckInEnd',
                                format: 'H:i',
                                fieldLabel: l('to'),
                                labelWidth: 20,
                                margins: '0 0 0 20',
                                flex: 1,
                                listeners: {
                                    change: function (end, value) {
                                        var start = end.previousNode();
                                        start.setMaxValue(value);
                                        start.expanded = false;
                                        start.allowBlank = value ? false : true;
                                        start.validate();
                                    },
                                    expand: me.timeFieldExpand()
                                }
                            }
                        ]
                    },
                    {
                        name: 'checkOut',
                        fieldLabel: l('hotel.info.checkOut') + Pms.requiredStatus,
                        xtype: 'timefield',
                        format: 'H:i'
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        defaultType: 'timefield',
                        fieldLabel: l('hotel.info.lateCheckOut'),
                        items: [
                            {
                                name: 'lateCheckOutStart',
                                format: 'H:i',
                                fieldLabel: l('from'),
                                startLabel: l('from'),
                                labelWidth: 20,
                                flex: 1,
                                listeners: {
                                    change: function (start, value) {
                                        var end = start.nextNode();
                                        end.setMinValue(value);
                                        end.expanded = false;
                                        end.allowBlank = value ? false : true;
                                        end.validate();
                                    },
                                    expand: me.timeFieldExpand()
                                }
                            },
                            {
                                name: 'lateCheckOutEnd',
                                format: 'H:i',
                                fieldLabel: l('to'),
                                startLabel: l('to'),
                                labelWidth: 20,
                                margins: '0 0 0 20',
                                flex: 1,
                                value: null,
                                listeners: {
                                    change: function (end, value) {
                                        var start = end.previousNode();
                                        start.setMaxValue(value);
                                        start.expanded = false;
                                        start.allowBlank = value ? false : true;
                                        start.validate();
                                    },
                                    expand: me.timeFieldExpand()
                                }
                            }
                        ]
                    },
                    {
                        xtype: 'numberfield',
                        name: 'tourismTax',
                        fieldLabel: l('settings.touristTax') + ', %',
                        step: 1,
                        minValue: -100,
                        maxValue: 100
                    },
                    {
                        xtype: 'combobox',
                        name: 'timeZone',
                        fieldLabel: l('settings.timeZone') + Pms.requiredStatus,
                        store: Ext.create('Pms.modules.settings.store.TimeZone').load(),
                        valueField: 'id',
                        displayField: 'id',
                        queryMode: 'local',
                        listConfig: {
                            getInnerTpl: function (a) {
                                var items = this.up('combobox').getStore().data.items,
                                    item = null;
                                Ext.each(items, function (data) {
                                    item = data.data;
                                    var hours = (item.offset / 3600000).toFixed(),
                                        minutes = (Math.abs(item.offset / 60000 - hours * 60)).toFixed(),
                                        time = null;
                                    if (minutes == 0) minutes += '0';
                                    if (hours >= 0 && hours < 10) hours = '0' + hours;
                                    if (hours < 0 && hours > -10) hours = '-0' + Math.abs(hours);
                                    if (hours >= 0) hours = '+' + hours;
                                    time = hours + ':' + minutes;
                                    item.name = '(' + time + ')';
                                });
                                return '<div><b>{name}</b> {id}</div>';
                            }
                        }
                    },
                    {
                        xtype: 'combobox',
                        name: 'currency',
                        fieldLabel: l('currency') + Pms.requiredStatus,
                        allowBlank: false,
                        store: Ext.create('Ext.data.Store', {
//                            url: 'rest/common/currencies',
                            fields: ['code', 'label'],
                            data: [
                                {code: 'RUR', label: 'p'},
                                {code: 'USD', label: '$'},
                                {code: 'UAH', label: '₴'},
                                {code: 'EUR', label: '€'},
                                {code: 'GPB', label: '£'},
                                {code: 'BYR', label: 'Br'},
                                {code: 'GEL', label: 'GEL'},
                                {code: 'MDL', label: 'MDL'},
                                {code: 'KZT', label: 'KZT'},
                                {code: 'AZN', label: 'AZN'}
                            ]
                        }).load(),
                        queryMode: 'local',
                        valueField: 'code',
                        listConfig: {
                            getInnerTpl: function () {
                                return '<div class="search-item">' +
                                    '<span><tpl switch="code">' +
                                    '<tpl case="UAH">' +
                                    l('currency.uah') +
                                    '<tpl case="RUR">' +
                                    l('currency.rur') +
                                    '<tpl case="USD">' +
                                    l('currency.usd') +
                                    '<tpl case="EUR">' +
                                    l('currency.eur') +
                                    '<tpl case="GPB">' +
                                    l('currency.gpb') +
                                    '<tpl case="BYR">' +
                                    l('currency.byr') +
                                    '<tpl case="GEL">' +
                                    l('currency.gel') +
                                    '<tpl case="MDL">' +
                                    l('currency.mdl') +
                                    '<tpl case="KZT">' +
                                    l('currency.kzt') +
                                    '<tpl case="AZN">' +
                                    l('currency.azn') +
                                    '</tpl><b> {label}</b></span>' +
                                    '</div>';
                            }
                        },
                        displayField: 'code'
                    },
                    {
                        xtype: 'textarea',
                        name: 'paymentInfo',
                        fieldLabel: l('hotel.paymentInfo'),
                        allowBlank: true
                    },
                    {
                        xtype: 'textarea',
                        name: 'importantInfo',
                        fieldLabel: l('hotel.importantInfo'),
                        allowBlank: true
                    }
                ]
            },
            {
                title: l('settings.hotelLogo'),
                region: 'east',
                layout: 'vbox',
                items: [
                    {
                        xtype: 'form',
                        border: false,
                        url: 'rest/document/upload',
                        items: [
                            {
                                fieldLabel: l('settings.hotelLogo'),
                                hideLabel: true,
                                name: 'file',
                                xtype: 'filefield',
                                anchor: '100%'
                            }
                        ]
                    },
                    {
                        xtype: 'image',
                        border: 5,
                        src: Ext.isEmpty(me.data.hotelInfo.logo) ? Pms.emptyImgSrc : _('imagesUrlPrefix') + me.data.hotelInfo.logo,
                        style: {
                            borderColor: '#eee',
                            borderStyle: 'solid',
                            margin: '0 0 5px 0',
                            maxWidth: '290px',
                            maxHeight: '290px'
                        }
                    },
                    {
                        xtype: 'button',
                        text: l('tooltip.delete'),
                        iconCls: 'app-icon-remove',
                        requestDisable: true,
                        disabled: !me.data || Ext.isEmpty(me.data.hotelInfo.logo),
                        handler: function (btn, eOpts) {
                            Pms.Ajax.request({
                                url: 'rest/document/photo/' + me.data.hotelInfo.logo,
                                method: 'DELETE',
                                success: function (response) {
                                    me.data.hotelInfo.logo = null;
                                    Pms.Ajax.request({
                                        url: 'rest/settings/logo',
                                        method: 'DELETE'
                                    });
                                    btn.up('fieldset').down('image').setSrc(Pms.emptyImgSrc);
                                    Pms.App.showNotification({
                                        message: l('settings.logoDeleted'),
                                        icon: Pms.notificationOk
                                    });
                                },
                                failure: function () {
                                    Pms.App.showNotification({
                                        message: l('settings.logoNotDeleted')
                                    });
                                }
                            });
                        }
                    },
                    {
                        title: l('settings.description'),
                        xtype: 'fieldset',
                        items: [
                            {
                                xtype: 'textareafield',
                                name: 'description',
                                height: 150
                            }
                        ]
                    }
                ]
            }
        ];

        me.callParent();
    },

    bbar: [
//        {
//            iconCls: 'booking-system-shortcut-icon',
//            text: l('importFromWubook.button'),
//            action: 'importFromWubook',
//            requestDisable: true,
//            hint: l('importFromWubook.hint')
//        },
        '->',
        {
            iconCls: 'save-action-icon',
            text: l('save.btn'),
            action: 'update',
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
});