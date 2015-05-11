package com.github.jrgen.test.profile;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import com.github.jrgen.context.JrgenContext;
import com.github.jrgen.settings.Settings;

public class UserProfileTest {

	private static final Log log = LogFactory.getLog(UserProfileTest.class);
	private JrgenContext jrgenContext;
	private Settings settings;
	
	public UserProfileTest() {
		settings = Settings.getInstance();
		jrgenContext = new JrgenContext(settings).initalizeContext();
		jrgenContext.getAbstractTypeHandler()
			.registerAbstractType(Group.class, ProfessionalGroup.class);
		jrgenContext.getSettings().setMaxContainerSize(1);
	}
	
	@Test
	public void testUserProfile() {
		UserProfile profile = jrgenContext.generate(UserProfile.class);
		log.info(profile);
	}
	
	@Test
	public void testUserProfileDefGrp() {
		for (int i=0; i < 10; i++) {
			Group group = jrgenContext.generate(Group.class);
			log.info(group);
		}
		
		log.info(StringUtils.EMPTY);
		
		for (int i =0; i < 10; i++) {
			UserProfile profile = jrgenContext.generate(UserProfile.class);
			log.info(profile);
		}
	}
}
