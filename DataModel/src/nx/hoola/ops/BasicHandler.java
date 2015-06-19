package nx.hoola.ops;

import java.util.List;

import nx.hoola.data.redis.PersonDataHandler;

public class BasicHandler
{
	public boolean isPersonExist(PersonDataHandler personData, String personId)
	{
		return personData.isPersonExist(personId);
	}
	
	public void updatePersonInterestGridByScore(PersonDataHandler personData, String personId, List<String> interests,
			Double scoreChange)
	{
		interests.stream().forEach(
				(interest) ->
				{
					personData.increaseInterestScore(personId, interest, scoreChange);
				});
	}
}
