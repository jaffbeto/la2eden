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

import com.la2eden.gameserver.handler.BypassHandler;
import com.la2eden.gameserver.handler.IBypassHandler;
import com.la2eden.gameserver.model.actor.instance.L2ClassMasterInstance;
import com.la2eden.gameserver.model.actor.instance.L2PcInstance;
import com.la2eden.gameserver.model.quest.QuestState;
import com.la2eden.gameserver.network.serverpackets.TutorialCloseHtml;

public class RequestTutorialLinkHtml extends L2GameClientPacket
{
	private static final String _C__85_REQUESTTUTORIALLINKHTML = "[C] 85 RequestTutorialLinkHtml";
	
	private String _bypass;
	
	@Override
	protected void readImpl()
	{
		_bypass = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		if (_bypass.equalsIgnoreCase("close"))
		{
			player.sendPacket(TutorialCloseHtml.STATIC_PACKET);
		}
		
		final IBypassHandler handler = BypassHandler.getInstance().getHandler(_bypass);
		if (handler != null)
		{
			handler.useBypass(_bypass, player, null);
		}
		else
		{
			L2ClassMasterInstance.onTutorialLink(player, _bypass);
			
			final QuestState qs = player.getQuestState("Q00255_Tutorial");
			if (qs != null)
			{
				qs.getQuest().notifyEvent(_bypass, null, player);
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__85_REQUESTTUTORIALLINKHTML;
	}
}
