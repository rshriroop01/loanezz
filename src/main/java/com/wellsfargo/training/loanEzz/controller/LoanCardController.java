package com.wellsfargo.training.loanEzz.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wellsfargo.training.loanEzz.model.Employee;
import com.wellsfargo.training.loanEzz.model.EmployeeCardDetails;
import com.wellsfargo.training.loanEzz.model.EmployeeIssueDetails;
import com.wellsfargo.training.loanEzz.model.InputLoan;
import com.wellsfargo.training.loanEzz.model.Item;
import com.wellsfargo.training.loanEzz.model.LoanCard;
import com.wellsfargo.training.loanEzz.service.EmployeeCardService;
import com.wellsfargo.training.loanEzz.service.EmployeeIssueService;
import com.wellsfargo.training.loanEzz.service.EmployeeService;
import com.wellsfargo.training.loanEzz.service.LoanCardService;
import com.wellsfargo.training.loanEzz.service.ItemService;
import com.wellsfargo.training.loanEzz.exception.ResourceNotFoundException;

@RestController
@RequestMapping(value = "/api")
public class LoanCardController {
	
	@Autowired
	private LoanCardService ls;
	
	@Autowired
	private EmployeeService es;
	
	@Autowired
	private EmployeeCardService ecs;
	
	@Autowired
	private EmployeeIssueService eis;
	
	@Autowired
	private ItemService ps;
	
	@PostMapping("/apply-loan")
	public EmployeeIssueDetails applyLoan(@Validated @RequestBody InputLoan inputLoan) throws ResourceNotFoundException
    {
            
		LoanCard lc = ls.getLoanCardByType(inputLoan.getItemCategory()).orElseThrow(() ->
        new ResourceNotFoundException("Loan Card not found :: "));
		
		Employee e = es.findEmployeeById(inputLoan.getEmployeeId()).orElseThrow(() ->
        new ResourceNotFoundException("Employee not found :: "));
        
        EmployeeCardDetails c = ecs.saveLoanCard(inputLoan, lc, e);       
        Item i = ps.getSingleProduct(inputLoan.getItemId()).orElseThrow(() ->
        new ResourceNotFoundException("Item not found :: "));
        EmployeeIssueDetails ei = eis.saveLoanCard(e, i, lc);
        return ei;
    }
	
	@GetMapping("/loans")
	public List<LoanCard> getLoans() {
		return ls.getAllLoans();
	}
	
	@GetMapping("/loans/{eid}")
	public List<LoanCard> getLoansForEmployee(@PathVariable(value = "eid") Long eId) {
		return ls.getAllLoanCardsForEmployee(eId);
	}
	
	@PostMapping("/loancard")
	public LoanCard addLoanCard(@Validated @RequestBody LoanCard c) {
		return ls.saveLoanCard(c);
	}
	
	@PutMapping("/loancard/{cid}")
	public LoanCard editLoanCard(@PathVariable(value = "cid") Long cId, @Validated @RequestBody LoanCard lc) throws ResourceNotFoundException {
		LoanCard c = ls.getLoanCardById(cId).orElseThrow(() ->
        new ResourceNotFoundException("Loan Card not found :: "));
		c.setDurationInYears(lc.getDurationInYears());
		c.setLoanType(lc.getLoanType());
		return ls.saveLoanCard(c);
	}
	
	@DeleteMapping("/loancard/{cid}")
	public Map<String, Boolean> deleteLoanCard(@PathVariable(value="cid") Long cId) throws ResourceNotFoundException {
		LoanCard c = ls.getLoanCardById(cId).orElseThrow(() ->
        new ResourceNotFoundException("Loan Card not found :: "));
		ls.deleteLoanCard(c);
		Map<String, Boolean> response = new HashMap<>();
        response.put("Deleted", Boolean.TRUE);
        return response;
	}
}
