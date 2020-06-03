Ext.define('Pms.desktop.HelpWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.helpWindow',

    title: l('helpWin.title'),
    layout: 'border',
    autoShow: false,
    width: 640,
    height: 480,
    resizable: true,
    modal: true,

    initComponent: function () {
        this.items = [
            {
                region: 'west',
                xtype: 'panel',
                width: 200,
                collapsible: true,
                split: false,
                title: l('theme'),
                html: l('theme'),
                minSize: 50,
                maxSize: 50
            },
            {
                region: 'center',
                xtype: 'panel',
                title: l('contents'),
                html: l('contents')
            }
        ];
        this.callParent(arguments);
    }
});