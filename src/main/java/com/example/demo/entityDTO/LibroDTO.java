package com.example.demo.entityDTO;

import java.util.Set;

public record LibroDTO(

        String titolo,
        int annoPubblicazione,
        Set<String> nomiAutori

) {
    public String nomiAutoriString() {
        return String.join(", ", nomiAutori); //.join: unisce più stringhe in una sola stringa,
        //separate da un delimitatore, virgola in questo cado
    }
}
