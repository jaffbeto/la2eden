/*
 * This file is part of the L2J Mobius project.
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
package handlers.itemhandlers;

import com.la2eden.Config;
import com.la2eden.gameserver.handler.IItemHandler;
import com.la2eden.gameserver.model.L2Object;
import com.la2eden.gameserver.model.actor.L2Character;
import com.la2eden.gameserver.model.actor.L2Playable;
import com.la2eden.gameserver.model.actor.instance.L2PcInstance;
import com.la2eden.gameserver.model.holders.SkillHolder;
import com.la2eden.gameserver.model.items.instance.L2ItemInstance;
import com.la2eden.gameserver.network.SystemMessageId;
import com.la2eden.gameserver.network.serverpackets.ActionFailed;

/**
 * @author l3x
 */
public final class Harvester implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!Config.ALLOW_MANOR)
		{
			return false;
		}
		else if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.YOUR_PET_CANNOT_CARRY_THIS_ITEM);
			return false;
		}
		
		final SkillHolder[] skills = item.getItem().getSkills();
		if (skills == null)
		{
			_log.warning(getClass().getSimpleName() + ": is missing skills!");
			return false;
		}
		
		final L2PcInstance activeChar = playable.getActingPlayer();
		final L2Object target = activeChar.getTarget();
		if ((target == null) || !target.isMonster() || !((L2Character) target).isDead())
		{
			activeChar.sendPacket(SystemMessageId.INVALID_TARGET);
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		for (SkillHolder sk : skills)
		{
			activeChar.useMagic(sk.getSkill(), false, false);
		}
		return true;
	}
}
