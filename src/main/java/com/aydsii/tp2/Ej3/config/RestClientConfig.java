package com.aydsii.tp2.Ej3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;


//Indica que es de tipo configuracion
@Configuration
public class RestClientConfig {
    //crea una unica instancia de ese objeto al arrancar la aplicacion, lo guarda y se lo entrega automaticamente 
    //a cualquier clase que lo pida (Inyección de Dependencias)
    @Bean
    public RestClient frankfurterRestClient() {
        //Es la herramienta que utilizo para comunicarse con el exterior a través de internet
        //En este caso me buildea mi API, es decir trae la API, la construye para poder utilizarla
        return RestClient.builder()
                .baseUrl("https://api.frankfurter.dev/v2")
                .build();
    }
}