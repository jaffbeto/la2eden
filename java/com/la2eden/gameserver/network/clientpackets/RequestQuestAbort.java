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
package com.la2eden.gameserver.network.clientpackets;

import com.la2eden.Config;
import com.la2eden.gameserver.instancemanager.QuestManager;
import com.la2eden.gameserver.model.actor.instance.L2PcInstance;
import com.la2eden.gameserver.model.quest.Quest;
import com.la2eden.gameserver.model.quest.QuestState;
import com.la2eden.gameserver.network.serverpackets.QuestList;

/**
 * This class ...
 * @version $Revision: 1.3.4.2 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestQuestAbort extends L2GameClientPacket
{
	private static final String _C__63_REQUESTQUESTABORT = "[C] 63 RequestQuestAbort";
	
	private int _questId;
	
	@Override
	protected void readImpl()
	{
		_questId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		final Quest qe = QuestManager.getInstance().getQuest(_questId);
		if (qe != null)
		{
			final QuestState qs = activeChar.getQuestState(qe.getName());
			if (qs != null)
			{
				qs.exitQuest(true);
				activeChar.sendPacket(new QuestList());
			}
			else
			{
				if (Config.DEBUG)
				{
					_log.info("Player '" + activeChar.getName() + "' try to abort quest " + qe.getName() + " but he didn't have it started.");
				}
			}
		}
		else
		{
			if (Config.DEBUG)
			{
				_log.warning("Quest (id='" + _questId + "') not found.");
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__63_REQUESTQUESTABORT;
	}
}