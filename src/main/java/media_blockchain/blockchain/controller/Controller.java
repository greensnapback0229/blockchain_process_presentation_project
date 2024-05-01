package media_blockchain.blockchain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import media_blockchain.blockchain.dto.RegisterDto;
import media_blockchain.blockchain.service.Service;

@org.springframework.stereotype.Controller
public class Controller {

	@Autowired
	Service service;

	@GetMapping("/")
	public String mainPage(){
		return "mainView";
	}

	@GetMapping("/wallet")
	public String walletPage(){
		return "wallet";
	}
	@PostMapping("/main/register")
	public ResponseEntity mainRegister(@RequestBody RegisterDto dto) {
		ResponseEntity result = service.registerService(dto);
		System.out.println(result);
		return result;
	}

}
