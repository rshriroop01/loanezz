package com.wellsfargo.training.loanEzz.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wellsfargo.training.loanEzz.model.Item;
import com.wellsfargo.training.loanEzz.repository.ItemRepository;

import jakarta.transaction.Transactional;

/*
 * A service layer is an additional layer in an MVC application that 
 * mediates communication between a controller and repository layer. 
 * The service layer contains business logic. 
 * In particular, it contains validation logic. */

/* @Service annotation allows developers to add business functionalities.
 *  @Transactional annotation allows to manage Database transactions efficiently */

@Service
@Transactional
public class ItemService {
	
	 @Autowired // dependency injection
	    private ItemRepository prepo;
	    
	    public Item saveProduct(Item p) {
	        return prepo.save(p);
	    }
	    
	    public List<Item> listAll() {
	        return prepo.findAll();
	    }
	    
	    public Optional<Item> getSingleProduct(long id) {
	        return prepo.findById(id);
	    }
	    
	    public List<Item> findSimilarProduct(Item i) {
	    	return prepo.findSimilarItem(i.getItemCategory(), i.getItemMake(), i.getItemDescription());
	    }
	    
	    public void updateProductAvailability(long id) {
	    	Optional<Item> i = prepo.findById(id);
	    	if(i.isEmpty()) {
	    		return;
	    	}
	    	Item item = i.get();
	    	item.setItemStatus('N');
	    	prepo.save(item);
	    }
	    
	    public void deleteProduct(long id) {
	        prepo.deleteById(id);
	    }
	    
	    public List<Item> getAllItemsPurchased(Long eId) {
	    	return prepo.findAllItemsForEmployee(eId);
	    }

}
