Ext.onReady(function() {
    var cm = Ext.ClassManager,
        exists = Ext.Function.bind(cm.get, cm);

    if(Ext.Updater) {
        Ext.Updater.defaults.indicatorText = '<div class="loading-indicator">Триває завантаження...</div>';
    }

    Ext.define("Ext.locale.uk.view.View", {
        override: "Ext.view.View",
        emptyText: ""
    });

    Ext.define("Ext.locale.uk.grid.Panel", {
        override: "Ext.grid.Panel",
        ddText: "{0} вибраних рядків"
    });

    Ext.define("Ext.locale.uk.TabPanelItem", {
        override: "Ext.TabPanelItem",
        closeText: "Закрити цю вкладку"
    });

    Ext.define("Ext.locale.uk.form.field.Base", {
        override: "Ext.form.field.Base",
        invalidText: "Значення в цьому полі невірне"
    });

    Ext.define('Ext.locale.uk.FileButton', {
        override: 'Ext.form.field.File',
        buttonText: 'Вибрати...'
    });

    // changing the msg text below will affect the LoadMask
    Ext.define("Ext.locale.uk.view.AbstractView", {
        override: "Ext.view.AbstractView",
        msg: "Завантаження..."
    });

    if(Ext.Date) {
        Ext.Date.monthNames = [
            "Січень",
            "Лютий",
            "Березень",
            "Квітень",
            "Травень",
            "Червень",
            "Липень",
            "Серпень",
            "Вересень",
            "Жовтень",
            "Листопад",
            "Грудень"
        ];

        Ext.Date.shortMonthNames = [
            "Січ",
            "Лют",
            "Бер",
            "Квт",
            "Трав",
            "Чер",
            "Лип",
            "Сер",
            "Вер",
            "Жов",
            "Лис",
            "Гру"
        ];

        Ext.Date.getShortMonthName = function(month) {
            return Ext.Date.shortMonthNames[month];
        };

        Ext.Date.monthNumbers = {
            'Січ': 0,
            'Лют': 1,
            'Бер': 2,
            'Квт': 3,
            'Трав': 4,
            'Чер': 5,
            'Лип': 6,
            'Сер': 7,
            'Вер': 8,
            'Жов': 9,
            'Лис': 10,
            'Гру': 11
        };

        Ext.Date.getMonthNumber = function(name) {
            return Ext.Date.monthNumbers[name.substring(0, 1).toUpperCase() + name.substring(1, 3).toLowerCase()];
        };

        Ext.Date.dayNames = [
            "Неділя",
            "Понеділок",
            "Вівторок",
            "Середа",
            "Четвер",
            "П'ятниця",
            "Субота"];

        Ext.Date.shortDayName = ['Нд', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'];

        Ext.Date.getShortDayName = function(day) {
            return Ext.Date.dayNames[day];
        };
    }
    if(Ext.MessageBox) {
        Ext.MessageBox.buttonText = {
            ok: "OK",
            cancel: "Відміна",
            yes: "Так",
            no: "Ні"
        };
    }

    if(exists('Ext.util.Format')) {
        Ext.apply(Ext.util.Format, {
            thousandSeparator: '.',
            decimalSeparator: ',',
            currencySign: '\u0440\u0443\u0431',
            // Russian Ruble
            dateFormat: 'd.m.Y'
        });
    }

    Ext.define("Ext.locale.uk.picker.Date", {
        override: "Ext.picker.Date",
        todayText: "Сьогодні",
        minText: "Ця дата раніше мінімальної дати",
        maxText: "Ця дата пізніше максимальної дати",
        disabledDaysText: "",
        disabledDatesText: "",
        monthNames: Ext.Date.monthNames,
        dayNames: Ext.Date.dayNames,
        nextText: 'Наступний місяць (Control+Вправо)',
        prevText: 'Попередній місяць (Control+Влево)',
        monthYearText: 'Вибір місяця (Control+Вверх/Вниз для выбора года)',
        todayTip: "{0} (Пробіл)",
        format: "d.m.y",
        startDay: 1
    });

    Ext.define("Ext.locale.uk.picker.Month", {
        override: "Ext.picker.Month",
        okText: "&#160;OK&#160;",
        cancelText: "Відміна"
    });

    Ext.define("Ext.locale.uk.toolbar.Paging", {
        override: "Ext.PagingToolbar",
        beforePageText: "Сторінка",
        afterPageText: "з {0}",
        firstText: "Перша сторінка",
        prevText: "Попередня сторінка",
        nextText: "Наступна сторінка",
        lastText: "Остання сторінка",
        refreshText: "Оновити",
        displayMsg: "Відображаються записи з {0} по {1}, всього {2}",
        emptyMsg: 'Немає даних для відображення'
    });

    Ext.define("Ext.locale.uk.form.field.Text", {
        override: "Ext.form.field.Text",
        minLengthText: "Мінімальна довжина цього поля {0}",
        maxLengthText: "Максимальна довжина цього поля {0}",
        blankText: "Це поле обов'язково для заповнення",
        regexText: "",
        emptyText: null
    });

    Ext.define("Ext.locale.uk.form.field.Number", {
        override: "Ext.form.field.Number",
        minText: "Значення цього поля не може бути менше {0}",
        maxText: "Значення цього поля не може бути більше {0}",
        nanText: "{0} не є числом"
    });

    Ext.define("Ext.locale.uk.form.field.Date", {
        override: "Ext.form.field.Date",
        disabledDaysText: "Не доступно",
        disabledDatesText: "Не доступно",
        minText: "Дата в цьому полі повинна бути пізніше {0}",
        maxText: "Дата в цьому полі повинна бути менше {0}",
        invalidText: "{0} не є правильною датою - дата повинна бути вказана у форматі {1}",
        format: "d.m.y",
        altFormats: "d.m.y|d/m/Y|d-m-y|d-m-Y|d/m|d-m|dm|dmy|dmY|d|Y-m-d"
    });

    Ext.define("Ext.locale.uk.form.field.ComboBox", {
        override: "Ext.form.field.ComboBox",
        valueNotFoundText: undefined
    }, function() {
        Ext.apply(Ext.form.field.ComboBox.prototype.defaultListConfig, {
            loadingText: "Завантаження..."
        });
    });

    if(exists('Ext.form.field.VTypes')) {
        Ext.apply(Ext.form.field.VTypes, {
            emailText: 'Це поле повинно містити адресу електронної пошти у форматі "user@example.com"',
            urlText: 'Це поле повинно містити URL у форматі "http:/' + '/www.example.com"',
            alphaText: 'Це поле повинно містити виключно латинські літери та символ підкреслення "_"',
            alphanumText: 'Це поле повинно містити виключно латинські літери, цифри та символ підкреслення "_"'
        });
    }

    Ext.define("Ext.locale.uk.form.field.HtmlEditor", {
        override: "Ext.form.field.HtmlEditor",
        createLinkText: 'Будь ласка, введіть адресу:'
    }, function() {
        Ext.apply(Ext.form.field.HtmlEditor.prototype, {
            buttonTips: {
                bold: {
                    title: 'напівжирний (Ctrl+B)',
                    text: 'Застосування напівжирного до виділеного тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                italic: {
                    title: 'Курсив (Ctrl+I)',
                    text: 'Применение курсивного начертания к выделенному тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                underline: {
                    title: 'Підкреслений (Ctrl+U)',
                    text: 'Підкреслення виділеного тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                increasefontsize: {
                    title: 'Збільшити розмір',
                    text: 'Збільшення розміру шрифту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                decreasefontsize: {
                    title: 'Зменшити розмір',
                    text: 'Зменшення розміру шрифту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                backcolor: {
                    title: 'Заливка',
                    text: 'Зміна кольору фону для виділеного тексту або абзацу.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                forecolor: {
                    title: 'Колір тексту',
                    text: 'Зміна кольору тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                justifyleft: {
                    title: 'Вирівняти текст по лівому краю',
                    text: 'Вирівнювання тексту по лівому краю.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                justifycenter: {
                    title: 'По центру',
                    text: 'Вирівнювання тексту по центру.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                justifyright: {
                    title: 'Вирівняти текст по правому краю',
                    text: 'Вирівнювання тексту по правому краю.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                insertunorderedlist: {
                    title: 'Маркери',
                    text: 'Почати маркований список.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                insertorderedlist: {
                    title: 'Нумерація',
                    text: 'Почати нумернованний список.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                createlink: {
                    title: 'Втавити гіперпосилання',
                    text: 'Створення посилання із виділеного тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                sourceedit: {
                    title: 'Вихідний код',
                    text: 'Переключитися на вихідний код.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                }
            }
        });
    });

    Ext.define("Ext.locale.uk.form.Basic", {
        override: "Ext.form.Basic",
        waitTitle: "Будь ласка зачекайте..."
    });

    Ext.define("Ext.locale.uk.grid.header.Container", {
        override: "Ext.grid.header.Container",
        sortAscText: "Сортировать по возрастанию",
        sortDescText: "Сортировать по убыванию",
        lockText: "Закрепить столбец",
        unlockText: "Снять закрепление столбца",
        columnsText: "Столбцы"
    });

    Ext.define("Ext.locale.uk.grid.GroupingFeature", {
        override: "Ext.grid.GroupingFeature",
        emptyGroupText: '(Пусто)',
        groupByText: 'Группировать по этому полю',
        showGroupsText: 'Отображать по группам'
    });

    Ext.define("Ext.locale.uk.grid.PropertyColumnModel", {
        override: "Ext.grid.PropertyColumnModel",
        nameText: "Название",
        valueText: "Значение",
        dateFormat: "d.m.Y"
    });

    Ext.define("Ext.locale.uk.SplitLayoutRegion", {
        override: "Ext.SplitLayoutRegion",
        splitTip: "Тяните для изменения размера.",
        collapsibleSplitTip: "Тяните для изменения размера. Двойной щелчок спрячет панель."
    });

    if(Ext.Updater) {
        Ext.Updater.defaults.indicatorText = '<div class="loading-indicator">Идет загрузка...</div>';
    }

    if(Ext.view.View) {
        Ext.view.View.prototype.emptyText = "";
    }

    if(Ext.grid.Panel) {
        Ext.grid.Panel.prototype.ddText = "{0} вибраних рядків";
    }

    if(Ext.TabPanelItem) {
        Ext.TabPanelItem.prototype.closeText = "Закрити цю вкладку";
    }

    if(Ext.form.field.Base) {
        Ext.form.field.Base.prototype.invalidText = "Значення в цьому полі невірне";
    }

    if(Ext.LoadMask) {
        Ext.LoadMask.prototype.msg = "Завантаження...";
    }

    if(Ext.Date) {
        Ext.Date.monthNames = [
            "Січень",
            "Лютий",
            "Березень",
            "Квітень",
            "Травень",
            "Червень",
            "Липень",
            "Серпень",
            "Вересень",
            "Жовтень",
            "Листопад",
            "Грудень"
        ];

        Ext.Date.shortMonthNames = [
            "Січ",
            "Лют",
            "Бер",
            "Квт",
            "Трав",
            "Чер",
            "Лип",
            "Сер",
            "Вер",
            "Жов",
            "Лис",
            "Гру"
        ];

        Ext.Date.getShortMonthName = function(month) {
            return Ext.Date.shortMonthNames[month];
        };

        Ext.Date.monthNumbers = {
            'Січ': 0,
            'Лют': 1,
            'Бер': 2,
            'Квт': 3,
            'Трав': 4,
            'Чер': 5,
            'Лип': 6,
            'Сер': 7,
            'Вер': 8,
            'Жов': 9,
            'Лис': 10,
            'Гру': 11
        };

        Ext.Date.getMonthNumber = function(name) {
            return Ext.Date.monthNumbers[name.substring(0, 1).toUpperCase() + name.substring(1, 3).toLowerCase()];
        };

        Ext.Date.dayNames = [
            "Неділя",
            "Понеділок",
            "Вівторок",
            "Середа",
            "Сетвер",
            "П'ятниця",
            "Субота"
        ];

        Ext.Date.dayShortNames = [
            "Нд",
            "Пн",
            "Вт",
            "Ср",
            "Чт",
            "Пт",
            "Сб"
        ];

        Ext.Date.getShortDayName = function(day) {
            return Ext.Date.dayShortNames[day];
        };
    }
    if(Ext.MessageBox) {
        Ext.MessageBox.buttonText = {
            ok: "OK",
            cancel: "Відміна",
            yes: "Так",
            no: "Ні"
        };
    }

    if(Ext.util.Format) {
        Ext.apply(Ext.util.Format, {
            thousandSeparator: '.',
            decimalSeparator: ',',
            currencySign: '\u0440\u0443\u0431',  // Russian Ruble
            dateFormat: 'd.m.Y'
        });
    }

    if(Ext.picker.Date) {
        Ext.apply(Ext.picker.Date.prototype, {
            todayText: "Сьогодні",
            minText: "Ця дата раніше мінімальної дати",
            maxText: "Ця дата пізніше максимальної дати",
            disabledDaysText: "",
            disabledDatesText: "",
            monthNames: Ext.Date.monthNames,
            dayNames: Ext.Date.dayNames,
            nextText: 'Наступний місяць (Control+Вправо)',
            prevText: 'Попередній місяць (Control+Влево)',
            monthYearText: 'Вибір місяця (Control+Вверх/Вниз для выбора года)',
            todayTip: "{0} (Пробіл)",
            format: "d.m.y",
            startDay: 1
        });
    }

    if(Ext.picker.Month) {
        Ext.apply(Ext.picker.Month.prototype, {
            okText: "&#160;OK&#160;",
            cancelText: "Відміна"
        });
    }

    if(Ext.toolbar.Paging) {
        Ext.apply(Ext.PagingToolbar.prototype, {
            beforePageText: "Сторінка",
            afterPageText: "из {0}",
            firstText: "Перша сторінка",
            prevText: "Попередня сторінка",
            nextText: "Наступна сторінка",
            lastText: "Остання сторінка",
            refreshText: "Оновити",
            displayMsg: "Відображаються записи з {0} по {1}, всього {2}",
            emptyMsg: 'Немає даних для відображення'
        });
    }

    if(Ext.form.field.Text) {
        Ext.apply(Ext.form.field.Text.prototype, {
            minLengthText: "Мінімальна довжина цього поля {0}",
            maxLengthText: "Максимальна довжина цього поля {0}",
            blankText: "Це поле обов'язкове для заповнення",
            regexText: "",
            emptyText: null
        });
    }

    if(Ext.form.field.Number) {
        Ext.apply(Ext.form.field.Number.prototype, {
            minText: "Значення цього поля не може бути менше {0}",
            maxText: "Значення цього поля не може бути більше {0}",
            nanText: "{0} не є числом"
        });
    }

    if(Ext.form.field.Date) {
        Ext.apply(Ext.form.field.Date.prototype, {
            disabledDaysText: "Не доступно",
            disabledDatesText: "Не доступно",
            minText: "Дата в цьому полі повинна бути пізніше {0}",
            maxText: "Дата в цьому полі повинна бути раніше {0}",
            invalidText: "{0} не є правильною датою - дата повинна бути вказана у форматі {1}",
            format: "d.m.y",
            altFormats: "d.m.y|d/m/Y|d-m-y|d-m-Y|d/m|d-m|dm|dmy|dmY|d|Y-m-d"
        });
    }

    if(Ext.form.field.ComboBox) {
        Ext.apply(Ext.form.field.ComboBox.prototype, {
            loadingText: "Завантаження...",
            valueNotFoundText: undefined
        });
    }

    if(Ext.form.field.VTypes) {
        Ext.apply(Ext.form.field.VTypes, {
            emailText: 'Це поле повинно містити адресу електронної пошти у форматі "user@example.com"',
            urlText: 'Це поле повинно містити URL у форматі "http:/' + '/www.example.com"',
            alphaText: 'Це поле повинно містити виключно латинські літери та символ підкреслення "_"',
            alphanumText: 'Це поле повинно містити виключно латинські літери, цифри та символ підкреслення "_"'
        });
    }

    if(Ext.form.field.HtmlEditor) {
        Ext.apply(Ext.form.field.HtmlEditor.prototype, {
            createLinkText: 'Будь ласка, введіть адресу:',
            buttonTips: {
                bold: {
                    title: 'напівжирний (Ctrl+B)',
                    text: 'Застосування напівжирного до виділеного тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                italic: {
                    title: 'Курсив (Ctrl+I)',
                    text: 'Применение курсивного начертания к выделенному тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                underline: {
                    title: 'Підкреслений (Ctrl+U)',
                    text: 'Підкреслення виділеного тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                increasefontsize: {
                    title: 'Збільшити розмір',
                    text: 'Збільшення розміру шрифту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                decreasefontsize: {
                    title: 'Зменшити розмір',
                    text: 'Зменшення розміру шрифту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                backcolor: {
                    title: 'Заливка',
                    text: 'Зміна кольору фону для виділеного тексту або абзацу.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                forecolor: {
                    title: 'Колір тексту',
                    text: 'Зміна кольору тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                justifyleft: {
                    title: 'Вирівняти текст по лівому краю',
                    text: 'Вирівнювання тексту по лівому краю.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                justifycenter: {
                    title: 'По центру',
                    text: 'Вирівнювання тексту по центру.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                justifyright: {
                    title: 'Вирівняти текст по правому краю',
                    text: 'Вирівнювання тексту по правому краю.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                insertunorderedlist: {
                    title: 'Маркери',
                    text: 'Почати маркований список.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                insertorderedlist: {
                    title: 'Нумерація',
                    text: 'Почати нумернованний список.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                createlink: {
                    title: 'Втавити гіперпосилання',
                    text: 'Створення посилання із виділеного тексту.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                },
                sourceedit: {
                    title: 'Вихідний код',
                    text: 'Переключитися на вихідний код.',
                    cls: Ext.baseCSSPrefix + 'html-editor-tip'
                }
            }
        });
    }

    if(Ext.form.Basic) {
        Ext.form.Basic.prototype.waitTitle = "Будь ласка зачекайте...";
    }

    if(Ext.grid.header.Container) {
        Ext.apply(Ext.grid.header.Container.prototype, {
            sortAscText: "Сортувати по зростанню",
            sortDescText: "Сортувати за спаданням",
            lockText: "Закріпити стовпець",
            unlockText: "Зняти закріплення стовпчика",
            columnsText: "Стовпці"
        });
    }

    if(Ext.grid.GroupingFeature) {
        Ext.apply(Ext.grid.GroupingFeature.prototype, {
            emptyGroupText: '(Пусто)',
            groupByText: 'Групувати за цим полем',
            showGroupsText: 'Відображати по групах'
        });
    }

    if(Ext.grid.PropertyColumnModel) {
        Ext.apply(Ext.grid.PropertyColumnModel.prototype, {
            nameText: "Назва",
            valueText: "Значення",
            dateFormat: "d.m.Y"
        });
    }

    if(Ext.SplitLayoutRegion) {
        Ext.apply(Ext.SplitLayoutRegion.prototype, {
            splitTip: "Тягніть для зміни розміру.",
            collapsibleSplitTip: "Тягніть для зміни розміру. Подвійний клік сховає панель."
        });
    }

    if(Ext.layout.BorderLayout && Ext.layout.BorderLayout.SplitRegion) {
        Ext.apply(Ext.layout.BorderLayout.SplitRegion.prototype, {
            splitTip: "Тяните для изменения размера.",
            collapsibleSplitTip: "Тягніть для зміни розміру. Подвійний клік сховає панель."
        });
    }
});
