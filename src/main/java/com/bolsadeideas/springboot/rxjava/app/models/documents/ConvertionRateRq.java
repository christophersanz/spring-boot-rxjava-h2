package com.bolsadeideas.springboot.rxjava.app.models.documents;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConvertionRateRq {
	
	@NotBlank(message = "Amount is mandatory")
	private String amount;
	@NotBlank(message = "CurrencyFrom is mandatory")
	private String currencyFrom;
	@NotBlank(message = "CurrencyTo is mandatory")
	private String currencyTo;

}
