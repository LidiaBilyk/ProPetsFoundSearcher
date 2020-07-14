package propets.foundsearcher.dao;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import propets.foundsearcher.dto.LostEntityDto;

public interface LostSearcherRepository extends ElasticsearchRepository<LostEntityDto, String>{
	List<LostEntityDto> findByTypeAndSexAndBreed(String type, String sex, String breed);
}
