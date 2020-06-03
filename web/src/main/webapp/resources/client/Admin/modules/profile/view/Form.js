Ext.define('Admin.modules.profile.view.Form', {
    extend: 'Admin.generic.form.FieldConfig',
    items: {
        language: {
            fieldLabel: l('user.language'),
            name: "language",
            lookupType: 'language',
            itemId: 'editWinLanguage',
            xtype: 'lookupCombobox',
            valueNotFoundText: null,
        }
    }
})
