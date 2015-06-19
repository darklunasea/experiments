package nx.hoola.data.redis;

import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

public class RedisEngine
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
	
	public Long getNumberOfMembersOnSet(String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.scard(key);
		}
	}

	public boolean getBitOnKey(String key, long offset)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.getbit(key, offset);
		}
	}

	public boolean setBitOnKey(String key, long offset, boolean value)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.setbit(key, offset, value);
		}
	}

	public boolean isMemberOnSortedSet(String key, String member)
	{
		try (Jedis jedis = pool.getResource())
		{
			if (jedis.zrank(key, member) == null)
			{
				return false;
			}
			return true;
		}
	}

	public void increaseMemberScoreOnSortedSet(String key, String member, Double score)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.zincrby(key, score, member);
		}
	}

	public void decreaseMemberScoreOnSortedSet(String key, String member, Double score)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.zincrby(key, (score * -1), member);
		}
	}

	public void removeMembersOnSortedSet(String key, String... members)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.zrem(key, members);
		}
	}

	public Set<Tuple> getTopRankedOnSortedSet(String key, long topN)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.zrevrangeWithScores(key, 0, topN - 1);
		}
	}

	public List<Tuple> getAllMembersOnSortedSet(String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			ScanResult<Tuple> result = jedis.zscan(key, ScanParams.SCAN_POINTER_START);
			return result.getResult();
		}
	}

	public void increaseKeyLongValue(String key, long incrBy)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.incrBy(key, incrBy);
		}
	}

	public void decreaseKeyLongValue(String key, long decrBy)
	{
		try (Jedis jedis = pool.getResource())
		{
			jedis.decrBy(key, decrBy);
		}
	}

	public Long getKeyLongValue(String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return Long.valueOf(jedis.get(key));
		}
	}

	public String getKeyValue(String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.get(key);
		}
	}

	public Long getNextIdByKey(String key)
	{
		try (Jedis jedis = pool.getResource())
		{
			return jedis.incr(key);
		}
	}
}
