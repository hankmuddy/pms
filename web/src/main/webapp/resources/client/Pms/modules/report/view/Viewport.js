Ext.define('Pms.modules.report.view.Viewport', {
    extend: 'Pms.abstract.Viewport',
    alias: 'widget.reportViewport',

    autoScroll: false,

    initComponent: function () {
        var me = this;
        me.items = this.buildItems();
        me.callParent();
    },

    buildItems: function () {
        var me = this,
            reports = [
                me.singleDateForm('hotelLoad'),
                me.singleDateForm('roomLoadMap'),
                me.singleDateForm('breakfast'),
                me.periodForm('receptionCash'),
                me.periodForm('income'),
                me.periodForm('dateStat'),
                me.periodForm('freeRooms'),
                me.periodForm('foreignGuestBook'),
                me.periodForm('guestBook')
            ];

        if(_('hotel').country == 'UA') {
            reports.push(me.periodForm('bookingRegBook'));
            reports.push(me.periodForm('roomTypeSales'));
        }

        if(_('hotel').country == 'RU') {
            reports.push({
                xtype: 'panel',
                title: l('report.foreignerArrivalNotification'),
                autoScroll: false,
                items: [{
                    xtype: 'button',
                    text: l('settings.upload') + " \"" + l('report.foreignerArrivalNotification') + "\"",
                    scale: 'large',
                    margin: 10,
                    width: '95%',
                    handler: function(btn, e) {
                        Ext.create('Ext.window.Window', {
                            title: l('report.foreignerArrivalNotification'),
                            width: 1000,
                            height: 600,
                            maximizable: true,
                            listeners: {
                                close: Pms.removePreloader
                            },
                            layout: 'fit',
                            items: [{
                                xtype: 'box',
                                autoEl: {
                                    tag: 'iframe',
                                    style: 'height: 100%; width: 100%',
                                    src: '/app/client/foreignerArrivalNotification.pdf',
                                    onload: 'Pms.removePreloader()'
                                },
                                listeners: {
                                    beforerender: function(panel) {
                                        panel.setLoading(true, true);
                                        Pms.createPreloader();
                                    }
                                }
                            }]
                        }).show();
                    }
                }],
            });
        }

        return [{
            xtype: 'panel',
            region: 'center',
            layout: 'accordion',
            items: reports
        }];
    },

    singleDateForm: function (title) {
        var me = this;
        
        return {
            xtype: 'panel',
            title: l('report.' + title),
            autoScroll: false,
            items: [{
                xtype: 'form',
                items: [{
                    xtype: 'pmsdatefield',
                    name: 'reportDate',
                    fieldLabel: l('report.date'),
                    value: Ext.Date.format(new Date(), "d/m/Y"),
                    format: 'd/m/y',
                    submitFormat: 'U',
                    margin: 10
                }],
                rbar: [{
                    text: l('report.loadReport'),
                    scale: 'large',
                    handler: function(btn, e) {
                        var reportDate = btn.up('form').down('datefield').getValue(),
                            param = '',
                            titleDate = Ext.Date.format(new Date(), 'd/m/Y');

                        if(reportDate) {
                            titleDate = Ext.Date.format(reportDate, 'd/m/Y');
                            param = '?reportDate=' + Pms.toUTC(reportDate);
                        }
                        var urlSuffix = title + '/' + param,
                            winTitle = l('report.' + title + 'On') + " " + titleDate;
                        me.openReportWindow(urlSuffix, winTitle);
                    }
                }]
            }]
        };
    },

    periodForm: function (title) {
        var me = this;
        
        return {
            xtype: 'panel',
            title: l('report.' + title),
            autoScroll: false,
            items: [{
                xtype: 'form',
                items: [{
                    xtype: 'pmsdatefield',
                    name: 'periodStart',
                    fieldLabel: l('report.periodStart'),
                    value: Ext.Date.format(new Date(), "d/m/Y"),
                    format: 'd/m/y',
                    submitFormat: 'U',
                    margin: 10
                },{
                    xtype: 'pmsdatefield',
                    name: 'periodEnd',
                    fieldLabel: l('report.periodEnd'),
                    value: Ext.Date.format(new Date(), "d/m/Y"),
                    format: 'd/m/y',
                    submitFormat: 'U',
                    margin: 10
                }],
                rbar: ['->',{
                    text: l('report.loadReport'),
                    scale: 'large',
                    handler: function(btn, e) {
                        var periodStart = btn.up('form').down('datefield[name=periodStart]').getValue(),
                            periodEnd = btn.up('form').down('datefield[name=periodEnd]').getValue(),
                            param = '';
                        titleDate1 = Ext.Date.format(new Date(), 'd/m/Y');
                        titleDate2 = Ext.Date.format(Ext.Date.add(new Date(), Ext.Date.DAY, 1), 'd/m/Y');

                        if(periodStart) {
                            titleDate1 = Ext.Date.format(periodStart, 'd/m/Y');
                            param += '?periodStart=' + Pms.toUTC(periodStart);
                            if(periodEnd) {
                                titleDate2 = Ext.Date.format(periodEnd, 'd/m/Y');
                                param += '&periodEnd=' + Pms.toUTC(periodEnd);
                            }
                            else titleDate2 = Ext.Date.format(Ext.Date.add(periodStart, Ext.Date.DAY, 1), 'd/m/Y');
                        }
                        var urlSuffix = title + '/' + param,
                            winTitle = l('report.' + title + 'On') + " " + titleDate1 + " - " + titleDate2;
                        me.openReportWindow(urlSuffix, winTitle);
                    }
                },'->']
            }]
        };
    },

    openReportWindow: function (urlSuffix, winTitle) {
        Ext.create('Ext.window.Window', {
            title: winTitle,
            width: 1000,
            height: 600,
            maximizable: true,
            listeners: {
                close: Pms.removePreloader
            },
            layout: 'fit',
            items: [{
                xtype: 'box',
                autoEl: {
                    tag: 'iframe',
                    style: 'height: 100%; width: 100%',
                    src: '/app/report/' + urlSuffix,
                    onload: 'Pms.removePreloader()'
                },
                listeners: {
                    beforerender: function(panel) {
                        panel.setLoading(true, true);
                        Pms.createPreloader();
                    }
                }
            }]
        }).show();
    },

    buildTopButtons: function () {
    }
});
