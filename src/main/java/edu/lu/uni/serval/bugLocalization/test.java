package edu.lu.uni.serval.bugLocalization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;

public class test {
	/*
Alarm Type :UPM_UNCALLED_PRIVATE_METHOD : 13
Alarm Type :SS_SHOULD_BE_STATIC : 2
Alarm Type :FE_FLOATING_POINT_EQUALITY : 1
Alarm Type :UWF_UNWRITTEN_FIELD : 2
Alarm Type :CN_IDIOM_NO_SUPER_CALL : 1
Alarm Type :SBSC_USE_STRINGBUFFER_CONCATENATION : 2
Alarm Type :RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT : 2
Alarm Type :URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD : 15
Alarm Type :SF_SWITCH_FALLTHROUGH : 1
Alarm Type :EI_EXPOSE_REP2 : 7
Alarm Type :REC_CATCH_EXCEPTION : 1
Alarm Type :MS_MUTABLE_ARRAY : 1
Alarm Type :FI_MISSING_SUPER_CALL : 1
Alarm Type :RI_REDUNDANT_INTERFACES : 1
Alarm Type :RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE : 5
Alarm Type :DB_DUPLICATE_SWITCH_CLAUSES : 1
Alarm Type :UCF_USELESS_CONTROL_FLOW : 6
Alarm Type :NP_DEREFERENCE_OF_READLINE_VALUE : 1
Alarm Type :RV_RETURN_VALUE_IGNORED_BAD_PRACTICE : 4
Alarm Type :DLS_DEAD_LOCAL_STORE : 41
Alarm Type :EI_EXPOSE_REP : 1
Alarm Type :URF_UNREAD_FIELD : 19
Alarm Type :ODR_OPEN_DATABASE_RESOURCE : 1
Alarm Type :DMI_RANDOM_USED_ONLY_ONCE : 1
Alarm Type :NP_LOAD_OF_KNOWN_NULL_VALUE : 1
Alarm Type :OBL_UNSATISFIED_OBLIGATION : 1
Alarm Type :WA_NOT_IN_LOOP : 1
Alarm Type :MS_SHOULD_BE_FINAL : 3
Alarm Type :RV_CHECK_FOR_POSITIVE_INDEXOF : 1
Alarm Type :DM_DEFAULT_ENCODING : 13
Alarm Type :ODR_OPEN_DATABASE_RESOURCE_EXCEPTION_PATH : 1
Alarm Type :DM_GC : 1
Alarm Type :SIC_INNER_SHOULD_BE_STATIC : 3
Alarm Type :ES_COMPARING_STRINGS_WITH_EQ : 3
Alarm Type :ICAST_INTEGER_MULTIPLY_CAST_TO_LONG : 1
Alarm Type :ES_COMPARING_PARAMETER_STRING_WITH_EQ : 3
Alarm Type :UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR : 3
Alarm Type :WMI_WRONG_MAP_ITERATOR : 1
Alarm Type :BC_UNCONFIRMED_CAST : 1
Alarm Type :MS_PKGPROTECT : 2
Alarm Type :SIC_INNER_SHOULD_BE_STATIC_ANON : 3
Alarm Type :PZLA_PREFER_ZERO_LENGTH_ARRAYS : 2
Alarm Type :SC_START_IN_CTOR : 1
Alarm Type :RR_NOT_CHECKED : 4
Alarm Type :RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE : 2
Alarm Type :DM_CONVERT_CASE : 10
Alarm Type :DE_MIGHT_IGNORE : 1
Alarm Type :SW_SWING_METHODS_INVOKED_IN_SWING_THREAD : 1
Alarm Type :ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD : 14
	 */

	public static void main(String[] args) throws IOException {
		String content = FileHelper.readFile("OUTPUT/DEL_INS.list");
		Map<String, Integer> map = new HashMap<>();
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		StringBuilder singleAlarm = new StringBuilder();
		List<String> alarms = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("Alarm")) {
				if (singleAlarm.length() > 0) {
					alarms.add(singleAlarm.toString());
					singleAlarm = new StringBuilder();
				}
			}
			singleAlarm.append(line + "\n");
//			String type = line.trim();
//			
//			if (map.containsKey(type)) {
//				map.put(type, map.get(type) + 1);
//			} else {
//				map.put(type, 1);
//			}
		}
		reader.close();
		alarms.add(singleAlarm.toString());
		
		ListSorter<String> sorter = new ListSorter<String>(alarms);
		alarms = sorter.sortAscending();
		
		StringBuilder alarmsBuilder = new StringBuilder();
		for (String a : alarms) {
			alarmsBuilder.append(a);
		}
		FileHelper.outputToFile("OUTPUT/DEL.list", alarmsBuilder, false);
		
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
		System.out.println();
	}

}
