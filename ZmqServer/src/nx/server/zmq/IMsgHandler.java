package nx.server.zmq;

public interface IMsgHandler
{
	String getServiceName();

	String process(String data) throws Exception;
}
