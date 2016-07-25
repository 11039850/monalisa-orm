/*******************************************************************************************
 *	Copyright (c) 2016, zzg.zhou(11039850@qq.com)
 * 
 *  Monalisa is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU Lesser General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU Lesser General Public License for more details.

 *	You should have received a copy of the GNU Lesser General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************************/
package com.tsc9526.monalisa.orm.datasource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class DBTasks {
	private static Map<String,Timer> timers=new LinkedHashMap<String,Timer>(); 
	
	public  static void schedule(String taskName,TimerTask task,long delay,long period){
		if(!timers.containsKey(taskName)){
			addSchedule(taskName,task,delay,period);
		}
	}
	
	private synchronized static void addSchedule(String taskName,TimerTask task,long delay,long period){
		if(!timers.containsKey(taskName)){
			Timer timer=new Timer(taskName,true);
			timers.put(taskName,timer);
			
			if(delay<=0){
				task.run();
				
				delay=period;
			}
			timer.schedule(task,delay,period);
		}
	}
	
	static void shutdown(){
		 for(Timer timer:timers.values()){
			 timer.cancel();
		 }
	}
}
