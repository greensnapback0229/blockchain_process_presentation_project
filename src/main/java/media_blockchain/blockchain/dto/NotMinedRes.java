package media_blockchain.blockchain.dto;

import java.util.List;

import lombok.Data;

@Data
public class NotMinedRes {
	List<String> notMindedCode;
	List<String> notMindedData;
}
