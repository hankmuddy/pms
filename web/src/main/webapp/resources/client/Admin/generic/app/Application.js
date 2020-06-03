Ext.define('Admin.generic.app.Application', {
        requires: ['Admin.generic.Utils', 'Admin.generic.Error'],
        extend: 'Ext.app.Application',
        helpWindow: null,
        name: 'Admin',
        appFolder: 'client/Admin',
        launch: function () {
            var me = this;
            window.addEventListener("hashchange", function () {
                me.start();
            });
            Ext.create('Admin.Lookup');
            Ext.Ajax.on("beforerequest", this.onBeforeRequest, this);
            Ext.Ajax.on("requestcomplete", this.onRequestComplete, this);
            Ext.Ajax.on('requestexception', function (conn, response, options) {
                if (Ext.ComponentQuery.query('button[requestDisable=true]').length)
                    Ext.ComponentQuery.query('button[requestDisable=true]')[0].enable();
                switch (response.status) {
                    case 401:
                        window.location = 'welcome';
                        break;
                    case 404:
                        Admin.getApplication().notFound({async: true});
                        break;
                    case 500:
                        Admin.getApplication().start({controller: 'Admin.error.controller.Main', action: 'serverCrash'})
                        break;
                }
            });
            Ext.create('Admin.view.Viewport');
            me.start();
            connectWebSocket(0);
        },
        onRequestComplete: function () {
            if (Ext.ComponentQuery.query('button[requestDisable=true]').length)
                Ext.ComponentQuery.query('button[requestDisable=true]')[0].enable();
        },
        onBeforeRequest: function () {
            if (Ext.ComponentQuery.query('button[requestDisable=true]').length)
                Ext.ComponentQuery.query('button[requestDisable=true]')[0].disable();

        },
        start: function (conf) {
//        this.dispatchController(conf);
            try {
                this.dispatchController(conf);
            } catch (error) {
                Admin.generic.Error.handle(error)
            }
        },
        getAppName: function () {
            if (this.appName) {
                return this.appName;
            }
            return ""
        },
        restart: function (conf) {
            Admin.generic.Error.throw('restartApplication', {controller: conf.controller, action: conf.action, params: conf.params})
        },
        navigateTo: function (path, params) {
            if (!params) {
                location.href = '#' + path;
                return
            }
            history.pushState(params, null, '#' + path);
            this.start();
        },
        getStateParams: function () {
            return history.state
        },
        notFound: function (conf) {
            Ext.resumeLayouts();
            if (conf && conf.async) {
                this.start({controller: 'Admin.error.controller.Main', action: 'notFound'})
                return
            }
            this.restart({controller: 'Admin.error.controller.Main', action: 'notFound'});
        },
        switchContent: function (item) {
            var contentWrapper = Ext.ComponentQuery.query('contentwrapper')[0];
            contentWrapper.getLayout().setActiveItem(item);
        },
        dispatchController: function (conf) {
            if (conf && conf.controller && conf.action) {
                // var controller = this.getController(conf.controller);
                try {
                    var controller = this.getController(conf.controller);
                    controller.dispatchAction(conf.action, conf.params);
                    return
                } catch (e) {
                    console.error('dispatchController({controller:Unknown})', conf.controller)
                    this.notFound({async: true});
                    return
                }
            }
            var ctrl, ctrl_name = Admin.generic.Utils.getHashPart(1) || "";
            if (ctrl_name == '') this.restart(this.defaultAction);
            else ctrl = this.route(ctrl_name);
            if (!ctrl) {
                console.error(this, "Unknown controller: ", ctrl_name)
                this.notFound();
            }
            var controller = this.getController(ctrl);
            controller.dispatchAction();
        },
        routeMap: {
//            profile: 'Admin.clazz.profile.controller.Main',
//            error: 'Admin.error.controller.Main'
        },
//    route: function(ctrl_name){
//        return this.routeMap[ctrl_name]
//    },
        defaultAction: {controller: "Admin.modules.hotel.controller.Main", action: "list"},
        route: function (ctrl_name) {
            var routeMap = {};
            Ext.Object.merge(routeMap, this.routeMap, {
                hotel: 'Admin.modules.hotel.controller.Main',
                user: 'Admin.modules.user.controller.Main',
                profile: 'Admin.modules.profile.controller.Main',
                bookingButton: 'Admin.modules.bookingButton.controller.Main'
//                creditCompany:'Admin.adminApp.creditCompany.controller.Main',
//                financialTransfer:'Admin.adminApp.financialTransfer.controller.Main',
//                message:'Admin.adminApp.message.controller.Main',
//                financialRequest: 'Admin.adminApp.financialRequest.controller.Main',
//                accountRequest: 'Admin.adminApp.accountRequest.controller.Main',
//                finmanager: 'Admin.adminApp.user.finmanager.controller.Main',
//                operator: 'Admin.adminApp.user.operator.controller.Main',
//                client: 'Admin.adminApp.user.client.controller.Main',
//                supervisor: 'Admin.adminApp.user.supervisor.controller.Main',
//                trash: 'Admin.adminApp.trash.controller.Main',
            });
            return routeMap[ctrl_name];
        }
    }/*, function () {
     Ext.application('Admin.generic.app.Application')
     }*/
//Ext.onReady(function () {
//
//    Pms.Admin = Ext.create('Pms.desktop.Application', {
//        name: "Pms.Admin",
//        listeners: {
//            ready: function () {
//                setTimeout(function () {
//                    Ext.get("loading").remove();
//                    Ext.get("loading-mask").fadeOut({
//                        remove: true
//                    });
//                }, 250);
//            }
//        }
//    });
);
