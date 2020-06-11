package propets.foundsearcher.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.support.MessageBuilder;
import propets.foundsearcher.dao.FoundRepository;
import propets.foundsearcher.dao.LostSearcherRepository;
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
	LostSearcherRepository lostSearcherRepository;	
	
	@StreamListener(DispatcherService.INPUT)	
	public void handler (FoundEntityDto foundEntityDto) {	
		if (foundEntityDto.getId().startsWith(MARK)) {
			String deleteId = foundEntityDto.getId().substring(MARK.length());			
			foundRepository.deleteById(deleteId);
			return;
		}
		foundRepository.save(foundEntityDto);
		List<LostEntityDto> matches = lostSearcherRepository.findByType(foundEntityDto.getType());
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
