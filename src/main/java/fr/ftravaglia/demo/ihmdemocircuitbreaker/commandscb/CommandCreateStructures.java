/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package fr.ftravaglia.demo.ihmdemocircuitbreaker.commandscb;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import fr.ftravaglia.demo.ihmdemocircuitbreaker.cache.CachePasBeauAPasReproduire;
import fr.ftravaglia.demo.ihmdemocircuitbreaker.cache.CacheStrAPersistPasBeauAPasReproduire;
import fr.ftravaglia.demo.ihmdemocircuitbreaker.model.Structure;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ftravaglia
 */
public class CommandCreateStructures extends HystrixCommand<Structure> {
    
    private static final String URL_STR_REST = "http://localhost:9080/api/v1/structures/create/";
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandCreateStructures.class);
    private final Structure structure;
    public CommandCreateStructures(Structure structure) {
        super(HystrixCommandGroupKey.Factory.asKey("ihm-structures-group"));
        this.structure = structure;
    }
    
    @Override
    protected Structure getFallback() {
        LOGGER.info("[Create] Il semble que la ressource REST soit défaillante ... il faut compenser ! Erreur : " + getFailedExecutionException().getMessage());
        
        // Imaginer ici un mécanisme qui attend que la ressource soit dispo pour persister la data.
        // Le but d'envoyer le meme objets est de faire croire a l'utilisateur que la donnnée est persistée alors que c'est en attente.
        CacheStrAPersistPasBeauAPasReproduire.STRS.add(structure);
        CachePasBeauAPasReproduire.STRS.add(structure);
        LOGGER.info("Il y a dans le cache des structrures a persister " + CacheStrAPersistPasBeauAPasReproduire.STRS.size() + " elements");
        
        return structure;
    }
    
    @Override
    protected Structure run() {
        
        final RestTemplate restTemplate = new RestTemplate();
        final HttpEntity<Structure> request = new HttpEntity<>(structure);
        ResponseEntity<List<Structure>> exchange = restTemplate
                .exchange(URL_STR_REST, HttpMethod.POST, request, new ParameterizedTypeReference<List<Structure>>() {
                });
        
        LOGGER.info("HTTP code -> " +  exchange.getStatusCode());
        // Si c'est OK alors on peut persister ce qu'il n'a pas été persisté quand la ressource n'était pas dispo.
        if(!CacheStrAPersistPasBeauAPasReproduire.STRS.isEmpty()){
            LOGGER.info("Il y a en attente " + CacheStrAPersistPasBeauAPasReproduire.STRS.size() + " Structures a persister");
            for (Structure strWaiting : CacheStrAPersistPasBeauAPasReproduire.STRS) {
                final HttpEntity<Structure> requestWait = new HttpEntity<>(strWaiting);
                ResponseEntity<List<Structure>> exchange1 = restTemplate
                        .exchange(URL_STR_REST, HttpMethod.POST, requestWait, new ParameterizedTypeReference<List<Structure>>() {
                });
                
                LOGGER.info("HTTP Code Pour les str en attente -> " +  exchange1.getStatusCode());

            }
            
            CacheStrAPersistPasBeauAPasReproduire.STRS.clear();
            
            LOGGER.info("Dans le cache des structures a persister il y a maintenant " + CacheStrAPersistPasBeauAPasReproduire.STRS.size() + " Structures");
        }
        
        return structure;
    }
}