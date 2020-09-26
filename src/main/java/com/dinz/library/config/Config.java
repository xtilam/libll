package com.dinz.library.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import com.dinz.library.cache.AdminLoginSession;
import com.dinz.library.cache.AdminUserLoginCache;
import com.dinz.library.cache.IAdminLoginSession;
import com.dinz.library.cache.PermissionCache;
import com.dinz.library.constants.SystemConstants;

@Configuration
public class Config {

    @Autowired
    PermissionCache permissionCache;

    @Autowired
    AdminUserLoginCache adminCache;

    @Autowired
    private TaskExecutor taskExecutor;

    @Bean
    public void updatePermissionCache() {
        permissionCache.updateAllPermission();
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
                            if (iASession instanceof AdminLoginSession) {
                                ((AdminLoginSession) iASession).clearCache(adminId);
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
