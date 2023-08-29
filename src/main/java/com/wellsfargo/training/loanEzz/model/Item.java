package com.wellsfargo.training.loanEzz.model;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long pid;
    
    private String itemDescription;
    private char itemStatus;
    private int itemValue;
    private String itemMake;
    private String itemCategory;
    
//    @OneToMany(cascade=CascadeType.ALL)
//	private List<EmployeeIssueDetails> employeeIssueDetails;
    
    
    
}
