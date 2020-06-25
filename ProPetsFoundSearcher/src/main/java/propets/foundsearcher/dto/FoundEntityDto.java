package propets.foundsearcher.dto;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
@Document("foundentities")
public class FoundEntityDto {
	@Id
	String id;
	boolean typePost;
	String userLogin;
	String type;
	String breed;
	String sex;
	List<String> tags;
    LocationDto location;

}
