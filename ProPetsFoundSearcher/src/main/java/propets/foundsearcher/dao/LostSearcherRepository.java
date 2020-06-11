package propets.foundsearcher.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import propets.foundsearcher.dto.LostEntityDto;

public interface LostSearcherRepository extends MongoRepository<LostEntityDto, String>{
	List<LostEntityDto> findByType(String type);
}
