Ext.define('Admin.modules.bookingButton.view.ColumnList', {
    extend: 'Admin.generic.form.FieldConfig',
    items: {
        name: {
            header: l('bb.name'),
            dataIndex: 'name',
            flex: 2
        },
        language: {
            header: l('bb.language'),
            dataIndex: 'language',
            flex: 3
        },
        color: {
            header: l('bb.color'),
            dataIndex: 'color',
            flex: 2
        },
        backgroundColor: {
            header: l('bb.backgroundColor'),
            dataIndex: 'backgroundColor',
            flex: 2
        }
    }
});
