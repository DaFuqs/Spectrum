$s = '				{ "_____________", "____XtStX____", "_____________", "_____________", "_X__OtttO__X_", "_T__T___T__T_", "_S__T___T__S_", "_T__T___T__T_", "_X__OtttO__X_", "_____________", "_____________", "____XtStX____", "_____________" },
				{ "_____________", "_SttR___RttS_", "_T__T___T__T_", "_T__T___T__T_", "_RttX___XttR_", "_____________", "_____________", "_____________", "_RttX___XttR_", "_T__T___T__T_", "_T__T___T__T_", "_SttR___RttS_", "_____________" },
				{ "_____________", "_Q__Q___Q__Q_", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_Q__Q___Q__Q_", "_____________" },
				{ "_____________", "_C__Q___Q__C_", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_____________", "_Q_________Q_", "_____________", "_____________", "_C__Q___Q__C_", "_____________" },
				{ "_____________", "_Q__L___L__Q_", "_____________", "___S_____S___", "_L_________L_", "_____________", "_____________", "_____________", "_L_________L_", "___S_____S___", "_____________", "_Q__L___L__Q_", "_____________" },
				{ "_____________", "_K__Q___Q__K_", "_____________", "___Q_____Q___", "_Q_________Q_", "_____________", "______4______", "_____________", "_Q_________Q_", "___Q_____Q___", "_____________", "_K__Q___Q__K_", "_____________" },
				{ "XXXXXXXXXXXXX", "XXmmXmmmXmmXX", "XMXXXXXXXXXMX", "XMXXXXXXXXXMX", "XXXXXRXRXXXXX", "XMXXRXXXRXXMX", "XMXXXX0XXXXMX", "XMXXRXXXRXXMX", "XXXXXRXRXXXXX", "XMXXXXXXXXXMX", "XMXXXXXXXXXMX", "XXmmXmmmXmmXX", "XXXXXXXXXXXXX" }'

$replaced = $s -replace "`"", "" -replace " ", "" -replace "{", "" -replace "}", "" -replace "`r`n", "" -replace "_", "" -replace ",", ""  -replace "`t", "";
$replaced.GetEnumerator() | Sort-Object -Property Count | Group-Object

<#
		Object[] targetBlocksCheck = {
				'X', "#spectrum:polished_base_blocks",
				'T', "#spectrum:crest_base_blocks",
				't', "#spectrum:crest_base_blocks",
				'Q', "#spectrum:pillar_base_blocks",
				'L', "#spectrum:gemstone_lamps",
				'S', "#spectrum:gemstone_storage_blocks",
				'C', "#spectrum:chiseled_base_blocks",
				'K', "#spectrum:notched_base_blocks",
				'R', "#spectrum:basic_gemstone_chiseled_base_blocks",
				'O', "#spectrum:onyx_chiseled_base_blocks",
				'm', "#spectrum:moonstone_chiseled_base_blocks",
				'M', "#spectrum:moonstone_chiseled_base_blocks",
				'n', "#spectrum:polished_base_blocks_or_moonstone_chiseled",
				'N', "#spectrum:polished_base_blocks_or_moonstone_chiseled",
				'2', "#spectrum:pedestals",
				'3', "#spectrum:pedestals",
				'4', "#spectrum:pedestals",
				'_', StateMatcher.ANY,
				'0', StateMatcher.ANY
		};
#>