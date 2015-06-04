package nx.hoola.datamodel;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

class RedisEngine
{	
	String host;
	JedisPool pool;

	public RedisEngine(String host)
	{
		this.host = host;
		pool = new JedisPool(new JedisPoolConfig(), host);
		pool.getResource();
	}

	public void close()
	{
		if (pool != null)
		{
			pool.destroy();
		}
	}

	public boolean isMemberOnSet(String key, String member)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.sismember(key, member);
		}
	}
	
	public void addMemberOnSet(String key, String member)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.sadd(key, member);
		}
	}
	
	public void addMembersOnSet(String key, String... members)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.sadd(key, members);
		}
	}
	
	public void removeMemberOnSet(String key, String member)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.srem(key, member);
		}
	}
	
	public void removeMembersOnSet(String key, String... members)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.srem(key, members);
		}
	}
	
	public List<String> getAllMembersOnSet(String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			ScanResult<String> result = jedis.sscan(key, ScanParams.SCAN_POINTER_START);
			return result.getResult();
		}
	}
	
	public boolean getBitOnKey(String key, long offset)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.getbit(key, offset);
		}
	}
	
	public void increaseMemberScoreOnSortedSet(String key, String member, Double score)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.zincrby(key, score, member);
		}
	}
	
	public void removeMembersOnSortedSet(String key, String... members)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.zrem(key, members);
		}
	}
	
	public void increaseKeyValue(String key, long incrBy)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.incrBy(key, incrBy);
		}
	}
}