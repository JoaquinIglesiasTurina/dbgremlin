import com.databricks.sdk.WorkspaceClient
import com.databricks.sdk.service.jobs.Job
import com.databricks.sdk.service.jobs.RunJobTask
import com.databricks.sdk.service.jobs.Task

import scala.jdk.CollectionConverters._

def getJobIdFromTask(task: Task): Option[Long] =
  task.getRunJobTask() match
    case runJobTask: RunJobTask => Some(runJobTask.getJobId())
    case null => None

def getDependentJobIds(job: Job): List[Long] =
  job.getSettings().getTasks().asScala.toList
    .map(getJobIdFromTask)
    .filter(_.isDefined)
    .map(_.get)
    .distinct

def getAllDependentJobs(
  workspaceClient: WorkspaceClient,
  job: Job
): List[Job] = 
  var jobIds = getDependentJobIds(job) 
  var outputJobs: List[Job] = List(job)
  while (jobIds.size > 0)
    val j = workspaceClient.jobs().get(jobIds.head)
    val newJobs = getDependentJobIds(j)
    outputJobs = outputJobs :+ j
    val outputJobIds = outputJobs.map(_.getJobId) 
    jobIds = (jobIds.tail ++ newJobs)
      .distinct
      .filter(ji => !(outputJobIds.contains(ji)))
  outputJobs