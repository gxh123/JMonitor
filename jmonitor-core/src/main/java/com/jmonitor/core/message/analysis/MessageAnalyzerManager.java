package com.jmonitor.core.message.analysis;

import com.jmonitor.core.util.BeanUtils;
import com.jmonitor.core.util.ScanClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.jmonitor.core.util.TimeUtil.ONE_HOUR;
import static com.jmonitor.core.util.TimeUtil.ONE_MINUTE;

/**
 * description: manage the analyzers of each period
 * author jmonitor
 * date 2016/10/9
 */
public class MessageAnalyzerManager{
	private long duration = ONE_HOUR;
	private long extraTime = 3 * ONE_MINUTE;
	private List<String> analyzerNames;
    private Map<String, Class<MessageAnalyzer>> analyzerClassMap= new HashMap<>();

    //analyzersOfPeriod stores each period's startTime and the analyzers belong to that period
    //startTime(the long type) -> Map<String, MessageAnalyzer> -> string -> MessageAnalyzer
	private Map<Long, Map<String, MessageAnalyzer>> analyzersOfPeriod = new HashMap<Long, Map<String, MessageAnalyzer>>();
	private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.analysis.analyzer.TransactionAnalyzer");

    /**
     * description：scan the class in package "core.message.analysis.analyzer" and find the class with Analyzer annotation,
     *              this will find all the available analyzers.
     */
	public MessageAnalyzerManager(){
        Set<Class<?>> setClasses = ScanClassUtil.getClassess("com.jmonitor.core.message.analysis.analyzer");
		if(setClasses.isEmpty()){
			logger.error("no analyzer found in com.jmonitor.core.message.analysis.analyzer package!");
		}
        for(Class<?> clazz : setClasses){
            if(clazz.isAnnotationPresent(Analyzer.class)){
                analyzerClassMap.put(clazz.getSimpleName(), (Class<MessageAnalyzer>) clazz);
            }
        }
        analyzerNames = new ArrayList<String>(analyzerClassMap.keySet());
    }

    /**
     * description：find the analyzer with the specific name and startTime
     * param：name: the message's name, like 'event','transaction'... each message has a unique analyzer
     *        startTime: the period 's startTime
     * return: the analyzer
     */
	public MessageAnalyzer getAnalyzer(String name, long startTime) {
		Map<String, MessageAnalyzer> map = analyzersOfPeriod.get(startTime);  //find the map first
		if (map == null) {
			synchronized (analyzersOfPeriod) {
				map = analyzersOfPeriod.get(startTime);
				if (map == null) {
					map = new HashMap<String, MessageAnalyzer>();
                    analyzersOfPeriod.put(startTime, map);
				}
			}
		}

		MessageAnalyzer analyzer = map.get(name);      //then find the analyzer in the map
		if (analyzer == null) {
			synchronized (map) {
				analyzer = map.get(name);
				if (analyzer == null) {
                    analyzer = BeanUtils.instanceClass(analyzerClassMap.get(name));
                    analyzer.initialize(startTime, duration, extraTime);
					map.put(name, analyzer);
				}
			}
		}
		return analyzer;
	}

	public List<String> getAnalyzerNames() {
		return analyzerNames;
	}
}
