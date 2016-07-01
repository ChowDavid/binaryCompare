package com.david.binaryCompare;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestFileAlias {

	@Test
	public void test() {
		String inputString="common-logger-1.2.3.jar/com/cathaypacific/utility/log/Configurator$1.class";
		String outputString="common-logger.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		 inputString="common-logger-1.3-SNAPSHOT.jar/com/cathaypacific/utility/log/Configurator$1.class";
		 outputString="common-logger.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		 inputString="common-logger-1.3-RELEASE.jar/com/cathaypacific/utility/log/Configurator$1.class";
		 outputString="common-logger.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		 inputString="common-logger-1.3-betal.jar/com/cathaypacific/utility/log/Configurator$1.class";
		 outputString="common-logger.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		 inputString="common-logger_1.0_spec-1.1.jar/com/cathaypacific/utility/log/Configurator$1.class";
		 outputString="common-logger_1.0_spec.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		 inputString="common-logger-1.5rc3.jar/com/cathaypacific/utility/log/Configurator$1.class";
		 outputString="common-logger.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		 inputString="common-logger-2016.05.12.jar/com/cathaypacific/utility/log/Configurator$1.class";
		 outputString="common-logger.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		 inputString="jakarta-oro.jar/com/cathaypacific/utility/log/Configurator$1.class";
		 outputString="jakarta-oro.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		 inputString="easymock-1.2_Java1.3.jar/com/cathaypacific/utility/log/Configurator$1.class";
		 outputString="easymock.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		 inputString="commons-codec-1.3.jar/com/cathaypacific/utility/log/Configurator$1.class";
		 outputString="commons-codec.jar/com/cathaypacific/utility/log/Configurator$1.class";
		assertEquals(outputString, FileAliasUtil.getAlias(inputString));
		
	}

}
