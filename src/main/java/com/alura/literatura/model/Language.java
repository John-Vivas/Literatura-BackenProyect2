package com.alura.literatura.model;

public enum Language {
    ES("es"),
    EN("en"),
    FR("fr"),
    PT("pt");
    private final String idioma;
    Language(String idioma) {
        this.idioma = idioma;
    }

    public static Language fromString(String lenguaje){
        for (Language language : Language.values()){
            if(language.idioma.equalsIgnoreCase(lenguaje)){
                return language;
            }
        }
        throw new IllegalArgumentException("Ningun lenguaje encontrado: " + lenguaje);
    }

    public String getIdioma(){
        return this.idioma;
    }

}
