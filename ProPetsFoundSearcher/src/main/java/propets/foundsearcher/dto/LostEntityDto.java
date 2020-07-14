package propets.foundsearcher.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
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
@Document(indexName = "lostentities")
public class LostEntityDto {
	@Id
	String id;
	boolean typePost;
	String userLogin;
	String type;
	String sex;
	String breed;	
	String tags;
    LocationEntityDto location;

}
