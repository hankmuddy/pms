Ext.define('Admin.modules.hotel.view.Create', {
    extend: 'Admin.generic.form.CreateForm',
    requires: ['Admin.modules.hotel.view.LocationMap'],
    title: l('hotel.add'),
    width: 550,
    requiredStatus: '<span style="color:red;font-weight:bold" data-qtip="' + l('common.required') + '">*</span>',
    initComponent: function () {
        var me = this;
        me.items = [
            {
                xtype: 'fieldset',
                name: 'info',
                maxWidth: 500,
                title: l('hotel.hotelInfo'),
                defaultType: 'textfield',
                defaults: {width: '75%'},
                layout: {
                    type: 'vbox',
                    align: 'center'
                },
                frame: true,
                margin: '5px',
                items: [
                    {
                        name: 'name',
                        fieldLabel: l('hotel.name') + me.requiredStatus,
                        allowBlank: false
                    },
                    {
                        name: 'country',
                        fieldLabel: l('country') + me.requiredStatus,
                        allowBlank: false,
                        xtype: 'combobox',
                        store: Ext.create('Ext.data.Store', {
                            fields: [
                                'abbr', 'country'
                            ],
                            autoLoad: 'true',
                            data: countries
                        }),
                        valueField: 'abbr',
                        forceSelection: true,
                        displayField: 'country',
                        queryMode: 'local'
                    },
                    {
                        name: 'city',
                        fieldLabel: l('city'),
                        allowBlank: true
                    },
                    {
                        xtype: 'combobox',
                        name: 'currency',
                        fieldLabel: l('currency') + me.requiredStatus,
                        allowBlank: false,
                        store: Ext.create('Ext.data.Store', {
                            fields: ['code', 'label'],
                            data: [
                                {code: 'RUR', label: 'p'},
                                {code: 'USD', label: '$'},
                                {code: 'UAH', label: '₴'},
                                {code: 'EUR', label: '€'},
                                {code: 'GPB', label: '£'},
                                {code: 'BYR', label: 'Br'},
                                {code: 'GEL', label: ''},
                                {code: 'MDL', label: ''},
                                {code: 'KZT', label: '₸'},
                                {code: 'AZN', label: ''}
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
                        fieldLabel: l('hotel.isExtranet'),
                        name: 'extranet',
                        xtype: 'checkbox',
                        inputValue: true
                    },
                    {
                        fieldLabel: l('onlineBooking'),
                        xtype: 'checkbox',
                        listeners: {
                            change: function (checkbox, val) {
                                var lcode = checkbox.nextNode(),
                                    wuName = lcode.nextNode(),
                                    wuPass = wuName.nextNode();
                                if (val) {
                                    lcode.show().allowBlank = false;
                                    wuName.show().allowBlank = false;
                                    wuPass.show().allowBlank = false;
                                } else {
                                    lcode.hide().allowBlank = true;
                                    wuName.hide().allowBlank = true;
                                    wuPass.hide().allowBlank = true;
                                }
                            }
                        }
                    },
                    {
                        name: 'lcode',
                        fieldLabel: l('hotel.lcode') + me.requiredStatus,
                        hidden: true
                    },
                    {
                        name: 'wuName',
                        fieldLabel: l('hotel.wuName') + me.requiredStatus,
                        hidden: true
                    },
                    {
                        name: 'wuPass',
                        fieldLabel: l('hotel.wuPass') + me.requiredStatus,
                        hidden: true
                    },
                    {
                        name: 'timeZone',
                        fieldLabel: l('hotel.timezone') + me.requiredStatus,
                        xtype: 'combobox',
                        allowBlank: false,
                        store: Ext.create('Admin.modules.hotel.timezone.Store').load(),
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
                    //info fields
                    {
                        name: 'index',
                        fieldLabel: l('hotel.index')
                    },
                    {
                        name: 'postAddress',
                        fieldLabel: l('hotel.postAddress')
                    },
                    {
                        name: 'officialAddress',
                        fieldLabel: l('hotel.officialAddress') + me.requiredStatus,
                        allowBlank: false,
                        change: function () {
                            console.log('ok');
                        }

                    },
                    {
                        name: 'bookPhone',
                        fieldLabel: l('hotel.bookPhone')
                    },
                    {
                        name: 'accountPhone',
                        fieldLabel: l('hotel.accountPhone')
                    },
                    {
                        name: 'infoPhone',
                        fieldLabel: l('hotel.infoPhone')
                    },
                    {
                        name: 'hotelEmail',
                        fieldLabel: l('hotel.email'),
                        vtype: 'email'
                    },
                    {
                        name: 'webSite',
                        fieldLabel: l('hotel.webSite')
                    },
                    {
                        name: 'description',
                        xtype: 'textarea',
                        fieldLabel: l('hotel.description')
                    },
                    {
                        name: 'vat',
                        xtype: 'numericfield',
                        fieldLabel: l('hotel.vat')
                    },
                    {
                        name: 'multiplier',
                        xtype: 'numericfield',
                        minValue: 0,
                        fieldLabel: l('hotel.multiplier')
                    },
                    {
                        name: 'tourismTax',
                        xtype: 'numericfield',
                        maxValue: 100,
                        minValue: 0,
                        fieldLabel: l('hotel.tourismTax')
                    },
                    {
                        name: 'maxRooms',
                        xtype: 'numericfield',
                        minValue: 0,
                        fieldLabel: l('hotel.maxRooms')
                    },
                    {
                        name: 'checkIn',
                        fieldLabel: l('hotel.info.checkIn') + me.requiredStatus,
                        xtype: 'timefield',
                        allowBlank: false,
                        format: 'H : i',
                        submitFormat: "H"
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        defaultType: 'timefield',
                        fieldLabel: l('hotel.info.earlyCheckIn'),
                        items: [
                            {
                                name: 'earlyCheckInStart',
                                format: 'H : i',
                                submitFormat: "H",
                                fieldLabel: l('from'),
                                labelWidth: 20,
                                flex: 1,
                                listeners: {
                                    change: function (start, value) {
                                        var end = start.nextNode();
                                        end.setMinValue(value);
                                        end.validate();
                                        end.allowBlank = value ? false : true;
                                    }
                                }
                            },
                            {
                                name: 'earlyCheckInEnd',
                                format: 'H : i',
                                submitFormat: "H",
                                fieldLabel: l('to'),
                                labelWidth: 20,
                                margins: '0 0 0 20',
                                flex: 1,
                                listeners: {
                                    change: function (end, value) {
                                        var start = end.previousNode();
                                        start.setMaxValue(value);
                                        start.validate();
                                        start.allowBlank = value ? false : true;
                                    }
                                }
                            }
                        ]
                    },
                    {
                        name: 'checkOut',
                        fieldLabel: l('hotel.info.checkOut') + me.requiredStatus,
                        xtype: 'timefield',
                        allowBlank: false,
                        format: 'H : i',
                        submitFormat: "H"
                    },
                    {
                        xtype: 'fieldcontainer',
                        layout: 'hbox',
                        defaultType: 'timefield',
                        fieldLabel: l('hotelinfo.lateCheckOut'),
                        items: [
                            {
                                name: 'lateCheckOutStart',
                                format: 'H : i',
                                submitFormat: "H",
                                fieldLabel: l('from'),
                                startLabel: l('from'),
                                labelWidth: 20,
                                flex: 1,
                                listeners: {
                                    change: function (start, value) {
                                        var end = start.nextNode();
                                        end.setMinValue(value);
                                        end.validate();
                                        end.allowBlank = value ? false : true;
                                    }
                                }
                            },
                            {
                                name: 'lateCheckOutEnd',
                                format: 'H : i',
                                submitFormat: "H",
                                fieldLabel: l('to'),
                                startLabel: l('to'),
                                labelWidth: 20,
                                margins: '0 0 0 20',
                                flex: 1,
                                listeners: {
                                    change: function (end, value) {
                                        var start = end.previousNode();
                                        start.setMaxValue(value);
                                        start.validate();
                                        start.allowBlank = value ? false : true;
                                    }
                                }
                            }
                        ]
                    },
                    {
                        fieldLabel: l('hotel.earlyCheckInNoBreakfast'),
                        name: 'earlyCheckInNoBreakfast',
                        xtype: 'checkbox',
                        inputValue: true
                    }
                ]
            },
            {
                xtype: 'fieldset',
                name: 'info',
                maxWidth: 500,
                title: l('hotel.personInfo'),
                margin: '5px',
                defaultType: 'textfield',
                defaults: {width: '75%'},
                layout: {
                    type: 'vbox',
                    align: 'center'
                },
                items: [
                    {
                        name: 'username',
                        fieldLabel: l('person.username') + me.requiredStatus,
                        allowBlank: false
                    },
                    {
                        name: 'password',
                        inputType: 'password',
                        fieldLabel: l('person.password') + me.requiredStatus,
                        allowBlank: false
                    },
                    {
                        inputType: 'password',
                        fieldLabel: l('person.passwordRepeat') + me.requiredStatus,
                        allowBlank: false,
                        validator: function (value) {
                            return value == this.previousNode().getValue() ? true : l('password.doNotMatch');
                        },
                        validateOnChange: false
                    },
                    {
                        name: 'email',
                        fieldLabel: l('person.email'),
                        vtype: 'email'
                    },
                    {
                        name: 'phone',
                        fieldLabel: l('person.phone') + me.requiredStatus,
                        allowBlank: false
                    },
                    {
                        name: 'firstName',
                        fieldLabel: l('person.firstName') + me.requiredStatus,
                        allowBlank: false
                    },
                    {
                        name: 'lastName',
                        fieldLabel: l('person.lastName') + me.requiredStatus,
                        allowBlank: false
                    },
                    {
                        name: 'position',
                        fieldLabel: l('person.position')
                    }
                ]
            },
//            {
//                xtype: 'locationMap',
//                margin: '0 0 0 50px'
//            }
        ];
        me.callParent(arguments);
    }
})
;
