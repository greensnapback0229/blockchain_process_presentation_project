package media_blockchain.blockchain.dto;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class SendMoneyMessage {
	Integer amount;
	String sender;
	String receiver;
}
