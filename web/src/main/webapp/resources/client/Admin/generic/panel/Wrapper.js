Ext.define('Admin.generic.panel.Wrapper', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.wrapper',
    requires: ['Admin.generic.ux.Breadcrumbs'],
    layout: 'card',
    constructor: function () {
        this.setCurrentUrl();
        this.setCurrentState();
        this.callParent(arguments);
    },
    getCurrentUrl: function () {
        return this.currentUrl;
    },
    setCurrentUrl: function () {
        var hash = location.hash;
        if (hash[0] == '#')hash = hash.slice(1);
        this.currentUrl = hash;
    },
    getCurrentState: function () {
        return this.currentState;
    },
    setCurrentState: function () {
        this.currentState = history.state;
    }
});
