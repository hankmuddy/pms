/**
 @class Sch.ux.plugin.TimeLine
 @extends Sch.plugin.Lines

 Plugin (ptype = 'scheduler_timeline') Same as CurrentTimeLine plugin, but uses Pms.fromUTC() method to adjust timezone offset.
 */
Ext.define("Sch.ux.plugin.TimeLine", {
    extend: "Sch.plugin.Lines",
    alias: 'plugin.scheduler_timeline',
    mixins: ['Sch.mixin.Localizable'],

    /**
     * @cfg {String} tooltipText The text to show in the tooltip next to the current time (defaults to 'Current time').
     * @deprecated Please use {@link #l10n l10n} instead.
     */
    /**
     * @cfg {Object} l10n
     * A object, purposed for the class localization. Contains the following keys/values:

     */
    tooltipText: l('currentTime'),

    /**
     * @cfg {Int} updateInterval This value (in ms) defines how often the timeline shall be refreshed. Defaults to every once every minute.
     */
    updateInterval: 60000,

    /**
     * @cfg {Boolean} autoUpdate true to automatically update the line position over time. Default value is `true`
     */
    autoUpdate: true,

    expandToFitView: true,

    timer: null,

    init: function(cmp) {
        var store = Ext.create("Ext.data.JsonStore", {
            fields: ['Date', 'Cls', 'Text'],
            data: [
                {Date: Pms.fromUTC(new Date(), true), Cls: 'sch-todayLine', Text: l('currentTime')}
            ]
        });

        var record = store.first();

        if(this.autoUpdate) {
            this.timer = setInterval(function() {
                record.set('Date', Pms.fromUTC(new Date(), true));
            }, this.updateInterval);
        }

        cmp.on('destroy', this.onHostDestroy, this);

        this.store = store;
        this.callParent(arguments);
    },

    onHostDestroy: function() {
        if(this.timer) {
            clearInterval(this.timer);
            this.timer = null;
        }

        if(this.store.autoDestroy) {
            this.store.destroy();
        }
    }
});