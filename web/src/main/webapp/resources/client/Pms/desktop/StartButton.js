Ext.define('Pms.desktop.StartButton', {
    extend: 'Ext.button.Button',

    menuAlign: 'bl-tl',
    clickEvent: 'mousedown',
    text: l('startMenu.settingsUpperCase'),
    cls: "pms-start-button",
    iconCls: "icon-start-menu",
    arrowCls: null
    // iconCls : "pms-start-button-icon",
});