/*
 * This file is part of the La2Eden project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.la2eden.gameserver.model.conditions;

import com.la2eden.gameserver.model.L2Clan;
import com.la2eden.gameserver.model.actor.L2Character;
import com.la2eden.gameserver.model.items.L2Item;
import com.la2eden.gameserver.model.skills.Skill;

/**
 * The Class ConditionPlayerHasFort.
 * @author MrPoke
 */
public final class ConditionPlayerHasFort extends Condition
{
	private final int _fort;
	
	/**
	 * Instantiates a new condition player has fort.
	 * @param fort the fort
	 */
	public ConditionPlayerHasFort(int fort)
	{
		_fort = fort;
	}
	
	/**
	 * Test impl.
	 * @return true, if successful
	 */
	@Override
	public boolean testImpl(L2Character effector, L2Character effected, Skill skill, L2Item item)
	{
		if (effector.getActingPlayer() == null)
		{
			return false;
		}
		
		final L2Clan clan = effector.getActingPlayer().getClan();
		if (clan == null)
		{
			return _fort == 0;
		}
		
		// Any fortress
		if (_fort == -1)
		{
			return clan.getFortId() > 0;
		}
		return clan.getFortId() == _fort;
	}
}
