package com.dinz.library.cache;

import javax.servlet.http.HttpServletRequest;

import com.dinz.library.LibApplication;
import com.dinz.library.exception.DeathUserException;

public class DeathAdminLoginSession implements IAdminLoginSession {
	private Long deletedAt;
	private Long userId;
	private ClearCache clearCache;
	private static final Long timeWaitClean = 10000L;

	private static class ClearCache extends Thread {
		private DeathAdminLoginSession deathSession;

		ClearCache(DeathAdminLoginSession deathSession) {
			this.deathSession = deathSession;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(DeathAdminLoginSession.timeWaitClean);
				this.deathSession.clearCache();
			} catch (InterruptedException e) {
			}
		}

	}

	DeathAdminLoginSession(long userId) {
		this.userId = userId;
		this.deletedAt = System.currentTimeMillis();
		this.clearCache = new ClearCache(this);
	}

	public void destroy() {
		this.deletedAt = 0L;
		this.clearCache();
	}

	@Override
	public boolean updateToken(Long tokenId, HttpServletRequest req, Long adminId, AdminUserLoginCache adminCache) {
		throw new DeathUserException();
	}

	@Override
	public void clearCache(Long userId, AdminUserLoginCache adminCache) {
		if (this.deletedAt + timeWaitClean > System.currentTimeMillis()) {
			synchronized (adminCache.getCacheMap()) {
				IAdminLoginSession adminSession = adminCache.getCacheMap().get(this.userId);
				if (adminSession instanceof DeathAdminLoginSession) {
					adminCache.getCacheMap().remove(this.userId);
				}
			}
			this.clearCache.interrupt();
		}
	}

	public void clearCache() {
		this.clearCache(this.userId, LibApplication.getContext().getBean(AdminUserLoginCache.class));
	}

	@Override
	public void clearGroups() {
		throw new DeathUserException();
	}

	@Override
	public void clearPermissions() {
		throw new DeathUserException();
	}

	@Override
	public void removeToken(Long tokenId) {
		throw new DeathUserException();
	}

	@Override
	public Long addToken(AdminLoginInfo adminLoginInfo) {
		return null;
	}

	@Override
	public boolean checkPermission(String permission, Long adminId) {
		throw new DeathUserException();
	}

}
