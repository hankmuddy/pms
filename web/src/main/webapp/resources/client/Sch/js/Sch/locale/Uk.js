/*

 Ext Scheduler 2.2.9
 Copyright(c) 2009-2013 Bryntum AB
 http://bryntum.com/contact
 http://bryntum.com/license

 */
/**
 * English translations for the Scheduler component
 *
 * NOTE: To change locale for month/day names you have to use the corresponding Ext JS language file.
 */
Ext.define('Sch.locale.RuRU', {
    extend: 'Sch.locale.Locale',
    singleton: true,

    l10n: {
        'Sch.util.Date': {
            unitNames: {
                YEAR: { single: 'рік', plural: 'років', abbrev: 'р' },
                QUARTER: { single: 'квартал', plural: 'кварталів', abbrev: 'квар' },
                MONTH: { single: 'місяць', plural: 'місяців', abbrev: 'міс' },
                WEEK: { single: 'тиждень', plural: 'тижні', abbrev: 'тиж' },
                DAY: { single: 'день', plural: 'днів', abbrev: 'д' },
                HOUR: { single: 'година', plural: 'годин', abbrev: 'ч' },
                MINUTE: { single: 'хвилина', plural: 'хвилин', abbrev: 'хв' },
                SECOND: { single: 'секунда', plural: 'секунд', abbrev: 'с' },
                MILLI: { single: 'миллисек', plural: 'міллісек', abbrev: 'мс' }
            }
        },

        'Sch.plugin.CurrentTimeLine': {
            tooltipText: 'Поточний час'
        },

        'Sch.plugin.EventEditor': {
            saveText: l('save.btn'),
            deleteText: l('delete.btn'),
            cancelText: l('cancel.btn')
        },

        'Sch.plugin.SimpleEditor': {
            newEventText: 'Нова подія ...'
        },

        'Sch.widget.ExportDialog': {
            generalError: 'Сталася помилка, спробуйте ще раз.',
            title: 'Налаштування експорту',
            formatFieldLabel: 'Розмір листа',
            orientationFieldLabel: 'Орієнтація',
            rangeFieldLabel: 'Діапазон експорту',
            showHeaderLabel: 'Додати номери сторінок',
            orientationPortraitText: 'Портрет',
            orientationLandscapeText: 'Ландшафт',
            completeViewText: 'Повний розклад',
            currentViewText: 'Поточна видима область',
            dateRangeText: 'Діапазон дат',
            dateRangeFromText: 'Експортувати с',
            pickerText: 'Виставте бажані розміри стовпців / рядків',
            dateRangeToText: 'Експортувати по',
            exportButtonText: 'Експортувати',
            cancelButton: ('cancel.btn'),
            progressBarText: 'Експортування ...',
            exportToSingleLabel: 'Експортувати як одну сторінку',
            adjustCols: 'Налаштування ширини стовпців',
            adjustColsAndRows: 'Налаштування ширини стовпців і висоти рядків',
            specifyDateRange: 'Вкажіть діапазон'
        },

        // -------------- View preset date formats/strings -------------------------------------
        'Sch.preset.Manager': function() {
            var M = Sch.preset.Manager,
                vp = M.getPreset("hourAndDay");

            if(vp) {
                vp.displayDateFormat = 'g:i A';
                vp.headerConfig.middle.dateFormat = 'g A';
                vp.headerConfig.top.dateFormat = 'd.m.Y';
            }

            vp = M.getPreset("dayAndWeek");
            if(vp) {
                vp.displayDateFormat = 'd.m h:i A';
                vp.headerConfig.middle.dateFormat = 'd.m.Y';
            }

            vp = M.getPreset("weekAndDay");
            if(vp) {
                vp.displayDateFormat = 'd.m';
                vp.headerConfig.bottom.dateFormat = 'd M';
                vp.headerConfig.middle.dateFormat = 'Y F d';
            }

            vp = M.getPreset("weekAndMonth");
            if(vp) {
                vp.displayDateFormat = 'd.m.Y';
                vp.headerConfig.middle.dateFormat = 'd.m';
                vp.headerConfig.top.dateFormat = 'd.m.Y';
            }

            vp = M.getPreset("weekAndDayLetter");
            if(vp) {
                vp.displayDateFormat = 'd/m/Y';
                vp.headerConfig.middle.dateFormat = 'D d M Y';
            }

            vp = M.getPreset("weekDateAndMonth");
            if(vp) {
                vp.displayDateFormat = 'd.m.Y';
                vp.headerConfig.middle.dateFormat = 'd';
                vp.headerConfig.top.dateFormat = 'Y F';
            }

            vp = M.getPreset("monthAndYear");
            if(vp) {
                vp.displayDateFormat = 'd.m.Y';
                vp.headerConfig.middle.dateFormat = 'M Y';
                vp.headerConfig.top.dateFormat = 'Y';
            }

            vp = M.getPreset("year");
            if(vp) {
                vp.displayDateFormat = 'd.m.Y';
                vp.headerConfig.middle.dateFormat = 'Y';
            }

            vp = M.getPreset("manyyears");
            if(vp) {
                vp.displayDateFormat = 'd.m.Y';
                vp.headerConfig.middle.dateFormat = 'Y';
            }
        }
    }
});
