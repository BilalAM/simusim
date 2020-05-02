FogNode 2 Summary
=====================================
Total tuples processed : ${totalTuples}
Date and time :          ${dateTime}
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


