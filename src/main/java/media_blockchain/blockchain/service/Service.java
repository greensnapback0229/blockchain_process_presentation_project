package media_blockchain.blockchain.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.socket.WebSocketSession;

import media_blockchain.blockchain.Blockchain.Block;
import media_blockchain.blockchain.dto.RegisterDto;
import media_blockchain.blockchain.dto.WalletDto;

@org.springframework.stereotype.Service
@Primary
public class Service {

	public ArrayList<Block> blockchain = new ArrayList<Block>();

	private Map<WebSocketSession, WalletDto> walletList = new HashMap<>();

	public int difficulty = 5;

	public void addUser(WalletDto newWallet, WebSocketSession newSession){
		walletList.put(newSession,newWallet);
	}

	public void deleteUser(WebSocketSession session){
		walletList.remove(session);
	}

	public String sendMoney(WebSocketSession currentSession, HashMap<WebSocketSession, WalletDto> sessions){
		String result = "";
		return result;
	}


	public Boolean isChainValid() {
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
	}

	public void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}

	public ResponseEntity registerService(RegisterDto dto){
		for(WalletDto wallet : walletList.values()){
			if(wallet.getName().equals(dto.getWalletName())){
				return new ResponseEntity(HttpStatus.BAD_REQUEST);
			}
		}
		System.out.println(dto.getWalletName());
		return new ResponseEntity<>(new RegisterDto(dto.getWalletName()), HttpStatus.OK);
	}
}
