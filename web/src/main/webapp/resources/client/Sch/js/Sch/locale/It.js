/*

 Ext Scheduler 2.2.19
 Copyright(c) 2009-2014 Bryntum AB
 http://bryntum.com/contact
 http://bryntum.com/license

 */
/**
 * Italian translations for the Scheduler component
 *
 * NOTE: To change locale for month/day names you have to use the corresponding Ext JS language file.
 */
Ext.define('Sch.locale.It', {
    extend: 'Sch.locale.Locale',
    singleton: true,

    l10n: {
        'Sch.util.Date': {
            unitNames: {
                YEAR: { single: 'anno', plural: 'anni', abbrev: 'anno' },
                QUARTER: { single: 'quadrimestre', plural: 'quadrimestri', abbrev: 'q' },
                MONTH: { single: 'mese', plural: 'mesi', abbrev: 'mese' },
                WEEK: { single: 'settimana', plural: 'settimane', abbrev: 'sett' },
                DAY: { single: 'giorno', plural: 'giorni', abbrev: 'g' },
                HOUR: { single: 'ora', plural: 'ore', abbrev: 'o' },
                MINUTE: { single: 'minuto', plural: 'minuti', abbrev: 'min' },
                SECOND: { single: 'secondo', plural: 'secondi', abbrev: 's' },
                MILLI: { single: 'ms', plural: 'ms', abbrev: 'ms' }
            }
        },

        'Sch.view.SchedulerGridView': {
            loadingText: 'Caricamento eventi...'
        },

        'Sch.plugin.CurrentTimeLine': {
            tooltipText: 'Tempo attuale'
        },

        'Sch.plugin.EventEditor': {
            saveText: 'Salva',
            deleteText: 'Elimina',
            cancelText: 'Annulla'
        },

        'Sch.plugin.SimpleEditor': {
            newEventText: 'Nuova prenotazione...'
        },

        'Sch.widget.ExportDialog': {
            generalError: 'Errore, prova nuovamente.',
            title: 'Impostazioni Esportazione',
            formatFieldLabel: 'Formato Carta',
            orientationFieldLabel: 'Orientamento',
            rangeFieldLabel: 'Range esportazione',
            showHeaderLabel: 'Aggiungi numero pagina',
            orientationPortraitText: 'Verticale',
            orientationLandscapeText: 'Orizzontale',
            completeViewText: 'Schedulatore completo',
            currentViewText: 'Vista attuale',
            dateRangeText: 'Range di date',
            dateRangeFromText: 'Esporta da',
            pickerText: 'Ridimensiona colonne/righe al valore desiderato',
            dateRangeToText: 'Esporta a',
            exportButtonText: 'Esporta',
            cancelButtonText: 'Annulla',
            progressBarText: 'Esporta...',
            exportToSingleLabel: 'Esporta come pagina singola',
            adjustCols: 'Imposta larghezza colonna',
            adjustColsAndRows: 'Imposta larghezza colonna e altezza riga',
            specifyDateRange: 'Specifica intervallo date'
        },

        // -------------- View preset date formats/strings -------------------------------------
        'Sch.preset.Manager': function () {
            var M = Sch.preset.Manager,
                vp = M.getPreset("hourAndDay");

            if (vp) {
                vp.displayDateFormat = 'G:i';
                vp.headerConfig.middle.dateFormat = 'G:i';
                vp.headerConfig.top.dateFormat = 'D d/m';
            }

            vp = M.getPreset("secondAndMinute");
            if (vp) {
                vp.displayDateFormat = 'G:i';
                vp.headerConfig.top.dateFormat = 'D, d/m G:i';
            }

            vp = M.getPreset("dayAndWeek");
            if (vp) {
                vp.displayDateFormat = 'd/m h:i A';
                vp.headerConfig.middle.dateFormat = 'D d M';
            }

            vp = M.getPreset("weekAndDay");
            if (vp) {
                vp.displayDateFormat = 'd/m';
                vp.headerConfig.bottom.dateFormat = 'd M';
                vp.headerConfig.middle.dateFormat = 'Y F d';
            }

            vp = M.getPreset("weekAndMonth");
            if (vp) {
                vp.displayDateFormat = 'd/m/Y';
                vp.headerConfig.middle.dateFormat = 'd/m';
                vp.headerConfig.top.dateFormat = 'd/m/Y';
            }

            vp = M.getPreset("weekAndDayLetter");
            if (vp) {
                vp.displayDateFormat = 'd/m/Y';
                vp.headerConfig.middle.dateFormat = 'D d M Y';
            }

            vp = M.getPreset("weekDateAndMonth");
            if (vp) {
                vp.displayDateFormat = 'd/m/Y';
                vp.headerConfig.middle.dateFormat = 'd';
                vp.headerConfig.top.dateFormat = 'Y F';
            }

            vp = M.getPreset("monthAndYear");
            if (vp) {
                vp.displayDateFormat = 'd/m/Y';
                vp.headerConfig.middle.dateFormat = 'M Y';
                vp.headerConfig.top.dateFormat = 'Y';
            }

            vp = M.getPreset("year");
            if (vp) {
                vp.displayDateFormat = 'd/m/Y';
                vp.headerConfig.middle.dateFormat = 'Y';
            }
        }
    }
});
