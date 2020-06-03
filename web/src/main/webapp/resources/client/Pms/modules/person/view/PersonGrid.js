Ext.define('Pms.modules.person.view.PersonGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.personGrid',
    store: 'Pms.modules.person.store.Person',
    forceFit: true,

    initComponent: function () {
        var me = this,
            entryTypes = [],
            vipTypes = [],
            visaTypes = [],
            languages = [],
            countriesArr = [];

        for (var i in countries) {
            countriesArr[countries[i].abbr] = countries[i].country;
        }

        languages['ru'] = 'Русский';
        languages['uk'] = 'Українська';
        languages['en'] = 'English';

        me.columns = [
            {
                xtype: 'rownumberer',
                header: '№',
                width: 35,
                sortable: false,
                shrinkWrap: 3,
                renderer: function (value, meta, record) {
                    return record.index + 1;
                }
            },
            {
                header: l('firstName'),
                dataIndex: 'lastName',
                xtype: 'templatecolumn',
                tpl: '<span style="text-decoration:underline;cursor:pointer;">{lastName} {firstName}</span>',
                flex: 2
            },
            {
                header: l('phone'),
                dataIndex: 'phone',
                flex: 2
            },
            {
                header: l('discount'),
                dataIndex: 'discount',
                width: 60,
                renderer: function (val) {
                    return val ? val + '%' : '&mdash;'
                }
            },
            {
                header: l('email'),
                dataIndex: 'email',
                flex: 2,
                hidden: true
            },
            {
                header: l('person.country'),
                dataIndex: 'country',
                flex: 1,
                renderer: function (val, row) {
                    if (val) {
                        return Ext.create('Pms.modules.person.store.Country')
                            .findRecord('abbr', val)
                            .get('country');
                    } else {
                        return "&mdash;";
                    }
                }
            },
            {
                header: l('person.city'),
                dataIndex: 'city',
                flex: 1
            },
            {
                header: l('language'),
                dataIndex: 'language',
                flex: 1,
                renderer: function (val, row) {
                    return languages[val];
                }
            },
            {
                header: l('dateOfBirth'),
                xtype: 'datecolumn',
                dataIndex: 'dob',
                format: 'd/m/Y',
                flex: 1,
                hidden: false
            },
            {
                header: l('person.passportNumber'),
                dataIndex: 'passportNumber',
                flex: 1,
                hidden: true
            },
            {
                header: l('person.passportValidTill'),
                dataIndex: 'passportValidTill',
                flex: 1,
                hidden: true
            },
            {
                header: l('person.passportIssued'),
                dataIndex: 'passportIssued',
                flex: 1,
                hidden: true
            },
            {
                header: l('person.cio'),
                dataIndex: 'cio',
                flex: 1,
                hidden: true
            },
            {
                header: l('person.visaType'),
                dataIndex: 'visaType',
                flex: 1,
                renderer: function (val, row) {
                    return entryTypes[val];
                },
                hidden: true
            },
            {
                header: l('person.entryValidFrom'),
                dataIndex: 'entryValidFrom',
                flex: 1,
                hidden: true
            },
            {
                header: l('person.entryValidTill'),
                dataIndex: 'entryValidTill',
                flex: 1,
                hidden: true
            },
//            {
//                header: l('room'),
//                dataIndex: 'property_entry_number',
//                flex: 1,
//                hidden: true
//            },
//            {
//                header: 'Тип вызы',
//                dataIndex: 'property_entry_visa_type',
//                flex: 1,
//                renderer: function (val, row) {
//                    return visaTypes[val];
//                },
//                hidden: true
//            },
//            {
//                header: 'Марка авто',
//                dataIndex: 'property_car_brand',
//                flex: 1,
//                hidden: true
//            },
//            {
//                header: 'Номер авто',
//                dataIndex: 'property_car_number',
//                flex: 1,
//                hidden: true
//            },
//            {
//                header: 'VIP тип',
//                dataIndex: 'property_vip_type',
//                flex: 1,
//                renderer: function (val, row) {
//                    return vipTypes[val];
//                },
//                hidden: true
//            },
//            {
//                header: 'VIP причина',
//                dataIndex: 'property_vip_reason',
//                flex: 1,
//                hidden: true
//            },
//            {
//                header: 'Заполнен полностью',
//                dataIndex: 'property_filled_completely',
//                xtype: 'booleancolumn',
//                trueText: Pms.iconOk,
//                falseText: '',
//                flex: 1,
//                hidden: true
//            }
        ];
        me.callParent();
    }
});