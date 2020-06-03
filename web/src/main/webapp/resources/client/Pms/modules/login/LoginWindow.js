Ext.define("Pms.modules.login.LoginWindow", {
    extend: "Pms.abstract.ModalWindow",
    requires: ["Pms.modules.login.LoginForm"],
    layout: "auto",
    modal: false,
    width: 340,
    height: 220,
    closable: false,
    forward: true,

    initComponent: function () {
        this.items = this.buildItems();
        this.callParent();
    },

    buildItems: function () {
        return [
            {
                xtype: "component"
            },
            Ext.create("Pms.modules.login.LoginForm", {
                forward: this.forward
            })
        ];
    }
});