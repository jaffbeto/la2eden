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
package handlers.effecthandlers;

import com.la2eden.gameserver.enums.ShotType;
import com.la2eden.gameserver.model.StatsSet;
import com.la2eden.gameserver.model.actor.L2Character;
import com.la2eden.gameserver.model.conditions.Condition;
import com.la2eden.gameserver.model.effects.AbstractEffect;
import com.la2eden.gameserver.model.effects.L2EffectType;
import com.la2eden.gameserver.model.skills.BuffInfo;
import com.la2eden.gameserver.model.skills.Skill;
import com.la2eden.gameserver.model.stats.Formulas;
import com.la2eden.gameserver.network.SystemMessageId;
import com.la2eden.gameserver.network.serverpackets.SystemMessage;

/**
 * Physical Attack effect implementation.
 * @author Adry_85
 */
public final class PhysicalAttack extends AbstractEffect
{
	public PhysicalAttack(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params)
	{
		super(attachCond, applyCond, set, params);
	}
	
	@Override
	public boolean calcSuccess(BuffInfo info)
	{
		return !Formulas.calcPhysicalSkillEvasion(info.getEffector(), info.getEffected(), info.getSkill());
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.PHYSICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		final L2Character target = info.getEffected();
		final L2Character activeChar = info.getEffector();
		final Skill skill = info.getSkill();
		
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		if (((info.getSkill().getFlyRadius() > 0) || (skill.getFlyType() != null)) && activeChar.isMovementDisabled())
		{
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS);
			sm.addSkillName(skill);
			activeChar.sendPacket(sm);
			return;
		}
		
		if (target.isPlayer() && target.getActingPlayer().isFakeDeath())
		{
			target.stopFakeDeath(true);
		}
		
		int damage = 0;
		final boolean ss = skill.isPhysical() && activeChar.isChargedShot(ShotType.SOULSHOTS);
		final byte shld = Formulas.calcShldUse(activeChar, target, skill);
		// Physical damage critical rate is only affected by STR.
		boolean crit = false;
		if (skill.getBaseCritRate() > 0)
		{
			crit = Formulas.calcCrit(activeChar, target, skill);
		}
		
		damage = (int) Formulas.calcPhysDam(activeChar, target, skill, shld, false, ss);
		
		if (crit)
		{
			damage *= 2;
		}
		
		if (damage > 0)
		{
			activeChar.sendDamageMessage(target, damage, false, crit, false);
			target.reduceCurrentHp(damage, activeChar, skill);
			target.notifyDamageReceived(damage, activeChar, skill, crit, false);
			
			// Check if damage should be reflected
			Formulas.calcDamageReflected(activeChar, target, skill, crit);
		}
		else
		{
			activeChar.sendPacket(SystemMessageId.YOUR_ATTACK_HAS_FAILED);
		}
		
		if (skill.isSuicideAttack())
		{
			activeChar.doDie(activeChar);
		}
	}
}