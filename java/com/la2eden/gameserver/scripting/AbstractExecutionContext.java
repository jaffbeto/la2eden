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
package com.la2eden.gameserver.scripting;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HorridoJoho
 * @param <T>
 */
public abstract class AbstractExecutionContext<T extends IScriptingEngine> implements IExecutionContext
{
	private final T _engine;
	private final Map<String, String> _properties;
	private volatile Path _currentExecutingScipt;
	
	protected AbstractExecutionContext(final T engine)
	{
		if (engine == null)
		{
			throw new IllegalArgumentException();
		}
		_engine = engine;
		_properties = new HashMap<>();
	}
	
	protected final void setCurrentExecutingScript(final Path currentExecutingScript)
	{
		_currentExecutingScipt = currentExecutingScript;
	}
	
	@Override
	public final String setProperty(final String key, final String value)
	{
		return _properties.put(key, value);
	}
	
	@Override
	public final String getProperty(final String key)
	{
		if (!_properties.containsKey(key))
		{
			return _engine.getProperty(key);
		}
		return _properties.get(key);
	}
	
	@Override
	public final Path getCurrentExecutingScript()
	{
		return _currentExecutingScipt;
	}
	
	@Override
	public final T getScriptingEngine()
	{
		return _engine;
	}
}
