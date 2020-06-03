Ext.define("Pms.modules.child.store.Child", {
    extend: "Pms.abstract.Store",
    model: "Pms.modules.child.model.Child",
    alias: 'widget.childStore',
    url: 'rest/child',
    sorters: [{
        property: 'lastName',
        direction: 'ASC'
    }],
    extravailable: true,


    extraParams: {
        sort: {lastName: 'ASC'}
    },

    fromSearchCombo: false,
//    filterVal: '',
//    filterName: '',
//    children: null,
//
//    listeners: {
//        beforeload: function (store, operation, eOpts) {
//            if (!operation.params) {
//                operation.params = {};
//            }
//            if (this.paging) {
//                operation.params.limit = {count: this.pageSize, page: this.currentPage};
//            }
//            if (this.fromSearchCombo && this.filterVal != '') {
//                operation.params.filter = {property_surname: {contain: this.filterVal}};
//            }
//            if (this.fromSearchCombo && this.filterName != '') {
//                operation.params.filter = {property_name: {contain: this.filterName}};
//            }
//            if (this.children) {
//                operation.params.filter = {id: {in: this.children}};
//            }
//        }
//    }
});