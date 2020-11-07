package com.dinz.library.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import com.dinz.library.cache.AdminUserLoginCache;
import com.dinz.library.cache.IAdminLoginSession;
import com.dinz.library.constants.SystemConstants;

@Configuration
public class Config {

	@Autowired
	AdminUserLoginCache adminCache;

	@PersistenceContext
	EntityManager em;

	@Autowired
	private TaskExecutor taskExecutor;

	@Bean
	public void updatePermissionCache() {
		adminCache.refeshPermissionGroupMap();
	}

	@Bean
	public void warningPermissionDB() {
		Set<Class<?>> restControllers = new Reflections("com.dinz.library").getTypesAnnotatedWith(RestController.class);
		HashMap<String, String> permissions = new HashMap<>();

		final Pattern parternHasPermisison = Pattern.compile("hasPermission\\(.+?\\,.*?'(.*?)'\\)");
		for (Class<?> restController : restControllers) {
			for (Method method : restController.getDeclaredMethods()) {
				PreAuthorize preAuth = method.getAnnotation(PreAuthorize.class);
				if (preAuth != null) {
					Matcher matcher = parternHasPermisison.matcher(preAuth.value());
					while (matcher.find()) {
						permissions.put(matcher.group(1), "[class: " + restController.getName() + ", methodName: "
								+ method.getName() + ", type: PreAuthorize, permission: " + matcher.group(1));
					}
				}
				PostAuthorize postAuth = method.getAnnotation(PostAuthorize.class);
				if (postAuth != null) {
					Matcher matcher = parternHasPermisison.matcher(postAuth.value());
					while (matcher.find()) {
						permissions.put(matcher.group(1), "[ Class: " + restController.getName() + ", methodName: "
								+ method.getName() + ", type: PostAuthorize, permission: " + matcher.group(1) + " ]");
					}
				}
			}
		}

		List<String> permissionInDB = this.em
				.createQuery("select p.permissionCode from Permission p where p.deleteStatus = 0", String.class)
				.getResultList();

		boolean isHavePermissionNotFound = false;
		for (String per : permissionInDB) {
			String permissionInfo = permissions.remove(per);
			if (null == permissionInfo) {
				if (!isHavePermissionNotFound) {
					System.err.println("The following permissions are not found in the your code:");
					isHavePermissionNotFound = true;
				}
				System.err.println(per);
			}
		}

		if (!permissions.isEmpty()) {
			System.err.println("The following permissions are not found in the database:");
			for (Entry<String, String> entry : permissions.entrySet()) {
				System.err.println(entry.getValue());
			}
		}
	}

	@Bean
	public void startClearCache() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {

					try {
						Thread.sleep(SystemConstants.SLEEP_TIME_CLEAN_CACHE);
						Set<Long> keys = adminCache.getAdminUsersId();
						for (Long adminId : keys) {
							IAdminLoginSession iASession = adminCache.getCacheMap().get(adminId);
							if (iASession != null) {
								iASession.clearCache(adminId, adminCache);
							}
						}

						System.gc();
					} catch (InterruptedException e) {
						System.out.println("clean cache closed");
						break;
					} catch (Exception e) {
						System.err.println("clean cache error : " + e.getMessage());
					}
				}
			}
		};

		taskExecutor.execute(thread);
	}
}
