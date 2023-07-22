
package com.shubham.eduanalyser.model;

public class word {
    private String mCategryTranslation;
    private String mNameTranslation;


    public word(String CategryTranslation, String NameTranslation) {
        mCategryTranslation = CategryTranslation;
        mNameTranslation = NameTranslation;
    }


    public String getCategryTranslation() {
        return mCategryTranslation;
    }

    public String getNameTranslation() {
        return mNameTranslation;
    }


}
