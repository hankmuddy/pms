Ext.define('Admin.modules.bookingButton.controller.List', {
    extend: 'Admin.generic.app.controller.List',
    viewClass: 'Admin.modules.bookingButton.view.List',
    storeClass: 'Admin.modules.bookingButton.Store',
    setHandlersOnViewEvents: function () {
        this.callParent(arguments);
        this.view.on('bbCode', this.onBbCode, this);
    },

    onBbCode: function (record) {
        console.log(record);
        var id = record.getId(),
            me = this;
        Ext.Ajax.request({
            url: 'admin/bbs/' + id,
            method: 'GET',
            success: function (data) {
                me.prepareCode(data.responseText);
            }
        })
    },

    prepareCode: function (data) {
        var widgetCode = '',
            params = '';

        data = Ext.decode(data);
        data = data.content;

        widgetCode += "<!--  PmsCloud Javascript Library Eventually, put it inside your <head/> tag --> \n";

        if (window.location.host == 'localhost:8443') {
            widgetCode += '<script src="https://localhost:8443/app/pbb/js/pmsbook.js"></script>';
        } else if (window.location.host == 'pmscloud.com') {
            widgetCode += '<script src="https://pmscloud.com/app/pbb/js/pmsbook.js"></script>';
        } else if (window.location.host == '88.198.41.177:8081') {
            widgetCode += '<script src="http://88.198.41.177:8081/app/pbb/js/pmsbook.js"></script>';
        }

        widgetCode += "<!--  put the following div where widget be placed. Make sure to preserve the div contents (backlink)-->\n";
        widgetCode += '<div id="_pmsWidget_"></div>\n';
        widgetCode += "<script>";

        if (data.hotel.hotelId) {
            widgetCode += "\nvar Pms = new _Pms('" + data.hotel.hotelId + "');\n";
        } else {
            widgetCode += "\nvar Pms = new _Pms('" + data.hotel.hotelId + "');\n";
        }

        params += "hotelId: '" + data.hotel.hotelId + "',\n";
        params += "button: '" + data.id + "',\n";

        if (data.language) {
            params += "lang:'" + data.language + "',\n";
        }

        if (data.template) {
            params += "template:'" + data.template + "',\n";
        }

        if (data.height) {
            params += "height:" + data.height + ",\n";
        } else {
            params += "height:400,\n";
        }

        if (data.cancel) {
            params += "cancel:" + data.cancel + ",\n";
        } else {
            params += "cancel: true,\n";
        }

        if (data.textColor) {
            params += "textColor:'" + data.textColor + "',\n";
        } else {
            params += "textColor:'#FFFFFF',\n";
        }

        if (data.width) {
            params += "width:" + data.width + ",\n";
        } else {
            params += "width: 300, \n";
        }

        if (data.backgroundColor) {
            params += "backgroundColor:'" + data.backgroundColor + "',\n";
        } else {
            params += "backgroundColor:'#14293a',\n";
        }

        widgetCode += "var params = {\n" + params + "};\n";

        widgetCode += 'Pms.widget("_pmsWidget_", params);\n';
        widgetCode += '</script>';

        widgetCode += "\n <!--  that's all :) Thank you for using PmsCloud! -->";

        console.log(widgetCode);

        var tagsToReplace = {
            '&': '&amp;',
            '<': '&lt;',
            '>': '&gt;'
        };

        function replaceTag(tag) {
            return tagsToReplace[tag] || tag;
        }

        var safe_tags_replace = function (str) {
            return str.replace(/[&<>]/g, replaceTag);
        };

        Ext.Msg.alert(l('bb.siteCode'), safe_tags_replace(widgetCode));
    }
});