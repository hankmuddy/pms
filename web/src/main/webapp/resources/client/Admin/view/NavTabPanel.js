Ext.define('Admin.view.NavTabPanel', function (self) {
    return{
        extend: 'Ext.tab.Panel',
        alias: 'widget.navtabpanel',
        listeners: {
            tabchange: (function (self) {
                return function (tabpanel, tab) {
                    self.onTabChange(tabpanel, tab)
                }
            })(self)
        },
        frame: true,
        flex: 2,
        statics: {
            onTabChange: function (tabpanel, tab) {
                if (tab.initialConfig.href) {
                    var href = tab.initialConfig.href;
                    delete tab.initialConfig['href'];
                    Admin.getApplication().navigateTo(href);
                    return
                }
                while (tabpanel = tab.down('tabpanel')) {
                    tab = tabpanel.getActiveTab()
                }
                var wrapper = tab.down('wrapper');
                Admin.getApplication().navigateTo(wrapper.getCurrentUrl(), wrapper.getCurrentState())
//            var breadcrumbs = tab.down('breadcrumbs');
//            if(!breadcrumbs){
//                return
//            }
//            var curUrl = breadcrumbs.getCurrentUrl();
//            location.hash = curUrl;
            }
        },
        tabBar: {
//            plain:true,
            items: [
                {
                    xtype: 'tbfill'
                },
//                {
//                    xtype: "tbtext",
////                    text:_('nickname'),
//                    style:{
//                        color: 'white',
//                        height:'25px',
//                        lineHeight:'25px'
//
//                    }
//                },
                {
//                    text: l(''),
                    xtype: 'button',
                    height: 20,
                    width: 20,
                    iconCls: 'fa fa-gear',
                    closable: false,
                    handler: function () {
                        Admin.getApplication().navigateTo('profile')
                    }
                },
                {
                    text: l(''),
                    xtype: 'button',
                    height: 20,
                    width: 20,
                    icon: 'themes/default/images/icons/16/exit.png',
                    closable: false,
                    handler: function () {
                        Ext.Msg.confirm(l('logout.info'), l('logout.message'), function (btn) {
                            if (btn === "yes") {
                                location.href = 'logout';
                            }
                        });

                    }
                }
            ]
        },
        showTab: function (itemId) {
            this.items.each(function (item) {
                if (item.itemId == itemId) {
                    item.tab.setVisible(true)
                }
            }, this)
        },
        items: [
            {
                title: l('common.hotel'),
                itemId: 'hotel',
                href: 'hotel/list',
                layout: 'fit'
            },
            {
                title: l('common.user'),
                itemId: 'user',
                href: 'user/list',
                layout: 'fit'
            },
            {
                title: l('common.bookingButton'),
                itemId: 'bookingButton',
                href: 'bookingButton/list',
                layout: 'fit'
            }
        ]
    }

});