package fr.ftravaglia.demo.ihmdemocircuitbreaker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author ftravaglia
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Structure {
    
    private Long id;
    private String name;
    private String code;
    private String description;
    
    public Structure() {}
    
    public Structure(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
    }
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Structure{" + "id=" + id + ", name=" + name + ", code=" + code + ", description=" + description + '}';
    }
    
    
}
