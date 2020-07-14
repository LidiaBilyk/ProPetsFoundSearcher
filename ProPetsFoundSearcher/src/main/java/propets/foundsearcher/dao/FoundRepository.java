package propets.foundsearcher.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import propets.foundsearcher.dto.FoundEntityDto;

public interface FoundRepository extends ElasticsearchRepository<FoundEntityDto, String>{

}
