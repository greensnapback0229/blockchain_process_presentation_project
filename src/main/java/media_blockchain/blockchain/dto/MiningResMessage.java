package media_blockchain.blockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MiningResMessage {
	String miningMessage;
	String newBlockMessage;
	String senderName;
	String receiverName;
	String minerName;
	int senderAssert;
	int receiverAssert;
	int minerAssert;

}
