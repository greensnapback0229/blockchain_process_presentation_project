package media_blockchain.blockchain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import media_blockchain.blockchain.Blockchain.Block;
import media_blockchain.blockchain.dto.MiningReq;
import media_blockchain.blockchain.dto.MiningRes;
import media_blockchain.blockchain.dto.MiningResMessage;
import media_blockchain.blockchain.dto.NotMinedRes;
import media_blockchain.blockchain.dto.RegisterDto;
import media_blockchain.blockchain.dto.AddWalletMessage;
import media_blockchain.blockchain.dto.SendMoneyMessage;
import media_blockchain.blockchain.dto.WalletDto;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Primary
public class Service {
	private final SimpMessageSendingOperations sendingOperations;

	public ArrayList<Block> blockchain = new ArrayList<Block>();
	private Map<String, WalletDto> wallets = new HashMap<>(100);
	private Map<String,String> notMined = new HashMap<>(100);

	private List<String> ledger = new ArrayList<>();
	public int difficulty = 5;

	@PostConstruct
	public void init(){
		addBlock(new Block("initial block", "0"));
	}

	public void sendMoney(SendMoneyMessage message){
		String data =
			"[송금] " + message.getSender() +
			" -> " + message.getAmount() + "₩" +
			" -> " + message.getReceiver();
		String notMinedCode = UUID.randomUUID().toString();
		notMined.put(notMinedCode,data);
		System.out.println("Request-송금: " + data + "/code : " + notMinedCode);
	}

	public ResponseEntity getNotMined(){
		NotMinedRes notMindedRes = new NotMinedRes();
		notMindedRes.setNotMindedData(new ArrayList<String>());
		notMindedRes.setNotMindedCode(new ArrayList<String>());
		for(Map.Entry<String, String> notMined : notMined.entrySet()){
			notMindedRes.getNotMindedCode().add(notMined.getKey());
			notMindedRes.getNotMindedData().add(notMined.getValue());
		}
		return new ResponseEntity(notMindedRes, HttpStatus.OK);
	}

	public ResponseEntity miningBlock(MiningReq req){
		MiningRes res;
		System.out.println("[mining-request] - " + req.getNotMinedCode());
		for(Map.Entry<String, String> notMined : notMined.entrySet()){
			System.out.println("key= " + notMined.getKey() + " value= " + notMined.getValue());
		}
		if(!notMined.containsKey(req.getNotMinedCode())){
			res = new MiningRes("이미 채굴되었습니다");
			return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
		}
		else{
			String[] extract = extractData(notMined.get(req.getNotMinedCode()));
			String senderName = extract[0];
			int amount = Integer.parseInt(extract[1]);
			String receiverName = extract[2];
			modifyAssert(req.getMinerName(), 500);
			modifyAssert(senderName, 0-amount);
			modifyAssert(receiverName, 500);

			//채굴 과정 추가
			String miningMessage = "[채굴] " + req.getMinerName() + " + 500mc";
			ledger.add(miningMessage);
			ledger.add(notMined.get(req.getNotMinedCode()));

			notMined.remove(req.getNotMinedCode());

			MiningResMessage message = MiningResMessage.builder()
				.newBlockMessage(notMined.get(req.getNotMinedCode()))
				.miningMessage(miningMessage)
				.senderName(senderName)
				.senderAssert(wallets.get(senderName).getAssert())
				.receiverName(receiverName)
				.receiverAssert(wallets.get(receiverName).getAssert())
				.minerName(req.getMinerName())
				.minerAssert(wallets.get(req.getMinerName()).getAssert())
				.ledger(ledger)
				.build();

			System.out.println(miningMessage + message);
			sendingOperations.convertAndSend("/topic",message);
			res = new MiningRes("채굴완료!");
			return new ResponseEntity<>(res,HttpStatus.OK);
		}
	}

	public void addWallet(AddWalletMessage message){
		WalletDto walletDto = new WalletDto(message.getName(), 5000);
		wallets.put(message.getName(), walletDto);


	}

	public ResponseEntity registerService(RegisterDto dto){
		if(dto.getWalletName().length() > 10) return new ResponseEntity(HttpStatus.BAD_REQUEST);
		for(WalletDto wallet : wallets.values()){
			System.out.println("current: "  + wallet.getName());
			if(wallet.getName().equals(dto.getWalletName())){
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
		}
		WalletDto walletDto = new WalletDto(dto.getWalletName(), 5000);
		wallets.put(dto.getWalletName(), walletDto);
		System.out.println("[wallet register req]"+ dto.getWalletName());
		return new ResponseEntity<>(new RegisterDto(dto.getWalletName()), HttpStatus.OK);
	}


	/*public Boolean isChainValid() {
		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');

		//loop through blockchain to check hashes:
		for(int i=1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
				System.out.println("Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("This block hasn't been mined");
				return false;
			}

		}
		return true;
	}*/

	public void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}


	private String[] extractData(String transactionString) {
		String pattern = "\\[송금\\] (.+) -> (\\d+)₩ -> (.+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(transactionString);
		if (m.find()) {
			String sender = m.group(1);
			String amount = m.group(2);
			String receiver = m.group(3);
			return new String[]{sender, amount, receiver};
		} else {
			return null;
		}
	}


	private void modifyAssert(String name, int amount){
		wallets.get(name).setAssert(
			wallets.get(name).getAssert() + amount
		);
	}
}
