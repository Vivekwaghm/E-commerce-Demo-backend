package com.ecommerce.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInformation {

	@Column(name="cardholder_name")
	private String cardHolderName;
	
	@Column(name="card_number")
	private String cardNumber;
	
	@Column(name="expiration_date")
	private LocalDate expirationDate;
	
	private String cvv;
}
