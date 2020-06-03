Ext.define("Pms.modules.person.model.Person", {
    extend: "Ext.data.Model",
    fields: [
        {
            name: 'id',
            type: 'int',
            useNull: true
        },
        'firstName',
        'lastName',
        'patronymic',
        Pms.stringFieldUseNull('country'),
        Pms.stringFieldUseNull('language'),
        Pms.stringFieldUseNull('email'),
        'city',
        'address',
        'phone',
        {
            name: 'discount',
            type: 'int'
        },
        {
            name: 'active',
            persist: false,
            type: 'boolean'
        },
        {
            name: 'dob',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        'passportNumber',
        'passportIssued',
        {
            name: 'passportValidTill',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        'cio',
        {
            name: 'entryType',
            serialize: function (value) {
                if (value == "")
                    return null
            }
        },
        {
            name: 'entryValidFrom',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        {
            name: 'entryValidTill',
            type: 'UTCDate',
            serialize: Pms.serializeUTC
        },
        'entryNumber',
        {
            name: 'visaType',
            serialize: function (value) {
                if (value == "")
                    return null;
            }
        },
        'carBrand',
        {
            name: 'postIndex', serialize: function (value) {
            if (value == "") {
                return null;
            }
        }
        },
        'carNumber',
        {
            name: 'listMembership', serialize: function (value) {
            if (value == "") {
                return null;
            }
        }
        },
        'membershipReason',
        {
            name: 'type',
            defaultValue: 'adult'
        },
        {name: 'identity', type: 'auto'},
        {name: 'person', type: 'auto', persist: false},
        {name: 'roomUse', type: 'auto', persist: false}
    ],
    associations: [
        {
            type: 'belongsTo',
            model: 'Pms.modules.group.model.Group',
            name: 'groups',
            associationKey: 'groups',
            primaryKey: 'id',
            foreignKey: 'persons'
        },
        {
            type: 'hasMany',
            model: 'Pms.modules.bill.model.Bill',
            name: 'bills',
            associationKey: 'bills',
            primaryKey: 'id',
            foreignKey: 'payer_person'
        }
    ]
});

