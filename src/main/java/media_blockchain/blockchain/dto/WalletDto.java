package media_blockchain.blockchain.dto;

import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Service
@NoArgsConstructor
public class WalletDto {

	Integer Assert;

	String name;

	@Builder
	public WalletDto(String name, Integer money){
		this.Assert = money;
		this.name = name;
	}

}
