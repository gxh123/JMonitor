package com.jmonitor.core.message.consume;

import com.jmonitor.core.message.MessageQueue;
import com.jmonitor.core.message.analysis.MessageAnalyzer;
import com.jmonitor.core.message.analysis.MessageAnalyzerManager;
import com.jmonitor.core.message.type.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * description: a period represents a time interval - one hour, determined by the startTime and endTime, messages in this time interval
 *              will process by this period's analyzers( defined in the field: tasks). if this period is over, next period starts(in fact a few
 *              minutes ahead), then messages in the time interval will process by the next period's analyzers.
 *              the period just divides the message in the unit of an hour.
 * author jmonitor
 * date 2016/10/9
 */
public class Period {
    private static int QUEUE_SIZE = 30000;

	private long startTime;
	private long endTime;
	private Map<String, PeriodTask> tasks;
	private Logger logger = LoggerFactory.getLogger("com.jmonitor.core.message.consume.Period");

	public Period(long startTime, long endTime, MessageAnalyzerManager analyzerManager) {
		this.startTime = startTime;
		this.endTime = endTime;

		List<String> names = analyzerManager.getAnalyzerNames();

		tasks = new HashMap<String, PeriodTask>();
		for (String name : names) {
			MessageAnalyzer analyzer = analyzerManager.getAnalyzer(name, startTime);
			MessageQueue queue = new MessageQueue(QUEUE_SIZE);
			PeriodTask task = new PeriodTask(analyzer, queue, startTime);
            tasks.put(name, task);
		}
	}

    /**
     * description：distribute the message by put the message in each task's queue
     * param：tree:  the tree includes the message and some other information
     * return: void
     */
	public void distribute(Message message) {
		String messageType = message.getType();
        String requiredAnalyzer = messageType + "Analyzer";
        PeriodTask task = tasks.get(requiredAnalyzer);
        if(task != null) {
            task.enqueue(message);
        } else {
			logger.error("no PeriodTask found for {}",requiredAnalyzer);
        }
	}

    /**
     * description： start the period, in fact just start several task Threads
     * param：no param
     * return: void
     */
    public void start() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		logger.info(String.format("Starting %s tasks in period [%s, %s]", tasks.size(),
				df.format(new Date(startTime)), df.format(new Date(endTime - 1))));
//        System.out.println(String.format("Starting %s tasks in period [%s, %s]", tasks.size(),
//                df.format(new Date(startTime)), df.format(new Date(endTime - 1))));

        for (Entry<String, PeriodTask> entry : tasks.entrySet()) {
            PeriodTask task = entry.getValue();
            new Thread(task).start();
        }
    }

    /**
     * description： finish the period, in fact just finish several task Threads
     * param：no param
     * return: void
     */
	public void finish() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = new Date(startTime);
		Date endDate = new Date(endTime - 1);

//		System.out.println(String.format("Finishing %s tasks in period [%s, %s]", tasks.size(), df.format(startDate),
//		      df.format(endDate)));
		logger.info(String.format("Finishing %s tasks in period [%s, %s]", tasks.size(), df.format(startDate),
				df.format(endDate)));

		try {
			for (Entry<String, PeriodTask> entry : tasks.entrySet()) {
                PeriodTask task = entry.getValue();
                task.finish();
			}
		} catch (Throwable e) {
			logger.error(e.getMessage());
		} finally {
//			System.out.println(String.format("Finished %s tasks in period [%s, %s]", tasks.size(), df.format(startDate),
//			      df.format(endDate)));
			logger.info(String.format("Finished %s tasks in period [%s, %s]", tasks.size(), df.format(startDate),
					df.format(endDate)));
		}
	}

	public MessageAnalyzer getAnalyzer(String name) {
		PeriodTask task = tasks.get(name);

		if (task != null) {
            return task.getAnalyzer();
		}
		return null;
	}

	public List<MessageAnalyzer> getAnalyzers() {
		List<MessageAnalyzer> analyzers = new ArrayList<MessageAnalyzer>(tasks.size());

		for (Entry<String, PeriodTask> entry : tasks.entrySet()) {
			PeriodTask task = entry.getValue();
            analyzers.add(task.getAnalyzer());
		}
		return analyzers;
	}

	public long getStartTime() {
		return startTime;
	}

	public boolean isIn(long timestamp) {
		return timestamp >= startTime && timestamp < endTime;
	}

}
