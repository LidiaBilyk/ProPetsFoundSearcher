package propets.foundsearcher.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Entity {
	
	String id;
	boolean typePost;
	String userLogin;
	String type;
	String breed;
	String sex;
	@Singular
	List<String> tags;
    Location location;

}
