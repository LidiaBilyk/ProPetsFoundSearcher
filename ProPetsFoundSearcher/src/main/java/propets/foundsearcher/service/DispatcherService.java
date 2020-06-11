package propets.foundsearcher.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.MessageChannel;

public interface DispatcherService extends Sink{
	
	String MATCHES = "matches";

	@Output(MATCHES)
	MessageChannel matches();
}
