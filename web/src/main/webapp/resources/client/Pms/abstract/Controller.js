Ext.define("Pms.abstract.Controller", {
    extend: "Ext.app.Controller",

    inUse: 0,
    onDesktop: false,
    // info: {
    //     text: '',
    //     iconCls: '',
    //     klass: '',
    //     desktop: false
    // },
    subControllers: [],

    extravailable: false,
    pmsavailable: true,

    label: l('module.label'),
    addSuccessMsg: l('addSuccess.msg'),
    updateSuccessMsg: l('updateSuccess.msg'),
    savedSuccessMsg: l('savedSuccess.msg'),
    deleteSuccessMsg: l('deleteSuccess.msg'),

    init: function () {
        var me = this;
        //Расширение окон вывода ошибок
        Ext.Msg.add({
            maxHeight: 1000,
            maxWidth: 1000
        });
        Ext.Msg.on('beforeshow', function () {
            Ext.Msg.doLayout();
        });
    },

    control: function (actions) {
        var me = this,
            eventbus = me.application.eventbus.bus;

        if (Ext.isObject(actions)) {
            var obj = {};
            for (var s in actions) {
                for (var e in actions[s]) {
                    if (Ext.isEmpty(eventbus[e])
                        || Ext.isEmpty(eventbus[e][s])
                        || Ext.isEmpty(eventbus[e][s][me.self.getName()])) {

                        obj[s] = actions[s];
                    }
                }
            }

            delete actions;

            if (!Ext.Object.isEmpty(obj)) {
                if (!me.selectors) {
                    me.selectors = [];
                }
                me.selectors.push(obj);

                me.callParent([obj]);
            }
        } else {
            me.callParent(arguments);
        }
    },

    routeAction: function (btn, e) {
        if (!Ext.isEmpty(btn.action)) {
            if (btn.gridAction) {
                var grid = btn.up('grid');
                if (!Ext.isEmpty(grid)) {
                    var rows = grid.getSelectionModel().getSelection();
                    if (rows.length) this[btn.action](grid, rows[0].index, null, rows);
                    else Ext.Msg.alert(l('error'), l('warning.selectRecord'));
                }
            }
            else {
                this[btn.action](btn, e);
            }
        }
    },

    showError: function (msg) {
        this.win.statusBar.setStatus({
            text: msg,
            iconCls: "x-status-error"
        });
    },

    buildItems: function (view) {
        if (typeof(this.win) !== 'undefined') {
            this.win.add(view);
        }
    },

    /**
     * This method displays a success message in the status bar of the main window
     * @param {String} msg The message to display
     */
    showMessage: function (msg) {
        this.win.statusBar.setStatus({
            text: msg,
            iconCls: "x-status-valid"
        });
    },

    /**
     * An abstract method to be implemented in the subclass, this method is executed
     * in the "init" method of the controller, the idea is to set the content of the main
     * window.
     *

     setViewport    : function(){
        this.win.add(Ext.create("Ext.panel.Panel",{html:"Hello world!"}));      
    }

     *
     */
    setViewport: Ext.emptyFn,
    /**
     * An abstract method. This method is executed when the user clicks in any button
     * withing the main window than contain a property "action" equals to "new".
     *
     */
    add: Ext.emptyFn,
    /**
     * An abstract method. This method is executed when the user clicks in any button
     * withing the main window than contain a property "action" equals to "save".
     */
    save: Ext.emptyFn,
    /**
     * An abstract method. This method is executed when the user clicks in any button
     * withing the main window than contain a property "action" equals to "delete".
     */
    remove: Ext.emptyFn
});