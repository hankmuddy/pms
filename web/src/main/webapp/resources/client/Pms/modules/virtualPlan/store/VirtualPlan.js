Ext.define('Pms.modules.virtualPlan.store.VirtualPlan', {
    extend: 'Pms.abstract.Store',
    model: 'Pms.modules.virtualPlan.model.VirtualPlan',
    alias: 'widget.virtualPlanStore',
    url: 'rest/virtualPlan'
});