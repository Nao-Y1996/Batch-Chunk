package com.example.demo.config;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private ItemReader<String> reader;
	@Autowired
	private ItemProcessor<String, String> processor;
	@Autowired
	private ItemWriter<String> writer;
	@Autowired
	private JobExecutionListener helloJobListener;
	@Autowired
	private StepExecutionListener helloStepListener;
	
	@Bean
	/** Chunkのstepを生成 */
	public Step chunkStep() {
		return stepBuilderFactory.get("HelloChunkStep")
				.<String, String>chunk(3) // Chunkの設定 <Input,Output>chunk(コミット間隔). 
				// Inputはreaderで扱うデータ型, Outputはwriterで扱うデータ型. コミット間隔は一度に処理する件数.
				// Chunkでは、コミット間隔に指定した件数だけReader、Processorを実行し,その後、Writerを1回実行する。Readerがnullを返すまで、この処理を繰り返す。
				.reader(reader) // ItemReaderの指定
				.processor(processor) // ItemProcessorの指定
				.writer(writer) // ItemWriterの指定
				.listener(helloStepListener)// Listenerの指定
				.build(); // stepの生成
	}
	
	/** Jobを生成 */
	@Bean
	public Job chunkJob() throws Exception{
		return jobBuilderFactory.get("HelloWorldchunkJob")
				.incrementer(new RunIdIncrementer())
				.start(chunkStep())
				.listener(helloJobListener)
				.build();
	}
	
	
}
