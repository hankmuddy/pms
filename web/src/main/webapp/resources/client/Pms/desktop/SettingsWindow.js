Ext.define('Pms.desktop.SettingsWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.settingsWindow',

    title: l('settingWindow.title'),
    layout: 'border',
    autoShow: false,
    width: 640,
    height: 480,
    resizable: false,
    modal: true,

    initComponent: function () {
        this.items = [
            {
                region: 'center',
                border: false,
                xtype: 'panel',
                height: '100%',
                html: l('startMenu.settings')
            }
        ];

        this.callParent(arguments);
    }
});