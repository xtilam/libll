/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.exception;

/**
 *
 * @author DinzeniLL
 */
public class QueryRepoException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QueryRepoException(String message) {
		super(message);
	}

	public static class NotFoundMethodSetParam extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public NotFoundMethodSetParam(String methodName, Class<?> resultClass) {
			throw new QueryRepoException("Not found method \"" + methodName + "\" in class \"" + resultClass.getName());
		}
	}

	public static class NotFoundMethodRepository extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public NotFoundMethodRepository(Class<?> repoClass, String methodName) {
			throw new QueryRepoException("Not found method \"" + methodName + "\" in class \"" + repoClass.getName());
		}

		public NotFoundMethodRepository(String method, Class<?> repoClass) {
			throw new QueryRepoException("Not found method \"" + method + "\" in class \"" + repoClass.getName());
		}
	}

	public static class NotFoundConstructResult extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public NotFoundConstructResult(Class<?> resultClass) {
			throw new QueryRepoException("Not found NoArgsConstruct \"" + resultClass.getName() + "\"");
		}
	}

	public static class ReturnTypeNotExpected extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ReturnTypeNotExpected(Class<?> repoClass, String methodName) {
			throw new QueryRepoException("Return type must java.util.stream.Stream<java.lang.Object[]> at method \""
					+ methodName + "\" in class \"" + repoClass.getName());
		}
	}

	public static class NotFoundQuery extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public NotFoundQuery(Class<?> repoClass, String methodName) {
			throw new QueryRepoException(
					"Not found org.springframework.data.jpa.repository.Query annotation at method \"" + methodName
							+ "\" in class \"" + repoClass.getName());
		}
	}

	public static class NotFoundBeanContextRepo extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public NotFoundBeanContextRepo(Class<?> repoClass, String methodName) {
			throw new QueryRepoException(
					"Not found org.springframework.data.jpa.repository.Query annotation at method \"" + methodName
							+ "\" in class \"" + repoClass.getName());
		}
	}
}
