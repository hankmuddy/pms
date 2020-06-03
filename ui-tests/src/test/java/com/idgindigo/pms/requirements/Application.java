package com.idgindigo.pms.requirements;

import net.thucydides.core.annotations.Feature;

public class Application {
    @Feature
    public class Login {
    }

    @Feature
    public class Search {
        public class SearchByKeyword {
        }

        public class SearchByMultipleKeywords {
        }
    }
}