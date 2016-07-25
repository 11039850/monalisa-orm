package com.tsc9526.monalisa.orm.tools.agent;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;
import com.tsc9526.monalisa.orm.tools.helper.Helper;

public class AgentHotSpotVM {

	public void loadAgent(String agentJar, String options) {
		VirtualMachine vm = findVirtualMachine();
		if (vm == null) {
			throw new RuntimeException("Can't attach to jvm(" + System.getProperty("java.vm.name") + "). Add -javaagent:" + agentJar+ " to the commandline");
		}

		try {
			try {
				vm.loadAgent(agentJar, options);
			} finally {
				vm.detach();
			}
		} catch (Exception e) {
			throw new RuntimeException("Can't attach to jvm. Add -javaagent:" + agentJar + " to the commandline", e);
		}
	}

	private VirtualMachine findVirtualMachine() {
		VirtualMachine vm = findVirtualMachineFromList();
		if (vm == null) {
			vm = findVirtualMachineFromName();
		}
		return vm;
	}

	private VirtualMachine findVirtualMachineFromList() {
		if (VirtualMachine.list().size() > 0) {
			String pid = Helper.getPid();
			try {
				return VirtualMachine.attach(pid);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	private VirtualMachine findVirtualMachineFromName() {
		String jvm = System.getProperty("java.vm.name").toLowerCase();
		if (jvm.contains("hotspot") || jvm.contains("openjdk")) {
			Class<VirtualMachine> vm = loadVirtualMachine();
			try {
				AttachProviderPlaceHolder holder = new AttachProviderPlaceHolder();
				Constructor<VirtualMachine> vmConstructor = vm.getDeclaredConstructor(AttachProvider.class, String.class);
				vmConstructor.setAccessible(true);
				VirtualMachine newVM = vmConstructor.newInstance(holder, Helper.getPid());
				return newVM;
			} catch (UnsatisfiedLinkError e) {
				throw new RuntimeException("This jre doesn't support the native library for attaching to the jvm", e);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private Class<VirtualMachine> loadVirtualMachine() {
		String os = System.getProperty("os.name").toLowerCase();
		try {
			if (os.contains("win")) {
				return (Class<VirtualMachine>) AgentHotSpotVM.class.getClassLoader().loadClass("sun.tools.attach.WindowsVirtualMachine");
			}
			if (os.contains("nix") || os.contains("nux") || os.indexOf("aix") > 0) {
				return (Class<VirtualMachine>) AgentHotSpotVM.class.getClassLoader().loadClass("sun.tools.attach.LinuxVirtualMachine");
			}
			if (os.contains("mac")) {
				return (Class<VirtualMachine>) AgentHotSpotVM.class.getClassLoader().loadClass("sun.tools.attach.BsdVirtualMachine");
			}
			if (os.contains("sunos") || os.contains("solaris")) {
				return (Class<VirtualMachine>) AgentHotSpotVM.class.getClassLoader().loadClass("sun.tools.attach.SolarisVirtualMachine");
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		
		throw new RuntimeException("Can't find a vm implementation for the operational system: " + os);
	}

	public static class AttachProviderPlaceHolder extends AttachProvider {

		public String name() {
			return null;
		}

		public String type() {
			return null;
		}

		public VirtualMachine attachVirtualMachine(final String id) throws AttachNotSupportedException, IOException {
			return null;
		}

		public List<VirtualMachineDescriptor> listVirtualMachines() {
			return null;
		}
	}
}
