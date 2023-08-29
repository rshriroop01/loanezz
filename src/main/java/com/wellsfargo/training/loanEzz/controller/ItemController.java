package com.wellsfargo.training.loanEzz.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wellsfargo.training.loanEzz.exception.ResourceNotFoundException;
import com.wellsfargo.training.loanEzz.model.InputLoan;
import com.wellsfargo.training.loanEzz.model.Item;
import com.wellsfargo.training.loanEzz.service.ItemService;

// @RequestMapping - maps HTTP request with a path to a controller 
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/api")
public class ItemController {
	
	@Autowired
    private ItemService pservice;
    
    /* ResponseEntity represents an HTTP response, including headers, body, and status.
     *  @RequestBody annotation automatically deserializes the JSON into a Java type
     *  */
    
    //Open PostMan, make a POST Request - http://localhost:8085/pms/api/products/
    //Select body -> raw -> JSON 
    //Insert JSON product object.
    @PostMapping("/products")
    public ResponseEntity<Item> saveProduct(@Validated @RequestBody Item p) {
        List<Item> il = pservice.findSimilarProduct(p);
        if(il.size() > 0) {
        	return ResponseEntity.ofNullable(null);
        }
        Item i = pservice.saveProduct(p);
        return ResponseEntity.ok(i);
    }
    
    @GetMapping("/products")
    public List<Item> listAll() {
    	List<Item> items = new ArrayList<Item>();
    	List<Item> allItems = pservice.listAll();
    	for(int i = 0; i < allItems.size(); i++) {
    		char status = allItems.get(i).getItemStatus();
    		if(status == 'Y') {
    			items.add(allItems.get(i));
    		}
    	}
        return items;
    }
    
    @GetMapping("/purchased-items/{eid}")
    public List<Item> getAllItemsForUser(@PathVariable(value = "eid") Long eId) {
    	return pservice.getAllItemsPurchased(eId);
    }
    
    /* @PathVariable is a Spring annotation which indicates that a method parameter should be
     *  bound to a URI template variable.
       @PathVariable annotation is used to read an URL template variable.
     */
    //Open PostMan, make a GET Request - http://localhost:8085/pms/api/products/4
    @GetMapping("/products/{id}")
    public ResponseEntity<Item> getProductById(@PathVariable(value = "id") Long pId) 
                                                throws ResourceNotFoundException {
        Item p = pservice.getSingleProduct(pId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        
        return ResponseEntity.ok().body(p);
        
    }
    
    @PutMapping("/products/{id}")
    public ResponseEntity<Item> updProductById(@PathVariable(value = "id") Long pId, @Validated @RequestBody Item p) 
                                                throws ResourceNotFoundException {
        Item product = pservice.getSingleProduct(pId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        
        product.setItemDescription(p.getItemDescription());
        product.setItemMake(p.getItemMake());
        product.setItemStatus(p.getItemStatus());
        product.setItemCategory(p.getItemCategory());
        product.setItemValue(p.getItemValue());
        

        return ResponseEntity.ok().body(pservice.saveProduct(product));
        
    }
    
    @DeleteMapping("/products/{id}")
    public Map<String, Boolean> deleteProduct(@PathVariable(value = "id") Long pId) 
                                                throws ResourceNotFoundException {
        Item p = pservice.getSingleProduct(pId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
        
        pservice.deleteProduct(pId);
        Map<String, Boolean> response = new HashMap<>();
        
        
        response.put("Deleted", Boolean.TRUE);
        
        
        return response;
        
    }
    
//    @PostMapping("/test")
//    public ResponseEntity<String> handleTest(@Validated @RequestBody InputLoan loan) {
//    	System.out.println(new String(loan.getItemCategory()));
//    	return ResponseEntity.ok("Hello");
//    }
    
    

}
