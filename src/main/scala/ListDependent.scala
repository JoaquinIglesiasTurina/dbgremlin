package ListDependent

import com.databricks.sdk.WorkspaceClient
import com.databricks.sdk.service.jobs.Job

val workspaceClient = new WorkspaceClient()

def printJob(job: Job): Unit =
  print(Console.GREEN + job.getJobId().toString() + Console.RESET)
  println(s"\t ${job.getSettings().getName()}")

def listDependent(
  jobId: Long,
  workspaceClient: WorkspaceClient = workspaceClient
): Unit =
  val job = workspaceClient.jobs().get(jobId)
  DatabricksUtils
    .getAllDependentJobs(workspaceClient, job)
    .foreach(printJob(_))