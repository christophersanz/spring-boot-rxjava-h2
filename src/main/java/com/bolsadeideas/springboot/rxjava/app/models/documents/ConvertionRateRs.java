package com.bolsadeideas.springboot.rxjava.app.models.documents;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConvertionRateRs {
	
	private String amount;
	private String exchangeRate;
	private String currencyFrom;
	private String currencyTo;
	private String convertedAmount;

}
