package propets.foundsearcher.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.messaging.support.MessageBuilder;
import propets.foundsearcher.dao.FoundRepository;
import propets.foundsearcher.dto.FoundEntityDto;
import propets.foundsearcher.dto.LostEntityDto;


@EnableBinding(DispatcherService.class)
public class FoundSearcherService {
	
	private static final String MARK = "DELETE";
	
	@Autowired
	DispatcherService dispatcherService;
	@Autowired
	FoundRepository foundRepository;	
	@Autowired
	ElasticsearchRestTemplate template;

	
	@StreamListener(DispatcherService.INPUT)	
	public void handler (FoundEntityDto foundEntityDto) {	
		if (foundEntityDto.getId().startsWith(MARK)) {
			String deleteId = foundEntityDto.getId().substring(MARK.length());			
			foundRepository.deleteById(deleteId);
			return;
		}
		foundRepository.save(foundEntityDto);

		BoolQueryBuilder builder = new BoolQueryBuilder()	
				.must(QueryBuilders.matchQuery("type", foundEntityDto.getType()))
				.filter(QueryBuilders.matchQuery("tags", foundEntityDto.getTags()).minimumShouldMatch("50%"));
		
		if (foundEntityDto.getSex() != null) {
			builder.filter(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("sex", foundEntityDto.getSex()))
			.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("sex"))));
			
		}
		if (foundEntityDto.getBreed() != null) {
			builder.filter(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("breed", foundEntityDto.getBreed()).operator(Operator.AND))
			.should(QueryBuilders.boolQuery().mustNot(QueryBuilders.existsQuery("breed"))));
			
		}
		
		QueryBuilder geoBuilder = new GeoDistanceQueryBuilder("location")
				.point(foundEntityDto.getLocation().getLat(), foundEntityDto.getLocation().getLon()).distance(3, DistanceUnit.KILOMETERS);
		Query query = new NativeSearchQueryBuilder()				
				.withQuery(builder)
				.withFilter(geoBuilder)
				.build();

		List<LostEntityDto> matches = template.search(query, LostEntityDto.class).getSearchHits().stream()
				.map(h -> h.getContent()).collect(Collectors.toList());
		if (!matches.isEmpty()) {
			sendMatches(foundEntityDto, matches);
		}		
	}

//	matches losts - one id, many logins
	private void sendMatches(FoundEntityDto foundEntityDto, List<LostEntityDto> matches) {
		Set<String> logins = matches.stream().map(e -> e.getUserLogin()).collect(Collectors.toSet());
		Map<String, Set<String>> result = new HashMap<>();
		result.put(foundEntityDto.getId(), logins);
		dispatcherService.matches().send(MessageBuilder.withPayload(result).build());
	}
}
