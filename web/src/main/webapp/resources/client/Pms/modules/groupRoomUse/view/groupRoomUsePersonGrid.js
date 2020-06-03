Ext.define('Pms.modules.groupRoomUse.view.groupRoomUsePersonGrid', {
    extend: 'Pms.abstract.Grid',
    alias: 'widget.groupRoomUsePersonGrid',
    store: 'Pms.modules.person.store.Person',

    masterPerson: false,
    masterCompany: false,

    initComponent: function () {
        var me = this
        languages = [],
            countries = [];

        Ext.create('Pms.modules.person.store.Country').load({
            callback: function (records, operation, success) {
                for (var i in records) countries[records[i].data.name] = records[i].data.label;
            }
        });
        Ext.create('Pms.modules.person.store.Language').load({
            callback: function (records, operation, success) {
                for (var i in records) languages[records[i].data.name] = records[i].data.label;
            }
        });

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
                dataIndex: 'name',
                xtype: 'templatecolumn',
                tpl: '<span style="text-decoration:underline;cursor:pointer;">{property_name} {property_surname} {property_patronymic}</span>',
                flex: 1
            },
            {
                header: l('contacts'),
                dataIndex: 'phone',
                xtype: 'templatecolumn',
                tpl: '{property_phone}<br />{property_email}',
                flex: 1
            },
//            {
//                header: 'Адрес',
//                dataIndex: 'property_reside_adress',
//                flex: 1,
//                renderer: function (v, col) {
//                    var rec = col.record.data;
//                    cell = [];
//
//                    rec.property_reside_country != '' && rec.property_reside_country != null ? cell.push(countries[rec.property_nationality]) : null;
//                    rec.property_reside_city != '' && rec.property_reside_city != null ? cell.push(rec.property_reside_city) : null;
//                    rec.property_reside_adress != '' && rec.property_reside_adress != null ? cell.push(rec.property_reside_adress) : null;
//
//                    return cell.join('<br />');
//                }
//            },
//            {
//                header: 'Данные',
//                dataIndex: 'property_nationality',
//                flex: 1,
//                renderer: function (v, col) {
//                    var rec = col.record.data;
//                    cell = [];
//
//                    rec.property_nationality != '' && rec.property_nationality != null ? cell.push('Национальность: ' + countries[rec.property_nationality]) : null;
//                    rec.property_language != '' && rec.property_language != null ? cell.push('Язык: ' + languages[rec.property_language]) : null;
//                    rec.property_birth != '' && rec.property_birth != null ? cell.push('Дата рождения: ' + Ext.Date.format(rec.property_birth, 'd/m/y')) : null;
//
//                    return cell.join('<br />');
//                }
//            },
            {
                header: l('masterPerson'),
                renderer: function (v, col) {
                    var rec = col.record.data,
                        personId = rec.id;

                    return personId == this.masterPerson ? Pms.iconOk : '';
                }
            }
        ];

        me.callParent();
    }
});