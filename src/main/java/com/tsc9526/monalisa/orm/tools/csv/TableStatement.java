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
package com.tsc9526.monalisa.orm.tools.csv;

import org.relique.jdbc.csv.CsvConnection;
import org.relique.jdbc.csv.CsvStatement;

/**
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
class TableStatement extends CsvStatement{
	protected TableStatement(CsvConnection connection, int resultSetType) {
		super(connection, resultSetType);
	}
}