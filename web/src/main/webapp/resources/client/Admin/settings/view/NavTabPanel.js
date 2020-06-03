Ext.define('Admin.settings.view.NavTabPanel', {
    extend: 'Admin.view.NavTabPanel',
    tabBar: {
//            plain:true,
        items: [
            {
                xtype: 'tbfill'
            },
            {
                text: l(''),
                xtype: 'button',
                iconCls: 'fa fa-home',
                height: 20,
                width: 20,
                closable: false,
                handler: function () {
                    Admin.getApplication().navigateTo('')
                }
            },
//            {
//                text: l(''),
//                xtype: 'button',
//                icon: 'resources/images/exit.png',
//                closable: false,
//                handler: function () {
//                    location.href = 'logout';
//                }
//            }
        ]
    },
    items: [
        {
            title: l('common.profile'),
            itemId: 'profile',
            href: 'profile',
            layout: 'fit',
        },
    ]
})