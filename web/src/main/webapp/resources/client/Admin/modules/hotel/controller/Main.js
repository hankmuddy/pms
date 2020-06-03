Ext.define('Admin.modules.hotel.controller.Main', {
    extend: 'Admin.generic.app.controller.Main',
    viewClass: 'Admin.modules.hotel.view.Wrapper',
    name: 'hotel',
    action_list: 'Admin.modules.hotel.controller.List',
    action_add: 'Admin.modules.hotel.controller.Create',
    action_edit: 'Admin.modules.hotel.controller.Update',
    action_view: 'Admin.modules.hotel.controller.View'
});

