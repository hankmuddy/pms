Ext.define('Admin.modules.user.controller.List', {
    extend: 'Admin.generic.app.controller.List',
    viewClass: 'Admin.modules.user.view.List',
    storeClass: 'Admin.modules.user.Store',
    setHandlersOnViewEvents: function () {
        this.callParent(arguments);
        this.view.on('broadcastAlert', this.broadcastAlert, this);
    },
    broadcastAlert: function(){
        Ext.Ajax.request({
            url: 'rest/settings/broadcast',
            params: {text: 'Внимание! Сайт PMSCloud.com будет перезапущен через 5 минут. Выйдите из системы и зайдите позже. \nAttention! PMSCloud.com will be restarted in 5 minutes. Please logout and re-login later. '},
            method: 'POST'
        })
    }
});
