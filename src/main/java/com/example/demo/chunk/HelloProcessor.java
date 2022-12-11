package com.example.demo.chunk;



import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 配列内の文字列を処理する.
 * @author NaoYamada
 *
 */
@Component
@StepScope
@Slf4j
// ProcessorはItemProcessorインタフェースの実装とする. 
// ジェネリクスの引数はそれぞれ、ItemReaderから渡されるデータ型, ItemWiterに渡すデータ型
public class HelloProcessor implements ItemProcessor<String, String> {
	
	// Readerから渡されてくる値が、processメソッドの引数にセットされる。
	// そして、processメソッドの戻り値がWriterに渡される。
	@Override
	public String process(String item) throws Exception{
		item = item + "★";
		log.info("Proccesor : {}", item);
		return item;
	}

}
