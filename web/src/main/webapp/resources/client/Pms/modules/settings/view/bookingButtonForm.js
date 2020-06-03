Ext.define('Pms.modules.settings.view.bookingButtonForm', {
    extend: 'Pms.abstract.Form',
    alias: 'widget.bookingButtonForm',

    layout: 'border',
    bodyStyle: {
        backgroundColor: '#fff'
    },
    autoScroll: false,

    data: {},

    initComponent: function () {
        var me = this;


        me.items = [
            {
                xtype: 'fieldset',
                width: 300,
                title: l('settings.bookingButton'),
                layout: 'hbox',
                items: [
                    {
                        title: l('settings.synchronizeQuota'),
                        xtype: 'fieldset',
                        layout: 'vbox',
                        width: 400,
                        items: [
                            {
                                xtype: 'fieldcontainer',
//                                layout: 'hbox',
                                defaultType: 'textfield',
//                                padding: '20 0 0 0',
                                items: [
                                    {
                                        fieldLabel: 'Код гостиницы',
                                        xtype: 'textfield',
                                        name: 'hotelId',
                                        allowBlank: false,
                                        width: 300
                                    },
                                    {
                                        fieldLabel: 'Ширина',
                                        width: 300,
                                        xtype: 'textfield',
                                        name: 'width'
                                    },
                                    {
                                        fieldLabel: 'Цвет фона',
                                        width: 300,
                                        xtype: 'textfield',
                                        name: 'backgroundColor'
                                    },
                                    {
                                        fieldLabel: 'Цвет текста',
                                        width: 300,
                                        xtype: 'textfield',
                                        name: 'textColor'
                                    },
                                    {
                                        fieldLabel: 'Цветовая схема',
                                        width: 300,
                                        xtype: 'textfield',
                                        name: 'template'
                                    },
                                    {
                                        fieldLabel: 'Язык',
                                        width: 300,
                                        xtype: 'textfield',
                                        name: 'language'
                                    },
                                    {
                                        fieldLabel: 'Язык по-умолчанию',
                                        width: 300,
                                        xtype: 'textfield',
                                        name: 'defaultLanguage'
                                    },
//                                    {
//                                        fieldLabel: 'Выбор дат',
//                                        xtype: 'checkbox',
//                                        name: '1'
//                                    },
//                                    {
//                                        fieldLabel: 'Число ночей вместо даты выезда',
//                                        xtype: 'checkbox',
//                                        name: '1'
//                                    },
//                                    {
//                                        fieldLabel: 'Отмена бронирований',
//                                        xtype: 'checkbox',
//                                        name: '1'
//                                    },
//                                    {
//                                        fieldLabel: 'Форма отравки email',
//                                        xtype: 'checkbox',
//                                        name: '1'
//                                    },
//                                    {
//                                        fieldLabel: 'Иконка лучшей цены',
//                                        xtype: 'checkbox',
//                                        name: '1'
//                                    },
                                    {
                                        xtype: 'button',
                                        iconCls: 'fa fa-code-fork',
//                                        text: l('updateQuota.button'),
                                        text: "Сгенерировать виджет",
                                        handler: this.getFormData,
//                                        action: 'updateQuota',
//                                        requestDisable: true,
                                        hint: l('synchronizeQuota.hint'),
                                        margins: '0 0 0 25'
                                    }
//                                    {
//                                        name: 'dfrom',
//                                        width: 160,
//                                        fieldLabel: l('from'),
//                                        startLabel: l('from'),
//                                        format: 'd/m/Y',
//                                        labelWidth: 20,
//                                        allowBlank: false
//                                    }
                                ]
                            }
//                            {
//                                xtype: 'datefield',
//                                name: 'dto',
//                                width: 160,
//                                fieldLabel: l('to'),
//                                startLabel: l('to'),
//                                format: 'd/m/Y',
//                                labelWidth: 20,
//                                defaultAlign: "tl-bl?",
//                                allowBlank: false
//                            },
                        ]
                    },
                    {
                        title: 'Код виджета',
                        xtype: 'fieldset',
                        layout: 'vbox',
                        width: 400,
                        items: [
                            {
                                xtype: 'textarea',
                                width: 370,
                                height: 300,
                                name: 'widgetCode'
//                                text: l('updateQuota.button')
                            }
                        ]
                    }
                ]
            }
        ];

        me.callParent();
    },

    getFormData: function (btn) {
        var win = btn.up('window'),
            textarea = win.down('textarea[name=widgetCode]'),
            formPanel = btn.up('form'),
            form = formPanel.getForm(),
            formData = form.getFieldValues(),
            widgetCode = '',
            params = '';

        if (form.isValid()) {
            widgetCode += "<!--  PmsCloud Javascript Library Eventually, put it inside your <head/> tag --> \n";
//        widgetCode += '<script src="https://pmscloud.com/app/js/button.js"></script>';
            widgetCode += '<script src="http://booking-button.loc/js/button/pmsbook.js"></script>';
            widgetCode += "<!--  put the following div where widget be placed. Make sure to preserve the div contents (backlink)-->\n";
            widgetCode += '<div id="_pmsWidget_"></div>\n';
            widgetCode += "<script>";

            if (formData.hotelId) {
                widgetCode += "\n var Pms = new _Pms('" + formData.hotelId + "');\n";
            } else {
                widgetCode += "\n var Pms = new _Pms(2121211);\n";
            }

            if (formData.language) {
                params += "lang:'" + formData.language + "',\n";
            }

            if (formData.template) {
                params += "template:'" + formData.template + "',\n";
            }

            if (formData.textColor) {
                params += "textColor:'" + formData.textColor + "',\n";
            }

            if (formData.width) {
                params += "width:" + formData.width + ",\n";
            } else {
                params += "width: 200, \n";
            }

            if (formData.backgroundColor) {
                params += "backgroundColor:'" + formData.backgroundColor + "',\n";
            }

            if (formData.defaultLanguage) {
                params += "defaultLanguage:'" + formData.defaultLanguage + "'\n";
            }
            widgetCode += "var params = {\n" + params + "};\n";

            widgetCode += 'Pms.widget("_pmsWidget_", params);\n';
            widgetCode += '</script>';

            widgetCode += "\n <!--  that's all :) Thank you for using PmsCloud! -->";

            textarea.setValue(widgetCode);
        }
    },

    bbar: [
//        {
//            iconCls: 'booking-system-shortcut-icon',
//            text: l('importFromWubook.button'),
//            action: 'importFromWubook',
//            requestDisable: true,
//            hint: l('importFromWubook.hint')
//        },
        '->',
//        {
//            iconCls: 'save-action-icon',
//            text: l('save.btn'),
//            action: 'update',
//            requestDisable: true,
////            hint: 'Сохранить настройки'
//        },
        {
            iconCls: 'app-icon-remove',
            text: l('cancel.btn'),
            handler: function (btn) {
                btn.up('window').close();
            }
        }
    ]
});