// StudentDetailsDTO.java
package com.application.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class StudentDetailsDTO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String studentName;
    private String surname;
    private String fathername;
    private String mothername;
    private String concessionReason; 
    private String gender;
    private float applicationFee;
    private float confirmationAmount; 
    private List<Float> concessionAmounts;
}