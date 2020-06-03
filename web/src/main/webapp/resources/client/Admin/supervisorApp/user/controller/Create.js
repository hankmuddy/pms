Ext.define('Admin.supervisorApp.user.controller.Create', {
    extend: 'Admin.modules.user.controller.Create',
    viewClass: 'Admin.supervisorApp.user.view.Create',
    items: [
        {
            title: l('common.hotel'),
            itemId: 'hotel',
            href: 'hotel/list',
            layout: 'fit'
        }
    ]
});
