package media_blockchain.blockchain.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import media_blockchain.blockchain.dto.AddWalletMessage;
import media_blockchain.blockchain.dto.MiningReq;
import media_blockchain.blockchain.dto.RegisterDto;
import media_blockchain.blockchain.dto.SendMoneyMessage;
import media_blockchain.blockchain.service.Service;
import util.BaseResponse;

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

	@GetMapping("/wallet/{walletName}") String wallet(@PathVariable String walletName, Model model){
		model.addAttribute("walletName", walletName);
		return "wallet";
	}

	@PostMapping("/main/register")
	public ResponseEntity mainRegister(@RequestBody RegisterDto dto, Model model) {
		ResponseEntity result = service.registerService(dto);
		return result;
	}

	@GetMapping("/wallet/not-mined")
	public ResponseEntity getNotMinted(){
		ResponseEntity result = service.getNotMined();
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
