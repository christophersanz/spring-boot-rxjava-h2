package com.bolsadeideas.springboot.rxjava.app.controllers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.rxjava.app.models.dao.IRateDao;
import com.bolsadeideas.springboot.rxjava.app.models.documents.ConvertionRateRq;
import com.bolsadeideas.springboot.rxjava.app.models.documents.ConvertionRateRs;
import com.bolsadeideas.springboot.rxjava.app.models.documents.Rate;
import com.bolsadeideas.springboot.rxjava.app.util.Constants;
import com.bolsadeideas.springboot.rxjava.app.util.ResponseCloudApi;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@RestController
@RequestMapping("/api/tasas")
public class RateController {
	
	private static final Logger log = LoggerFactory.getLogger(RateController.class);
	
	private static float tasa_dolar = 0;
	private static float tasa_euro = 0;
	
	@Autowired
	private IRateDao rateDao;
	
	@GetMapping
	public Flowable<Rate> index(){
		List<Rate> ratesx = rateDao.findAll();
		
		Flowable<Rate> rates = Flowable.defer(() -> Flowable.fromIterable(rateDao.findAll()).map(tasa -> {
			tasa.setDescription(StringUtils.capitalize(tasa.getDescription()));
			return tasa;
		})
//		.repeat(9)
//		.zipWith(Flowable.interval(1, TimeUnit.SECONDS), (item, interval) -> item) //tiempo que espera en lanzar el lote de registros por segundo -> 11s
		);
		
		rates.subscribe(prod -> log.info(prod.getDescription()));
		
		return rates;
	}
	
	@GetMapping("/{id}")
	public Maybe<Optional<Rate>> show(@PathVariable String id){
		return Maybe.just(rateDao.findById(id));
	}
	
	@PostMapping
	public void save(@RequestBody Rate rate){
		Maybe.just(rate)
        .map(tasa -> {
        	tasa.setDescription(rate.getDescription().toUpperCase());
			tasa.setCreateAt(new Date());
			return rateDao.save(tasa);
        }).subscribe(product -> log.info("Insert: " + product.getId() + " " + product.getDescription()));
	}
	
	@PutMapping("/{id}")
	public Rate edit(@RequestBody Rate newRate, @PathVariable String id){
		return rateDao.findById(id).map(rate -> {
			rate.setCreateAt(new Date());
			rate.setRate(newRate.getRate());
			return rateDao.save(rate);
		}).orElseGet(() -> {
			newRate.setId(id);
			newRate.setCreateAt(new Date());
			return rateDao.save(newRate);
		});
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id){
		rateDao.deleteById(id);
	}
	
	@PostMapping("/convertir")
	public ResponseCloudApi<Object> converter(@Valid @RequestBody ConvertionRateRq ConvertionRateRq, BindingResult result){
		ResponseCloudApi<Object> responseApi = new ResponseCloudApi<Object>();
		if(result.hasErrors()) {
			responseApi.setCode(Constants.GENERAL_ERROR_CODE);
			responseApi.setMessage(HttpStatus.BAD_REQUEST.toString());
			List<String> errors = new ArrayList<>();
			for (FieldError error : result.getFieldErrors()) {
			    errors.add(error.getDefaultMessage());
		    }
			responseApi.setBody(errors);
			return responseApi;
		}
		
		ConvertionRateRs convertionRateRs = new ConvertionRateRs();
		float monto = Float.parseFloat(ConvertionRateRq.getAmount());
		float montoConvertido = 0;
		String monedaOrigenDestino = ConvertionRateRq.getCurrencyFrom().concat(ConvertionRateRq.getCurrencyTo()).toUpperCase();
		
		getAllRatesValues();
		switch (monedaOrigenDestino) {
		case "DS":
			montoConvertido = monto*tasa_dolar;
			convertionRateRs.setConvertedAmount(format(montoConvertido));
			convertionRateRs.setExchangeRate(format(tasa_dolar));
			System.out.println(ConvertionRateRq.getAmount() + " dolares equivalen a " + format(montoConvertido) + " soles" + " [tipoDeCambio="+tasa_dolar+"]");
			break;
		
		case "SD":
			montoConvertido = monto*(1/tasa_dolar);
			convertionRateRs.setConvertedAmount(format(montoConvertido));
			convertionRateRs.setExchangeRate(format(1/tasa_dolar));
			System.out.println(ConvertionRateRq.getAmount() + " sol equivalen a " + format(montoConvertido) + " dolares" + " [tipoDeCambio="+(1/tasa_dolar)+"]");
			break;
			
		case "ES":
			montoConvertido = monto*tasa_euro;
			convertionRateRs.setConvertedAmount(format(montoConvertido));
			convertionRateRs.setExchangeRate(format(tasa_euro));
			System.out.println(ConvertionRateRq.getAmount() + " euro equivalen a " + format(montoConvertido) + " soles" + " [tipoDeCambio="+tasa_euro+"]");
			break;
		
		case "SE":
			montoConvertido = monto*(1/tasa_euro);
			convertionRateRs.setConvertedAmount(format(montoConvertido));
			convertionRateRs.setExchangeRate(format(1/tasa_euro));
			System.out.println(ConvertionRateRq.getAmount() + " sol equivalen a " + format(montoConvertido) + " euro" + " [tipoDeCambio="+(1/tasa_euro)+"]");
			break;
			
		case "DE":
			montoConvertido = monto*tasa_dolar; //DS
			montoConvertido = montoConvertido*(1/tasa_euro); //SE
			convertionRateRs.setConvertedAmount(format(montoConvertido));
			convertionRateRs.setExchangeRate(format(tasa_dolar*(1/tasa_euro)));
			System.out.println(ConvertionRateRq.getAmount() + " dolares equivalen a " + format(montoConvertido) + " euro" + " [tipoDeCambio="+tasa_dolar*(1/tasa_euro)+"]");
			break;
		
		case "ED":
			montoConvertido = monto*(tasa_euro); //ES
			montoConvertido = montoConvertido*(1/tasa_dolar); //SD
			convertionRateRs.setConvertedAmount(format(montoConvertido));
			convertionRateRs.setExchangeRate(format(tasa_euro*(1/tasa_dolar)));
			System.out.println(ConvertionRateRq.getAmount() + " euro equivalen a " + format(montoConvertido) + " dolares" + " [tipoDeCambio="+tasa_euro*(1/tasa_dolar)+"]");
			break;
		
		default:
			convertionRateRs.setConvertedAmount(format(0));
			convertionRateRs.setExchangeRate(format(0));
			break;	
		}
		
		convertionRateRs.setAmount(format(monto));
		convertionRateRs.setCurrencyFrom(ConvertionRateRq.getCurrencyFrom().toUpperCase());
		convertionRateRs.setCurrencyTo(ConvertionRateRq.getCurrencyTo().toUpperCase());
		
		//responseAccountApi.setCode(Constants.GENERAL_SUCCESS_CODE);
		//responseAccountApi.setMessage(HttpStatus.OK.toString());
		responseApi.setBody(convertionRateRs);
		
		return responseApi;
	}
	
	public void getAllRatesValues() {
		for (Rate rates : rateDao.findAll()) {
			if(rates.getDescription().equalsIgnoreCase("Dolar")) {
				tasa_dolar = rates.getRate();
			} else if(rates.getDescription().equalsIgnoreCase("Euro")) {
				tasa_euro = rates.getRate();
			}
		}
	}
	
	public static String format(float f) {
		DecimalFormat formato = new DecimalFormat("###,##0.00");
		return formato.format(f);
	}
	
	
	/*
	public static void main(String[] args) {
		float tasa_dolar = 3.64F;     //1 Dólar estadounidense equivale a 3.64 sol peruano
		float tasa_euro = 4.41F;      //1 Euro equivale a 4.41 sol peruano
		float monto = 59.00F;
		float montoConvertido = 0;
		String monedaOrigenDestino = "ED"; //DS-SD-ES-SE...DE-ED
		
		if("DS".equals(monedaOrigenDestino)) {
			montoConvertido = monto*tasa_dolar;
			System.out.println(monto + " dolares equivalen a " + format(montoConvertido) + " soles" + " [tipoDeCambio="+tasa_dolar+"]");
		} 
		
		if("SD".equals(monedaOrigenDestino)) {
			montoConvertido = monto*(1/tasa_dolar);
			System.out.println(monto + " sol equivalen a " + format(montoConvertido) + " dolares" + " [tipoDeCambio="+(1/tasa_dolar)+"]");
		}
		
		if("ES".equals(monedaOrigenDestino)) {
			montoConvertido = monto*tasa_euro;
			System.out.println(monto + " euro equivalen a " + format(montoConvertido) + " soles" + " [tipoDeCambio="+tasa_euro+"]");
		}
		
		if("SE".equals(monedaOrigenDestino)) {
			montoConvertido = monto*(1/tasa_euro);
			System.out.println(monto + " sol equivalen a " + format(montoConvertido) + " euro" + " [tipoDeCambio="+(1/tasa_euro)+"]");
		}
		
		if("DE".equals(monedaOrigenDestino)) {
			montoConvertido = monto*tasa_dolar; //DS
			montoConvertido = montoConvertido*(1/tasa_euro); //SE
			System.out.println(monto + " dolares equivalen a " + format(montoConvertido) + " euro" + " [tipoDeCambio="+tasa_dolar*(1/tasa_euro)+"]");
		}
		
		if("ED".equals(monedaOrigenDestino)) {
			montoConvertido = monto*(tasa_euro); //ES
			montoConvertido = montoConvertido*(1/tasa_dolar); //SD
			System.out.println(monto + " euro equivalen a " + format(montoConvertido) + " dolares" + " [tipoDeCambio="+tasa_euro*(1/tasa_dolar)+"]");
		}
		
	} 
	*/

}
