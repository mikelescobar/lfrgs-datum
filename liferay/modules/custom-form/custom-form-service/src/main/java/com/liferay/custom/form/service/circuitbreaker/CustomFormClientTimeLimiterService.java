package com.liferay.custom.form.service.circuitbreaker;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

import org.osgi.service.component.annotations.Component;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Component(
		service = CustomFormClientTimeLimiterService.class)
@SuppressWarnings({	"rawtypes", "unchecked" })
public class CustomFormClientTimeLimiterService {

	public CompletableFuture getCompletableFuture(String name, long miliseconds, Supplier supplier) {

		TimeLimiter timeLimiter = getTimeLimiter(name, miliseconds);
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);	

		return timeLimiter.executeCompletionStage(
				scheduler, () -> CompletableFuture.supplyAsync(supplier))
					.toCompletableFuture();
	}

	private TimeLimiter getTimeLimiter(String name, long miliseconds) {
		return TimeLimiter.of(name, 
				TimeLimiterConfig.custom().timeoutDuration(
						Duration.ofMillis(miliseconds)).build());
	}

}
