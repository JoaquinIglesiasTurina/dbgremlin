# The Problem
You need to grant permissions to your colleague in a Databricks Workflow.
This Workflow is full of nested Workflows. 
It looks like this:
![Workflow](./workflow.PNG)

You have to click on each workflow and grant permissions on each
of the nested Workflows.

# The solution
`java -jar dbgremlin.jar give-job-permissions --user-email "your-colleague@email.com" --job-id 1234567897`

A single command recursively sets permissions on each of the nested Workflows.
The `jar` is available on the [actions artifacts](https://github.com/JoaquinIglesiasTurina/dbgremlin/actions).
Click on any successful run, and you can download the jar there.

# Prerequisites
You need to have [Databricks CLI configured](https://learn.microsoft.com/en-us/azure/databricks/dev-tools/cli/tutorial)
Make sure that the Workspace you want to grant premissions in is set as default. In your `.databrickscfg` file:
```
[DEFAULT]
host  = https://adb-<your-workspace-id>.10.azuredatabricks.net
token = dapp<your-personal-access-token>-2
```
Yout can check that `databricks jobs list` contains the Workflow you want to grant permissions on. If so, the Databricks CLI is configured correctly.

You also need `java`.

## Limitations
As of now, only `CAN_MANAGE` permissions are granted.
Tested on Azure Databricks only.