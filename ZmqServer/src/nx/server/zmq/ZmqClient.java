package nx.server.zmq;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ZmqClient
{
	Gson gson;
	ZmqDealer client;
	
	public ZmqClient(String clientId, String host, int port, Integer timeout)
	{
		gson = new GsonBuilder().create();
		client = new ZmqDealer(clientId.getBytes(), host, port, timeout);
	}
	
	public void send(ZmqClientRequest req)
	{
		client.send(gson.toJson(req));
	}
	
	public String receive()
	{
		return client.receiveStr();
	}
	
	public void close()
	{
		client.close();
	}
}
