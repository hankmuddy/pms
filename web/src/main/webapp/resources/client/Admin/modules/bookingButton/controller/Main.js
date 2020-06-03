Ext.define('Admin.modules.bookingButton.controller.Main', {
    extend: 'Admin.generic.app.controller.Main',
    viewClass: 'Admin.modules.bookingButton.view.Wrapper',
    name: 'bookingButton',
    action_list: 'Admin.modules.bookingButton.controller.List',
    action_add: 'Admin.modules.bookingButton.controller.Create',
    action_edit: 'Admin.modules.bookingButton.controller.Update',
    action_view: 'Admin.modules.bookingButton.controller.View'
});

