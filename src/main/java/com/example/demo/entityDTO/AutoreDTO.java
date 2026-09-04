package com.example.demo.entityDTO;

import java.sql.Date;
import java.util.Set;


public record AutoreDTO(

        String nomeAutore,
        String cognome,
        Date dataDiNascita,
        String nazionalita,
        String pseudonimo,
        Set<String> nomiLibri
) {

}
