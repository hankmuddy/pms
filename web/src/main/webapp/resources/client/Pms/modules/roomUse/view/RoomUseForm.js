Ext.define('Pms.modules.group.view.GroupForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.groupForm',

    layout: 'hbox',
    autoscroll: true,
    fileupload: true,

    items: [
        {
            xtype: 'container',
            margin: 5,
            items: [
                {
                    xtype: 'fieldset',
                    title: l('common'),
                    defaultType: 'textfield',

                    items: []
                }
            ]
        },
        {
            xtype: 'container',
            margin: 5,
            items: [
                {
                    xtype: 'fieldset',
                    title: l('contacts'),
                    defaultType: 'textfield',

                    items: []
                },
                {
                    xtype: 'fieldset',
                    title: l('bankDetails'),
                    defaultType: 'textfield',

                    items: []
                },
                {
                    xtype: 'button',
                    text: l('save.btn'),
                    action: 'save-group',
                    requestDisable: true
                }
            ]
        }
    ]
});