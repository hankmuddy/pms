Ext.define('Admin.modules.user.controller.Main', {
    extend: 'Admin.generic.app.controller.Main',
    viewClass: 'Admin.modules.user.view.Wrapper',
    name: 'user',
    action_list: 'Admin.modules.user.controller.List',
    action_add: 'Admin.modules.user.controller.Create',
    action_edit: 'Admin.modules.user.controller.Update',
    action_view: 'Admin.modules.user.controller.View'
});

