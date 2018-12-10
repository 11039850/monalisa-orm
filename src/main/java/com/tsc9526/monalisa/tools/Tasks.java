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
package com.tsc9526.monalisa.tools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.tsc9526.monalisa.tools.logger.Logger;
 

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class Tasks {
	static Logger logger=Logger.getLogger(Tasks.class);
	
	public static TaskThread instance=new TaskThread();
  
	public static class TaskThread extends Thread{
		protected long    ts         = System.currentTimeMillis();
		protected boolean running    = false;
		protected boolean destoried  = false;
		protected boolean terminated = false;
		protected Timer   timer      = new Timer("Monalisa-DBTask-Timer",true);
		
		protected List<Runnable> cls   = new ArrayList<Runnable>();
		
		protected Set<String> timeTasks = new HashSet<String>();
		
		private TaskThread(){
			setName("Monalisa-Task-Daemon");
			setDaemon(true);
			
			start();
		}
		
		public void interrupt(){
			running=false;
			super.interrupt();
		}
		
		public void run(){
			running=true;
		 
			try{
				while(running){
					delay(200);
				}
			}finally{
				terminated=true;
				
				destory();
			}
		}
		
		public synchronized void destory(){
			if(running){
				running=false;
				
				synchronized(this){
					notifyAll();
				}
			}
			
			if(!destoried){
				destoried=true;
				
				timer.cancel();
				
				for(Runnable c:cls){
					c.run();
				}
				
				if(!terminated){
					interrupt();
				}
				
				logger.info("Monalisa task thread destoried!");
			}
		}
		
		public synchronized void addSchedule(String taskName,TimerTask task,long delay,long period){
			if(!timeTasks.contains(taskName)){
				timeTasks.add(taskName);
				
				if(delay<=0){
					task.run();
					
					delay=period;
				}
				timer.schedule(task,delay,period);
			}
		}
		
		public void addShutdown(Runnable c){
			cls.add(c);
		}
		 
		protected void delay(int millis){
			synchronized(this){
				try{
					wait(millis);
				}catch(InterruptedException ex){
					running=false;
				}
			} 		
		}
		
		public boolean isDestoried(){
			return destoried;
		}
	}
}
