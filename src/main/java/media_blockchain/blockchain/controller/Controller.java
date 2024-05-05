package media_blockchain.blockchain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;
import media_blockchain.blockchain.dto.AddWalletMessage;
import media_blockchain.blockchain.dto.MiningReq;
import media_blockchain.blockchain.dto.RegisterDto;
import media_blockchain.blockchain.dto.SendMoneyMessage;
import media_blockchain.blockchain.service.Service;

@org.springframework.stereotype.Controller
@RequiredArgsConstructor
public class Controller {
	private final SimpMessageSendingOperations sendingOperations;

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

	@PostMapping("/mining-block")
	public ResponseEntity miningBlock(@RequestBody MiningReq req){
		return service.miningBlock(req);
	}

	@MessageMapping("/send-money")
	public void sendMoney(SendMoneyMessage message){
		service.sendMoney(message);
	}

	@MessageMapping("/add-wallet")
	public void addWallet(AddWalletMessage message){
		service.addWallet(message);
	}


}
