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
package com.tsc9526.monalisa.tools.clazz;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Java8(ASM5)
 * 
 * @author zzg.zhou(11039850@qq.com)
 */
public class MelpAsm {
	static {
		MelpLib.loadClass(MelpLib.libAsmClass);
	}

	public static String[] getMethodParamNames(final Method m) {
		final String[] paramNames = new String[m.getParameterTypes().length];
		final String n = m.getDeclaringClass().getName();

		ClassReader cr = null;
		try {
			cr = new ClassReader(n);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		cr.accept(new ClassVisitor(Opcodes.ASM5) {
			public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
				final Type[] args = Type.getArgumentTypes(desc);

				// 方法名相同并且参数个数相同
				if (!name.equals(m.getName()) || !sameType(args, m.getParameterTypes())) {
					return super.visitMethod(access, name, desc, signature, exceptions);
				}

				MethodVisitor v = super.visitMethod(access, name, desc, signature, exceptions);
				return new MethodVisitor(Opcodes.ASM5, v) {
					public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
						int i = index - 1;
						// 如果是静态方法，则第一就是参数
						// 如果不是静态方法，则第一个是"this"，然后才是方法的参数
						if (Modifier.isStatic(m.getModifiers())) {
							i = index;
						}
						
						if (i >= 0 ) {
							setParamName(paramNames,name);
						}
						 
						super.visitLocalVariable(name, desc, signature, start, end, index);
					}

				};
			}
		}, 0);

		return paramNames;
	}
	
	private static void setParamName(String[] paramNames,String name) {
		for(int i=0;i<paramNames.length;i++) {
			if(paramNames[i]==null) {
				paramNames[i] = name;
				return;
			}
		}
	}

	private static boolean sameType(Type[] types, Class<?>[] clazzes) {
		// 个数不同
		if (types.length != clazzes.length) {
			return false;
		}

		for (int i = 0; i < types.length; i++) {
			if (!Type.getType(clazzes[i]).equals(types[i])) {
				return false;
			}
		}
		return true;
	}
}
