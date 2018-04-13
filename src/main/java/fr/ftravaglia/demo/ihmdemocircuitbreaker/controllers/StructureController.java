/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package fr.ftravaglia.demo.ihmdemocircuitbreaker.controllers;

import fr.ftravaglia.demo.ihmdemocircuitbreaker.commandscb.CommandCreateStructures;
import fr.ftravaglia.demo.ihmdemocircuitbreaker.commandscb.CommandListStructures;
import fr.ftravaglia.demo.ihmdemocircuitbreaker.model.Structure;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author ftravaglia
 */
@Controller
public class StructureController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StructureController.class);
    
    
    @GetMapping("/structures")
    public List<Structure> structuresForm(Model model) {
        
        // ETAPE 2
        loadForm(model);

        return new ArrayList<>();
    }
    
    @PostMapping("/structures")
    public List<Structure> structuresSubmit(@ModelAttribute Structure structure, Model model) {
        LOGGER.info("Demande de création de la structure " + structure.toString());
        
        
        // ETAPE 2
        final CommandCreateStructures cmdCreateStr = new CommandCreateStructures(structure);
        cmdCreateStr.execute();
        //////////////////////////////////
        
        
        loadForm(model);

        return new ArrayList<>();
    }
    
    // Methode commune pour le chargement du formulaire html
    private void loadForm(Model model){
        
        /*
        // ETAPE 1
        final RestTemplate restTemplate = new RestTemplate();
        
        final ResponseEntity<List<Structure>> rateResponse =
                restTemplate.exchange("http://localhost:9080/api/v1/structures/",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Structure>>() {
                        });
        final List<Structure> strList = rateResponse.getBody();
        
        */
        
        // ETAPE 2
        final CommandListStructures commandStr = new CommandListStructures();
        final List<Structure> strList = commandStr.execute();
        
        model.addAttribute("strs", strList);
        model.addAttribute("structure", new Structure());
        ////////////////////////////////////////////////////////////////////////
    }
}
