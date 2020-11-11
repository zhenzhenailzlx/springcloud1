package com.zhenzhen.demo.zuul.config;

import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;

@ConfigurationProperties("customZuul")
@EnableConfigurationProperties(CustomZuulProperties.class)
@Component
public class CustomZuulProperties {
	
	private String separateThreadPoolKeys;
	
	public String getSeparateThreadPoolKeys() {
		return separateThreadPoolKeys;
	}

	public void setSeparateThreadPoolKeys(String separateThreadPoolKeys) {
		this.separateThreadPoolKeys = separateThreadPoolKeys;
	}

	private ExecutionIsolationStrategy ribbonIsolationStrategy = THREAD;
	
	public ExecutionIsolationStrategy getRibbonIsolationStrategy() {
		return ribbonIsolationStrategy;
	}

	public void setRibbonIsolationStrategy(
			ExecutionIsolationStrategy ribbonIsolationStrategy) {
		this.ribbonIsolationStrategy = ribbonIsolationStrategy;
	}
	
	private HystrixSemaphore semaphore = new HystrixSemaphore();
	
	public HystrixSemaphore getSemaphore() {
		return semaphore;
	}
	 

	public void setSemaphore(HystrixSemaphore semaphore) {
		this.semaphore = semaphore;
	}
	
	public static class HystrixSemaphore {
		/**
		 * The maximum number of total semaphores for Hystrix.
		 */
		private int maxSemaphores = 200;

		public HystrixSemaphore() {}

		public HystrixSemaphore(int maxSemaphores) {
			this.maxSemaphores = maxSemaphores;
		}

		public int getMaxSemaphores() {
			return maxSemaphores;
		}

		public void setMaxSemaphores(int maxSemaphores) {
			this.maxSemaphores = maxSemaphores;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			HystrixSemaphore that = (HystrixSemaphore) o;
			return maxSemaphores == that.maxSemaphores;
		}

		@Override
		public int hashCode() {
			return Objects.hash(maxSemaphores);
		}

		@Override
		public String toString() {
			final StringBuffer sb = new StringBuffer("HystrixSemaphore{");
			sb.append("maxSemaphores=").append(maxSemaphores);
			sb.append('}');
			return sb.toString();
		}
	}
	

	

	private HystrixThreadPool threadPool = new HystrixThreadPool();
	
	public HystrixThreadPool getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(HystrixThreadPool threadPool) {
		this.threadPool = threadPool;
	}


	public static class HystrixThreadPool {
		/**
		 * Flag to determine whether RibbonCommands should use separate thread pools for hystrix.
		 * By setting to true, RibbonCommands will be executed in a hystrix's thread pool that it is associated with.
		 * Each RibbonCommand will be associated with a thread pool according to its commandKey (serviceId).
		 * As default, all commands will be executed in a single thread pool whose threadPoolKey is "RibbonCommand".
		 * This property is only applicable when using THREAD as ribbonIsolationStrategy
		 */
		private boolean useSeparateThreadPools = false;

		/**
		 * A prefix for HystrixThreadPoolKey of hystrix's thread pool that is allocated to each service Id.
		 * This property is only applicable when using THREAD as ribbonIsolationStrategy and useSeparateThreadPools = true
		 */
		private String threadPoolKeyPrefix = "";

		public boolean isUseSeparateThreadPools() {
			return useSeparateThreadPools;
		}

		public void setUseSeparateThreadPools(boolean useSeparateThreadPools) {
			this.useSeparateThreadPools = useSeparateThreadPools;
		}

		public String getThreadPoolKeyPrefix() {
			return threadPoolKeyPrefix;
		}

		public void setThreadPoolKeyPrefix(String threadPoolKeyPrefix) {
			this.threadPoolKeyPrefix = threadPoolKeyPrefix;
		}
	}

}
