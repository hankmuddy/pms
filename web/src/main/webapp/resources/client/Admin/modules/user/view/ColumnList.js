Ext.define('Admin.modules.user.view.ColumnList', {
    extend: 'Admin.generic.form.FieldConfig',
    items: {
        username: {
            header: l('user.username'),
            dataIndex: 'username',
            flex: 2
        },
        language: {
            header: l('user.language'),
            dataIndex: 'language',
            flex: 3
        },
        userType: {
            header: l('user.userType'),
            dataIndex: 'userType',
            flex: 2
        },
        hotel: {
            header: l('user.hotel'),
            dataIndex: 'hotel',
            flex: 2,
            renderer: function (val) {
                return val ? '<span style="text-decoration:underline;cursor:pointer;">' + val.info.name + '</span>' : '&mdash;';
            },
            listeners: {
                click: function(cell, el, row, col, event, rec){
                    Admin.getApplication().navigateTo('hotel/view/' + rec.data.hotel.id);
                }
            }
        },
        online: {
            header: l('user.online'),
            dataIndex: 'online',
            sortable: false,
            flex: 2,
            renderer: function (val) {
                return val ? l('yes') : '&mdash;';
            }
        },
        lastLoggedIn: {
            header: l('user.lastLoggedIn'),
            dataIndex: 'lastLoggedIn',
            sortable: false,
            flex: 2,
            renderer: function (v) {
                if (!v) return '&mdash;';
                return Ext.Date.format(new Date(Admin.generic.Utils.fromUTC(v, true)), 'd/m/y H:i');
            }
        }
    }
});
