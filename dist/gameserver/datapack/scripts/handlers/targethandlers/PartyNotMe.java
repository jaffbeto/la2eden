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
package handlers.targethandlers;

import java.util.ArrayList;
import java.util.List;

import com.la2eden.Config;
import com.la2eden.gameserver.handler.ITargetTypeHandler;
import com.la2eden.gameserver.model.L2Object;
import com.la2eden.gameserver.model.actor.L2Character;
import com.la2eden.gameserver.model.actor.instance.L2PcInstance;
import com.la2eden.gameserver.model.skills.Skill;
import com.la2eden.gameserver.model.skills.targets.L2TargetType;
import com.la2eden.gameserver.util.Util;

/**
 * @author UnAfraid
 */
public class PartyNotMe implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		final List<L2Character> targetList = new ArrayList<>();
		if (activeChar.getParty() != null)
		{
			final List<L2PcInstance> partyList = activeChar.getParty().getMembers();
			for (L2PcInstance partyMember : partyList)
			{
				if ((partyMember == null) || partyMember.isDead())
				{
					continue;
				}
				else if (partyMember == activeChar)
				{
					continue;
				}
				else if (!Util.checkIfInRange(Config.ALT_PARTY_RANGE, activeChar, partyMember, true))
				{
					continue;
				}
				else if ((skill.getAffectRange() > 0) && !Util.checkIfInRange(skill.getAffectRange(), activeChar, partyMember, true))
				{
					continue;
				}
				else
				{
					targetList.add(partyMember);
					
					if ((partyMember.getSummon() != null) && !partyMember.getSummon().isDead())
					{
						targetList.add(partyMember.getSummon());
					}
				}
			}
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.PARTY_NOTME;
	}
}
