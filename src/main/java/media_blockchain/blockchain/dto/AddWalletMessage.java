package media_blockchain.blockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddWalletMessage {
	String flag;
	String name;
}
