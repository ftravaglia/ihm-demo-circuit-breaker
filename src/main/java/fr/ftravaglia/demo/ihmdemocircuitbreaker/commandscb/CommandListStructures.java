/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package fr.ftravaglia.demo.ihmdemocircuitbreaker.commandscb;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import fr.ftravaglia.demo.ihmdemocircuitbreaker.cache.CachePasBeauAPasReproduire;
import fr.ftravaglia.demo.ihmdemocircuitbreaker.model.Structure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ftravaglia
 */
public class CommandListStructures extends HystrixCommand<List<Structure>> {
    
    private static final String URL_STR_REST = "http://localhost:9080/api/v1/structures/";
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandListStructures.class);
    
    public CommandListStructures() {
        super(HystrixCommandGroupKey.Factory.asKey("ihm-structures-group"));
    }
    
    @Override
    protected List<Structure> getFallback() {
        LOGGER.info("[List] Il semble que la ressource REST soit défaillante ... il faut compenser ! ");
        
        List<Structure> result;
        if (CachePasBeauAPasReproduire.STRS.isEmpty()) {
            LOGGER.info("Il y a rien dans le cache ... Bon on renvoi du mock alors !");
            
            final Structure structure1 = new Structure("Str Mocked 1", "STRM1", "DESC 1");
            final Structure structure2 = new Structure("Str Mocked 2", "STRM2", "DESC 2");
            final Structure structure3 = new Structure("Str Mocked 3", "STRM3", "DESC 3");
            final Structure structure4 = new Structure("Str Mocked 4", "STRM4", "DESC 4");
            final Structure structure5 = new Structure("Str Mocked 5", "STRM5", "DESC 5");
            
            result = Arrays.asList(structure1, structure2, structure3, structure4, structure5);
        }else{
            
            LOGGER.info("Cool il y a des data dans le cache ("+CachePasBeauAPasReproduire.STRS.size()+"), on revoi ca !");
            result = new ArrayList<>(CachePasBeauAPasReproduire.STRS);
        }
        return result;
    }
    
    @Override
    protected List<Structure> run() {        
        
        final RestTemplate restTemplate = new RestTemplate();
        
        final ResponseEntity<List<Structure>> rateResponse =
                restTemplate.exchange(URL_STR_REST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Structure>>() {
                        });
        final List<Structure> restResult = rateResponse.getBody();
        
        
        LOGGER.info("Renvoi des infos sur les structures venant de la ressource REST.");
        CachePasBeauAPasReproduire.STRS.clear();
        CachePasBeauAPasReproduire.STRS.addAll(restResult);
        LOGGER.info("Cache size -> " + CachePasBeauAPasReproduire.STRS.size());
        return restResult;
    }
}