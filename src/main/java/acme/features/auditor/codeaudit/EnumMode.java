
package acme.features.auditor.codeAudit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import acme.entities.audits.Mark;

public class EnumMode {

	public static String mode(final Collection<Mark> marks) {
		if (marks == null || marks.isEmpty())
			return null;

		Map<Mark, Integer> frequencyMap = new HashMap<>();

		for (Mark mark : marks)
			if (frequencyMap.containsKey(mark))
				frequencyMap.put(mark, frequencyMap.get(mark) + 1);
			else
				frequencyMap.put(mark, 1);

		Mark modeMark = null;
		int maxFrequency = 0;

		for (Map.Entry<Mark, Integer> entry : frequencyMap.entrySet()) {
			Mark mark = entry.getKey();
			int frequency = entry.getValue();

			if (frequency > maxFrequency || frequency == maxFrequency && mark.ordinal() > modeMark.ordinal()) {
				modeMark = mark;
				maxFrequency = frequency;
			}
		}

		return modeMark.toString();
	}

}
