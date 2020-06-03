Ext.define("Pms.modules.login.LoginForm", {
    extend: "Pms.abstract.Form",
    alias: "loginform",
    forward: true,

    initComponent: function () {
        this.items = this.createLoginFields();
        this.fbar = this.createButtons();
        this.callParent();
    },

    createButtons: function () {
        return [
            {
                text: l('login.submit'),
//                text: l('login.pageTitle'),
                scope: this,
                handler: this.login
            },
            {
//                text: l('login.signup')
                text: l('cancel'),
                scope: this,
                handler: this.cancel
            }
        ];
    },

    createLoginFields: function () {
        return [
            {
                xtype: "fieldcontainer",
                layout: "vbox",
                defaultType: "textfield",
                width: 290,
                items: [
                    {
                        labelAlign: "top",
//                        msgTarget: 'side',
                        width: 290,
//                        fieldLabel: l('login.login'),
                        fieldLabel: l('login.hotel'),
                        name: "hotel",
                        allowBlank: true,
                        flex: 1
                    },
                    {
                        labelAlign: "top",
//                        msgTarget: 'side',
                        width: 290,
//                        fieldLabel: l('login.login'),
                        fieldLabel: l('login.login'),
                        name: "login",
                        allowBlank: false,
                        flex: 1
                    },
                    {
                        labelAlign: "top",
                        msgTarget: 'side',
                        inputType: 'password',
                        fieldLabel: l('login.password'),
//                        fieldLabel: 'Пароль',
                        name: 'password',
                        allowBlank: false,
                        width: 290,
                        flex: 1,
                        listeners: {
                            scope: this,
                            specialkey: function (f, e) {
                                if (e.getKey() == e.ENTER) {
                                    this.login();
                                }
                            }
                        }
                    }
                ]
            }
        ];
    },

    login: function () {
        if (this.getForm().isValid()) {
            var values = this.getForm().getValues();
            values.username = values.login + "%%%" + values.hotel;
            Pms.Ajax.request({
                url: '/app/login',
                params: values,
                el: this.up("window").el,
                scope: this,
                success: this.onSuccess,
                failure: this.onFailure
            });
        }
    },

    cancel: function () {
        document.location.href = '/app';
    },

    onSuccess: function (data, response) {
        if (data.success) {
            if (this.forward) {
                document.location = '/app';
            } else {
                var win = this.up("window");
                if (win) {
                    win.close();
                }
            }

        }
    },

    onFailure: function (data, response) {
        console.log('ok');
        var passwrd = this.down("textfield[name=password]");

        if (data && data.message) {
            Ext.create("Ext.tip.ToolTip", {
                anchor: "left",
                target: passwrd.bodyEl,
                trackMouse: false,
                html: data.message,
                autoShow: true
            });
        }

        passwrd.markInvalid(data.message);
    }
});