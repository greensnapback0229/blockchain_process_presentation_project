package media_blockchain.blockchain.dto;

import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Service
@NoArgsConstructor
public class WalletDto {

	String uuid;

	Integer money;

	String name;

	@Builder
	public WalletDto(String uuid,Integer money, String nickname){
		this.uuid = uuid;
		this.money = money;
		this.name = nickname;
	}

}
