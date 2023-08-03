WorkManager is the new Android background task
We use WorkManager to run deferrable... That means capable of being postponeded background task 
WorkManager provides the guarantee that the system will run the postponded  background task even if 
if the app closed

# All included
* Work Manager One Time Work Request
* Get Status Updates From Workers
* Set Constraints (We provide here the specific conditions when the app should run-i.e while 
connected, charging, etc)
* Set Input & Output Data of a WorkManager
* Chaining Workers (Run multiple task in series or parallel way)
* Periodic Work Request Example (Implemented but the function call was commented out in MainActivity
Class line 52)