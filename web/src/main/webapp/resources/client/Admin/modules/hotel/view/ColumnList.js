Ext.define('Admin.modules.hotel.view.ColumnList', {
    extend: 'Admin.generic.form.FieldConfig',
    items: {
        name: {
            header: l('hotel.name'),
            dataIndex: 'info',
            renderer: function (val) {
                return val.name
            },
            flex: 2
        },
        address: {
            header: l('hotel.address'),
            dataIndex: 'info',
            renderer: function (val) {
                return val.officialAddress
            },
            flex: 3
        },
        country: {
            header: l('hotel.country'),
            dataIndex: 'info',
            flex: 2,
            renderer: function (val) {
                return val.country
            }
        },
        city: {
            header: l('hotel.city'),
            dataIndex: 'info',
            flex: 2,
            renderer: function (val) {
                return val.city
            }
        },
        hotelId: {
            header: l('hotel.id'),
            dataIndex: 'hotelId',
            sortable: false,
            flex: 2
        },
        lcode: {
            header: l('hotel.lcode'),
            dataIndex: 'lcode',
            flex: 2
        },
        createdDate: {
            header: l('createdDate'),
            dataIndex: 'createdDate',
            flex: 2,
            renderer: function (v) {
                v = v.toString().length > 10 ? v : v * 1000;
                var _date = new Date(v);
                return Ext.Date.format(_date, 'd/m/y')
            }
        },
        manager: {
            header: l('userType.manager'),
            dataIndex: 'manager',
            flex: 2,
            renderer: function (v) {
                if (typeof v !== 'object') return ''
                return v ? '<a href="#user/view/' + v.id + '" style="color: #000000;text-decoration:underline;cursor:pointer;">' + v.username + '</a>' : ''
            },
            editor: {
                xtype: 'combobox',
                allowBlank: false,
                name: 'manager',
//                triggerAction: 'all',
                selectOnTab: true,
                store: Ext.create('Admin.modules.user.Store', {
                    loadParams: {
                        params: {
                            filter: [
                                {
                                    field: 'userType',
                                    comparison: 'eq',
                                    data: {
                                        type: 'string',
                                        value: 'manager'
                                    }
                                }
                            ]
                        }
                    }
                }).load(),
                displayField: 'username',
                valueField: 'id',
                queryMode: 'remote',
            }

        },
        supervisor: {
            header: l('userType.managerSupervisor'),
            dataIndex: 'supervisor',
            flex: 2,
            renderer: function (v) {
                if (typeof v !== 'object') return ''
                return v ? '<a href="#user/view/' + v.id + '"style="color: #000000;text-decoration:underline;cursor:pointer;">' + v.username + '</a>' : ''
            },
            editor: {
                xtype: 'combobox',
                allowBlank: false,
                name: 'supervisor',
                selectOnTab: true,
                store: Ext.create('Admin.modules.user.Store', {
                    loadParams: {
                        params: {
                            filter: [
                                {
                                    field: 'userType',
                                    comparison: 'eq',
                                    data: {
                                        type: 'string',
                                        value: 'MANAGER_SUPERVISOR'
                                    }
                                }
                            ]
                        }
                    }
                }).load(),
                displayField: 'username',
                valueField: 'id',
                queryMode: 'remote',
            }
        }
//        tin: {
//            header: l('company.tin'),
//            dataIndex: 'tin',
//            flex: 1
//        }
    }
});
