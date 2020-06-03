Ext.define('Pms.desktop.AboutWindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.aboutWindow',

    title: l('aboutWin.title'),
    layout: 'border',
    autoShow: false,
    width: 340,
    height: 350,
    resizable: false,
    modal: true,

    initComponent: function () {
        this.items = [
            {
                region: 'center',
                border: false,
                xtype: 'panel',
//                baseCls: 'support-panel',
                height: '100%',
                html: '<div class="system_windows">' +
                    '<img src="themes/default/images/logo.svg"><br>' +
                    '<strong>PMS Cloud</strong>' +
                    '<p>' +
                    l('about.version') + ' ' + version + '<br>' +
                    l('about.buildDate') + ' ' + buildDate.split(' ')[0].split('-').reverse().join('/') +
                    '</p>' +
                    '<p>' +
                    'Copyright (c) 2012-2014 PMS Cloud<br>' +
                    l('about.allRightsProtected') +
                    '</p>' +
                    '<p>' + l('about.webSite') + ': www.pmscloud.com</p>' +
                    '</div>'
            }
        ];

        this.callParent(arguments);
    }
});