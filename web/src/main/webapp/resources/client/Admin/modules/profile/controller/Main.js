Ext.define('Admin.modules.profile.controller.Main', {
    extend: 'Admin.generic.app.controller.Main',
    viewClass: 'Admin.modules.profile.view.Wrapper',
    defaultAction: 'view',
    name: 'profile',
    card: 'settings',
    action_edit: 'Admin.modules.profile.controller.Update',
    action_view: 'Admin.modules.profile.controller.View'
});
