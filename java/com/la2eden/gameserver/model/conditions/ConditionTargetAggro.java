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

import com.la2eden.gameserver.model.actor.L2Character;
import com.la2eden.gameserver.model.actor.instance.L2MonsterInstance;
import com.la2eden.gameserver.model.actor.instance.L2PcInstance;
import com.la2eden.gameserver.model.items.L2Item;
import com.la2eden.gameserver.model.skills.Skill;

/**
 * The Class ConditionTargetAggro.
 * @author mkizub
 */
public class ConditionTargetAggro extends Condition
{
	private final boolean _isAggro;
	
	/**
	 * Instantiates a new condition target aggro.
	 * @param isAggro the is aggro
	 */
	public ConditionTargetAggro(boolean isAggro)
	{
		_isAggro = isAggro;
	}
	
	@Override
	public boolean testImpl(L2Character effector, L2Character effected, Skill skill, L2Item item)
	{
		if (effected instanceof L2MonsterInstance)
		{
			return ((L2MonsterInstance) effected).isAggressive() == _isAggro;
		}
		if (effected instanceof L2PcInstance)
		{
			return ((L2PcInstance) effected).getKarma() > 0;
		}
		return false;
	}
}
