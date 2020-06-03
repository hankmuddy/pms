Ext.define('Pms.modules.groupRoomUse.view.groupRoomUseAddTabs', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.groupRoomUseAddTabs',

    setItems: [],
    listener: {
        afterrender: function (tabPanel) {
            if (tabPanel.setItems.length > 3)
                tabPanel.setActiveTab(tabPanel.setItems.length - 3);
        }
    },

    initComponent: function () {
        var me = this;
        if (me.setItems.length > 3)
            me.activeTab = me.setItems.length - 3;
        if (!me.setItems.length) {
            me.items = [
                {
                    title: l('guest'),
                    iconCls: 'app-icon-add',
                    listeners: {
                        activate: function (tab) {
                            me.addTab(tab, false);
                        }
                    }
                },
                {
                    title: l('child'),
                    iconCls: 'app-icon-add',
                    listeners: {
                        activate: function (tab) {
                            me.addTab(tab, true);
                        }
                    }
                }
            ];
        }
        else {
            me.items = me.setItems;
        }

        me.callParent(arguments);
    },

    addTab: function (tab, isChild) {
        var tabPanel = tab.up('tabpanel'),
            tabTitle = isChild ? l('child') : l('guest'),
            xtype = isChild ? 'childBookForm' : 'personBookForm';

        tabPanel.tabCount = tabPanel.items.items.length - 2;

        tabPanel.insert(tabPanel.tabCount, {
            title: tabTitle,// + ' ' + tabPanel.tabCount,
            closable: true,
            items: [
                {
                    xtype: xtype
                }
            ],
            listeners: {
                close: function () {
                    tabPanel.tabCount--;
                    tabPanel.setActiveTab(0);
                }
            }
        }).show();
        tabPanel.tabCount++;
    }
});