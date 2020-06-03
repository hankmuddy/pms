Ext.define('Admin.modules.hotel.controller.Update', {
    extend: 'Admin.generic.app.controller.Update',
    viewClass: 'Admin.modules.hotel.view.Update',
    modelClass: 'Admin.modules.hotel.Model',
    initView: function (conf) {
        if (Admin.generic.Utils.getHashPart(4) == 'paidUntil') {
            this.view = Ext.create('Admin.modules.hotel.view.PaidUntil');
        }
        else {
            this.view = Ext.create(this.viewClass)
        }
    },
    onUpdateSubmit: function (record) {
        var maxRooms = record.get('maxRooms'),
            id = record.getId(),
            paidUntil = Ext.Date.format(record.get('paidUntil'),'Y-m-d');
        if (Admin.generic.Utils.getHashPart(4) == 'paidUntil') {
            Ext.Ajax.request({
                url: 'admin/hotel/' + id + '/paidUntil/' + paidUntil,
                method: 'PUT',
                success: function () {
                    Admin.getApplication().navigateTo('hotel/list');
                }
            })
        }
        else {
            Ext.Ajax.request({
                url: 'admin/hotel/' + id + '/maxRooms/' + maxRooms,
                method: 'PUT',
                success: function () {
                    Admin.getApplication().navigateTo('hotel/list');
                }
            })
        }
    },
    convertBeforeRender: function (record) {
        if(record.data.paidUntil) {
            record.set('paidUntil', new Date(record.data.paidUntil * 1000));
        }
        return record;
    },
    setBreadcrumbs: function(record){
        if (this.breadcrumbs) {
            var href = this.links['edit'];
            var breadcrumbsConf = {};
            breadcrumbsConf[href] = {
                href: href + '/' + this.currentId,
                title: record.get(record.data.info.name)
            };
            this.breadcrumbs.setBreadcrumbs(href, breadcrumbsConf);
        }
    }
});
