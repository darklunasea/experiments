package nx.server.zmq.components;

public class ZmqWorkerRequest
{
	byte[] clientId;
	String data;

	public byte[] getClientId()
	{
		return clientId;
	}

	public void setClientId(byte[] clientId)
	{
		this.clientId = clientId;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

}
