/*

 Plugin for Ext Scheduler (tested on 2.1.7)
 Copyright(c) 2009-2012 Bryntum AB
 http://bryntum.com/contact
 http://bryntum.com/license

 */
/**
 @class Sch.ux.plugin.GroupedEvents

 @author      Lukappa
 @version     1.0.1
 @date        2012/09/11

 Plugin for selecting and handling multiple events by a "MasterId" reference in the model.Event.

 Current features:
 * Group "fake" selection clicking one event of the group
 * Resizing related events of clicked event - Overlap & Tooltip validation

 ================================================================================
 TODO:
 * Drag&Drop of related events

 To add this plugin to scheduler:

 Overwrite the Sch.model.Event and add these methods (where MasterId is the event group common ID):
 ...
 getRelatedEvents : function() {
           if (!this.store) return null;

           return this.store.queryBy(function(ev) {
               return ev !== this && ev.get('MasterId') === this.get('MasterId');
           }, this);
       },
 ...

 var scheduler = Ext.create('Sch.panel.SchedulerGrid', {
            ...

            resourceStore   : resourceStore,
            eventStore      : eventStore,

            plugins         : [
                Ext.create('Sch.ux.plugin.GroupedEvents')
            ]
        });

 Dataset for test case:
 [
 {Id : 'e10', ResourceId: 'r1', MasterId: 'm1' Name : 'My 1st Special Task', StartDate : "2012-09-12", EndDate : "2010-09-14"},
 {Id : 'e11', ResourceId: 'r2', MasterId: 'm1' Name : 'My 1st Special Task', StartDate : "2012-09-12", EndDate : "2010-09-14"},
 {Id : 'e12', ResourceId: 'r3', MasterId: 'm1' Name : 'My 1st Special Task', StartDate : "2012-09-12", EndDate : "2010-09-14"},

 {Id : 'e13', ResourceId: 'r4', MasterId: 'm2' Name : 'My 2nd Special Task', StartDate : "2012-09-10", EndDate : "2010-09-12"},
 {Id : 'e14', ResourceId: 'r5', MasterId: 'm2' Name : 'My 2nd Special Task', StartDate : "2012-09-10", EndDate : "2010-09-12"},

 ...
 ]

 */
Ext.define("Sch.ux.plugin.GroupedEvents", {
    mixins: ['Ext.AbstractPlugin'],
    lockableScope: 'top',

    /**
     * @cfg {String} customMasterIdField The name of the field identifying the event group to which an event belongs. Defaults to "MasterId".
     */
    customMasterIdField: 'MasterId', // still not used

    /**
     * @cfg {Boolean} debugMode Set to true to enable debug alerts. Defaults to "false".
     */
    debugMode: false,
    //private
    lastEventsGroupSelected: null,
    warningTxt: "Sch.ux.plugin.GroupedEvents Warning!\nPlease, overwrite the Sch.event.Model and add the getRelatedEvents() method as explained into the doc.",

    constructor: function(config) {
        if(config)
            Ext.apply(this, config);
        this.callParent(arguments);
    },

    init: function(scheduler) {
        scheduler.on({
            eventpartialresize: this.onEventPartialResize,
//            eventresizeend: this.onEventResizeEnd,
            scope: this
        });
        this.scheduler = scheduler;
    },

    onEventPartialResize: function(s, rec, startDate, endDate, el) {

        var scheduler = this.scheduler,
            eventsOverlap = false,
            relatedEvents = rec.getRelatedEvents();

        if(relatedEvents.length == 0)
            return false;

        var eventWidth = Ext.fly(el).getWidth();

        relatedEvents.each(function(rel) {
            var relEl = scheduler.getSchedulingView().getElementFromEventRecord(rel);
            relEl.setWidth(eventWidth);
            //overlapping validation
            if(!scheduler.getSchedulingView().isDateRangeAvailable(startDate, endDate, rel, rel.getResource()))
                eventsOverlap = true;
        }, this);

        if(eventsOverlap) {
            scheduler.getSchedulingView().resizePlug.tip.update(startDate, endDate, false);
        }
    }
});