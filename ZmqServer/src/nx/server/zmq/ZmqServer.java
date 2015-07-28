package nx.server.zmq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class ZmqServer
{
	final static Logger logger = Logger.getLogger(ZmqServer.class);

	ZmqProxy proxy;
	int workerResponsePort;
	List<ZmqWorker> workerList;

	ExecutorService executor;

	public ZmqServer(int clientRequestPort, int workerResponsePort)
	{
		this.workerResponsePort = workerResponsePort;
		workerList = new ArrayList<ZmqWorker>();

		proxy = new ZmqProxy(clientRequestPort, workerResponsePort);

		executor = Executors.newCachedThreadPool();
	}

	public void addMsgHandler(IMsgHandler msgHandler, int scale) throws Exception
	{
		if(scale < 1)
		{
			throw new Exception("Invalid scale number: " + scale);
		}
		for (int i = 0; i < scale; i++)
		{
			ZmqWorker worker = new ZmqWorker(workerResponsePort, msgHandler, i);
			workerList.add(worker);
		}
	}

	public void start() throws Exception
	{
		// run the proxy
		executor.execute(proxy);
		Thread.sleep(10);
		// run all the workers
		for (ZmqWorker w : workerList)
		{
			executor.execute(w);
		}
	}

	public void stop()
	{
		proxy.stop();
		for (ZmqWorker w : workerList)
		{
			w.stop();
		}
	}
}
