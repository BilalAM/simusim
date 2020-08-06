FogNode 1 Summary
=====================================
Total tuples processed : ${totalTuples}
<#if rams?? && rams?size != 0>
Average RAM   :          ${rams[rams?size - 1]}
</#if>
Date and time :          ${dateTime}
Average Processing time of all tuples :  ${averageTime}
Connected to :           ${connectedDevices}
=====================================


--------------------------
Ram Utilization History : -
---------------------------
Second : Ram Utilized
<#list rams as ram>
${ram?index+1},${ram}
</#list>


---------------------------
Tuples Processing Summary :
---------------------------
Tuple : Tuple Processed Time
<#list tuples as tuple>
${tuple?index+1},${tuple}
</#list>


