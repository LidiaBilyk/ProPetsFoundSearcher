package propets.foundsearcher.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import propets.foundsearcher.dto.FoundEntityDto;

public interface FoundRepository extends MongoRepository<FoundEntityDto, String>{

}
